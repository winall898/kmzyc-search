package com.kmzyc.search.facade.service;

import java.util.List;
import java.util.Map;

import com.kmzyc.search.facade.action.SearchConfig;
import com.kmzyc.search.facade.vo.Suggest;

public interface IB2BESSearchService {
    /**
     * b2b 关键词搜索
     * 
     * @param keyword
     * @param config
     * @return
     */
    public Map<String, Object> keywordSearch(String keyword, SearchConfig config);

    /**
     * 物理类目搜索
     * 
     * @param config
     * @return
     */
    public Map<String, Object> categorySearch(SearchConfig config);

    /**
     * 运营类目搜索
     * 
     * @param oid 运营类目ID
     * @param config
     * @return
     */
    public Map<String, Object> operCateSearch(String oid, SearchConfig config);

    /**
     * 品牌搜索
     * 
     * @param config
     * @return
     */
    public Map<String, Object> brandSearch(SearchConfig config);

    /**
     * suggest搜索
     * 
     * @param config
     * @return
     */
    public List<Suggest> suggestSearch(SearchConfig config);

    /**
     * 拆词搜索
     * 
     * @param query
     * @return
     */
    public List<Map<String, Object>> splitSearch(String query);

    /**
     * 品牌主页搜索
     * 
     * @param bid 品牌ID
     * @param config
     * @return
     */
    public Map<String, Object> brandWebSearch(String bid, SearchConfig config);

    /**
     * 商铺搜索
     * 
     * @param config
     * @return
     */
    public Map<String, Object> shopSearch(SearchConfig config);

    /**
     * 商铺商品搜索
     * 
     * @param config
     * @return
     */
    public Map<String, Object> shopProductsSearch(SearchConfig config);

}
