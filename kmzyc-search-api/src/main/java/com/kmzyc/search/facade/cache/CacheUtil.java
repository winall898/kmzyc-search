package com.kmzyc.search.facade.cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * cache工具类
 * 
 * @author zhoulinhong
 * @since 20160525
 */
public class CacheUtil {

  private static Map<String, Cache<Object, Object>> caches = new WeakHashMap<String, Cache<Object, Object>>();

  public synchronized static Cache<Object, Object> getCache(String cacheName, boolean fixDelay) {

    return getCache(cacheName, 1000, fixDelay);
  }

  public synchronized static Cache<Object, Object> getCache(String cacheName, int maxSize, boolean fixDelay) {

    return getCache(cacheName, maxSize, 5, TimeUnit.MINUTES, fixDelay);
  }

  public synchronized static Cache<Object, Object> getCache(String cacheName, int maxSize, long duration, TimeUnit unit, boolean fixDelay) {

    Cache<Object, Object> cache = caches.get(cacheName);
    if (null == cache) {
      if (fixDelay) {
        cache = CacheBuilder.newBuilder().maximumSize(maxSize).softValues().expireAfterWrite(duration, unit).build();
      } else {
        cache = CacheBuilder.newBuilder().maximumSize(maxSize).softValues().expireAfterAccess(duration, unit).build();
      }
      caches.put(cacheName, cache);
    }

    return cache;
  }
}
