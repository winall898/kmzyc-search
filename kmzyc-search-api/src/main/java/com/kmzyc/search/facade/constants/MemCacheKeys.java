package com.kmzyc.search.facade.constants;

/**
 * 缓存KEYS
 * 
 * 
 * @author zhoulinhong
 * @since 20160525
 * 
 */
public interface MemCacheKeys {
    // 导航属性排序缓存KEY
    public static final String FACET_SORT = "search_facet_sort";
    // 运营类目
    public static final String OPERATION_CATEGORY_SUFFIX = "_operation_category";
    // 运营类目
    public static final String SITE_CATEGORY = "_site_category";
    // 运营类目
    public static final String SITE_CATEGORY_GROUP = "_site_category_group";
    // 热闹搜索商品(搜索结果页左则，数据产品系统提供)
    public static final String HOT_PRODUCTS = "product_hotsectionproducts";
    // 缓存过期时间
    public static final int LOSE_EFFICACY = 24;
    // 运营类目编码与名称映射关系
    public static final String SEARCH_OPRATION_CATEGORY = "search_operation_category";
    // CMS出版的B2B运营类目信息
    public static final String CMS_CATEGLIST = "_cms_categList";
}
