package com.kmzyc.search.config;

import org.apache.curator.framework.CuratorFramework;

/**
 * ZK集群启动监听接口
 * 
 * @author river
 * 
 */
public interface ZKStartListener {
  /**
   * 执行
   * 
   * @param client
   */
  void executor(CuratorFramework client);
}
