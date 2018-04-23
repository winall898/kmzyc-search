package com.kmzyc.search.searcher;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.kmzyc.search.facade.request.IRequestTransverter;
import com.kmzyc.search.facade.response.IResponseTransverter;
import com.kmzyc.search.facade.util.IndexClientUtil;

/**
 * 搜索执行者
 * 
 * @author KM
 *
 */
public class Searcher {

  protected static final Logger LOG = LoggerFactory.getLogger(Searcher.class);

  private String indexDB;
  private String indexType;

  /**
   * 搜索参数处理对象
   */
  private IRequestTransverter requestTransverter;

  /**
   * 搜索结果处理对象
   */
  private IResponseTransverter responseTranverter;

  public Searcher(String indexDB, String indexType, IRequestTransverter requestTransverter, IResponseTransverter responseTranverter) {
    this.indexDB = indexDB;
    this.indexType = indexType;
    this.requestTransverter = requestTransverter;
    this.responseTranverter = responseTranverter;
  }

  public Map<String, Object> search(Object searchParams) {
    if (searchParams == null) {
      return Maps.newHashMap();
    }
    String queryStr = requestTransverter.convert(searchParams);
    if (StringUtils.isBlank(queryStr)) {
      return Maps.newHashMap();
    }
    LOG.info("===ES search=== indexDB ：" + indexDB);
    LOG.info(" - es queryJOSN = " + queryStr);  
    SearchResponse searchResponse = IndexClientUtil.getInstance().getSearchResponse(indexDB, indexType, queryStr);
    if (searchResponse == null) {
      return Maps.newHashMap();
    }
    LOG.info(" - times = " + searchResponse.getTookInMillis() + " - totalCount = " + searchResponse.getHits().totalHits());   
    return responseTranverter.convert(searchResponse);
  }

}
