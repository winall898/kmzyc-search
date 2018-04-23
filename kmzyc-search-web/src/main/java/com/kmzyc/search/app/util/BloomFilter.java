package com.kmzyc.search.app.util;

import java.util.BitSet;

/**
 * 布隆过滤器实现
 */
public class BloomFilter
{
	private int		defaultSize	= 2 << 24;			// 4M

	private int		basic		= defaultSize - 1;

	private BitSet	bits		= null;

	public BloomFilter()
	{
		bits = new BitSet(defaultSize);
	}

	public BloomFilter(int bitsSize)
	{
		defaultSize = bitsSize << 12;
		basic = defaultSize - 1;
		bits = new BitSet(basic);
	}

	public boolean contains(String url)
	{
		if (url == null)
		{
			return true;
		}
		int pos1 = hash1(url);
		int pos2 = hash2(url);
		int pos3 = hash3(url);

		if (bits.get(pos1) && bits.get(pos2) && bits.get(pos3))
		{
			return true;
		}
		return false;
	}

	public void add(String url)
	{
		if (url == null)
		{
			return;
		}
		int pos1 = hash1(url);
		int pos2 = hash2(url);
		int pos3 = hash3(url);

		bits.set(pos1);
		bits.set(pos2);
		bits.set(pos3);
	}

	private int hash3(String line)
	{
		int h = 0;
		int len = line.length();
		for (int i = 0; i < len; i++)
		{
			h = 37 * h + line.charAt(i);
		}
		return check(h);
	}

	private int hash2(String line)
	{
		int h = 0;
		int len = line.length();
		for (int i = 0; i < len; i++)
		{
			h = 33 * h + line.charAt(i);
		}
		return check(h);
	}

	private int hash1(String line)
	{
		int h = 0;
		int len = line.length();
		for (int i = 0; i < len; i++)
		{
			h = 31 * h + line.charAt(i);
		}
		return check(h);
	}

	private int check(int h)
	{
		return basic & h;
	}

	public void clear()
	{
		if (null != bits)
		{
			bits.clear();
		}
	}

	/**
	 * 判断URL是否已重复 重复返回TRUE,否则返回false并将URL加入BITSET中。
	 * 
	 * @param url
	 * @return
	 */
	public synchronized boolean checkAndPut(String url)
	{
		if (contains(url)) return true;
		add(url);
		return false;
	}

	public void test()
	{
		String url = "石莲子的真伪鉴别要点 ";
		System.out.println(contains(url));
		add(url);
		System.out.println(contains(url));
	}

	public static void main(String arg[])
	{
		BloomFilter bf = new BloomFilter();
		bf.test();
	}
}