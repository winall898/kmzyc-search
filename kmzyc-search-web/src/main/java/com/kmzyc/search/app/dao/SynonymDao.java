package com.kmzyc.search.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.SynonymVo;

@Repository
public interface SynonymDao {

  /**
   * 获取分词总数
   * 
   * @param userName
   * @return
   */
  public int getSynonymCount(@Param("synonymVo") SynonymVo synonymVo);

  /**
   * 获取分词列表
   * 
   * @param userName
   * @return
   */
  public List<SynonymVo> getSynonymList(@Param("synonymVo") SynonymVo synonymVo,
      @Param("page") PageVo page);

  /**
   * 根据ID获取分词
   * 
   * @param id
   * @return
   */
  public SynonymVo getSynonymById(@Param("id") Long id);

  /**
   * 添加
   * 
   * @param synonymVo
   */
  public void addSynonym(@Param("synonymVo") SynonymVo synonymVo);

  /**
   * 修改
   * 
   * @param id
   */
  public void updateSynonym(@Param("synonymVo") SynonymVo synonymVo);

  /**
   * 根据ID删除分词
   * 
   * @param id
   */
  public void deleteSynonymById(@Param("id") Long id);

  /**
   * 根据ID集合批量删除
   * 
   * @param ids
   */
  public void batchDeleteSynonym(@Param("ids") List<Long> ids);

  /**
   * 根据原始词获取数量
   * 
   * @param synonym
   * @param id
   * @return
   */
  public int getCountByKeyword(@Param("keyword") String keyword, @Param("id") String id);
}
