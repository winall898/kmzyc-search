package com.kmzyc.search.app.index;

import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.app.util.SuggestTermFilter;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.DocFieldName;

/**
 * SUGGEST索引创建线程
 * 
 * @author zhoulinhong
 * @since 20160523
 */
public class SuggestIndexTask implements IndexTask {

    private static final Logger LOG = LoggerFactory.getLogger(SuggestIndexTask.class);

    private static final int ROWS = 1000;

    private final Channel channel;

    public SuggestIndexTask(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {

        // 清空对应分词过滤器
        SuggestTermFilter.clean(channel);

        LOG.info("开始创建SUGGEST索引，索引所属频道为：" + channel);

        // 删除旧搜索提示词索引
        delOldIndex();

        try {
            if (channel == Channel.shop) {
                // 店铺搜索提示词
                ShopSugIndexCreater creater = new ShopSugIndexCreater(channel);
                creater.run();
            } else {
                // 商品搜索提示词
                SuggestIndexCreater creater = new SuggestIndexCreater(channel);
                creater.run();
            }
        } catch (Exception e) {

            LOG.error("创建SUGGEST索引失败。索引所属频道为：" + channel, e);
        }

        LOG.info("创建SUGGEST索引成功，索引所属频道为：" + channel);
    }

    /**
     * 根据渠道删除索引数据
     * 
     * @author zhoulinhong
     * @param channel
     */
    private void delOldIndex() {

        for (int i = 0;; i++) {

            // 查询语句
            JSONObject queryJson = JSONObject.parseObject("{\"query\":{\"match_all\":{}}}");

            // 索引ID集合
            List<String> indexIds = Lists.newArrayList();

            // 分页返回
            queryJson.put("from", i * ROWS);
            queryJson.put("size", ROWS);

            // 过滤
            queryJson.put("post_filter", JSONObject
                    .parse("{\"term\": {\"channel\": \"" + channel.name().toUpperCase() + "\"}}"));

            try {
                SearchHits searchHits = IndexClientUtil.getInstance().queryIndexByConditions(
                        Channel.suggest.name(), Channel.suggest.name(), queryJson.toJSONString());

                if (null == searchHits || searchHits.getHits().length <= 0) {

                    break;
                }

                for (SearchHit searchHit : searchHits.getHits()) {

                    Map<String, Object> hitSource = searchHit.getSource();
                    indexIds.add(hitSource.get(DocFieldName.ID).toString());
                }

                if (null == indexIds || indexIds.isEmpty()) {

                    break;
                }

                // 删除处理
                IndexClientUtil.getInstance().batchDeleteIndex(channel.name(), channel.name(),
                        indexIds);

            } catch (Exception e) {

                LOG.error("删除" + channel + " 渠道的旧索引失败. query: " + queryJson.toJSONString(), e);
                break;
            }
        }

        LOG.info("删除旧的SUGGEST索引成功，索引所属频道为：" + channel);
    }
}
