package com.kmzyc.search.facade.request.transverter;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;


/**
 * WAP 运营类目搜索参数处理类
 * 
 * @author river
 * 
 */
public class WapCSParamsTransverter extends WapParamsTransverter {

  private final String categoryQuery;

  public WapCSParamsTransverter(String categoryQuery) {
    this.categoryQuery = categoryQuery;
  }

  @Override
  public String convert(Object param) {
    if (null == param) {

    }

    // 搜索语句
    String queryStr = super.convert(param);

    if (StringUtils.isBlank(queryStr)) {

      return null;
    }

    // 调整查询语句
    JSONObject queryJson = JSONObject.parseObject(queryStr);
    queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must").getJSONObject("query_string").remove("fields");
    queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must").getJSONObject("query_string").put("query", categoryQuery);

    return queryJson.toJSONString();
  }
}
