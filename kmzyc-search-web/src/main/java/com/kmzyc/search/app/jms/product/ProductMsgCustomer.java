package com.kmzyc.search.app.jms.product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kmzyc.search.app.jms.MessageCustomer;
import com.kmzyc.search.app.service.IndexService;

/**
 * 产品MQ消息消费处理
 * 
 * @author zhoulinhong
 * @since 20160510
 */
@Component("productMsgCustomer")
public class ProductMsgCustomer implements MessageCustomer {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMsgCustomer.class);

    @Resource(name = "indexService")
    private IndexService indexService;

    @SuppressWarnings("unchecked")
    @Override
    public void consume(Map<String, Object> msgData) {

        String operation = (String) msgData.get(ProductMsg.opType.name());

        if (StringUtils.isBlank(operation)) {

            LOG.error("消息中未设置索引操作类型，无法进行索引具体操作。");
            return;
        }

        List<Long> ids = (List<Long>) msgData.get(ProductMsg.ids.name());

        if (null == ids || ids.isEmpty()) {

            LOG.error("消息中未设置产品ID，无法查询产品系统信息。");
            return;
        }

        LOG.info("产品更新id集合 : [" + Arrays.toString(ids.toArray()) + "]");

        // 为了兼容MQ消息操作的错发，先进行删除操作(问题场景：b2b发送MQ消息原本是新增“add”操作，传过来的是“update”类型)
        if (Operation.valueOf(operation) == Operation.ADD) {
            // 新增
            indexService.addB2bIndex(ids);
        } else if (Operation.valueOf(operation) == Operation.UPDATE) {
            // 修改
            indexService.updateB2bIndex(ids);
        } else if (Operation.valueOf(operation) == Operation.DELETE) {
            // 删除
            indexService.deleteB2bIndex(ids);
        }
    }

    public IndexService getIndexService() {
        return indexService;
    }

    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    public enum Operation {
        ADD, // 添加索引
        UPDATE, // 更新
        DELETE // 删除
    }

    public enum ProductMsg {
        ids, // SKU id
        opType // 索引操作
    }
}
