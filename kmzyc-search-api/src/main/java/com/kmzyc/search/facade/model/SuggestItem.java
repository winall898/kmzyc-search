package com.kmzyc.search.facade.model;

import java.io.Serializable;

/**
 * 搜索提示词数据对象
 * 
 * @author zhoulinhong
 * @since 20160530
 */
public class SuggestItem implements Serializable {

  private static final long serialVersionUID = 137869275284172932L;

  /**
   * column APP_QUERY_TERM.ID
   */
  private Integer id;

  /**
   * column APP_QUERY_TERM.SOURCE
   */
  private String source;

  /**
   * column APP_QUERY_TERM.TERM
   */
  private String term;

  /**
   * column APP_QUERY_TERM.PINYIN
   */
  private String pinyin;

  /**
   * column APP_QUERY_TERM.JIANPIN
   */
  private String jianpin;

  /**
   * column APP_QUERY_TERM.COUNT
   */
  private Integer count;

  /**
   * column APP_QUERY_TERM.SORT
   */
  private Integer sort;

  private String channel;

  private String docId;

  // 分页查询参数
  int skip;
  int max;

  public SuggestItem() {
    super();
  }

  public SuggestItem(Integer id, String source, String term, String pinyin, String jianpin, Integer count, Integer sort) {
    this.id = id;
    this.source = source;
    this.term = term;
    this.pinyin = pinyin;
    this.jianpin = jianpin;
    this.count = count;
    this.sort = sort;
  }

  /**
   * getter for Column APP_QUERY_TERM.ID
   */
  public Integer getId() {
    return id;
  }

  /**
   * setter for Column APP_QUERY_TERM.ID
   * 
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * getter for Column APP_QUERY_TERM.SOURCE
   */
  public String getSource() {
    return source;
  }

  /**
   * setter for Column APP_QUERY_TERM.SOURCE
   * 
   * @param source
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * getter for Column APP_QUERY_TERM.TERM
   */
  public String getTerm() {
    return term;
  }

  /**
   * setter for Column APP_QUERY_TERM.TERM
   * 
   * @param term
   */
  public void setTerm(String term) {
    this.term = term;
  }

  /**
   * getter for Column APP_QUERY_TERM.PINYIN
   */
  public String getPinyin() {
    return pinyin;
  }

  /**
   * setter for Column APP_QUERY_TERM.PINYIN
   * 
   * @param pinyin
   */
  public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
  }

  /**
   * getter for Column APP_QUERY_TERM.JIANPIN
   */
  public String getJianpin() {
    return jianpin;
  }

  /**
   * setter for Column APP_QUERY_TERM.JIANPIN
   * 
   * @param jianpin
   */
  public void setJianpin(String jianpin) {
    this.jianpin = jianpin;
  }

  /**
   * getter for Column APP_QUERY_TERM.COUNT
   */
  public Integer getCount() {
    return count;
  }

  /**
   * setter for Column APP_QUERY_TERM.COUNT
   * 
   * @param count
   */
  public void setCount(Integer count) {
    this.count = count;
  }

  /**
   * getter for Column APP_QUERY_TERM.SORT
   */
  public Integer getSort() {
    return sort;
  }

  /**
   * setter for Column APP_QUERY_TERM.SORT
   * 
   * @param sort
   */
  public void setSort(Integer sort) {
    this.sort = sort;
  }

  public int getSkip() {
    return skip;
  }

  public void setSkip(int skip) {
    this.skip = skip;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public String getDocId() {
    return docId;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }
}
