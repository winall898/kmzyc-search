package com.kmzyc.search.facade.service;

import java.util.List;
import java.util.Map;

import com.kmzyc.search.facade.vo.Brand;
import com.kmzyc.search.facade.vo.SupplierInfo;

/**
 * 搜索服务接口
 * 
 * @author river
 * 
 */
public interface SearchDBDataService {

    public Map<String, String> getOperationCategorySql(int oid);

    /**
     * 获取品牌详细信息
     * 
     * @param brandID
     * @return
     */
    public Brand getBrandDetails(int brandID);

    /**
     * 获取所有运营类目信息，保存为MAP key:id value：类目信息
     * 
     * @param channel
     * @return
     */
    public Map<String, Map<String, String>> getAllOperationCategoryMap();

    /**
     * 获取所有运营类目信息，保存为MAP key:层级 value：类目信息
     * 
     * @param channel
     * @return
     */
    public Map<String, List<Map<String, String>>> getCategoryGroupByLevel();

    /**
     * 获取某个运营类目树信息
     * 
     * @param oid 运营类目ID
     * @return
     */
    public List<Map<String, Object>> getOperationCategoryTree(int oid);

    /**
     * 获取所有运营类目
     * 
     * @param
     * @return
     */
    public List<Map<String, String>> getCategoryByChannel();

    /**
     * 根据ID获取类目名称
     * 
     * @param
     * @return
     */
    public String getCategoryNameById(Integer categoryId);

    /**
     * 根据店铺ID获取店铺信息
     * 
     * @param shopId
     * @return
     */
    public String getShopNameId(Integer shopId);

    /**
     * 根据店铺ID获取供应商信息
     * 
     * @param shopId
     * @return
     */
    public SupplierInfo getSupplierInfoByShopId(Integer shopId);

    /**
     * 根据ID获取店铺类目名称
     * 
     * @param shopCategoryId
     * @return
     */
    public String getShopCategoryNameById(Integer shopCategoryId);
}
