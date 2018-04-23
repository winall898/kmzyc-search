package com.kmzyc.search.facade.request.transverter;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;

/**
 * 精确搜索参数处理类
 * 
 * @author KM
 *
 */
public class AccurateParamsTransverter extends RequestParamsTransverter {

    public AccurateParamsTransverter(Channel channel) {
        super(channel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convert(Object param) {
        if (param == null) {
            return null;
        }
        Map<String, String[]> searchParams = (Map<String, String[]>) param;
        String[] qs = searchParams.get(HTTPParam.q.name());

        String queryStr = super.convert(searchParams);

        JSONObject queryJson = JSONObject.parseObject(queryStr);
        StringBuilder qval = new StringBuilder();
        qval.append(DocFieldName.SKUCODE + ":\"" + qs[0] + "\""); // SKU编码
        qval.append(" OR " + DocFieldName.PRODUCT_NO + ":\"" + qs[0] + "\""); // 产品编码
        queryJson.put("query", JsonUtil.jsonPut(new JSONObject(), "query_string",
                JsonUtil.jsonPut(new JSONObject(), "query", qval)));
        return queryJson.toJSONString();
    }

}
