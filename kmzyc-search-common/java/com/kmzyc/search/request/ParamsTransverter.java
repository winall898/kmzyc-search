package com.kmzyc.search.request;

/**
 * 搜索参数处理接口
 * 
 * @author river
 * 
 */
public interface ParamsTransverter {

  public SearchParam convert(SearchParam param);
}
