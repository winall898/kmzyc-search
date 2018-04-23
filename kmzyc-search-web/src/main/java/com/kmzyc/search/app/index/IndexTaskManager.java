package com.kmzyc.search.app.index;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kmzyc.search.app.config.Configuration;

/**
 * 索引任务管理类
 * 
 * @author river
 * 
 */
public class IndexTaskManager
{

	private static final int		THREAD_COUNT	= Integer
															.parseInt(Configuration
																	.getContextProperty("indexThread"));

	private static ExecutorService	service			= Executors
															.newFixedThreadPool(THREAD_COUNT);

	public static void invokeTask(IndexTask task)
	{
		service.submit(task);
	}

	public static void shutdown()
	{
		if (!service.isShutdown()) service.shutdown();
	}
}
