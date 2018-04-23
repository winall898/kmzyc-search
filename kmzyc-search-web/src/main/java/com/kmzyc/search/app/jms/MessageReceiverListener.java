package com.kmzyc.search.app.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.km.framework.mq.bean.KmMsg;
import com.kmzyc.search.app.jms.product.ProductMsgDelegate;
import com.kmzyc.search.app.jms.product.PromotionMsgDelegate;
import com.kmzyc.search.app.jms.product.ShopMsgDelegate;

public class MessageReceiverListener implements MessageListener {

  private static final Logger LOG = LoggerFactory.getLogger(ProductMsgDelegate.class);

  @Override
  public void onMessage(Message message) {

    if (message == null) {

      LOG.error("接收MQ消息为空！");
      return;
    }

    if (message instanceof ObjectMessage) {
      ObjectMessage objectMessage = (ObjectMessage) message;
      KmMsg kmMsg = null;
      try {
        kmMsg = (KmMsg) objectMessage.getObject();
        String msgCode = kmMsg.getMsgCode();
        switch (msgCode) {
          case "2000":
            // 接收编码2000报文---产品
            ProductMsgDelegate.consume(kmMsg);
            break;
          case "2001":
            // 接收编码2001报文---促销
            PromotionMsgDelegate.consume(kmMsg);
            break;
          case "2004":
            // 接收编码2004报文---店铺
            ShopMsgDelegate.consume(kmMsg);
            break;
          default:
            break;
        }
      } catch (Exception e) {
        LOG.error("接收MQ消息发生异常！", e);
      }
    }
  }
}
