package com.kmzyc.search.facade.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.action.SearchConfig;
import com.kmzyc.search.facade.request.transverter.AccurateParamsTransverter;
import com.kmzyc.search.facade.request.transverter.CategorySearchParamsTransverter;
import com.kmzyc.search.facade.request.transverter.RequestParamsTransverter;
import com.kmzyc.search.facade.request.transverter.SuggestParamsTransverter;
import com.kmzyc.search.facade.response.transverter.B2BSearchResultTransverter;
import com.kmzyc.search.facade.response.transverter.CommonResultTransverter;
import com.kmzyc.search.facade.response.transverter.ShopProductSearchResultTransverter;
import com.kmzyc.search.facade.response.transverter.SuggestResultTransverter;
import com.kmzyc.search.facade.search.QueryBrandInfoTask;
import com.kmzyc.search.facade.service.IB2BESSearchService;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.util.IndexClientUtil;
import com.kmzyc.search.facade.util.MemCache;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.facade.vo.Brand;
import com.kmzyc.search.facade.vo.Suggest;
import com.kmzyc.search.searcher.Searcher;
import com.kmzyc.search.searcher.SplitSearcher;


/**
 * b2b elasticsearch 搜索服务类
 * 
 * @author KM
 *
 */
@Service("b2bESSearchService")
public class B2BESSearchService implements IB2BESSearchService {

    private static Logger logger = Logger.getLogger(B2BESSearchService.class);

    @Resource(name = "searchDBDataService")
    protected SearchDBDataService searchDBDataService;

    @Resource(name = "memCache")
    protected MemCache memCache;

    @Override
    public Map<String, Object> keywordSearch(String keyword, SearchConfig config) {
        try {
            Map<String, Object> resultMap = Maps.newHashMap();

            Map<String, String[]> parameters = config.getConfig();

            Map<String, String[]> accurateParams = Maps.newHashMap();
            accurateParams.putAll(parameters);
            accurateParams.put("q", new String[] {ParamUitl.escapeQueryChars(keyword)});

            Map<String, String[]> suggestParams = Maps.newHashMap();
            suggestParams.putAll(accurateParams);

            // 精确搜索
            Searcher accSearcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new AccurateParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            resultMap = accSearcher.search(accurateParams);

            // 若精确搜索无结果，则进行模糊搜索
            if (resultMap == null || resultMap.isEmpty() || resultMap.get("count") == null
                    || Integer.parseInt(resultMap.get("count").toString()) <= 0) {

                Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                        new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                                memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
                resultMap = searcher.search(parameters);
            }
            // 搜无结果，则返回空集合
            if (resultMap == null || resultMap.isEmpty() || resultMap.get("count") == null
                    || Integer.parseInt(resultMap.get("count").toString()) <= 0) {

                return Maps.newHashMap();
            }

            // 获取提示词搜索结果
            Searcher suggestSearcher = new Searcher(Channel.suggest.name(), Channel.suggest.name(),
                    new SuggestParamsTransverter(Channel.b2b), new SuggestResultTransverter());
            Map<String, Object> suggest = suggestSearcher.search(suggestParams);
            if (null != suggest && !suggest.isEmpty()) {
                resultMap.put("relevant", suggest.get("list"));
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("关键搜索发生异常.搜索词为：" + keyword, e);
        }
        return Maps.newHashMap();

    }

    @Override
    public Map<String, Object> categorySearch(SearchConfig config) {
        try {
            Map<String, Object> resultMap = Maps.newHashMap();
            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            resultMap = searcher.search(config.getConfig());
            if (null != resultMap) {
                return resultMap;
            }
        } catch (Exception e) {
            logger.error("获取物理类目搜索结果失败!", e);
        }
        return Maps.newHashMap();
    }

    @Override
    public List<Map<String, Object>> splitSearch(String query) {
        List<Map<String, Object>> searchResult = Lists.newArrayList();
        List<Map<String, Object>> queryResult = Lists.newArrayList();
        List<Map<String, Object>> terms = getTerms(query);
        if (null != terms && !terms.isEmpty()) {
            for (Map<String, Object> term : terms) {
                Map<String, Object> result = new SplitSearcher(Channel.b2b, term).search();
                if (result != null && !result.isEmpty()) {
                    searchResult.add(result);
                    queryResult.add(result);
                }
            }
        }

        // 去除包含分词搜索结果（即："abcd","bc" 去掉"bc"）
        if (!searchResult.isEmpty()) {
            for (Map<String, Object> result : queryResult) {
                String text = result.get("text").toString();
                for (Map<String, Object> resultTemp : queryResult) {
                    String textTemp = resultTemp.get("text").toString();
                    if (textTemp.contains(text) && !textTemp.equals(text)) {

                        searchResult.remove(result);
                        break;
                    }
                }
            }
        }

        return searchResult;
    }

    private List<Map<String, Object>> getTerms(String query) {

        // 通过httpclient请求获取分词结果
        String termsResult = IndexClientUtil.getAnalyzeTerms(query);
        if (StringUtils.isBlank(termsResult)) {

            return Collections.emptyList();
        }

        List<Map<String, Object>> terms = Lists.newArrayList();
        JSONObject termsJson = JSONObject.parseObject(termsResult);
        JSONArray tokens = termsJson.getJSONArray("tokens");
        for (Object token : tokens) {
            JSONObject tokenJson = (JSONObject) token;
            String termWord = tokenJson.getString("token");
            if (termWord.equals(query)) {
                continue;
            }
            Map<String, Object> term = Maps.newHashMap();
            int start = tokenJson.getInteger("start_offset");
            int end = tokenJson.getInteger("end_offset");
            term.put("text", termWord);
            term.put("type", tokenJson.getString("type"));
            term.put("start", start);
            term.put("end", end);
            term.put("warpQuery", warpQuery(query, start, end));
            terms.add(term);
        }
        return terms.isEmpty() ? null : terms;
    }

    /**
     * <b>手机</b><del>中</del><b>的</b><del>洗衣机fdfdfdsfsdf</del>
     * 
     * @param query
     * @param start
     * @param end
     * @return
     */
    private String warpQuery(String query, int start, int end) {
        StringBuilder target = new StringBuilder();
        if (start > 0) {
            target.append("<del>");
            target.append(query.subSequence(0, start));
            target.append("</del>");
        }
        target.append("<b>" + query.substring(start, end) + "</b>");
        if (end < query.length()) {
            target.append("<del>");
            target.append(query.subSequence(end, query.length()));
            target.append("</del>");
        }
        return target.toString();
    }


    @Override
    public Map<String, Object> operCateSearch(String oid, SearchConfig config) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            Map<String, String> paramMap =
                    searchDBDataService.getOperationCategorySql(Integer.parseInt(oid));
            String query = paramMap.get("sql");
            resultMap.put("tdkParam_Map", paramMap);
            if (StringUtils.isBlank(query)) {
                return resultMap;
            }
            Map<String, String[]> parameters = config.getConfig();
            StringBuilder q = new StringBuilder(parameters.get("q")[0]);
            if (StringUtils.isNotBlank(q.toString())) {
                q.append(" AND ");
            }
            q.append(query);
            config.getConfig().put("q", new String[] {q.toString()});

            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            Map<String, Object> searchResult = searcher.search(parameters);
            if (null != searchResult) {
                resultMap.put("result", searchResult);
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("运营类目搜索获取数据失败!", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> brandSearch(SearchConfig config) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            resultMap = searcher.search(config.getConfig());
        } catch (Exception e) {
            logger.error("获取品牌搜索数据失败!", e);
        }
        return resultMap;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Suggest> suggestSearch(SearchConfig config) {
        List<Suggest> result = Lists.newArrayList();
        List<Suggest> suggestList = Lists.newArrayList();
        Searcher suggestSearcher = new Searcher(Channel.suggest.name(), Channel.suggest.name(),
                new SuggestParamsTransverter(Channel.b2b), new SuggestResultTransverter());
        Map<String, Object> suggest = suggestSearcher.search(config.getConfig());
        if (null != suggest && !suggest.isEmpty()) {
            result = (List<Suggest>) suggest.get("list");
        }
        if (null != result && !result.isEmpty()) {
            Map<String, RecursiveTask<Long>> taskMap = Maps.newHashMap();
            for (Suggest s : result) { // 查询
                final String term = s.getName();
                RecursiveTask<Long> countTask = new RecursiveTask<Long>() {

                    private static final long serialVersionUID = 7608253978738173379L;

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
                if (count > 0) {
                    s.setCount(count);
                    s.setCountView("约" + count + "个商品");
                    suggestList.add(s);
                }
            }
        }
        return suggestList;
    }



    @Override
    public Map<String, Object> brandWebSearch(String bid, SearchConfig config) {
        Map<String, Object> result = Maps.newHashMap();
        Brand brand = null;
        try {

            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new CategorySearchParamsTransverter(Channel.b2b),
                    new CommonResultTransverter(Channel.b2b));
            result = searcher.search(config.getConfig());

            QueryBrandInfoTask queryTask = new QueryBrandInfoTask(Integer.parseInt(bid));
            queryTask.fork();
            brand = queryTask.join();
        } catch (Exception e) {
            logger.error("获取品基本数据失败!品牌ID：" + bid, e);
        }

        if (null == brand && (null == result || result.isEmpty())) {
            return brandSearch(config);
        }

        if (null == brand) {
            logger.error("获取品基本数据失败!品牌ID：" + bid);
        } else {
            result.put("brand_info", brand);
        }
        return result;
    }

    @Override
    public Map<String, Object> shopSearch(SearchConfig config) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            resultMap = searcher.search(config.getConfig());
        } catch (Exception e) {
            logger.error("获取商铺搜索数据失败!", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> shopProductsSearch(SearchConfig config) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b),
                    new ShopProductSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            resultMap = searcher.search(config.getConfig());
        } catch (Exception e) {
            logger.error("获取商铺搜索数据失败!", e);
        }
        return resultMap;
    }

}
