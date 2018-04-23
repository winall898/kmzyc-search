package com.kmzyc.search.shiro.cache;

import org.apache.shiro.cache.Cache;

/**
 * custom shiro cache manager interface
 * 
 * @author river
 */
public interface ShiroCacheManager {

  <K, V> Cache<K, V> getCache(String name);

  void destroy();

}
