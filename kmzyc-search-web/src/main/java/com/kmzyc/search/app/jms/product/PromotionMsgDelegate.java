package com.kmzyc.search.app.jms.product;

import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.km.framework.mq.bean.KmMsg;
import com.kmzyc.search.app.jms.MessageBus;
import com.kmzyc.search.app.jms.MessageQueue;
import com.kmzyc.search.app.model.Message;


/**
 * 促销MQ消息接收
 * 
 * @author zhoulinhong
 * @since 20160510
 */
@Component
public class PromotionMsgDelegate extends SearchCmdsBase {

  private static final Logger LOG = LoggerFactory.getLogger(PromotionMsgDelegate.class);

  /**
   * 接收到MQ消息后处理方法
   * 
   * @param kmMsg
   * @throws SQLException
   */
  public static void consume(KmMsg kmMsg) {

    if (null != kmMsg) {
      try {
        String code = kmMsg.getMsgCode();
        Map<String, Object> data = kmMsg.getMsgData();
        LOG.info(">>>.>>>.>>> 接收到 code: " + code + " 的消息。");
        Message message = new Message();
        message.setQueue(MessageQueue.product_promotion);
        data.put("code", code);
        message.setMsgData(data);
        MessageBus.submit(message);
      } catch (Exception e) {
        LOG.error("处理商品促销活动消息时发生异常。", e);
      }
    }
  }

}
