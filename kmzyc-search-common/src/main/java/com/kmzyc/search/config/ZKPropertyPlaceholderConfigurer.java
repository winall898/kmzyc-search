package com.kmzyc.search.config;


import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * SPRING启动时从ZK集群加载配置文件
 * 
 * @author river
 * 
 */
public class ZKPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

  private static final Logger LOG = LoggerFactory.getLogger(ZKPropertyPlaceholderConfigurer.class);

  // ZooKeeper 客户端
  private CuratorFramework zkClient;

  // 配置文件存放节点路径
  private List<String> zkPaths;

  private boolean checkSystemProperties = true;

  @Override
  protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
      Properties props) {

    // 保存本地配置信息
    for (Object key : props.keySet()) {
      String keyStr = key.toString();
      String value = props.getProperty(keyStr);
      Configuration.put(keyStr, value);
    }

    // 末指定ZK集群节点匹配，路过加载。
    if (null == zkPaths || zkPaths.isEmpty()) {
      // 执行 SPRING 后续工作
      super.processProperties(beanFactoryToProcess, props);
      return;
    }

    try {
      // 初始化zkClient
      initZkClient(props);
    } catch (Exception e) {
      // 初始化失败
      LOG.error("无法初始化zk客户端，加载ZooKeeper集群" + zkPaths + "节点配置信息失败！", e);
    }

    loadConfigFromZK(props);
    // 执行 SPRING 后续工作
    super.processProperties(beanFactoryToProcess, props);
    closeZKClient();
  }

  private void initZkClient(Properties props) {
    // 1000 是重试间隔时间基数，3 是重试次数
    String zkConnectionString = getPropertiesValue(props, "zk.host");
    String sleepTime = getPropertiesValue(props, "zk.sleepTime", "1000");
    int baseSleepTimeMs = Integer.parseInt(sleepTime);

    String retryTimes = getPropertiesValue(props, "zk.retryTimes", "3");
    int maxRetries = Integer.parseInt(retryTimes);

    String connTimeout = getPropertiesValue(props, "zk.connect.timeout", "10000");
    int connectionTimeoutMs = Integer.parseInt(connTimeout);

    String sesTimeout = getPropertiesValue(props, "zk.session.timeout", "10000");
    int sessionTimeoutMs = Integer.parseInt(sesTimeout);

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
    zkClient =
        createWithOptions(zkConnectionString, retryPolicy, connectionTimeoutMs, sessionTimeoutMs);
    zkClient.start();
  }

  private String getPropertiesValue(Properties props, String key) {
    String value = props.getProperty(key);
    if (StringUtils.isBlank(value) && checkSystemProperties) {
      // 尝试系统变量中找
      value = System.getProperty(key);
      if (StringUtils.isBlank(value)) {

        LOG.error("无法初始化zk客户端，未配置{}值！", key);
        return null;
      }
      props.put(key, value);
    }
    return value;
  }

  private String getPropertiesValue(Properties props, String key, String defaultValue) {
    String value = props.getProperty(key);
    if (StringUtils.isBlank(value) && checkSystemProperties) {
      // 尝试系统变量中找
      value = System.getProperty(key);
      if (StringUtils.isBlank(value)) {
        value = defaultValue;
      }
      props.put(key, value);
    }
    return value;
  }

  private void loadConfigFromZK(Properties props) {
    for (String path : zkPaths) {
      try {
        Stat stat = zkClient.checkExists().forPath(path);
        if (null != stat) {
          byte[] data = zkClient.getData().forPath(path);
          ByteArrayInputStream in = new ByteArrayInputStream(data, 0, data.length);
          props.load(in);
          LOG.info("加载ZK集群中{}配置文件成功", path);
        } else {
          LOG.warn("ZK集群不存在{}这一路径的节点", path);
        }
      } catch (Exception e) {

        LOG.error("加载ZooKeeper集群{}节点配置信息失败！", path, e);
      }
    }
    for (Object key : props.keySet()) {
      String keyStr = key.toString();
      String value = props.getProperty(keyStr);
      Configuration.put(keyStr, value);
    }
  }

  private void closeZKClient() {
    try {
      zkClient.close();
    } catch (Exception e) {

      zkClient = null;
      LOG.error("zkClient关闭时出现异常！", e);
    }
  }

  /**
   * 通过自定义参数创建
   */
  private CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy,
      int connectionTimeoutMs, int sessionTimeoutMs) {
    return CuratorFrameworkFactory.builder().connectString(connectionString)
        .retryPolicy(retryPolicy).connectionTimeoutMs(connectionTimeoutMs)
        .sessionTimeoutMs(sessionTimeoutMs).build();
  }

  public void setZkPaths(List<String> zkPaths) {
    this.zkPaths = zkPaths;
  }

  public void setCheckSystemProperties(boolean checkSystemProperties) {
    this.checkSystemProperties = checkSystemProperties;
  }

  public void setZkClient(CuratorFramework zkClient) {
    this.zkClient = zkClient;
  }

}
