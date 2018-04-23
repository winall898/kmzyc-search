package com.kmzyc.search;

import com.kmzyc.search.exception.SearchException;
import com.kmzyc.search.request.SearchParam;
import com.kmzyc.search.response.SearchResult;


/**
 * 搜索类标识接口
 * 
 * @author river
 * 
 */
public interface ISearcher {

  public SearchResult search(SearchParam params) throws SearchException;
}
