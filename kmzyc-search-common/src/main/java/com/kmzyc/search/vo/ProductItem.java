package com.kmzyc.search.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索列表商品显示对象
 * 
 * @author river
 * 
 */
public class ProductItem implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = -5183116625190009137L;
    /**
     * 商品SKU ID
     */
    private int skuId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品副标题
     */
    private String subTitle;
    /**
     * 商品销售价格
     */
    private double price;
    /**
     * 商品市场价格
     */
    private double mprice;
    /**
     * 商品图片
     */
    private String image;
    /**
     * 商品标签
     */
    private List<String> tags;
    /**
     * 商品用户评价等级
     */
    private String level;
    /**
     * 商品用户评价数
     */
    private int evalCount;
    /**
     * SKU CODE
     */
    private String skuCode;
    /**
     * code
     */
    private String code;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 供应商类型
     */
    private String supType;
    /**
     * 店铺ID
     */
    private String shopId;
    /**
     * 产品类别
     */
    private int prodType;
    /**
     * 评价数
     */
    private int pnum;
    /**
     * 销量
     */
    private int sales;
    /**
     * 属性
     */
    private String attrVals;
    /**
     * 排序
     */
    private String sortNo;
    /**
     * 商品库存 maliqun add 20150615 (响应需求 库存为零时搜索结果显示暂时缺货)
     */
    private long stock;
    /**
     * 促销类型（0普通 1促销 2预售）
     */
    private int promotion;
    /**
     * 起批量
     */
    private long bottomAmount;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 药材市场ID
     */
    private int marketId;
    /**
     * 药材市场名字
     */
    private String marketName;

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMprice() {
        return mprice;
    }

    public void setMprice(double mprice) {
        this.mprice = mprice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSupType() {
        return supType;
    }

    public void setSupType(String supType) {
        this.supType = supType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getProdType() {
        return prodType;
    }

    public void setProdType(int prodType) {
        this.prodType = prodType;
    }

    public String getAttrVals() {
        return attrVals;
    }

    public void setAttrVals(String attrVals) {
        this.attrVals = attrVals;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public long getBottomAmount() {
        return bottomAmount;
    }

    public void setBottomAmount(long bottomAmount) {
        this.bottomAmount = bottomAmount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

}
