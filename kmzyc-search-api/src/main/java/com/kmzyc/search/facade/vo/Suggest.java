package com.kmzyc.search.facade.vo;

import java.io.Serializable;

/**
 * 搜索提示条
 * 
 * @author river
 * 
 */
public class Suggest implements Serializable {

  private static final long serialVersionUID = 5627337915843530292L;

  private String id; // id
  private String name; // 提示内容
  private long count; // 商品数量
  private String countView;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public String getCountView() {
    return countView;
  }

  public void setCountView(String countView) {
    this.countView = countView;
  }

}
