package com.kmzyc.search.facade.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SPIRNG容器BEAN获取工具
 * 
 * @author zhoulinhong
 * @since 20160525
 * 
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

  private static ApplicationContext _context;

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    _context = context;
  }

  public static ApplicationContext getApplicationContext() {
    return _context;
  }

  public static Object getBean(String name) {
    return _context.getBean(name);
  }

  public static <T> T getBean(String name, Class<T> clazz) {
    return _context.getBean(name, clazz);
  }
}
