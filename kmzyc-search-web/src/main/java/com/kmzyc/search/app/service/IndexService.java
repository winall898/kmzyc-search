package com.kmzyc.search.app.service;

import java.util.List;
import java.util.Map;

import com.kmzyc.search.config.Channel;

/**
 * 索引服务接口
 * 
 * @author zhoulinhong
 * @since 20160510
 */
public interface IndexService {

    /**
     * 添加商品索引
     * 
     * @param ids SKU id
     */
    public void addB2bIndex(List<Long> ids);

    /**
     * 更新商品索引
     * 
     * @param ids SKU id
     */
    public void updateB2bIndex(List<Long> ids);

    /**
     * 删除商品索引
     * 
     * @param ids SKU id
     */
    public void deleteB2bIndex(List<Long> ids);

    /**
     * 创建运营类目树
     */
    public void createOperationCategoryTree();

    /**
     * 创建店铺索引
     * 
     * @param ids
     * @author zhoulinhong
     * @since 20160510
     */
    public void addShopIndex(List<Long> ids);

    /**
     * 删除店铺索引
     * 
     * @param ids
     * @author zhoulinhong
     * @since 20160510
     */
    public void delShopIndex(List<Long> ids);

    /**
     * 修改店铺索引
     * 
     * @param ids
     */
    public void updateShopIndex(List<Long> ids);

    /**
     * 更新促销索引信息
     * 
     * @param ids 需要更新的索引id集合
     * @param channel 更新的索引数据渠道
     * @param flag 是否全场促销更新
     * @author zhoulinhong
     * @since 20160510
     */
    public void updatePromotionIndex(List<Object> ids, Channel channel, boolean flag);

    /**
     * 更新促销索引信息
     * 
     * @param data 需要更新的索引数据集合
     * @param channel 更新的索引数据渠道
     * @author zhoulinhong
     * @since 20160510
     */
    public void updatePromotionIndex(Map<String, Map<String, Object>> data, Channel channel);
}
