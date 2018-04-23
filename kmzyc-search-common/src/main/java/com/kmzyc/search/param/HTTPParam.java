package com.kmzyc.search.param;

public enum HTTPParam {
    q, // suggest搜索传递参数
    kw, // 关键字
    f, // 过滤条件
    brandName, // 品牌名称
    marketName, // 药材市场
    price, // 价格
    pn, // 页码
    ps, // 每页大小
    sort, // 排序
    attr, // 属性过滤 attr=attrname_attrval
    attrname, // 属性过滤参数名
    attrval, // 属性过滤参数值
    c1, // 1级运营类目搜索参数
    c2, // 2级运营类目搜索参数
    c3, // 3级运营类目搜索参数
    cid, // 物理类目ID
    bid, // 品牌ID
    sc, // 店铺CODE
    shopid, // 店铺ID
    soid, // 店铺运营类目ID id1_id2_id3...
    oid, // 运营类目ID
    py, // 拼音搜索
    /********************* APP *********************/
    shopname, // 店铺名
    suptype, // 商家类型:13:自营，2:第三方
    ptype, // 商品类型：（1:上新、2:促销）
    skucode, stock, // 库存
    shopgory, // 店内分类id
    brandId, // 品牌id
    json // APP传递参数
}
