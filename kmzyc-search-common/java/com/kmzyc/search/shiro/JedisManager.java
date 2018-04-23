package com.kmzyc.search.shiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * jedis manager
 * 
 * @author michael
 */
public class JedisManager {

  private static Logger logger = LoggerFactory.getLogger(JedisManager.class);

  private JedisPool jedisPool;

  public Jedis getJedis() {
    Jedis jedis = null;
    try {
      jedis = getJedisPool().getResource();
    } catch (Exception e) {
      throw new JedisConnectionException(e);
    }
    return jedis;
  }

  public void returnResource(Jedis jedis, boolean isBroken) {
    if (jedis == null) {

      return;
    }
    if (isBroken) {
      getJedisPool().returnBrokenResource(jedis);
    } else {
      getJedisPool().returnResource(jedis);
    }
  }

  public byte[] getValueByKey(int dbIndex, byte[] key) {
    Jedis jedis = null;
    byte[] result = null;
    boolean isBroken = false;
    try {
      jedis = getJedis();
      jedis.select(dbIndex);
      result = jedis.get(key);
    } catch (Exception e) {
      isBroken = true;
      logger.error("getValueByKey方法异常！", e);
    } finally {
      returnResource(jedis, isBroken);
    }
    return result;
  }

  public void deleteByKey(int dbIndex, byte[] key) {
    Jedis jedis = null;
    boolean isBroken = false;
    try {
      jedis = getJedis();
      jedis.select(dbIndex);
      jedis.del(key);
    } catch (Exception e) {
      isBroken = true;
      logger.error("getValueByKey方法异常！", e);
    } finally {
      returnResource(jedis, isBroken);
    }
  }

  public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime) {
    Jedis jedis = null;
    boolean isBroken = false;
    try {
      jedis = getJedis();
      jedis.select(dbIndex);
      jedis.set(key, value);
      if (expireTime > 0) {
        jedis.expire(key, expireTime);
      }
    } catch (Exception e) {
      isBroken = true;
    } finally {
      returnResource(jedis, isBroken);
    }
  }

  public JedisPool getJedisPool() {
    return jedisPool;
  }

  public void setJedisPool(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }
}
