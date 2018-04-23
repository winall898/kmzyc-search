package com.kmzyc.search.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kmzyc.search.app.model.KeywordVo;
import com.kmzyc.search.app.model.PageVo;

@Repository
public interface KeywordDao {

  /**
   * 获取分词总数
   * 
   * @param userName
   * @return
   */
  public int getKeywordCount(@Param("keyword") String keyword);

  /**
   * 获取分词列表
   * 
   * @param userName
   * @return
   */
  public List<KeywordVo> getKeywordList(@Param("keyword") String keyword,
      @Param("page") PageVo page);

  /**
   * 根据ID获取分词
   * 
   * @param id
   * @return
   */
  public KeywordVo getKeywordById(@Param("id") Long id);

  /**
   * 添加
   * 
   * @param keywordVo
   */
  public void addKeyword(@Param("keywordVo") KeywordVo keywordVo);

  /**
   * 修改
   * 
   * @param id
   */
  public void updateKeyword(@Param("keywordVo") KeywordVo keywordVo);

  /**
   * 根据ID删除分词
   * 
   * @param id
   */
  public void deleteKeywordById(@Param("id") Long id);

  /**
   * 根据ID集合批量删除
   * 
   * @param ids
   */
  public void batchDeleteKeyword(@Param("ids") List<Long> ids);

  /**
   * 根据分词获取数量
   * 
   * @param keyword
   * @param id
   * @return
   */
  public int getCountByKeyword(@Param("keyword") String keyword, @Param("id") String id);
}
