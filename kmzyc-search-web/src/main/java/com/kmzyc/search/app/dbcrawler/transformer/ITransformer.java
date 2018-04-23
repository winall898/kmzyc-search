package com.kmzyc.search.app.dbcrawler.transformer;

import java.util.Map;


public interface ITransformer {

  /**
   * 索引数据处理方法
   * 
   * @param map
   * @return
   */
  public Map<String, Object> process(Map<String, Object> map, String crawlerName);

}
