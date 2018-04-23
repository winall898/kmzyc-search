package com.kmzyc.search.app.service;

import java.util.List;

import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.StopwordVo;

/**
 * 词库管理接口
 * 
 * @author zhoulinhong
 * @since 20160831
 */
public interface StopwordService {
  /**
   * 获取分词总数
   * 
   * @param userName
   * @return
   */
  public int getStopwordCount(String stopword);

  /**
   * 获取分词列表
   * 
   * @param stopword,page
   */
  public List<StopwordVo> getStopwordList(String stopword, PageVo page);

  /**
   * 根据ID获取分词
   * 
   * @param parseLong
   * @return
   */
  public StopwordVo getStopwordById(Long id);

  /**
   * 添加
   * 
   * @param stopwordVo
   */
  public void addStopword(StopwordVo stopwordVo);

  /**
   * 修改
   * 
   * @param stopwordVo
   */
  public void updateStopword(StopwordVo stopwordVo);

  /**
   * 删除分词
   * 
   * @param parseLong
   */
  public void deleteStopwordById(Long id);

  /**
   * 批量删除
   * 
   * @param ids
   */
  public void batchDeleteStopword(List<Long> ids);

  /**
   * 判断分词是否已存在
   * 
   * @param stopword
   * @param id
   * @return
   */
  public boolean isExistStopword(String stopword, String id);
}
