package com.kmzyc.search.facade.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 类目
 * 
 * @author river
 * 
 */
public class Category implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7212979448201769708L;
	private int					id;
	private int					parentId;
	private String				name;
	private String				code;
	private List<Category>		children;
	private boolean				selected;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Category> getChildren()
	{
		return children;
	}

	public void setChildren(List<Category> children)
	{
		this.children = children;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public int getParentId()
	{
		return parentId;
	}

	public void setParentId(int parentId)
	{
		this.parentId = parentId;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

}
