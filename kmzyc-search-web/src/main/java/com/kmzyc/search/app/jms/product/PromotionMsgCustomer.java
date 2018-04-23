package com.kmzyc.search.app.jms.product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kmzyc.search.app.jms.MessageCustomer;
import com.kmzyc.search.app.service.IndexService;
import com.kmzyc.search.config.Channel;


/**
 * 促销MQ消息消费处理
 * 
 * @author zhoulinhong
 * @since 20160510
 */
@Component("promotionMsgCustomer")
public class PromotionMsgCustomer implements MessageCustomer {

    private static final Logger LOG = LoggerFactory.getLogger(PromotionMsgCustomer.class);

    @Resource(name = "indexService")
    private IndexService indexService;

    @Override
    public void consume(Map<String, Object> msgData) {

        // 消费新版json格式
        consumeNewJson(msgData);
    }

    /**
     * 消费新版json格式
     * 
     * @author zhoulinhong
     * @param msgData
     */
    @SuppressWarnings({"unchecked"})
    private void consumeNewJson(Map<String, Object> msgData) {

        // 新：{code=2001, data={11910={price=10.0,dataType=0}, 11791={price=10.0,dataType=1}}}
        // 说明： code-消息编号 data:产品id:价格 dataType:类型（0普通 1促销 2预售）
        if (null == msgData || msgData.isEmpty()) {

            LOG.error("促销MQ消息内容为空!");
            return;
        }

        Object code = msgData.get("code");
        if (null == code) {

            LOG.error("促销消息code为空!");
            return;
        }

        // 获取促销MQ消息主体
        if (null == msgData.get("data")) {

            LOG.info("促销MQ消息主体data为空!");
            return;
        }

        // 获取商品集合
        Map<String, Map<String, Object>> data =
                (Map<String, Map<String, Object>>) msgData.get("data");

        if (null != data && !data.isEmpty()) {

            // 促销商品ID集合
            List<Object> ids = Arrays.asList(data.keySet().toArray());

            LOG.info("执行{}促销活动商品开始！", ids);

            // 更新促销索引信息
            indexService.updatePromotionIndex(data, Channel.b2b);

            LOG.info("执行{}促销活动商品完成！", ids);
        }
    }
}
