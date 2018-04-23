package com.kmzyc.search.app.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.kmzyc.search.app.constant.RedisKeys;

public class RedisKeyUtils
{

	/**
	 * 获取 搜索词条 保存的KEY
	 * 
	 * @param channel
	 *            渠道
	 * @param source
	 *            词条内容
	 * @param account
	 *            用户ID
	 * @return
	 */
	public static String getKey(String channel, String source)
	{
		try
		{
			String md5 = MD5Util.getMD5Str(source);
			String key = channel + RedisKeys.SEARCH_REDIS_TERM_KEY_MID + md5;
			return key;
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		catch (UnsupportedEncodingException e)
		{
		}
		return null;
	}
}
