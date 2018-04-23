package com.kmzyc.search.config;


import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CuratorFrameworkZKClientFactory
    implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private CuratorFramework zkClient;

  private String zkConnectionString;

  private List<ZKStartListener> listeners;

  private int connectionTimeoutMs = 10000;

  private int sessionTimeoutMs = 10000;

  private int baseSleepTimeMs = 1000;

  private int maxRetries = 3;

  // 设置Zookeeper启动后需要调用的监听或者，或者需要做的初始化工作。
  public void setListeners(List<ZKStartListener> listeners) {
    this.listeners = listeners;
  }

  // 设置ZK链接串
  public void setZkConnectionString(String zkConnectionString) {
    this.zkConnectionString = zkConnectionString;
  }

  @Override
  public CuratorFramework getObject() {
    return zkClient;
  }

  @Override
  public Class<?> getObjectType() {
    return CuratorFramework.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void destroy() throws Exception {
    zkClient.close();
  }

  // 创建ZK链接
  @Override
  public void afterPropertiesSet() {
    // 1000 是重试间隔时间基数，3 是重试次数
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
    zkClient =
        createWithOptions(zkConnectionString, retryPolicy, connectionTimeoutMs, sessionTimeoutMs);
    registerListeners(zkClient);
    zkClient.start();
  }

  /**
   * 通过自定义参数创建
   */
  public CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy,
      int connectionTimeoutMs, int sessionTimeoutMs) {
    return CuratorFrameworkFactory.builder().connectString(connectionString)
        .retryPolicy(retryPolicy).connectionTimeoutMs(connectionTimeoutMs)
        .sessionTimeoutMs(sessionTimeoutMs).build();
  }

  // 注册需要监听的监听者对像.
  private void registerListeners(CuratorFramework client) {
    client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
      @Override
      public void stateChanged(CuratorFramework client, ConnectionState newState) {
        logger.info("CuratorFramework state changed: {}", newState);
        if ((newState == ConnectionState.CONNECTED || newState == ConnectionState.RECONNECTED)
            && null != listeners && !listeners.isEmpty()) {
          for (ZKStartListener listener : listeners) {
            listener.executor(client);
            logger.info("Listener {} executed!", listener.getClass().getName());
          }
        }
      }
    });

    client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
      @Override
      public void unhandledError(String message, Throwable e) {
        logger.info("CuratorFramework unhandledError: {}", message);
      }
    });
  }

  public void setConnectionTimeoutMs(int connectionTimeoutMs) {
    this.connectionTimeoutMs = connectionTimeoutMs;
  }

  public void setSessionTimeoutMs(int sessionTimeoutMs) {
    this.sessionTimeoutMs = sessionTimeoutMs;
  }

  public void setBaseSleepTimeMs(int baseSleepTimeMs) {
    this.baseSleepTimeMs = baseSleepTimeMs;
  }

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }
}
