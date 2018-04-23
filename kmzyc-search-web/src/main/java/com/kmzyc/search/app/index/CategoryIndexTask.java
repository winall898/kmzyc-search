package com.kmzyc.search.app.index;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.config.Channel;

/**
 * 运营类目索引创建任务线程
 * 
 * @author river
 * 
 */
public class CategoryIndexTask {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryIndexTask.class);

    private static final int SIZE = 1000;

    private final Map<String, String> infoMap;

    private final Map<String, Map<String, String>> categories;

    public CategoryIndexTask(String id, Map<String, Map<String, String>> categories) {
        this.infoMap = categories.get(id);
        this.categories = categories;
    }

    /**
     * 操作类目
     */
    public void optCategory() {

        if (null == infoMap || infoMap.isEmpty()) {

            return;
        }

        String id = MapUtils.getString(infoMap, "CATEGORY_ID");
        String queryString = MapUtils.getString(infoMap, "EXEC_SQL");
        String level = MapUtils.getString(infoMap, "LEVEL");
        String name = MapUtils.getString(infoMap, "name");

        if (StringUtils.isBlank(queryString)) {

            LOG.warn(infoMap.get("CHANNEL") + " 运营类目搜索SQL为空。级别：[" + level + "] id: [" + id + "]"
                    + " name: [" + name + "]");
            return;
        }

        // solr查询转es查询方式
        JSONObject queryJson = JSONObject.parseObject("{\"query\":{\"query_string\":{}}}");

        queryJson.getJSONObject("query").getJSONObject("query_string").put("query",
                queryString.replaceAll("\"", "\\\"").replaceAll("/", "\\\\/"));

        // 索引数据查询
        SearchHits searchHits = IndexClientUtil.getInstance().queryIndexByConditions(
                Channel.b2b.name(), Channel.b2b.name(), JSONObject.toJSONString(queryJson));
        if (null == searchHits || searchHits.getTotalHits() <= 0) {

            LOG.warn(Channel.b2b.name() + " 级别：[" + level + "] id: [" + id + "]" + " name: [" + name
                    + "] 的运营类目下商品个数为0，请修改运营类目。");
            return;
        }

        long count = searchHits.getTotalHits();
        long pageCount = (count - 1) / SIZE + 1;
        for (int i = 0; i < pageCount; i++) {
            // 分页处理
            queryJson.put("from", SIZE * i);
            queryJson.put("size", SIZE);

            new CategoryIndexSubTask(id, categories, JSONObject.toJSONString(queryJson))
                    .optSubCategory();
        }
    }
}
