package com.kmzyc.search.facade.model;

import java.io.Serializable;
import java.util.List;

/**
 * 导航属性
 * 
 * @author river
 * 
 */
public class NavField implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5044741202997599837L;

	public static final String	PHYSICS				= "PHYSICS";				// 物理
	public static final String	OPERATE				= "OPERATE";				// 运营
	private Integer				id;
	private String				name;											// 中文名称
	private int					sortno;										// 从左到右排列，值越小排在越左边。
	private List<NavField>		value;

	private String				parent;

	private String				code;											// 编码：
																				// 索引字段名称
	private String				type;											// 运营
																				// OR
																				// 物理

	private String				sortVal;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<NavField> getValue()
	{
		return value;
	}

	public void setValue(List<NavField> value)
	{
		this.value = value;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public int getSortno()
	{
		return sortno;
	}

	public void setSortno(int sortno)
	{
		this.sortno = sortno;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getParent()
	{
		return parent;
	}

	public void setParent(String parent)
	{
		this.parent = parent;
	}

	public String getSortVal()
	{
		return sortVal;
	}

	public void setSortVal(String sortVal)
	{
		this.sortVal = sortVal;
	}

}