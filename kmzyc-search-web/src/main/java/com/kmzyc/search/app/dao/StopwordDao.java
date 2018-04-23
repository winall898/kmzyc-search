package com.kmzyc.search.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.StopwordVo;

@Repository
public interface StopwordDao {

  /**
   * 获取分词总数
   * 
   * @param userName
   * @return
   */
  public int getStopwordCount(@Param("stopword") String stopword);

  /**
   * 获取分词列表
   * 
   * @param userName
   * @return
   */
  public List<StopwordVo> getStopwordList(@Param("stopword") String stopword,
      @Param("page") PageVo page);

  /**
   * 根据ID获取分词
   * 
   * @param id
   * @return
   */
  public StopwordVo getStopwordById(@Param("id") Long id);

  /**
   * 添加
   * 
   * @param stopwordVo
   */
  public void addStopword(@Param("stopwordVo") StopwordVo stopwordVo);

  /**
   * 修改
   * 
   * @param id
   */
  public void updateStopword(@Param("stopwordVo") StopwordVo stopwordVo);

  /**
   * 根据ID删除分词
   * 
   * @param id
   */
  public void deleteStopwordById(@Param("id") Long id);

  /**
   * 根据ID集合批量删除
   * 
   * @param ids
   */
  public void batchDeleteStopword(@Param("ids") List<Long> ids);

  /**
   * 根据分词获取数量
   * 
   * @param stopword
   * @param id
   * @return
   */
  public int getCountByStopword(@Param("stopword") String stopword, @Param("id") String id);
}
