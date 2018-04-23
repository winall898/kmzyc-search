package com.kmzyc.search.param;

/**
 * 定义产品索引字段名称
 * 
 * @author river
 * 
 */
public interface DocFieldName {
    /**
     * SKU ID
     */
    public static final String ID = "id";
    /**
     * 产品SKU编码
     */
    public static final String SKUCODE = "skucode";
    /**
     * 产品ID
     */
    public static final String PRODUCT_ID = "prodId";
    /**
     * 产品销售价格
     */
    public static final String PRICE = "price";
    /**
     * 产品市场价格
     */
    public static final String MPRICE = "mprice";
    /**
     * <!-- 商品facet属性名 -->
     */
    public static final String ATTRNAME = "attrname";
    /**
     * <!-- 商品facet属性值 -->
     */
    public static final String ATTRVAL = "attrval";
    public static final String ATTRVAL_CP = "attrval_cp";
    /**
     * <!-- 商品运营属性名 -->
     */
    public static final String OPNAME = "opname";
    /**
     * <!-- 商品运营属性值 -->
     */
    public static final String OPVAL = "opval";
    /**
     * 产品名称
     */
    public static final String PROCUCT_NAME = "prodName";
    /**
     * 产品名称COPY
     */
    public static final String PROCUCT_NAME_CP = "prodName_cp";
    /**
     * 产品标题
     */
    public static final String PRODUCT_TITLE = "prodTitle";
    public static final String PRODUCT_TITLE_CP = "prodTitle_cp";
    /**
     * 产品副标题
     */
    public static final String SUBTITLE = "subtitle";
    /**
     * 产品SEO关键字
     */
    public static final String KEYWORD = "keyword";
    /**
     * 产品热度
     */
    public static final String PRODUCTHOT = "prodHot";
    /**
     * 产品品牌ID
     */
    public static final String BRAND_ID = "brandId";
    /**
     * 产品品牌名称
     */
    public static final String BRAND_NAME = "brandName";
    /**
     * 药材市场ID
     */
    public static final String MARKET_ID = "marketId";
    /**
     * 药材市场名称
     */
    public static final String MARKET_NAME = "marketName";
    /**
     * 产品创建时间
     */
    public static final String CREATE_TIME = "cTime";
    /**
     * 产品上架时间
     */
    public static final String UP_TIME = "upTime";
    /**
     * 产品批文类型
     */
    public static final String APPROVAL_TYPE = "approType";
    /**
     * 产品批文号
     */
    public static final String APPROVAL_NO = "approNum";
    /**
     * 产品3级类目
     */
    public static final String PHYSICS_ID = "pcId";
    public static final String PHYSICS_NAME = "pcName";
    public static final String PHYSICS_CODE = "pcCode";
    /**
     * SKU销量
     */
    public static final String SALES = "sales";
    /**
     * 库存量
     */
    public static final String STOCK = "stock";
    /**
     * 商铺唯一标示:康美电商默认：S001
     */
    public static final String SHOP_CODE = "scode";
    /**
     * 产品编码
     */
    public static final String PRODUCT_NO = "pno";
    /**
     * 品类ID
     */
    public static final String DRUG_ID = "drugId";
    /**
     * 品类编号
     */
    public static final String DRUG_CODE = "drugCode";
    /**
     * 渠道
     */
    public static final String CHANNEL = "channel";
    /**
     * 图片
     */
    public static final String IMAGE = "image";
    /**
     * 商品原始销售价格(没有参加活动时价格)
     */
    public static final String SPRICE = "sprice";
    /**
     * 商品评价等级
     */
    public static final String GRADE = "grade";
    /**
     * 商品评价人数
     */
    public static final String PNUM = "pnum";
    /**
     * 浏览次数
     */
    public static final String BROWSE = "browse";
    /**
     * 运营1级类目编码
     */
    public static final String FIRST_O_CODE = "foCode";
    /**
     * 运营2级类目编码
     */
    public static final String SECOND_O_CODE = "soCode";
    /**
     * 运营3级类目编码
     */
    public static final String THIRD_O_CODE = "toCode";
    /**
     * 运营类目名称
     */
    public static final String OPRATION_CATEGORY_NAME = "ocName";
    /**
     * 促销类型（0普通 1促销 2预售）
     */
    public static final String PROMOTION = "promotion";
    public static final String SINGLECHAR = "singleChar";
    /**
     * 3级运营类目首字拼音的首字母
     */
    public static final String OCNAME_PY = "ocName_py";
    /**
     * 供应商类型
     */
    public static final String SUPTYPE = "supType";
    /**
     * 店铺名称
     */
    public static final String SHOPNAME = "shopName";
    /**
     * 店铺ID
     */
    public static final String SHOPID = "shopId";
    /**
     * 商品类型
     */
    public static final String PRODUCT_TYPE = "prodType";
    /**
     * 商品店铺运营类目
     */
    public static final String SHOPGORY = "shopgory";
    /**
     * 起批量
     */
    public static final String BOTTOM_AMOUNT = "bottomAmount";
    /**
     * 计量单位
     */
    public static final String UNIT = "unit";
}
