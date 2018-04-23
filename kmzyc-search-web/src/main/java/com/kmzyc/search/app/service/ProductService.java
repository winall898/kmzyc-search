package com.kmzyc.search.app.service;

import java.util.List;
import java.util.Map;


public interface ProductService {

    public String getOperationCategorySql(int oid);

    /**
     * 获取站点下所有运营类目信息，保存为MAP key:id value：类目信息
     * 
     * @param channel
     * @return
     */
    public Map<String, Map<String, String>> getAllOperationCategoryMap();

    /**
     * 根据SKU ID查询商品信息
     * 
     * @param ids
     * @return
     */
    public List<Map<String, Object>> getProductInfo(List<String> ids);

    /**
     * 根据SKU ID查询商品信息
     * 
     * @param ids
     * @return
     */
    public Map<String, Object> getProductInfo(long id);

    /**
     * 获取平台里所有商品名称
     * 
     * @return
     */
    public List<Map<String, Object>> getAllProductName();

    /**
     * 获取所有已经上线的商品数量
     * 
     * @return
     */
    public long getProductCount();

    /**
     * 分页获取产品名称
     * 
     * @param pageNo 页码
     * @param pageSize 大小
     * @return
     */
    public List<Map<String, String>> getB2BSugInfoByPage(int pageNo, int pageSize);

    /**
     * 获取所有运营类目
     * 
     * @return
     */
    public List<Map<String, Object>> getChildrenOprationCateGory(int parentId);

    /**
     * 单个运营类目类树
     * 
     * @param oid
     * @return
     */
    public List<Map<String, Object>> getOperationCategoryTree(int oid);

    /**
     * 获取所有运营类目
     * 
     * @param channel
     * @return
     */
    public List<Map<String, String>> getCategoryByChannel();

    /**
     * 获取店铺信息
     * 
     * @param id
     * @return
     */
    public Map<String, Object> getShopInfo(long id);

    /**
     * 获取公司信息
     * 
     * @param id
     * @return
     */
    public Map<String, Object> getCorporateInfo(long id);

    /**
     * 获取店铺评分
     * 
     * @param id
     * @return
     */
    public Map<String, Object> getShopScore(long id);

    /**
     * 批量新增店铺(全量表)
     * 
     * @author zhoulinhong
     * @param id
     * @return
     * @since 20160405
     */
    public void batchAddShopWhole(List<Map<String, Object>> shopList);

    /**
     * 批量修改店铺(全量表)
     * 
     * @author zhoulinhong
     * @param id
     * @return
     * @since 20160512
     */
    public void batchUpdateShopWhole(List<Map<String, Object>> shopList);

    /**
     * 批量删除店铺(全量表)
     * 
     * @author zhoulinhong
     * @param id
     * @return
     * @since 20160405
     */
    public void batchDelShopWhole(List<Long> shopIds);

    /**
     * 批量新增产品数据(全量表)
     * 
     * @author zhoulinhong
     * @param id
     * @return
     * @since 20160405
     */
    public void batchAddB2bWhole(List<Map<String, Object>> productList);

    /**
     * 批量修改产品数据(全量表)
     * 
     * @author zhoulinhong
     * @param id
     * @return
     * @since 20160512
     */
    public void batchUpdateB2bWhole(List<Map<String, Object>> productList);

    /**
     * 批量删除产品数据(全量表)
     * 
     * @author zhoulinhong
     * @param id
     * @return
     * @since 20160405
     */
    public void batchDelB2bWhole(List<Long> skuIds);

    /**
     * 根据ID查询产品索引信息（从商品全量索引表中获取）
     * 
     * @author zhoulinhong
     * @since 20160509
     * @param ids
     * @return
     */
    public Map<String, Object> getB2bProductIndex(long id);



}
