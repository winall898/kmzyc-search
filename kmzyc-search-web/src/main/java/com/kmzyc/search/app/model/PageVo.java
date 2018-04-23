package com.kmzyc.search.app.model;

/**
 * 分页对象
 * 
 * @author KM
 *
 */
public class PageVo {

  /**
   * 页码
   */
  private int pageNo = 1;
  /**
   * 每页显示大小
   */
  private int pageSize = 10;
  /**
   * 总页数
   */
  private int pageCount = 1;
  /**
   * 总数据量
   */
  private int recordCount = 0;
  /**
   * 开始下标
   */
  private int startIndex = 0;
  /**
   * 结束下标
   */
  private int endIndex = 0;

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public int getRecordCount() {
    return recordCount;
  }

  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public void setEndIndex(int endIndex) {
    this.endIndex = endIndex;
  }

}
