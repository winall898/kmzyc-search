package com.kmzyc.search.app.service;

import java.util.List;

import com.kmzyc.search.app.model.KeywordVo;
import com.kmzyc.search.app.model.PageVo;

/**
 * 词库管理接口
 * 
 * @author zhoulinhong
 * @since 20160831
 */
public interface KeywordService {
  /**
   * 获取分词总数
   * 
   * @param userName
   * @return
   */
  public int getKeywordCount(String keyword);

  /**
   * 获取分词列表
   * 
   * @param keyword,page
   */
  public List<KeywordVo> getKeywordList(String keyword, PageVo page);

  /**
   * 根据ID获取分词
   * 
   * @param parseLong
   * @return
   */
  public KeywordVo getKeywordById(Long id);

  /**
   * 添加
   * 
   * @param keywordVo
   */
  public void addKeyword(KeywordVo keywordVo);

  /**
   * 修改
   * 
   * @param keywordVo
   */
  public void updateKeyword(KeywordVo keywordVo);

  /**
   * 删除分词
   * 
   * @param parseLong
   */
  public void deleteKeywordById(Long id);

  /**
   * 批量删除
   * 
   * @param ids
   */
  public void batchDeleteKeyword(List<Long> ids);

  /**
   * 判断分词是否已存在
   * 
   * @param keyword
   * @param id
   * @return
   */
  public boolean isExistKeyword(String keyword, String id);
}
