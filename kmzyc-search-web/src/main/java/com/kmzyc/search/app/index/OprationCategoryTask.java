package com.kmzyc.search.app.index;

import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.search.app.service.impl.IndexServiceImpl;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.app.util.SpringBeanUtil;
import com.kmzyc.search.config.Channel;

/**
 * 运营类目索引创建任务线程
 * 
 * @author river
 * 
 */
public class OprationCategoryTask implements IndexTask {

    private static final Logger LOG = LoggerFactory.getLogger(OprationCategoryTask.class);

    private final Channel channel;

    public OprationCategoryTask(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        LOG.info("开始创建运营类目索引，索引所属频道为：{}", channel);
        try {
            // 对于索引库数据是否为空，为空则不需要进行运营类目的处理
            SearchHits searchHits = IndexClientUtil.getInstance().queryIndexByConditions(
                    channel.name(), channel.name(), "{\"query\":{\"match_all\":{}}}");
            if (null == searchHits || searchHits.getTotalHits() <= 0) {

                return;
            }

            // 执行运营类目的创建
            IndexServiceImpl indexService =
                    SpringBeanUtil.getBean("indexService", IndexServiceImpl.class);
            indexService.createOperationCategoryTree();
        } catch (Exception e) {
            LOG.error("全量添加商品{}运营类目索引失败。", channel, e);
        }
    }

}
