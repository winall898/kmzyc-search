package com.kmzyc.search.app.index;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.DocFieldName;

public class CategoryIndexSubTask {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryIndexSubTask.class);

    private final String queryString;

    private final Map<String, String> infoMap;

    private final Map<String, Map<String, String>> categories;


    public CategoryIndexSubTask(String id, Map<String, Map<String, String>> categories,
            String queryString) {
        this.categories = categories;
        this.infoMap = categories.get(id);
        this.queryString = queryString;
    }

    /**
     * 操作子类目
     */
    public void optSubCategory() {
        try {
            Map<String, JSONObject> docs = createDocument();

            // 批量修改
            if (null != docs && !docs.isEmpty()) {
                IndexClientUtil.getInstance().batchUpdateIndex(Channel.b2b.name(),
                        Channel.b2b.name(), docs);
            }

        } catch (Exception e) {
            LOG.error(" 创建 " + MapUtils.getString(infoMap, "CATEGORY_NAME") + " 级运营类目索引失败！", e);
        }
    }

    /**
     * 处理符合运营类目的索引数据
     * 
     * @author zhoulinhong
     * @since 20160517
     */
    private Map<String, JSONObject> createDocument() throws Exception {

        SearchHits searchHits = IndexClientUtil.getInstance()
                .queryIndexByConditions(Channel.b2b.name(), Channel.b2b.name(), queryString);

        if (null == searchHits || null == searchHits.getHits()
                || searchHits.getHits().length <= 0) {

            return null;
        }

        // 查询结果
        SearchHit[] resultHits = searchHits.getHits();

        Map<String, JSONObject> results = Maps.newHashMap();

        for (SearchHit result : resultHits) {
            // 获取结果
            Map<String, Object> doc = result.getSource();

            // 组装修改的的JSON对象
            JSONObject inputDoc = createDoc(doc);

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
     * @since 20160517
     */
    @SuppressWarnings("unchecked")
    private JSONObject createDoc(Map<String, Object> doc) {

        if (null == doc || doc.isEmpty() || null == doc.get(DocFieldName.ID)) {

            return null;
        }

        // JSON对象
        JSONObject inputDoc = new JSONObject();
        try {
            List<String> codes = Lists.newArrayList();
            Set<String> names = Sets.newHashSet();

            // 索引ID
            String docId = doc.get(DocFieldName.ID).toString();
            inputDoc.put(DocFieldName.ID, docId);

            // 获取运营类目名称
            Collection<Object> oldNames = null;
            if (null != doc.get(DocFieldName.OPRATION_CATEGORY_NAME)) {
                oldNames = (Collection<Object>) doc.get(DocFieldName.OPRATION_CATEGORY_NAME);
            }
            if (null != oldNames && !oldNames.isEmpty()) {
                for (Object obj : oldNames) {
                    names.add(obj.toString());
                }
            }
            // 运营类目
            getValues(codes, names, infoMap);
            if (null == codes || codes.isEmpty()) {

                return null;
            }
            // 类目名称
            inputDoc.put(DocFieldName.OPRATION_CATEGORY_NAME, names);

            // 一级运营目录
            Set<String> foCodes = Sets.newHashSet();
            Collection<Object> oldFoCodes = null;
            if (null != doc.get(DocFieldName.FIRST_O_CODE)) {
                oldFoCodes = (Collection<Object>) doc.get(DocFieldName.FIRST_O_CODE);
            }
            // 追加到set集合中
            if (null != oldFoCodes && !oldFoCodes.isEmpty()) {
                for (Object obj : oldFoCodes) {
                    foCodes.add(obj.toString());
                }
            }

            // 二级运营目录
            Set<String> soCodes = Sets.newHashSet();
            Collection<Object> oldSoCodes = null;
            if (null != doc.get(DocFieldName.SECOND_O_CODE)) {
                oldSoCodes = (Collection<Object>) doc.get(DocFieldName.SECOND_O_CODE);
            }
            // 追加到set集合中
            if (null != oldSoCodes && !oldSoCodes.isEmpty()) {
                for (Object obj : oldSoCodes) {
                    soCodes.add(obj.toString());
                }
            }

            // 三级运营目录
            Set<String> toCodes = Sets.newHashSet();
            Collection<Object> oldToCodes = null;
            if (null != doc.get(DocFieldName.THIRD_O_CODE)) {
                oldToCodes = (Collection<Object>) doc.get(DocFieldName.THIRD_O_CODE);
            }
            // 追加到set集合中
            if (null != oldToCodes && !oldToCodes.isEmpty()) {
                for (Object obj : oldToCodes) {
                    toCodes.add(obj.toString());
                }
            }

            for (String c : codes) {
                int count = StringUtils.countMatches(c, "_");
                if (count == 0) {
                    // 一级运营类目
                    foCodes.add(c);
                    inputDoc.put(DocFieldName.FIRST_O_CODE, foCodes);
                } else if (count == 1) {
                    // 二级运营类目
                    soCodes.add(c);
                    inputDoc.put(DocFieldName.SECOND_O_CODE, soCodes);
                } else if (count == 2) {
                    // 三级运营类目
                    toCodes.add(c);
                    inputDoc.put(DocFieldName.THIRD_O_CODE, toCodes);
                }
            }
        } catch (Exception e) {
            LOG.error("创建运营类目索引时发生异常", MapUtils.getString(infoMap, "CATEGORY_NAME"), e);
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
     * @author zhoulinhong
     * @since 20160517
     */
    private String getValues(List<String> codes, Set<String> names, Map<String, String> infoMap) {
        if (infoMap == null || infoMap.isEmpty()) {

            return null;
        }
        String name = MapUtils.getString(infoMap, "CATEGORY_NAME");
        String parentId = MapUtils.getString(infoMap, "PARENT_ID");
        String id = MapUtils.getString(infoMap, "CATEGORY_ID");
        if (StringUtils.isNotBlank(name)) {
            if (null == names) {
                names = Sets.newHashSet();
            }
            names.add(name);
        }
        if ("0".equals(parentId)) {
            codes.add(id);
            return id;
        } else {
            String prfiex = getValues(codes, names, categories.get(parentId));
            if (StringUtils.isBlank(prfiex)) {

                return null;
            }
            String value = prfiex + "_" + id;
            codes.add(value);
            return value;
        }
    }
}
