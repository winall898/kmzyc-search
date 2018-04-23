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

import com.kmzyc.search.app.index.SuggestIndexTask;
import com.kmzyc.search.config.Channel;

public class B2BSuggestIndexJob {

    private static final Logger LOG = LoggerFactory.getLogger(B2BSuggestIndexJob.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Boolean> valueOps;

    private static String B2B_SUGGEST_PRE = "search_suggest_index_";

    public void execute() {

        // 从redis取值，判断定时任务当日是否已经执行过。
        String key = B2B_SUGGEST_PRE + new SimpleDateFormat("yyyyMMdd").format(new Date());
        boolean flag = valueOps.setIfAbsent(key, true);
        if (!flag) {

            LOG.info("suggest index定时任务当日已被执行过！");
            return;
        }
        // 设置失效时间为10分钟
        template.expire(key, 10, TimeUnit.MINUTES);

        LOG.info("开始执行suggest index任务");

        // 产品提示词
        new Thread(new SuggestIndexTask(Channel.b2b)).start();
        // 店铺提示词
        new Thread(new SuggestIndexTask(Channel.shop)).start();

        LOG.info("suggest index任务执行完成");

    }

}
