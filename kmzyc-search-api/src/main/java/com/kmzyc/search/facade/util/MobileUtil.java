package com.kmzyc.search.facade.util;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Sets;

public class MobileUtil
{

	/** Wap网关特有和某些手机终端浏览器特有的头信息 */
	private final static Set<String>	mobileHeaders			= Sets.newHashSet();
	static
	{
		mobileHeaders.add("HTTP_X_UP_CALLING_LINE_ID");
		mobileHeaders.add("x-up-calling-line-id");
		mobileHeaders.add("x-wap-profile");
		mobileHeaders.add("X_WAP_PROFILE");
		mobileHeaders.add("X-Nokia-MusicShop-Bearer");
		mobileHeaders.add("X-Nokia-MusicShop-Version");
		mobileHeaders.add("x-up-bear-type");
		mobileHeaders.add("X-Nokia-BEARER");
		mobileHeaders.add("X-Nokia-Gateway-Id");
		mobileHeaders.add("X-Nokia-MSISDN");
		mobileHeaders.add("x-source-id");
		mobileHeaders.add("X-Nokia-CONNECTION_MODE");
		mobileHeaders.add("X-Nokia-MaxDownlinkBitrate");
		mobileHeaders.add("X-Nokia-MaxUplinkBitrate");
		mobileHeaders.add("Bearer-Indication");
		mobileHeaders.add("X_NETWORK_INFO");
		mobileHeaders.add("x-NAS-Identifier");
		mobileHeaders.add("x-online-host");
		mobileHeaders.add("X_WAP_CLIENTID");
		mobileHeaders.add("X-OperaMini-Features");
		mobileHeaders.add("X-OperaMini-Phone-UA");
		mobileHeaders.add("X-OperaMini-Phone");
		mobileHeaders.add("X-OperaMini-UA");
		mobileHeaders.add("");
		mobileHeaders.add("");
		mobileHeaders.add("");
	}

	/** Wap网关Via头信息中特有的描述信息 */
	private final static Set<String>	mobileGateWayHeaders	= Sets.newHashSet();
	static
	{
		mobileGateWayHeaders.add("ZXWAP");
		mobileGateWayHeaders.add("chinamobile.com");
		mobileGateWayHeaders.add("monternet.com");
		mobileGateWayHeaders.add("infoX");
		mobileGateWayHeaders.add("XMS 724Solutions HTG");
		mobileGateWayHeaders.add("Bytemobile");

	}

	/** 电脑上的IE或Firefox浏览器等的User-Agent关键词 */
	private final static Set<String>	pcHeaders				= Sets.newHashSet();
	static
	{
		pcHeaders.add("Windows 98");
		pcHeaders.add("Windows ME");
		pcHeaders.add("Windows 2000");
		pcHeaders.add("Windows XP");
		pcHeaders.add("Windows NT");
		pcHeaders.add("Ubuntu");
	}

	/** 手机浏览器的User-Agent里的关键词 */
	private final static Set<String>	mobileUserAgents		= Sets.newHashSet();
	static
	{
		mobileUserAgents.add("Nokia");
		mobileUserAgents.add("SAMSUNG");
		mobileUserAgents.add("MIDP-2");
		mobileUserAgents.add("CLDC1.1");
		mobileUserAgents.add("SymbianOS");
		mobileUserAgents.add("MAUI");
		mobileUserAgents.add("UNTRUSTED/1.0");
		mobileUserAgents.add("Windows CE");
		mobileUserAgents.add("iPhone");
		mobileUserAgents.add("iPad");
		mobileUserAgents.add("Android");
		mobileUserAgents.add("BlackBerry");
		mobileUserAgents.add("UCWEB");
		mobileUserAgents.add("ucweb");
		mobileUserAgents.add("BREW");
		mobileUserAgents.add("J2ME");
		mobileUserAgents.add("YULONG");
		mobileUserAgents.add("YuLong");
		mobileUserAgents.add("COOLPAD");
		mobileUserAgents.add("TIANYU");
		mobileUserAgents.add("TY-");
		mobileUserAgents.add("K-Touch");
		mobileUserAgents.add("Haier");
		mobileUserAgents.add("DOPOD");
		mobileUserAgents.add("Lenovo");
		mobileUserAgents.add("LENOVO");
		mobileUserAgents.add("HUAQIN");
		mobileUserAgents.add("AIGO-");
		mobileUserAgents.add("CTC/1.0");
		mobileUserAgents.add("CTC/2.0");
		mobileUserAgents.add("CMCC");
		mobileUserAgents.add("DAXIAN");
		mobileUserAgents.add("MOT-");
		mobileUserAgents.add("SonyEricsson");
		mobileUserAgents.add("GIONEE");
		mobileUserAgents.add("HTC");
		mobileUserAgents.add("ZTE");
		mobileUserAgents.add("HUAWEI");
		mobileUserAgents.add("webOS");
		mobileUserAgents.add("GoBrowser");
		mobileUserAgents.add("IEMobile");
		mobileUserAgents.add("WAP2.0");
	}

	/**
	 * 根据当前请求的特征，判断该请求是否来自手机终端，主要检测特殊的头信息，以及user-Agent这个header
	 * 
	 * @param request
	 *            http请求
	 * @return 如果命中手机特征规则，则返回对应的特征字符串
	 */
	public static boolean isFromMobile(HttpServletRequest request)
	{// 识别当前请求，针对来自手机的请求则跳转到wap页面。
		return getFromMobileRule(request) != null;
	}

	public static String getFromMobileRule(HttpServletRequest request)
	{
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null)
		{
			for (String mheader : pcHeaders)
			{
				if (userAgent.contains(mheader))
				{
					return null;
				}
			}
		}

		for (String mheader : mobileHeaders)
		{// 只要存在网关特有的header，肯定是手机
			if (request.getHeader(mheader) != null
					&& request.getHeader(mheader).length() > 0)
			{
				return mheader;
			}
		}
		String via = request.getHeader("Via");
		if (via != null)
		{
			for (String mheader : mobileGateWayHeaders)
			{
				// 是不是wap网关的描述
				if (via.contains(mheader))
				{
					return mheader;
				}
			}

		}
		if (userAgent != null)
		{
			for (String mheader : mobileUserAgents)
			{
				if (userAgent.contains(mheader))
				{
					return mheader;
				}
			}
		}
		return null;
	}

}
