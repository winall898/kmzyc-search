package com.kmzyc.search.param;

public enum ModelAttribute {

	// 搜索关键词
	keyword,
	// 搜索结果第几页
	pn,
	// 搜索结果一页显示多少商品
	ps,
	// 一共多少页
	pc,
	// 一共多少商品
	count,

	// 产品列表
	productList,
	// 搜索过滤展示列表
	facterList,
	// 搜索结果产品类目列表
	cateList,
	// 搜索过滤被选中列表
	selectedFilter,
	// 排序参数
	sort,
	// 筛选项
	filtrate,
	// 你是不是想找
	relevant,
	// 热门搜索
	hotList,
	// 面包屑
	breadMap,

	// 热门搜索商品
	hotSearchPaoduct,

	// 品牌搜索
	brand,
	// 商铺ID
	shopid,
	// 供应商ID
	supplierId,
	// 搜索无结果时，拆搜索句子相关推荐搜索结果
	recommends, baseURL, searchURL
}
