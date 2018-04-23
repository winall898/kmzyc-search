package com.kmzyc.search.facade.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
	
	private JsonUtil(){
	}

	public static JSONObject jsonPut(JSONObject json,String key,Object value){
		json.put(key, value);
		return json;
	}
	
}
