package com.kmzyc.search.facade.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IWAPSearchService {
  
   /**
   * wap关键词搜索
   *
   * @param request
   * @author zhoulinhong
   * @since 20160603
   * @return
   */
   public Map<String, Object> keyWordSearch(HttpServletRequest request);
  
   /**
   * wap类目搜索
   *
   * @param request
   * @return
   */
   public Map<String, Object> categorySearch(HttpServletRequest request);
}
