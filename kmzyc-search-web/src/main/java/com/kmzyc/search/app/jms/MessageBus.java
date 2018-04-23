package com.kmzyc.search.app.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.search.app.model.Message;

/**
 * 消息消费线程池
 * 
 * @author zhoulinhong
 * @since 20160510
 */
public class MessageBus {

  private static final Logger LOG = LoggerFactory.getLogger(MessageBus.class);

  private static ExecutorService service = Executors.newFixedThreadPool(10);

  public static void submit(Message message) {
    if (null == message.getMsgData() || message.getMsgData().isEmpty()) {

      LOG.warn("索引更新消息内容为空,无法解析消息。");
      return;
    }

    if (LOG.isDebugEnabled()) LOG.debug("收到" + message.getQueue() + "频道消息：" + message.getMsgData());
    service.submit(new CustomerTask(message));
  }
}
