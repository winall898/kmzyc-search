package com.kmzyc.search.facade.util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.service.IOtherSystemService;

public class TagsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TagsUtil.class);

    private static Cache<Object, Object> cache =
            CacheUtil.getCache("tags", 10000, 6, TimeUnit.HOURS, true);

    /**
     * 获取商品标签
     * 
     * @param productId 产品ID
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getProductTags(final int productId) {
        String key = "b2b." + productId;
        try {
            return (List<String>) cache.get(key, new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    IOtherSystemService otherSystemService = ApplicationContextUtil
                            .getBean("otherSystemService", IOtherSystemService.class);
                    List<String> list = otherSystemService.getProductTags(productId);
                    if (list == null) {

                        list = Lists.newArrayList();
                    }
                    return list;
                }
            });
        } catch (ExecutionException e) {
            LOG.error("获取商品标签失败！skuid:{}", new Object[] {productId});
            LOG.error(e.getMessage());
        }

        return Lists.newArrayList();
    }
}
