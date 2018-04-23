package com.kmzyc.search.app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kmzyc.search.app.model.SuggestItem;

/**
 * 数据访问对象接口
 * 
 * @since 2013-09-09
 * @author river
 */
@Repository
public interface SuggestItemDAO {

  /**
   * 插入数据
   * 
   * @param suggestItemDo
   * @author river
   * @return 插入数据的主键
   */
  public Integer insertSuggestItemDO(SuggestItem suggestItemDo);

  /**
   * 统计记录数
   * 
   * @param suggestItemDo
   * @author river
   * @return 查出的记录数
   */
  public Integer countSuggestItemDOByExample(SuggestItem suggestItemDo);

  /**
   * 更新记录
   * 
   * @param suggestItemDo
   * @author river
   * @return 受影响的行数
   */
  public Integer updateSuggestItemDO(SuggestItem suggestItemDo);

  /**
   * 获取对象列表
   * 
   * @param suggestItemDo
   * @author river
   * @return 对象列表
   */
  public List<SuggestItem> findListByExample(SuggestItem suggestItemDo);

  /**
   * 根据主键获取SuggestItemDO
   * 
   * @param id
   * @author river
   * @return suggestItemDo
   */
  public SuggestItem findSuggestItemDOByPrimaryKey(Integer id);

  /**
   * 删除记录
   * 
   * @param id
   * @author river
   * @return 受影响的行数
   */
  public Integer deleteSuggestItemDOByPrimaryKey(Integer id);

  /**
   * 分页查询
   * 
   * @param suggestItemDo
   * @return
   */
  public List<SuggestItem> findPageByVo(SuggestItem suggestItemDo);

}
