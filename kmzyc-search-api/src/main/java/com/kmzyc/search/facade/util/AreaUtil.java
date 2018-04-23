package com.kmzyc.search.facade.util;

public class AreaUtil
{

	/**
	 * 根据地区字符串，返回地区数组值
	 * 
	 * @param areaText
	 *            字符串格式：全国,省,市,区
	 * @return [区,市,省,全国]
	 */
	public static int[] getAreas(String areaText)
	{
		String[] texts = areaText.split(",");
		int[] areaIds = new int[texts.length];
		int length = texts.length;
		int maxIndex = length - 1;
		int k = 0;
		for (int i = maxIndex; i >= 0; i--)
		{
			String text = texts[i];
			int id = Integer.parseInt(text);
			areaIds[k] = id;
			k++;
		}
		return areaIds;
	}
}
