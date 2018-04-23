package com.kmzyc.search.facade.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {

  private static Logger logger = Logger.getLogger(HttpClientUtil.class);

  /**
   * 定义编码格式 UTF-8
   */
  public static final String UTF8 = "UTF-8";

  private static MultiThreadedHttpConnectionManager connectionManager = null;

  private static int connectionTimeOut = 25000;

  private static int socketTimeOut = 25000;

  private static int maxConnectionPerHost = 20;

  private static int maxTotalConnections = 20;

  private static HttpClient client;

  static {
    connectionManager = new MultiThreadedHttpConnectionManager();
    connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
    connectionManager.getParams().setSoTimeout(socketTimeOut);
    connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
    connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
    client = new HttpClient(connectionManager);
  }

  private HttpClientUtil() {

  }

  /**
   * POST方式提交数据
   * 
   * @param url 待请求的URL
   * @param params 要提交的数据
   * @param enc 编码
   * @return 响应结果
   * @throws IOException IO异常
   */
  public static String URLPost(String url, Map<String, String> params) {

    String response = "";
    PostMethod postMethod = null;
    try {
      postMethod = new PostMethod(url);
      postMethod.setRequestHeader("Content-Type",
          "application/x-www-form-urlencoded;charset=" + UTF8);
      // 将表单的值放入postMethod中
      Set<String> keySet = params.keySet();
      for (String key : keySet) {
        String value = params.get(key);
        postMethod.addParameter(key, value);
      }
      // 执行postMethod
      int statusCode = client.executeMethod(postMethod);
      if (statusCode == HttpStatus.SC_OK) {
        response = postMethod.getResponseBodyAsString();
      } else {
        logger.error("响应状态码 = " + postMethod.getStatusCode());
      }
    } catch (HttpException e) {
      logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
    } catch (IOException e) {
      logger.error("发生网络异常", e);
    } finally {
      if (postMethod != null) {
        postMethod.releaseConnection();
        postMethod = null;
      }
    }

    return response;
  }

  /**
   * POST方式提交数据
   * 
   * @param url 待请求的URL
   * @param content 要提交的数据
   * @return 响应结果
   * @throws IOException IO异常
   */
  public static String contentPost(String url, String content) {

    String response = "";
    PostMethod postMethod = null;
    try {
      postMethod = new PostMethod(url);
      postMethod.setRequestHeader("Content-Type",
          "application/x-www-form-urlencoded;charset=" + UTF8);
      postMethod.setRequestEntity(new StringRequestEntity(content, null, UTF8));
      // 执行postMethod
      int statusCode = client.executeMethod(postMethod);
      if (statusCode == HttpStatus.SC_OK) {
        response = postMethod.getResponseBodyAsString();
      } else {
        logger.error("响应状态码 = " + postMethod.getStatusCode());
      }
    } catch (HttpException e) {
      logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
    } catch (IOException e) {
      logger.error("发生网络异常", e);
    } finally {
      if (postMethod != null) {
        postMethod.releaseConnection();
        postMethod = null;
      }
    }

    return response;
  }


  /**
   * GET方式提交数据
   * 
   * @param url 待请求的URL
   * @param params 要提交的数据
   * @param enc 编码
   * @return 响应结果
   * @throws IOException IO异常
   */
  public static String URLGet(String url) {
    String response = "";
    GetMethod getMethod = null;
    try {
      getMethod = new GetMethod(url);
      getMethod.setRequestHeader("Content-Type",
          "application/x-www-form-urlencoded;charset=" + UTF8);
      // 执行getMethod
      int statusCode = client.executeMethod(getMethod);
      if (statusCode == HttpStatus.SC_OK) {
        response = getMethod.getResponseBodyAsString();
      } else {
        logger.debug("响应状态码 = " + getMethod.getStatusCode());
      }
    } catch (HttpException e) {
      logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
    } catch (IOException e) {
      logger.error("发生网络异常", e);
    } finally {
      if (getMethod != null) {
        getMethod.releaseConnection();
        getMethod = null;
      }
    }

    return response;
  }

  public static String URLGet(String url, Map<String, String> params) {

    String response = "";
    GetMethod getMethod = null;
    StringBuilder strtTotalURL = new StringBuilder();
    strtTotalURL.append(url).append("?").append(getUrl(params, UTF8));
    logger.debug("GET请求URL = \n" + strtTotalURL.toString());
    try {
      getMethod = new GetMethod(strtTotalURL.toString());
      getMethod.setRequestHeader("Content-Type",
          "application/x-www-form-urlencoded;charset=" + UTF8);
      // 执行getMethod
      int statusCode = client.executeMethod(getMethod);
      if (statusCode == HttpStatus.SC_OK) {
        response = getMethod.getResponseBodyAsString();
      } else {
        logger.debug("响应状态码 = " + getMethod.getStatusCode());
      }
    } catch (HttpException e) {
      logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
    } catch (IOException e) {
      logger.error("发生网络异常", e);
    } finally {
      if (getMethod != null) {
        getMethod.releaseConnection();
        getMethod = null;
      }
    }

    return response;
  }

  /**
   * 据Map生成URL字符串
   * 
   * @param map Map
   * @param valueEnc URL编码
   * @return URL
   */
  private static String getUrl(Map<String, String> map, String valueEnc) {

    if (null == map || map.keySet().isEmpty()) {

      return null;
    }
    StringBuilder url = new StringBuilder();
    Set<String> keys = map.keySet();
    for (String key : keys) {
      String val = map.get(key);
      if (StringUtils.isNotBlank(val)) {
        try {

          url.append(key).append("=").append(URLEncoder.encode(val, valueEnc)).append("&");
        } catch (UnsupportedEncodingException e) {
          logger.error("encode异常", e);
        }
      }
    }
    if (url.length() > 1) {
      return url.substring(0, url.length() - 1);
    }

    return "";
  }


}
