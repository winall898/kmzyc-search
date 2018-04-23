package com.kmzyc.search.facade.service;

import java.util.List;
import java.util.Map;

import com.kmzyc.search.vo.ProductItem;


/**
 * 外部系统接口调用
 * 
 * @author river
 * 
 */
public interface IOtherSystemService {

    /**
     * 获取商品促销价格
     * 
     * @param skuid SKU ID
     * @param channel 商品渠道
     * @return 促销价格
     */
    public Double promotionPrice(long skuid);

    /**
     * 获取热门搜索商品
     * 
     * @param channel 商品渠道
     * @return
     */
    public List<ProductItem> getHotSearchProducts();

    /**
     * 获取热门搜索商品
     * 
     * @param channel 商品渠道
     * @return
     */
    public List<Map<String, Object>> getHotProducts();

    /**
     * 获取商品标签
     * 
     * @param productId 产品ID
     * @param channel 商品渠道
     * @return
     */
    public List<String> getProductTags(int productId);

    /**
     * 获取运营类目搜索SQL
     * 
     * @param oid 运营类目ID
     * @return
     */
    public String getExecSql(long oid);
}
