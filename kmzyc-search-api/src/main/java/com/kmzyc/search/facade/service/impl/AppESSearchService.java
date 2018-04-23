package com.kmzyc.search.facade.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.exception.SearchException;
import com.kmzyc.search.facade.config.Configuration;
import com.kmzyc.search.facade.request.transverter.AppAccurateParamsTransverter;
import com.kmzyc.search.facade.request.transverter.AppParamsTransverter;
import com.kmzyc.search.facade.request.transverter.SuggestParamsTransverter;
import com.kmzyc.search.facade.response.transverter.AppSearchResultTransverter;
import com.kmzyc.search.facade.response.transverter.SuggestResultTransverter;
import com.kmzyc.search.facade.service.IAppESSearchService;
import com.kmzyc.search.facade.util.IndexClientUtil;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.facade.util.MemCache;
import com.kmzyc.search.facade.vo.Suggest;
import com.kmzyc.search.param.HTTPParam;
import com.kmzyc.search.searcher.Searcher;

@Service("appESSearchService")
public class AppESSearchService implements IAppESSearchService {

    @Resource(name = "memCache")
    protected MemCache memCache;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> keywordSearch(Map<String, Object> params) throws SearchException {
        if (null == params || params.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 先进行精确搜索
        Searcher accSearcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                new AppAccurateParamsTransverter(Channel.b2b), new AppSearchResultTransverter(
                        memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
        result = accSearcher.search(params);
        // 当精确搜索为空时，进行模糊搜索
        if (result == null || result.isEmpty() || result.get("count") == null
                || Integer.parseInt(result.get("count").toString()) <= 0) {
            Searcher kwSearcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new AppParamsTransverter(true, false, Channel.b2b),
                    new AppSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            result = kwSearcher.search(params);
        }
        return result;
    }

    @Override
    public Map<String, Object> appCategorySearch(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == params || params.isEmpty()) {
            return result;
        }
        JSONObject searchJson = new JSONObject();

        // 设置查询语句
        searchJson.put("query", getQuery(params));

        // 设置分页
        Object start = params.get("start");
        searchJson.put("from", start == null ? 0 : Integer.valueOf(start.toString()));
        Object rows = params.get("rows");
        searchJson.put("size", rows == null ? 20 : Integer.valueOf(rows.toString()));

        JSONObject sortJson = new JSONObject();
        String sort = (String) params.get("sort");
        if (StringUtils.isNotBlank(sort) && sort.contains(" ")) {
            String[] sorts = sort.split(" ");
            sortJson.put(sorts[0], sorts[1]);
        }
        searchJson.put("sort", sortJson);

        SearchResponse searchResponse = IndexClientUtil.getInstance().getSearchResponse(
                Channel.b2b.name(), Channel.b2b.name(), searchJson.toJSONString());
        if (null != searchResponse) {
            result = new AppSearchResultTransverter(null).convert(searchResponse);
        }
        return result;
    }

    /**
     * 组装查询参数块
     * 
     * @param searchParams
     * @return
     */
    private JSONObject getQuery(Map<String, Object> searchParams) {
        // 添加查询参数
        Object qs = searchParams.get(HTTPParam.q.name());
        if (null != qs && StringUtils.isNotBlank(qs.toString())) {
            JSONObject queryJson = new JSONObject();
            // 获取查询字段权重配置
            String qf = Configuration.getString("b2b.es.query.fields");
            if (StringUtils.isNotBlank(qf)) {
                queryJson.put("fields", qf.split(" "));
            } else {
                queryJson.put("fields", new String[] {"text"});
            }
            queryJson.put("query", qs.toString().replaceAll("\"", "\\\"").replaceAll("/", "\\\\/"));
            queryJson.put("use_dis_max", false);
            queryJson.put("default_operator", "AND");
            return JsonUtil.jsonPut(new JSONObject(), "query_string", queryJson);
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "serial"})
    @Override
    public List<Suggest> appSuggestSearch(Channel channel, HttpServletRequest request) {
        List<Suggest> result = new ArrayList<Suggest>();
        Map<String, String[]> parameters = Maps.newHashMap(request.getParameterMap());

        // 获取提示词搜索结果
        Searcher suggestSearcher = new Searcher(Channel.suggest.name(), Channel.suggest.name(),
                new SuggestParamsTransverter(Channel.b2b), new SuggestResultTransverter());
        Map<String, Object> suggest = suggestSearcher.search(parameters);
        if (suggest != null && !suggest.isEmpty()) {
            result = (List<Suggest>) suggest.get("list");
        }
        if (null != result && result.size() > 0) {
            Map<String, RecursiveTask<Long>> taskMap = Maps.newHashMap();
            for (Suggest s : result) { // 查询
                final String term = s.getName();
                RecursiveTask<Long> countTask = new RecursiveTask<Long>() {
                    @Override
                    protected Long compute() {
                        return memCache.getSuggestCount(term, Channel.b2b);
                    }
                };
                countTask.fork();
                taskMap.put(term, countTask);
            }
            for (Suggest s : result) { // 收取结果
                final String term = s.getName();
                RecursiveTask<Long> task = taskMap.get(term);
                long count = task.join();
                s.setCount(count);
                s.setCountView("约" + count + "个商品");
            }
        }
        return result;
    }



}
