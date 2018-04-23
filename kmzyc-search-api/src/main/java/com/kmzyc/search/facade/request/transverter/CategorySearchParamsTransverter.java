package com.kmzyc.search.facade.request.transverter;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.DocFieldName;

public class CategorySearchParamsTransverter extends RequestParamsTransverter {

    public CategorySearchParamsTransverter(Channel channel) {
        super(channel);
    }

    @Override
    public String convert(Object param) {
        String queryString = super.convert(param);
        if (StringUtils.isBlank(queryString)) {
            return null;
        }
        JSONObject queryJSON = JSONObject.parseObject(queryString);
        JSONObject aggJSON = queryJSON.getJSONObject("aggs");
        if (aggJSON != null) {
            aggJSON.remove(DocFieldName.SECOND_O_CODE);
            aggJSON.remove(DocFieldName.THIRD_O_CODE);
            queryJSON.put("aggs", aggJSON);
        }
        return queryJSON.toJSONString();
    }

}
