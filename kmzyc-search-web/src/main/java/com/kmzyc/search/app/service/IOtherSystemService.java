package com.kmzyc.search.app.service;

public interface IOtherSystemService {

    /**
     * 获取商品促销价格
     * 
     * @param skuid SKU ID
     * @param sprice 原始价格
     * @param channel 商品渠道
     * @return 促销价格
     */
    public Double promotionPrice(long skuid);

    /**
     * 获取商品预售价格（B2B）
     * 
     * @param skuid SKU ID
     * @param sprice 原始价格
     * @param channel 商品渠道
     * @return 促销价格
     */
    public Double getPresellPrice(long skuid);
}
