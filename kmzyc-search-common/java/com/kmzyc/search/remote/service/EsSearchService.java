package com.kmzyc.search.remote.service;

import java.util.Map;

public interface EsSearchService {

    /**
     * 根据关键词搜索
     * 
     * @param keyword
     * @param config
     * @return
     */
    public Map<String, Object> keywordSearch(Map<String, String[]> params);


    /**
     * 根据运营类目搜索
     * 
     * @param paramMap
     * @return
     */
    public Map<String, Object> categorySearch(Map<String, String[]> params);
}
