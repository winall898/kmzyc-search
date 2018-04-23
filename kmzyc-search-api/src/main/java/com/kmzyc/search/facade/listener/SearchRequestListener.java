package com.kmzyc.search.facade.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 搜索请求监听 区分请求来自那个站点
 * 
 * @author river
 * 
 */
public class SearchRequestListener implements ServletRequestListener {

  protected static final Logger LOG = LoggerFactory.getLogger(SearchRequestListener.class);

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
    // TODO Auto-generated method stub
  }

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    // TODO Auto-generated method stub
  }


}
