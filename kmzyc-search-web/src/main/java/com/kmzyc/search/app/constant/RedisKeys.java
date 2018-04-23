package com.kmzyc.search.app.constant;

public interface RedisKeys
{

	// REDIS保存搜索词条队列KEY的后缀
	public static final String	SEARCH_REDIS_KEY_SUFIEX		= "_search_term_list";

	public static final String	SEARCH_REDIS_TERM_KEY_MID	= "_Q_";

	// REDIS中保存商品促销KEY
	public static final String	PROMOTION_KEY_PREFIX		= "r_product_promotion";

	public static final String	SYNONYMS					= "search_synonyms";
}
