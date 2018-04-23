package com.kmzyc.search.facade.vo;

import java.io.Serializable;

/**
 * 供应商信息类
 *
 */
public class SupplierInfo implements Serializable {
    /**
     * 序列号
     */
    private static final long serialVersionUID = 3630354640240545389L;

    /**
     * 供应商ID
     */
    private Integer supplierId;

    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     * 药材市场Id
     */
    private Integer marketId;

    /**
     * 药材市场
     */
    private String marketName;

    /**
     * 档口编号
     */
    private String stallsNo;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺介绍
     */
    private String introduce;

    /**
     * 店铺SEO
     */
    private String shopSeoKey;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStallsNo() {
        return stallsNo;
    }

    public void setStallsNo(String stallsNo) {
        this.stallsNo = stallsNo;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getShopSeoKey() {
        return shopSeoKey;
    }

    public void setShopSeoKey(String shopSeoKey) {
        this.shopSeoKey = shopSeoKey;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

}
