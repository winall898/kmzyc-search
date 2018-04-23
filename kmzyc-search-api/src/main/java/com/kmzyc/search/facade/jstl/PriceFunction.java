package com.kmzyc.search.facade.jstl;

/**
 * 自定义价格格式化JSTL标签
 * 
 * @author river
 * 
 */
public class PriceFunction
{

	/**
	 * 获取小数点前数字
	 * 
	 * @param price
	 * @return
	 */
	public static String head(Double price)
	{
		String priceVal = price.toString();
		int index = priceVal.indexOf(".");
		if (index < 0)
		{
			return priceVal;
		}

		return priceVal.substring(0, index);
	}

	/**
	 * 获取小数点后数字
	 * 
	 * @param price
	 * @return 返回至少两位数，不够补0
	 */
	public static String last(Double price)
	{
		String priceVal = price.toString();
		int index = priceVal.indexOf(".");
		if (index < 0)
		{
			return "";
		}
		String last = priceVal.substring(index, priceVal.length());
		if (last.length() < 3)
		{
			last += "0";
		}
		return last;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String formater(String source)
	{
		if (source.endsWith("-*"))
		{
			source = source.replace("-*", "以上");
		}
		return source;
	}
}
