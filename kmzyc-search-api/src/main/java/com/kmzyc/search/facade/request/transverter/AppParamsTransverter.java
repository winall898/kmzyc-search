package com.kmzyc.search.facade.request.transverter;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.constants.ORDER;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.facade.request.IRequestTransverter;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;

public class AppParamsTransverter implements IRequestTransverter {

    protected boolean enableElevation = false;

    protected boolean forceElevation = false;

    protected Channel channel;

    public AppParamsTransverter(boolean enableElevation, boolean forceElevation, Channel channel) {
        this.enableElevation = enableElevation;
        this.forceElevation = forceElevation;
        this.channel = channel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convert(Object param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> searchParams = (Map<String, Object>) param;

        JSONObject searchJson = new JSONObject();

        if (getQuery(searchParams) != null) {
            // 设置查询语句
            searchJson.put("query", getQuery(searchParams));
        }

        // 设置分页
        Object start = searchParams.get("start");
        searchJson.put("from", start == null ? 0 : Integer.valueOf(start.toString()));
        Object rows = searchParams.get("rows");
        searchJson.put("size", rows == null ? 10 : Integer.valueOf(rows.toString()));

        if (getPostFilter(searchParams) != null) {
            // 设置过滤
            searchJson.put("post_filter", getPostFilter(searchParams));
        }

        // 设置分组
        searchJson.put("aggs", getFacet());

        // 设置排序
        Object sort = searchParams.get("sort");
        JSONArray sortJSONArray = new JSONArray();
        if (null != sort) {
            int sortType = Integer.valueOf(sort.toString());
            JSONObject sortJson = getSort(sortType);
            if (sortJson == null || sortJson.isEmpty()) {
                sortJSONArray.add(JSONObject.parse("{\"_score\":\"desc\"}"));
            } else {
                sortJSONArray.add(sortJson);
            }
        } else {
            sortJSONArray.add(JSONObject.parse("{\"_score\":\"desc\"}"));
        }
        sortJSONArray.add(JSONObject.parse("{\"id\":\"asc\"}"));
        searchJson.put("sort", sortJSONArray);

        return searchJson.toJSONString();
    }



    /**
     * 组装查询参数块
     * 
     * @param searchParams
     * @return
     */
    private JSONObject getQuery(Map<String, Object> searchParams) {
        // 添加查询参数
        Object qs = searchParams.get(HTTPParam.q.name());
        if (null != qs && StringUtils.isNotBlank(qs.toString())) {
            // return
            // JSONObject.parseObject(RequestParamsTransverter.queryStringTemp.replace("<<keyword>>",
            // qs.toString().replaceAll("\"", "\\\\\"").replaceAll("/", " ")));
            JSONObject queryJO = JSONObject.parseObject(RequestParamsTransverter.QUERY_STRING_TEMP);
            queryJO.getJSONObject("bool").getJSONObject("must").getJSONObject("query_string")
                    .put("query", qs.toString().replaceAll("\"", "\\\"").replaceAll("/", "\\\\/"));
            return queryJO;

        }
        return null;
    }

    /**
     * 组装过滤参数块
     * 
     * @param searchParams
     * @return
     */
    private JSONObject getPostFilter(Map<String, Object> searchParams) {
        JSONArray jsonTerms = new JSONArray();

        // TODO APP端过滤掉预售商品的搜索
        // TODO add by zhoulinhong on 20160828
        jsonTerms.add(
                JSONObject.parse("{\"bool\": {\"must_not\": {\"term\": {\"promotion\": \"2\"}}}}"));

        Object stock = searchParams.get(DocFieldName.STOCK);
        if (null != stock && "1".equals(stock)) {
            jsonTerms.add(
                    JsonUtil.jsonPut(new JSONObject(), "range", JsonUtil.jsonPut(new JSONObject(),
                            DocFieldName.STOCK, JsonUtil.jsonPut(new JSONObject(), "gte", 1))));
        }

        addFilterTerm(jsonTerms, searchParams, DocFieldName.SHOPID);
        addFilterTerm(jsonTerms, searchParams, DocFieldName.SHOPNAME);
        addFilterTerm(jsonTerms, searchParams, DocFieldName.SKUCODE);
        addFilterTerm(jsonTerms, searchParams, DocFieldName.BRAND_ID);
        addFilterTerm(jsonTerms, searchParams, DocFieldName.SHOPGORY);
        addFilterTerm(jsonTerms, searchParams, DocFieldName.SECOND_O_CODE);
        addFilterTerm(jsonTerms, searchParams, DocFieldName.THIRD_O_CODE);

        Object supType = searchParams.get(DocFieldName.SUPTYPE);
        if (null != supType) {
            if ("13".equals(supType)) {
                JSONArray supArray = new JSONArray();
                supArray.add(JsonUtil.jsonPut(new JSONObject(), "term", JsonUtil.jsonPut(
                        new JSONObject(), DocFieldName.SUPTYPE, SupplierType.T1.getCode())));
                supArray.add(JsonUtil.jsonPut(new JSONObject(), "term", JsonUtil.jsonPut(
                        new JSONObject(), DocFieldName.SUPTYPE, SupplierType.T3.getCode())));
                jsonTerms.add(JsonUtil.jsonPut(new JSONObject(), "or", supArray));
            } else {
                jsonTerms.add(JsonUtil.jsonPut(new JSONObject(), "term", JsonUtil.jsonPut(
                        new JSONObject(), DocFieldName.SUPTYPE, SupplierType.T2.getCode())));
            }
        }

        Object ptype = searchParams.get("ptype");
        if (null != ptype) {
            if ("1".equals(ptype)) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date(System.currentTimeMillis());
                String endDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(date);

                calendar.setTime(date);
                calendar.add(Calendar.MONTH, -1);// 向前推1个月
                String startDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
                        .format(calendar.getTime());
                JSONObject dateJSON = new JSONObject();
                dateJSON.put("gte", startDate);
                dateJSON.put("lte", endDate);
                dateJSON.put("format", "yyyy-MM-dd HH:mm:ss");
                jsonTerms.add(JsonUtil.jsonPut(new JSONObject(), "range",
                        JsonUtil.jsonPut(new JSONObject(), DocFieldName.UP_TIME, dateJSON)));
            }
        }

        Object price = searchParams.get(DocFieldName.PRICE);
        if (null != price && price.toString().indexOf("_") > 0) {
            String[] prVals = price.toString().split("_");
            JSONObject prValJson = new JSONObject();
            prValJson.put("gte", Integer.valueOf(prVals[0]));
            if (!"*".equals(prVals[1])) {
                prValJson.put("lte", Integer.valueOf(prVals[1]));
            }
            jsonTerms.add(JsonUtil.jsonPut(new JSONObject(), "range",
                    JsonUtil.jsonPut(new JSONObject(), DocFieldName.PRICE, prValJson)));
        }

        Object brandName = searchParams.get(DocFieldName.BRAND_NAME);
        if (null != brandName && StringUtils.isNotBlank(brandName.toString())) {
            Iterable<String> brands =
                    Splitter.on(",").trimResults().omitEmptyStrings().split(brandName.toString());
            JSONArray brandArray = new JSONArray();
            for (String brand : brands) {
                brandArray.add(JsonUtil.jsonPut(new JSONObject(), "term",
                        JsonUtil.jsonPut(new JSONObject(), DocFieldName.BRAND_NAME, brand)));
            }
            jsonTerms.add(JsonUtil.jsonPut(new JSONObject(), "or", brandArray));
        }
        if (jsonTerms.isEmpty()) {
            return null;
        }
        return JsonUtil.jsonPut(new JSONObject(), "and", jsonTerms);
    }

    /**
     * 添加普通过滤规则的方法
     * 
     * @param jsonTerms
     * @param searchParams
     * @param fieldName
     */
    private void addFilterTerm(JSONArray jsonTerms, Map<String, Object> searchParams,
            String fieldName) {
        Object ob = searchParams.get(fieldName);
        if (ob != null) {
            jsonTerms.add(JsonUtil.jsonPut(new JSONObject(), "term",
                    JsonUtil.jsonPut(new JSONObject(), fieldName, ob)));
        }
    }

    private JSONObject getSort(int sortType) {

        JSONObject json = new JSONObject();
        switch (sortType) {
            case 1:
                json.put(DocFieldName.SALES, ORDER.desc);
                break;
            case 2:
                json.put(DocFieldName.SALES, ORDER.asc);
                break;
            case 3:
                json.put(DocFieldName.PRICE, ORDER.desc);
                break;
            case 4:
                json.put(DocFieldName.PRICE, ORDER.asc);
                break;
            default:
                return null;
        }
        return json;
    }



    private JSONObject getFacet() {
        JSONObject facetJson = new JSONObject();
        facetJson.put(DocFieldName.ATTRNAME, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.ATTRNAME + "\",\"size\":100}}"));
        facetJson.put(DocFieldName.ATTRVAL_CP, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.ATTRVAL_CP + "\",\"size\":100}}"));
        facetJson.put(DocFieldName.SECOND_O_CODE, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.SECOND_O_CODE + "\",\"size\":100}}"));
        facetJson.put(DocFieldName.THIRD_O_CODE, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.THIRD_O_CODE + "\",\"size\":100}}"));
        facetJson.put(DocFieldName.BRAND_NAME, JSONObject.parseObject(
                "{\"terms\":{\"field\":\"" + DocFieldName.BRAND_NAME + "\",\"size\":100}}"));
        return facetJson;
    }

}
