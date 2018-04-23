package com.kmzyc.search.facade.dao;

import org.springframework.stereotype.Repository;

import com.kmzyc.search.facade.vo.SupplierInfo;

/**
 * 数据访问对象接口
 * 
 * @since 20160808
 * @author zhoulinhong
 */
@Repository
public interface ShopMainDao {

    /**
     * 根据店铺ID获取店铺信息
     * 
     * @param shopId
     * @return
     */
    public String getShopNameId(Integer shopId);

    /**
     * 根据供应商ID获取供应商信息
     * 
     * @param categoryId
     * @return
     */
    public SupplierInfo getSupplierInfoByShopId(Integer shopId);
}
