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
import org.springframework.jdbc.core.JdbcTemplate;

import com.kmzyc.search.app.util.SpringBeanUtil;

public class ShopPROJob {

    private static final Logger LOG = LoggerFactory.getLogger(ShopPROJob.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Boolean> valueOps;

    private static String SHOP_PRO_PRE = "search_shop_pro_";

    public void execute() {

        // 从redis取值，判断定时任务当日是否已经执行过。

        String key = SHOP_PRO_PRE + new SimpleDateFormat("yyyyMMdd").format(new Date());
        boolean flag = valueOps.setIfAbsent(key, true);
        if (!flag) {

            LOG.info("Shop PRO定时任务当日已被执行过！");
            return;
        }
        // 设置失效时间为10分钟
        template.expire(key, 10, TimeUnit.MINUTES);

        LOG.info("开始执行Shop PRO定时任务");
        JdbcTemplate jdbcTemplate = SpringBeanUtil.getBean("jdbcTemplate", JdbcTemplate.class);
        jdbcTemplate.execute("call PRO_SEARCH_SHOP_DATA_WHOLE()");
        LOG.info("Shop PRO定时任务执行完成");
    }

}
