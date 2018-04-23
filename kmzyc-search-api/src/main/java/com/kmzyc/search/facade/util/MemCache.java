package com.kmzyc.search.facade.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.constants.MemCacheKeys;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.vo.Category;

/**
 * 运营类目缓存
 * 
 * @author 周林洪
 * @since 20160525
 */
@Component("memCache")
public class MemCache {

    private static final Logger LOG = LoggerFactory.getLogger(MemCache.class);

    Cache<Object, Object> mainCache = CacheUtil.getCache("main", 100, 1, TimeUnit.HOURS, true);

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, String>> getSiteCategory(final String channel) {
        if (StringUtils.isBlank(channel))
            return MapUtils.EMPTY_MAP;

        String operationCategoryKey = channel.toLowerCase() + MemCacheKeys.SITE_CATEGORY;
        Map<String, Map<String, String>> opercate = Maps.newHashMap();
        try {
            opercate = (Map<String, Map<String, String>>) mainCache.get(operationCategoryKey,
                    new Callable<Map<String, Map<String, String>>>() {

                        @Override
                        public Map<String, Map<String, String>> call() throws Exception {
                            try {
                                SearchDBDataService service = ApplicationContextUtil
                                        .getBean("searchDBDataService", SearchDBDataService.class);
                                Map<String, Map<String, String>> cateMap =
                                        service.getAllOperationCategoryMap();
                                return cateMap;
                            } catch (Exception e) {
                                LOG.error("无法获取运营类目.", e);
                            }
                            return Maps.newHashMap();
                        }
                    });
        } catch (ExecutionException e) {
            LOG.error("无法获取运营类目.", e);
        }
        return opercate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<Map<String, String>>> getCategoryGroupByLevel(final String channel) {
        if (StringUtils.isBlank(channel))
            return MapUtils.EMPTY_MAP;

        String operationCategoryKey = channel.toLowerCase() + MemCacheKeys.SITE_CATEGORY_GROUP;
        Map<String, List<Map<String, String>>> opercate = Maps.newHashMap();
        try {
            opercate = (Map<String, List<Map<String, String>>>) mainCache.get(operationCategoryKey,
                    new Callable<Map<String, List<Map<String, String>>>>() {

                        @Override
                        public Map<String, List<Map<String, String>>> call() throws Exception {
                            try {
                                SearchDBDataService service = ApplicationContextUtil
                                        .getBean("searchDBDataService", SearchDBDataService.class);
                                Map<String, List<Map<String, String>>> cateMap =
                                        service.getCategoryGroupByLevel();
                                return cateMap;
                            } catch (Exception e) {
                                LOG.error("无法获取运营类目.", e);
                            }
                            return Maps.newHashMap();
                        }
                    });
        } catch (ExecutionException e) {
            LOG.error("无法获取运营类目.", e);
        }
        return opercate;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Category> getPublishedOperationCategory(final String channel) {

        if (StringUtils.isBlank(channel)) {

            return MapUtils.EMPTY_MAP;
        }

        String operationCategoryKey =
                channel.toLowerCase() + MemCacheKeys.OPERATION_CATEGORY_SUFFIX;
        Map<Integer, Category> opercate = Maps.newHashMap();
        try {
            opercate = (Map<Integer, Category>) mainCache.get(operationCategoryKey,
                    new Callable<Map<Integer, Category>>() {

                        @Override
                        public Map<Integer, Category> call() throws Exception {
                            try {
                                SearchDBDataService service = ApplicationContextUtil
                                        .getBean("searchDBDataService", SearchDBDataService.class);
                                Map<String, Map<String, String>> cateMap =
                                        service.getAllOperationCategoryMap();
                                if (null != cateMap) {
                                    Collection<Map<String, String>> data = cateMap.values();
                                    if (null != data && !data.isEmpty()) {
                                        String key =
                                                channel.toLowerCase() + MemCacheKeys.CMS_CATEGLIST;
                                        mainCache.put(key, data);
                                        if (LOG.isDebugEnabled()) {

                                            LOG.debug("添加缓存内容。KEY: " + key);
                                        }

                                        Map<Integer, Category> result =
                                                new HashMap<Integer, Category>(data.size());
                                        createCacheData(data, result);
                                        return result;
                                    }
                                }

                            } catch (Exception e) {
                                LOG.error("无法获取运营类目.", e);
                            }
                            return Maps.newHashMap();
                        }
                    });
        } catch (ExecutionException e) {
            LOG.error("无法获取运营类目.", e);
        }
        return opercate;
    }

    private void createCacheData(Collection<Map<String, String>> catelist,
            Map<Integer, Category> result) {

        if (null == catelist || catelist.isEmpty()) {

            return;
        }

        for (Map<String, String> map : catelist) {

            if (null == map || map.isEmpty()) {

                continue;
            }

            // 类目名称
            String name = MapUtils.getString(map, "CATEGORY_NAME");
            // id
            Integer id = MapUtils.getInteger(map, "CATEGORY_ID");
            // 父类ID
            Integer parentId = MapUtils.getInteger(map, "PARENT_ID");

            // 运营类目对象
            Category category = new Category();
            category.setId(id);
            category.setParentId(parentId);
            category.setName(name);

            result.put(id, category);
        }
    }

    /**
     * 获取运营CODE与名称对应关系
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getOpraCategoryMap(final String channel) {
        if (StringUtils.isBlank(channel))
            return MapUtils.EMPTY_MAP;

        String key = MemCacheKeys.SEARCH_OPRATION_CATEGORY + "_" + channel.toLowerCase();
        Map<String, String> opraCategoryMap = Maps.newHashMap();
        try {
            opraCategoryMap =
                    (Map<String, String>) mainCache.get(key, new Callable<Map<String, String>>() {

                        @Override
                        public Map<String, String> call() throws Exception {
                            Map<String, String> result = new HashMap<String, String>();
                            Map<Integer, Category> opercate =
                                    getPublishedOperationCategory(channel);
                            if (null == opercate || opercate.isEmpty())
                                return result;

                            Iterator<Entry<Integer, Category>> it = opercate.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<Integer, Category> entry = it.next();
                                Category c = entry.getValue();
                                String name = c.getName();
                                String code = createKey(c, "", opercate);
                                result.put(code, name);
                            }
                            return result;
                        }
                    });
        } catch (ExecutionException e) {
            LOG.error("获取运营CODE与名称对应关系失败", e);
        }
        return opraCategoryMap;
    }

    private String createKey(Category c, String key, Map<Integer, Category> opercate) {
        int pid = c.getParentId();
        Category parent = opercate.get(pid);
        if (pid > 0 && null != parent) {
            if (StringUtils.isBlank(key))
                return createKey(parent, c.getId() + "", opercate);
            return createKey(parent, c.getId() + "_" + key, opercate);
        } else {
            if (StringUtils.isBlank(key))
                return c.getId() + "";
            return c.getId() + "_" + key;
        }
    }

    /**
     * 获取提示词对应搜索结果数量
     * 
     * @return
     */
    public long getSuggestCount(final String term, final Channel channel) {

        Cache<Object, Object> cache = CacheUtil.getCache(channel.name() + ".suggest.count", 5000,
                24, TimeUnit.HOURS, true);
        Long count = 0L;
        try {
            count = (Long) cache.get(term, new Callable<Long>() {

                @Override
                public Long call() throws Exception {
                    String keword = ParamUitl.escapeQueryChars(term);
                    if (StringUtils.isNotBlank(keword)) {
                        String queryStr = getQuerySource(keword);
                        SearchResponse sr = IndexClientUtil.getInstance()
                                .getSearchResponse(channel.name(), channel.name(), queryStr);
                        if (null != sr) {
                            return sr.getHits().getTotalHits();
                        }
                    }
                    return 0L;
                }
            });
        } catch (ExecutionException e) {
            LOG.error("获取 {} 商品数量发生异常.", term, e);
        }
        return count;
    }

    private String getQuerySource(String keword) {
        JSONObject jsonObject = JSONObject.parseObject("{\"query\":{}}");
        JSONObject queryJson = new JSONObject();
        queryJson.put("default_operator", "AND");
        // 参与搜索的字段
        List<String> fields = new ArrayList<String>();
        fields.add("prodTitle");
        fields.add("subtitle");
        fields.add("prodName");
        fields.add("brandName");
        fields.add("keyword");
        fields.add("ocName_ik");
        queryJson.put("fields", fields);
        // 搜索关键词
        queryJson.put("query", keword);
        queryJson.put("use_dis_max", false);

        jsonObject.getJSONObject("query").put("query_string", queryJson);

        return jsonObject.toJSONString();
    }
}
