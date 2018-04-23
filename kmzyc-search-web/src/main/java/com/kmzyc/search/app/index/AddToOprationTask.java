package com.kmzyc.search.app.index;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kmzyc.search.app.service.ProductService;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.app.util.SpringBeanUtil;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.DocFieldName;

/**
 * 根据ID更新索引的运营类目
 * 
 * @author zhoulinhong
 * @since 20160516
 */
public class AddToOprationTask implements IndexTask {

    private static final Logger LOG = LoggerFactory.getLogger(AddToOprationTask.class);

    private final long skuId;

    private Map<String, Map<String, String>> categories;

    public AddToOprationTask(long skuId, Channel channel) {
        this.skuId = skuId;
    }

    @Override
    public void run() {

        ProductService productService =
                SpringBeanUtil.getBean("productService", ProductService.class);
        // 获取所有运营类目信息
        categories = productService.getAllOperationCategoryMap();
        if (null == categories || categories.isEmpty()) {

            LOG.error("无法获取运营类目信息，创建{}运营类目索引失败!", skuId);
            return;
        }

        Iterator<Entry<String, Map<String, String>>> it = categories.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Entry<String, Map<String, String>> entry = it.next();
                Map<String, String> infoMap = entry.getValue();

                // 运营类目对应的索引查询语句
                String queryString = MapUtils.getString(infoMap, "EXEC_SQL");

                if (StringUtils.isBlank(queryString)) {

                    continue;
                }

                // solr查询转es查询方式
                JSONObject queryJson = JSONObject.parseObject("{\"query\":{\"query_string\":{}}}");

                queryJson.getJSONObject("query").getJSONObject("query_string").put("query",
                        "(" + queryString + ") AND id:" + skuId);

                // 修改索引数据运营类目
                updateIndex(infoMap, JSONObject.toJSONString(queryJson));

            } catch (Exception e) {
                LOG.error("创建{}运营类目信息索引发生异常。", skuId, e);
            }
        }
    }

    /**
     * 修改索引数据的运营类目
     * 
     * @param infoMap
     * @param queryString
     * @author zhoulinhong
     * @since 20160516
     */
    public void updateIndex(Map<String, String> infoMap, String queryString) {

        if (StringUtils.isBlank(queryString)) {

            LOG.error("es查询语句为空.");
            return;
        }

        String id = MapUtils.getString(infoMap, "CATEGORY_ID");
        try {
            // 需要修改的索引数据
            Map<String, JSONObject> indexs = createDocument(queryString, infoMap);

            // 批量修改
            if (null != indexs && !indexs.isEmpty()) {
                IndexClientUtil.getInstance().batchUpdateIndex(Channel.b2b.name(),
                        Channel.b2b.name(), indexs);
            }
        } catch (Exception e) {
            LOG.error("为skuid: " + id + " 商品创建 " + MapUtils.getString(infoMap, "CATEGORY_NAME")
                    + " 级运营类目索引失败！", e);
            LOG.error("运营类目查询商品 QUERY：" + queryString);
        }
    }

    /**
     * 处理符合运营类目的索引数据
     * 
     * @param queryString
     * @param infoMap
     * @return
     * @author zhoulinhong
     * @since 20160516
     */
    private Map<String, JSONObject> createDocument(String queryString,
            Map<String, String> infoMap) {

        if (StringUtils.isBlank(queryString) || null == infoMap || infoMap.isEmpty()) {

            return null;
        }

        // 需修改运营类目的索引集合
        Map<String, JSONObject> results = Maps.newHashMap();

        SearchHits searchHits = IndexClientUtil.getInstance()
                .queryIndexByConditions(Channel.b2b.name(), Channel.b2b.name(), queryString);

        if (null == searchHits || null == searchHits.getHits()
                || searchHits.getHits().length <= 0) {

            return null;
        }

        // 查询结果
        SearchHit[] resultHits = searchHits.getHits();


        for (SearchHit result : resultHits) {
            // 获取结果
            Map<String, Object> doc = result.getSource();

            // 组装修改的的JSON对象
            JSONObject inputDoc = createDoc(doc, infoMap);

            if (null != inputDoc && !inputDoc.isEmpty()) {

                results.put(doc.get(DocFieldName.ID).toString(), inputDoc);
            }
        }

        return results;
    }

    /**
     * 组装需要修改的JSON对象
     * 
     * @param doc
     * @param infoMap
     * @return
     * @author zhoulinhong
     * @since 20160516
     */
    @SuppressWarnings("unchecked")
    private JSONObject createDoc(Map<String, Object> doc, Map<String, String> infoMap) {

        if (null == doc || null == infoMap || doc.isEmpty() || infoMap.isEmpty()
                || null == doc.get(DocFieldName.ID)) {

            return null;
        }

        // JSON对象
        JSONObject inputDoc = new JSONObject();
        try {
            List<String> codes = Lists.newArrayList();
            Set<String> names = Sets.newHashSet();

            Collection<Object> oldNames =
                    (Collection<Object>) doc.get(DocFieldName.OPRATION_CATEGORY_NAME);

            // 获取索引所属运营类目的值
            getValues(codes, names, infoMap, oldNames);
            if (null == codes || codes.isEmpty() || null == names || names.isEmpty()) {

                return null;
            }

            // 索引ID
            String docId = doc.get(DocFieldName.ID).toString();
            inputDoc.put(DocFieldName.ID, docId);

            // 类目名称
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(names);
            inputDoc.put(DocFieldName.OPRATION_CATEGORY_NAME, jsonArray);

            for (String c : codes) {
                int count = StringUtils.countMatches(c, "_");
                if (count == 0) {
                    // 一级运营类目
                    JSONArray jsonArr = null;
                    try {
                        jsonArr = inputDoc.getJSONArray(DocFieldName.FIRST_O_CODE);
                        if (null == jsonArr) {
                            jsonArr = JSONArray.parseArray("[\"" + c + "\"]");
                        } else {
                            jsonArr.add(c);
                        }
                    } catch (Exception e) {

                        // 数据异常重新赋值
                        jsonArr = JSONArray.parseArray("[\"" + c + "\"]");
                    }

                    inputDoc.put(DocFieldName.FIRST_O_CODE, jsonArr);
                } else if (count == 1) {
                    // 二级运营类目
                    JSONArray jsonArr = null;
                    try {
                        jsonArr = inputDoc.getJSONArray(DocFieldName.SECOND_O_CODE);
                        if (null == jsonArr) {
                            jsonArr = JSONArray.parseArray("[\"" + c + "\"]");
                        } else {
                            jsonArr.add(c);
                        }
                    } catch (Exception e) {

                        // 数据异常重新赋值
                        jsonArr = JSONArray.parseArray("[\"" + c + "\"]");
                    }

                    inputDoc.put(DocFieldName.SECOND_O_CODE, jsonArr);
                } else if (count == 2) {
                    // 三级运营类目
                    JSONArray jsonArr = null;
                    try {
                        jsonArr = inputDoc.getJSONArray(DocFieldName.THIRD_O_CODE);
                        if (null == jsonArr) {
                            jsonArr = JSONArray.parseArray("[\"" + c + "\"]");
                        } else {
                            jsonArr.add(c);
                        }
                    } catch (Exception e) {

                        // 数据异常重新赋值
                        jsonArr = JSONArray.parseArray("[\"" + c + "\"]");
                    }

                    inputDoc.put(DocFieldName.THIRD_O_CODE, jsonArr);
                }
            }
        } catch (Exception e) {
            LOG.error("创建{}运营类目索引时发生异常", MapUtils.getString(infoMap, "CATEGORY_NAME"), e);
        }

        return inputDoc;
    }

    /**
     * 获取索引所属运营类目的值
     * 
     * @param codes
     * @param names
     * @param infoMap
     * @param oldNames
     * @return
     * @author zhoulinhong
     * @since 20160516
     */
    private String getValues(List<String> codes, Set<String> names, Map<String, String> infoMap,
            final Collection<Object> oldNames) {
        if (infoMap == null || infoMap.isEmpty()) {

            return null;
        }

        String name = MapUtils.getString(infoMap, "CATEGORY_NAME");
        String parentId = MapUtils.getString(infoMap, "PARENT_ID");
        String id = MapUtils.getString(infoMap, "CATEGORY_ID");
        if (null == oldNames || !oldNames.contains(name)) {
            names.add(name);
        }
        if ("0".equals(parentId)) {
            codes.add(id);
            return id;
        } else {
            String prfiex = getValues(codes, names, categories.get(parentId), oldNames);
            if (StringUtils.isBlank(prfiex)) {

                return null;
            }
            String value = prfiex + "_" + id;
            codes.add(value);
            return value;
        }
    }

}
