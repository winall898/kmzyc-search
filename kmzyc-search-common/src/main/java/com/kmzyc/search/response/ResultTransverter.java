package com.kmzyc.search.response;

/**
 * 搜索结果处理标识接口
 * 
 * @author river
 * 
 */
public interface ResultTransverter {

  public SearchResult convert(SearchResult result);
}
