package com.kmzyc.search.app.model;

import java.util.Date;

/**
 * 停止词对象
 * 
 * @author KM
 *
 */
public class StopwordVo {
  /**
   * ID
   */
  private Long id;
  /**
   * 分词
   */
  private String stopword;
  /**
   * 说明
   */
  private String description;
  /**
   * 创建人
   */
  private String creater;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 修改人
   */
  private String updater;
  /**
   * 创建时间
   */
  private Date updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStopword() {
    return stopword;
  }

  public void setStopword(String stopword) {
    this.stopword = stopword;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreater() {
    return creater;
  }

  public void setCreater(String creater) {
    this.creater = creater;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getUpdater() {
    return updater;
  }

  public void setUpdater(String updater) {
    this.updater = updater;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
