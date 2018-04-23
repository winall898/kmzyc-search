package com.kmzyc.search.app.schedul;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.kmzyc.search.app.config.Configuration;
import com.kmzyc.search.app.dbcrawler.DBCrawler;

/**
 * 产品全量索引任务
 * 
 * @author river
 * 
 */
public class B2BDIHJob {

    private static final Logger LOG = LoggerFactory.getLogger(B2BDIHJob.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Boolean> valueOps;

    private static String B2B_DIH_PRE = "search_b2b_dih_";

    public void execute() {

        // 从redis取值，判断定时任务当日是否已经执行过。
        String key = B2B_DIH_PRE + new SimpleDateFormat("yyyyMMdd").format(new Date());
        boolean flag = valueOps.setIfAbsent(key, true);
        if (!flag) {

            LOG.info("B2B DIH定时任务当日已被执行过！");
            return;
        }
        // 设置失效时间为10分钟
        template.expire(key, 10, TimeUnit.MINUTES);

        LOG.info("开始执行B2B DIH定时任务");
        try {
            new DBCrawler("b2b").startCrawl();
        } catch (Exception e) {
            LOG.error("全量索引创建任务执行失败。", e);
        }
        LOG.info("B2B DIH定时任务执行完成");
        try {
            String url = Configuration.getContextProperty("b2b.notify.url");
            if (null == url) {
                LOG.warn("全量索引任务完成通知的URL为空, 请检查kmconfig.properties配置文件的notify.url配置");
                return;
            } else {
                send(url);
            }
        } catch (IOException e) {
            LOG.error("发送全量索引任务完成通知失败。", e);
        }
    }

    /**
     * 发送HTTP请求
     * 
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    private void send(String link) throws IOException {
        LOG.info("开始发送DIH任务完成通知！URL: " + link);
        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.connect();
        int code = urlConnection.getResponseCode();
        LOG.info("DIH通知返回码为：" + code);
    }
}
