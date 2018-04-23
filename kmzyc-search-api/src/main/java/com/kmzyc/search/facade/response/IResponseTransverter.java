package com.kmzyc.search.facade.response;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;


public interface IResponseTransverter {
	
	public Map<String, Object> convert(SearchResponse searchResponse);

}
