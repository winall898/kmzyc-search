package com.kmzyc.search.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;

public class Configuration {

  private static Properties properties = new Properties();

  /**
   * 私有构造函数
   */
  private Configuration() {

  }

  static void put(Object key, Object value) {
    properties.put(key, value);
  }

  public static synchronized void load(InputStream in) throws IOException {
    properties.load(in);
  }

  public static String getString(String key) {
    return properties.getProperty(key);
  }

  /**
   * KEY=VALUE,value不为数字时，默认返回0
   * 
   * @param key
   * @return int
   */
  public static int getInt(String key) {
    String value = getString(key);
    return NumberUtils.toInt(value);
  }

  public static int getInt(String key, int defaultValue) {
    String value = getString(key);
    if (NumberUtils.isNumber(value)) {
      return NumberUtils.toInt(value);
    }
    return defaultValue;
  }

  /**
   * KEY=VALUE,value不为数字时，默认返回0
   * 
   * @param key
   * @return long
   */
  public static long getLong(String key) {
    String value = getString(key);
    return NumberUtils.toLong(value);
  }

  public static long getLong(String key, long defaultValue) {
    String value = getString(key);
    if (NumberUtils.isNumber(value)) {
      return NumberUtils.toLong(value);
    }
    return defaultValue;
  }

  /**
   * KEY=VALUE,value不为数字时，默认返回0
   * 
   * @param key
   * @return double
   */
  public static double getDouble(String key) {
    String value = getString(key);
    return NumberUtils.toDouble(value);
  }

  public static double getDouble(String key, double defaultValue) {
    String value = getString(key);
    if (NumberUtils.isNumber(value)) {
      return NumberUtils.toDouble(value);
    }
    return defaultValue;
  }

  /**
   * KEY=VALUE,value不为数字时，默认返回0
   * 
   * @param key
   * @return float
   */
  public static double getFloat(String key) {
    String value = getString(key);
    return NumberUtils.toFloat(value);
  }

  public static double getFloat(String key, double defaultValue) {
    String value = getString(key);
    if (NumberUtils.isNumber(value)) {
      return NumberUtils.toFloat(value);
    }
    return defaultValue;
  }

}
