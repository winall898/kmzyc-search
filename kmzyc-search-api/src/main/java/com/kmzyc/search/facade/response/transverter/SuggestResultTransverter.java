package com.kmzyc.search.facade.response.transverter;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.facade.response.IResponseTransverter;
import com.kmzyc.search.facade.vo.Suggest;

/**
 * 提示词前端搜索结果处理类
 * 
 * @author zhoulinhong
 * @since 20160614
 * 
 */
public class SuggestResultTransverter implements IResponseTransverter {

  @Override
  public Map<String, Object> convert(SearchResponse searchResponse) {

    if (null == searchResponse) {

      return null;
    }

    SearchHits searchHits = searchResponse.getHits();
    if (null == searchHits || searchHits.getTotalHits() <= 0) {

      return null;
    }

    List<Suggest> sugList = Lists.newArrayList();
    for (SearchHit searchHit : searchHits.getHits()) {
      Map<String, SearchHitField> fields = searchHit.getFields();
      if (null == fields) {

        continue;
      }

      Suggest suggest = new Suggest();
      String id = null;
      if (null != fields.get("id")) {
        id = fields.get("id").getValue();
      }
      suggest.setId(id);

      String source = null;
      if (null != fields.get("source")) {
        source = fields.get("source").getValue();
      }
      suggest.setName(source);
      sugList.add(suggest);
    }

    Map<String, Object> resultMap = Maps.newHashMap();
    resultMap.put("list", sugList);

    // 查询结果返回
    return resultMap;
  }
}
