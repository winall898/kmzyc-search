package com.kmzyc.search.facade.request.transverter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.facade.constants.ORDER;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.facade.request.IRequestTransverter;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.param.DocFieldName;

public class WapParamsTransverter implements IRequestTransverter {

    @Override
    public String convert(Object param) {

        if (null == param) {

            return null;
        }

        HttpServletRequest request = (HttpServletRequest) param;

        // 获取页码
        String pageNoStr = request.getParameter("pn");
        int pageno = 1;
        if (StringUtils.isNotBlank(pageNoStr)) {
            pageno = NumberUtils.toInt(pageNoStr.trim(), 1);
        }

        // 获取页面数据量大小
        String pageSizeStr = request.getParameter("ps");
        int pageSize = 6;
        if (StringUtils.isNotBlank(pageSizeStr)) {
            pageSize = NumberUtils.toInt(pageSizeStr.trim(), 6);
        }

        int start = (pageno - 1) * pageSize;
        start = start <= 0 ? 0 : start;

        JSONObject queryJson = JSONObject.parseObject(
                "{\"query\":{\"bool\":{\"must\":{\"query_string\":{}}}},\"post_filter\":{},\"aggs\":{}}");
        // 起始偏移量
        queryJson.put("from", start);
        // 搜索每页的产品数量
        queryJson.put("size", pageSize);

        // 分组
        addFacetField(queryJson);

        // 添加过滤条件
        getFilterParams(request, queryJson);
        // 添加排序参数
        getSortParams(request, queryJson);
        // 查询条件
        setFunction(queryJson);

        // 搜索关键词
        String kw = request.getParameter("kw");
        if (StringUtils.isBlank(kw)) {
            kw = "";
        } else {
            String escapeKw = ParamUitl.escapeQueryChars(kw);
            kw = StringUtils.isBlank(escapeKw) ? "" : escapeKw;
        }
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("query", kw);

        // 返回字段
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(DocFieldName.ID);
        jsonArray.add(DocFieldName.PRODUCT_TITLE);
        jsonArray.add(DocFieldName.PROCUCT_NAME);
        jsonArray.add(DocFieldName.SUBTITLE);
        jsonArray.add(DocFieldName.PRICE);
        jsonArray.add(DocFieldName.MPRICE);
        jsonArray.add(DocFieldName.SKUCODE);
        jsonArray.add(DocFieldName.IMAGE);
        jsonArray.add(DocFieldName.SHOPID);
        jsonArray.add(DocFieldName.PRODUCT_TYPE);
        jsonArray.add(DocFieldName.SUPTYPE);
        jsonArray.add(DocFieldName.SHOPNAME);
        // 返回促销类型（0普通 1促销 2预售）
        jsonArray.add(DocFieldName.PROMOTION);
        jsonArray.add("sav_ss");
        queryJson.put("fields", jsonArray);

        return queryJson.toJSONString();
    }

    protected void setFunction(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        // 设置查询类型为edismax
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("use_dis_max", false);

        // 给查询字段分配权重
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("prodTitle^1");
        jsonArray.add("keyword^0.9");
        jsonArray.add("ocName_ik^0.5");
        jsonArray.add("brandName^0.5");
        jsonArray.add("subtitle^0.2");
        jsonArray.add("pcName^0.1");
        jsonArray.add("shopName^0.5");
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("fields", jsonArray);

        // 促销打分权重
        queryJson.getJSONObject("query").getJSONObject("bool").put("should",
                JSONArray.parse("[{\"range\":{\"promotion\":{\"boost\":5,\"gt\":0}}}]"));

        // 设置搜索默认运算方式AND
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("default_operator", "AND");
    }

    protected void getFilterParams(HttpServletRequest request, JSONObject queryJson) {

        if (null == request || null == queryJson) {

            return;
        }

        JSONArray filterArray = new JSONArray();

        // 价格
        String priceText = request.getParameter("price");
        if (StringUtils.isNotBlank(priceText)) {
            String[] vals = priceText.split("_");
            if (priceText.startsWith("1000_")) {
                if (null != vals && vals.length > 0) {
                    filterArray.add(JSONObject
                            .parseObject("{\"range\": {\"price\": {\"gte\": " + vals[0] + "}}}"));
                }
            } else {
                if (null != vals && vals.length > 1) {
                    filterArray.add(JSONObject.parseObject("{\"range\": {\"price\": {\"gte\": "
                            + vals[0] + ",\"lte\": " + vals[1] + "}}}"));
                }
            }
        }

        // 商家类型
        String supplyText = request.getParameter("supply");
        if (StringUtils.isNotBlank(supplyText)) {
            if ("13".equals(supplyText)) {
                filterArray.add(JSONObject.parseObject("{\"or\":[{\"term\":{\"supType\":"
                        + SupplierType.T1.getCode() + "}},{\"term\":{\"supType\":"
                        + SupplierType.T3.getCode() + "}}]}"));
            } else {
                filterArray.add(JSONObject
                        .parseObject("{\"term\":{\"supType\":" + SupplierType.T2.getCode() + "}}"));
            }
        }

        // 类别
        String c3 = request.getParameter("c3");
        if (StringUtils.isNotBlank(c3)) {
            filterArray.add(JSONObject.parseObject(
                    "{\"term\":{\"" + DocFieldName.THIRD_O_CODE + "\":\"" + c3 + "\"}}"));
        }
        String c2 = request.getParameter("c2");
        if (StringUtils.isNotBlank(c2)) {
            filterArray.add(JSONObject.parseObject(
                    "{\"term\":{\"" + DocFieldName.SECOND_O_CODE + "\":\"" + c2 + "\"}}"));
        }
        String c1 = request.getParameter("c1");
        if (StringUtils.isNotBlank(c1)) {
            filterArray.add(JSONObject.parseObject(
                    "{\"term\":{\"" + DocFieldName.FIRST_O_CODE + "\":\"" + c1 + "\"}}"));
        }

        filterArray.add(
                JSONObject.parseObject("{\"bool\":{\"must_not\":{\"term\":{\"prodType\":3}}}}"));

        // 设置过滤条件
        queryJson.getJSONObject("post_filter").put("and", filterArray);
    }

    protected void getSortParams(HttpServletRequest request, JSONObject queryJson) {

        if (null == request || null == queryJson) {

            return;
        }

        // 排序
        JSONObject sortJson = new JSONObject();
        String sort = request.getParameter("sort");
        if (StringUtils.isNotBlank(sort)) {
            if ("1".equals(sort)) {
                sortJson.put(DocFieldName.SALES, ORDER.desc);
            } else if ("2".equals(sort)) {
                sortJson.put(DocFieldName.SALES, ORDER.asc);
            } else if ("3".equals(sort)) {
                sortJson.put(DocFieldName.PRICE, ORDER.desc);
            } else if ("4".equals(sort)) {
                sortJson.put(DocFieldName.PRICE, ORDER.asc);
            }
        }
        JSONArray sortJSONArray = new JSONArray();
        // 设置排序条件
        if (!sortJson.isEmpty()) {
            sortJSONArray.add(sortJson);
        } else {
            sortJSONArray.add(JSONObject.parse("{\"_score\":\"desc\"}"));
        }
        sortJSONArray.add(JSONObject.parse("{\"id\":\"asc\"}"));
        queryJson.put("sort", sortJSONArray);
    }

    private void addFacetField(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        // 商品2级运营类目
        queryJson.getJSONObject("aggs").put("soCode",
                JSONObject.parseObject("{\"terms\":{\"field\":\"soCode\",\"size\":100}}"));
        // 商品3级运营类目
        queryJson.getJSONObject("aggs").put("toCode",
                JSONObject.parseObject("{\"terms\":{\"field\":\"toCode\",\"size\":100}}"));
        // 商品价格
        queryJson.getJSONObject("aggs").put("price",
                JSONObject.parseObject("{\"terms\":{\"field\":\"price\",\"size\":100}}"));
        // 商家类型
        queryJson.getJSONObject("aggs").put("supType",
                JSONObject.parseObject("{\"terms\":{\"field\":\"supType\",\"size\":100}}"));
    }

}
