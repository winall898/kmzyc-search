package com.kmzyc.search.app.util;

import java.util.HashMap;
import java.util.Map;

import com.kmzyc.search.config.Channel;


/**
 * 搜索提示词分词过滤器
 * 
 * @author zhoulinhong
 * @since 20160523
 *
 */
public class SuggestTermFilter {

    public static Map<Channel, BloomFilter> filter = new HashMap<Channel, BloomFilter>();

    public static BloomFilter getFilter(Channel channel) {

        BloomFilter bf = filter.get(channel);
        if (null == bf) {

            bf = new BloomFilter();
        }
        return bf;
    }

    public static void clean(Channel channel) {

        filter.remove(channel);
    }
}
