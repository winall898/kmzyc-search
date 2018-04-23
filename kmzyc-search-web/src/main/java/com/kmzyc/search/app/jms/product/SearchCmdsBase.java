package com.kmzyc.search.app.jms.product;

import com.kmzyc.search.app.util.SpringBeanUtil;

/**
 * 搜索base类
 * 
 * @author zhoulinhong
 * @since 20160510
 */
public class SearchCmdsBase {

  public SearchCmdsBase() {}

  public static Object getBean(String beanName) {

    return SpringBeanUtil.getBean(beanName);
  }
}
