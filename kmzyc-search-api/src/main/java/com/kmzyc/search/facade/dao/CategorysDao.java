package com.kmzyc.search.facade.dao;

import org.springframework.stereotype.Repository;

/**
 * 数据访问对象接口
 * 
 * @since 20160808
 * @author zhoulinhong
 */
@Repository
public interface CategorysDao {

    /**
     * 根据ID获取类目名称
     * 
     * @param categoryId
     * @return
     */
    public String getCategoryNameById(Integer categoryId);

    /**
     * 根据ID获取店铺类目名称
     * 
     * @param categoryId
     * @return
     */
    public String getShopCategoryNameById(Integer shopCategoryId);

}
