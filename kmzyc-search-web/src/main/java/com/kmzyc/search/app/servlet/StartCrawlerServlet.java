package com.kmzyc.search.app.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.search.app.config.Configuration;
import com.kmzyc.search.app.dbcrawler.DBCrawler;

public class StartCrawlerServlet  extends HttpServlet {

	  private static final long serialVersionUID = 915075464342772144L;

	  private static final Logger LOG = LoggerFactory.getLogger(StartCrawlerServlet.class);

	  @Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String channel = req.getParameter("channel");
	    LOG.info("接收到 " + channel + " 爬虫抓取通知！");
	    if (StringUtils.isBlank(channel)){
	    	return;
	    };
	    try {
			new DBCrawler(channel).startCrawl();
		} catch (Exception e) {
			LOG.error(channel + " 爬虫抓取异常！",e);
		}
	    LOG.info( channel + "索引数据抓取完成，发送抓取完成通知");
	    try{
			String url = Configuration.getContextProperty(channel + ".notify.url");
			if (null == url){
				LOG.warn("全量索引任务完成通知的URL为空, 请检查kmconfig.properties配置文件的notify.url配置");
				return;
			}else{
				send(url);
			}
	    }catch (IOException e){
			LOG.error("发送全量索引任务完成通知失败。", e);
		}
	  }

	  @Override
	  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    doGet(req, resp);
	  }
	  
	  /**
		 * 发送HTTP请求
		 * 
		 * @param urlString
		 * @return 响映对象
		 * @throws IOException
		 */
		private void send(String link) throws IOException
		{
			LOG.info("开始发送DIH任务完成通知！URL: " + link);
			URL url = new URL(link);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.connect();
			int code = urlConnection.getResponseCode();
			LOG.info("DIH通知返回码为：" + code);
		}

	}
