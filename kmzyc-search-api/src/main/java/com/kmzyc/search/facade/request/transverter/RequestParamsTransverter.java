package com.kmzyc.search.facade.request.transverter;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.constants.ESSortParam;
import com.kmzyc.search.facade.request.IRequestTransverter;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;

/**
 * b2b 关键字模糊搜索参数组装类
 * 
 * @author KM
 *
 */
public class RequestParamsTransverter implements IRequestTransverter {

    protected static final Logger LOG = LoggerFactory.getLogger(RequestParamsTransverter.class);

    public static final String QUERY_STRING_TEMP =
            "{\"bool\":{\"must\":{\"query_string\":{\"fields\":[\"prodTitle^1\",\"subtitle^0.5\",\"pcName^0.2\",\"sav_ss^0.2\",\"prodName^0.1\",\"keyword^0.1\",\"brandName^0.05\"],"
                    + "\"use_dis_max\":false,\"default_operator\": \"AND\"}}}}";

    protected Channel channel;

    public RequestParamsTransverter(Channel channel) {
        this.channel = channel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convert(Object param) {
        if (param == null) {
            return null;
        }
        Map<String, String[]> searchParams = (Map<String, String[]>) param;
        JSONObject json = new JSONObject();
        // 添加查询参数
        String[] qs = searchParams.get(HTTPParam.q.name());
        if (ArrayUtils.isNotEmpty(qs) && StringUtils.isNotBlank(qs[0])) {
            // 查询语句处理
            JSONObject queryJO = JSONObject.parseObject(QUERY_STRING_TEMP);
            queryJO.getJSONObject("bool").getJSONObject("must").getJSONObject("query_string")
                    .put("query", qs[0].replaceAll("\"", "\\\"").replaceAll("/", "\\\\/"));
            json.put("query", queryJO);
        }
        // 添加排序参数
        String[] sort = searchParams.get(HTTPParam.sort.name());
        JSONArray sortJSONArray = new JSONArray();
        if (ArrayUtils.isNotEmpty(sort)) {
            // 排序处理
            JSONObject sortJson = ESSortParam.getSortText(Integer.parseInt(sort[0]));
            if (sortJson != null && !sortJson.isEmpty()) {
                sortJSONArray.add(sortJson);
            } else {
                sortJSONArray.add(JSONObject.parse("{\"_score\":\"desc\"}"));
            }
        } else {
            sortJSONArray.add(JSONObject.parse("{\"_score\":\"desc\"}"));
        }
        sortJSONArray.add(JSONObject.parse("{\"id\":\"asc\"}"));
        json.put("sort", sortJSONArray);

        // 设置过滤参数
        JSONObject filters = getFilter(searchParams.get(HTTPParam.f.name()));
        if (filters != null) {
            // 两种不同的筛选方式filter和postFilter判断
            String[] postFilter = searchParams.get("postFilter");
            if (ArrayUtils.isNotEmpty(postFilter) && Boolean.valueOf(postFilter[0])) {
                json.put("post_filter", filters);
            } else {
                json.getJSONObject("query").getJSONObject("bool").put("filter", filters);
            }
        }

        // 分类
        String[] facet = searchParams.get("facet");
        if (ArrayUtils.isNotEmpty(facet) && Boolean.valueOf(facet[0])) {
            json.put("aggs", getFacet());
        }

        // 设置高亮
        String[] highlight = searchParams.get("highlight");
        if (ArrayUtils.isNotEmpty(highlight) && "true".equals(highlight[0])) {
            json.put("highlight", getHighlight());
        }

        String[] pageNum = searchParams.get(HTTPParam.pn.name());
        String[] rows = searchParams.get(HTTPParam.ps.name());

        json.put("size", rows == null ? 25 : Integer.valueOf(rows[0]));
        int start =
                pageNum == null ? 0 : (Integer.valueOf(pageNum[0]) - 1) * json.getInteger("size");
        json.put("from", start);

        return json.toJSONString();
    }

    protected JSONObject getFilter(String[] filters) {
        if (ArrayUtils.isEmpty(filters) || StringUtils.isBlank(filters[0])) {

            return null;
        }

        // 过滤条件
        JSONArray jsonA = new JSONArray();
        String filtParamText = filters[0];
        try {
            filtParamText = URLDecoder.decode(filtParamText, "UTF-8");
        } catch (Exception e) {
            filtParamText = filters[0];
            LOG.error("decode filtParamText fail", e);
        }
        Iterator<String> it = Splitter.on("&").trimResults().split(filtParamText).iterator();
        while (it.hasNext()) {
            try {
                String filter = it.next();
                if (StringUtils.isBlank(filter)) {

                    continue;
                }

                // 筛选条件解析
                if (filter.startsWith("kmSelf=")) {
                    // 筛选自营和代销
                    jsonA.add(JSONObject
                            .parse("{\"bool\":{\"must_not\":{\"term\":{\"supType\":2}}}}"));
                } else {
                    String[] pairs = filter.split("=");
                    String paramName = pairs[0];
                    if (paramName.equals(DocFieldName.MARKET_NAME)) {
                        // 药材市场过滤
                        if (pairs.length == 2) {
                            String paramVal = pairs[1];
                            jsonA.add(JsonUtil.jsonPut(new JSONObject(), "term",
                                    JsonUtil.jsonPut(new JSONObject(), paramName, paramVal)));
                        }
                    } else if (paramName.equals(DocFieldName.STOCK)) {
                        String[] prVals = pairs[1].split("-");
                        JSONObject prValJson = new JSONObject();
                        prValJson.put("gte", Integer.valueOf(prVals[0]));
                        if (!"*".equals(prVals[1])) {
                            prValJson.put("lte", Integer.valueOf(prVals[1]));
                        }
                        jsonA.add(JsonUtil.jsonPut(new JSONObject(), "range",
                                JsonUtil.jsonPut(new JSONObject(), DocFieldName.STOCK, prValJson)));
                    } else {
                        // 属性过滤
                        List<String> filterParam =
                                Splitter.on("=").trimResults().splitToList(filter);
                        if (filterParam.size() > 1) {
                            String paramVal = filterParam.get(1);
                            paramVal = paramVal.replace("_", "=");
                            jsonA.add(JsonUtil.jsonPut(new JSONObject(), "term", JsonUtil
                                    .jsonPut(new JSONObject(), DocFieldName.ATTRVAL_CP, paramVal)));
                        }
                    }
                }
            } catch (Exception e) {

                LOG.error(e.getMessage());
            }
        }

        if (jsonA != null && !jsonA.isEmpty()) {
            return JsonUtil.jsonPut(new JSONObject(), "and", jsonA);
        }

        return null;
    }

    protected JSONObject getFacet() {
        JSONObject facetJson = new JSONObject();
        facetJson.put(DocFieldName.ATTRNAME, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.ATTRNAME + "\",\"size\":100}}"));
        facetJson.put(DocFieldName.ATTRVAL_CP, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.ATTRVAL_CP + "\",\"size\":100}}"));
        facetJson.put(DocFieldName.MARKET_NAME, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.MARKET_NAME + "\",\"size\":100}}"));

        return facetJson;
    }

    protected JSONObject getHighlight() {
        JSONObject highJson = new JSONObject();
        highJson.put("pre_tags", new String[] {"<font color=\'red\'>"});
        highJson.put("post_tags", new String[] {"</font>"});
        highJson.put("fields", JsonUtil.jsonPut(new JSONObject(), "prodTitle", new JSONObject()));
        return highJson;
    }

}
