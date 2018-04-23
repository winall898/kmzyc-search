package com.kmzyc.search.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;

/**
 * shiro cache manager class
 * 
 * @author river
 */
public class CustomShiroCacheManager implements CacheManager, Destroyable {

  private ShiroCacheManager shiroCacheManager;

  @Override
  public <K, V> Cache<K, V> getCache(String name) {
    return getShiroCacheManager().getCache(name);
  }

  @Override
  public void destroy() throws Exception {
    shiroCacheManager.destroy();
  }

  public ShiroCacheManager getShiroCacheManager() {
    return shiroCacheManager;
  }

  public void setShiroCacheManager(ShiroCacheManager shiroCacheManager) {
    this.shiroCacheManager = shiroCacheManager;
  }

}
