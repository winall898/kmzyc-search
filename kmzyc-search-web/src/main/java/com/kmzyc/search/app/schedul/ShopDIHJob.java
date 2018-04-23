package com.kmzyc.search.app.schedul;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.kmzyc.search.app.dbcrawler.DBCrawler;

/**
 * 产品全量索引任务
 * 
 * @author river
 * 
 */
public class ShopDIHJob {

    private static final Logger LOG = LoggerFactory.getLogger(ShopDIHJob.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Boolean> valueOps;

    private static String SHOP_DIH_PRE = "search_shop_dih_";

    public void execute() {

        // 从redis取值，判断定时任务当日是否已经执行过。
        String key = SHOP_DIH_PRE + new SimpleDateFormat("yyyyMMdd").format(new Date());
        boolean flag = valueOps.setIfAbsent(key, true);
        if (!flag) {

            LOG.info("Shop DIH定时任务当日已被执行过！");
            return;
        }
        // 设置失效时间为10分钟
        template.expire(key, 10, TimeUnit.MINUTES);

        LOG.info("开始执行SHOP DIH定时任务");
        try {
            new DBCrawler("shop").startCrawl();
        } catch (Exception e) {
            LOG.error("全量索引创建任务执行失败。", e);
        }
        LOG.info("SHOP DIH定时任务执行完成");
    }
}
