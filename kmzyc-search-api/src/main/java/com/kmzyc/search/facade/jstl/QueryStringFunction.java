package com.kmzyc.search.facade.jstl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 自定义价格格式化JSTL标签
 * 
 * @author river
 * 
 */
public class QueryStringFunction
{

	/**
	 * 替换查询参数值
	 * 
	 * @param queryString
	 *            查询URL
	 * @param paramName
	 *            参数名称
	 * @param paramValue
	 *            参数值
	 * @return 新查询URL
	 */
	public static String replace(String queryString, String paramName,
			String paramValue)
	{
		queryString = remove(queryString, paramName);
		return add(queryString, paramName, paramValue);
	}

	/**
	 * 添加查询参数值
	 * 
	 * @param queryString
	 *            查询URL
	 * @param paramName
	 *            参数名称
	 * @param paramValue
	 *            参数值
	 * @return 新查询URL
	 */
	public static String add(String queryString, String paramName,
			String paramValue)
	{
		queryString = StringEscapeUtils.unescapeHtml4(queryString);
		paramName = StringEscapeUtils.unescapeHtml4(paramName);
		paramValue = StringEscapeUtils.unescapeHtml4(paramValue);

		if (queryString.indexOf("?") > -1)
		{
			queryString += "&" + paramName + "=" + paramValue;
		}
		else
		{
			queryString += "?" + paramName + "=" + paramValue;
		}
		
		//格式处理
        if(queryString.contains("?&")){
          queryString = queryString.replace("?&", "?");
        }
        
		return queryString;
	}

	/**
	 * 删除查询参数值
	 * 
	 * @param queryString
	 *            查询URL
	 * @param paramName
	 *            参数名称
	 * @return 新查询URL
	 */
	public static String remove(String queryString, String paramName)
	{
		queryString = StringEscapeUtils.unescapeHtml4(queryString);
		paramName = StringEscapeUtils.unescapeHtml4(paramName);

		Pattern pattern = Pattern
				.compile("(\\?|&+)(" + paramName + ")=([^&]*)");
		Matcher matcher = pattern.matcher(queryString);
		if (matcher.find())
		{
			String matcherString = matcher.group();
			if (matcherString.indexOf("?") > -1)
			{
				queryString = queryString.replace(matcherString, "?");
			}
			else
			{
				queryString = queryString.replace(matcherString, "");
			}

		}
		return queryString;
	}

	/**
	 * 清除所有查询参数
	 * 
	 * @param queryString
	 * @return
	 */
	public static String clearParams(String queryString)
	{
		queryString = StringEscapeUtils.unescapeHtml4(queryString);
		int index = queryString.indexOf("?");
		if (index > 0)
		{
			return queryString.substring(0, index);
		}
		return queryString;
	}

	public static String join(String sprator, String params1, String params2)
	{
		sprator = StringEscapeUtils.unescapeHtml4(sprator);
		params1 = StringEscapeUtils.unescapeHtml4(params1);
		params2 = StringEscapeUtils.unescapeHtml4(params2);
		return params1 + sprator + params2;
	}

	public static void main(String[] args) throws Exception
	{
		String queryString = "10/shopProducts?shopid=143";
		// System.out.println(add(queryString, "sort", "1"));
		System.out.println(remove(queryString, "sort"));
		System.out.println(replace(queryString, "sort", "2"));
		String t = "&#x2f;10&#x2f;shopProducts&#x3f;shopid&#x3d;143";
		System.out.println(StringEscapeUtils.unescapeHtml4(t));
	}
}
