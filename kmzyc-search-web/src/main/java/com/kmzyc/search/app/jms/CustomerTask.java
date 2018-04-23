package com.kmzyc.search.app.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.search.app.model.Message;

/**
 * 消息消费线程
 * 
 * @author zhoulinhong
 * @since 20160510
 * 
 */
public class CustomerTask implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(CustomerTask.class);

  private final Message message;

  public CustomerTask(Message message) {
    this.message = message;
  }

  @Override
  public void run() {
    try {
      MessageCustomer customer = message.getQueue().getCustomerClass();
      customer.consume(message.getMsgData());
      customer = null;
    } catch (Exception e) {
      LOG.error("处理{}消息时发生异常。", message.getQueue(), e);
    }
  }

}
