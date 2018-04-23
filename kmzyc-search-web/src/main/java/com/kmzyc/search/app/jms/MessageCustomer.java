package com.kmzyc.search.app.jms;

import java.util.Map;

/**
 * 消息消费者接口
 * 
 * @author zhoulinhong
 * @since 20160510
 */
public interface MessageCustomer {

  public void consume(Map<String, Object> message);
}
