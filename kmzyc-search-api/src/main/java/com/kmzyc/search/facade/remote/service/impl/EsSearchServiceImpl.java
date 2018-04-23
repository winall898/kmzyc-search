package com.kmzyc.search.facade.remote.service.impl;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.request.transverter.RequestParamsTransverter;
import com.kmzyc.search.facade.response.transverter.B2BSearchResultTransverter;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.util.MemCache;
import com.kmzyc.search.param.HTTPParam;
import com.kmzyc.search.param.ModelAttribute;
import com.kmzyc.search.remote.service.EsSearchService;
import com.kmzyc.search.searcher.Searcher;


/**
 * b2b elasticsearch 搜索服务类
 * 
 * @author KM
 *
 */
@Service("esSearchService")
public class EsSearchServiceImpl implements EsSearchService {

    private static Logger logger = Logger.getLogger(EsSearchServiceImpl.class);

    @Resource(name = "memCache")
    protected MemCache memCache;

    @Resource(name = "searchDBDataService")
    protected SearchDBDataService searchDBDataService;

    @Override
    public Map<String, Object> keywordSearch(Map<String, String[]> params) {
        try {

            if (params == null || params.isEmpty()) {

                return Maps.newHashMap();
            }

            // 分类
            params.put("facet", new String[] {"true"});
            // 过滤方式(postFilter)
            params.put("postFilter", new String[] {"true"});
            // 每页大小
            Object ps = getParam(params, HTTPParam.ps.name());
            if (ps == null) {
                params.put(HTTPParam.ps.name(), new String[] {"20"});
            }

            // 搜索结果
            Map<String, Object> result = Maps.newHashMap();

            // 模糊搜索
            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            result = searcher.search(params);

            // 搜索关键词
            result.put(ModelAttribute.keyword.name(), getParam(params, HTTPParam.q.name()));
            // 排序
            result.put(ModelAttribute.sort.name(), getParam(params, HTTPParam.sort.name()));
            // 当前页
            Object pn = getParam(params, HTTPParam.pn.name());
            result.put(ModelAttribute.pn.name(), pn == null ? 1 : pn);
            // 每页大小
            ps = getParam(params, HTTPParam.ps.name());
            int pageSize = ps == null ? 20 : Integer.parseInt(ps.toString());
            result.put(ModelAttribute.ps.name(), pageSize);
            // 总页数
            int count = MapUtils.getIntValue(result, ModelAttribute.count.name());
            int pc = 1;
            if (count > 0) {
                pc = (count - 1) / pageSize + 1;
            }
            result.put(ModelAttribute.pc.name(), pc);
            // 已选过滤条件
            List<Map<String, String>> filters = getFilter(params);
            result.put(ModelAttribute.selectedFilter.name(), filters);

            return result;
        } catch (Exception e) {

            logger.error("搜索发生异常。", e);
        }

        return Maps.newHashMap();
    }

    @Override
    public Map<String, Object> categorySearch(Map<String, String[]> params) {
        try {

            if (params == null || params.isEmpty()) {

                return Maps.newHashMap();
            }

            // 分类
            params.put("facet", new String[] {"true"});
            // 过滤方式(postFilter)
            params.put("postFilter", new String[] {"true"});
            // 每页大小
            Object ps = getParam(params, HTTPParam.ps.name());
            if (ps == null) {
                params.put(HTTPParam.ps.name(), new String[] {"20"});
            }

            // 搜索结果
            Map<String, Object> result = Maps.newHashMap();

            // 运营类目ID
            Object oid = getParam(params, HTTPParam.oid.name());
            if (oid == null) {

                return Maps.newHashMap();
            }

            // 查询运营类目对应的搜索条件
            Map<String, String> paramMap =
                    searchDBDataService.getOperationCategorySql(Integer.parseInt(oid.toString()));
            if (paramMap == null) {

                return Maps.newHashMap();
            }

            String query = paramMap.get("sql");
            if (StringUtils.isBlank(query)) {

                return Maps.newHashMap();
            }

            // 判断是否有关键词搜索条件
            Object q = getParam(params, HTTPParam.q.name());
            StringBuilder qsb = new StringBuilder();
            if (q != null) {
                qsb.append(q.toString()).append(" AND ");
            }
            qsb.append(query);
            // 更新搜索语句
            params.put("q", new String[] {qsb.toString()});

            // 执行搜索
            Searcher searcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new RequestParamsTransverter(Channel.b2b), new B2BSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase())));
            result = searcher.search(params);

            // 将运营类目名称处理为搜索关键词
            result.put(ModelAttribute.keyword.name(), paramMap.get("c_name"));
            // 排序
            result.put(ModelAttribute.sort.name(), getParam(params, HTTPParam.sort.name()));
            // 当前页
            Object pn = getParam(params, HTTPParam.pn.name());
            result.put(ModelAttribute.pn.name(), pn == null ? 1 : pn);
            // 每页大小
            ps = getParam(params, HTTPParam.ps.name());
            int pageSize = ps == null ? 20 : Integer.parseInt(ps.toString());
            result.put(ModelAttribute.ps.name(), pageSize);
            // 总页数
            int count = MapUtils.getIntValue(result, ModelAttribute.count.name());
            int pc = 1;
            if (count > 0) {
                pc = (count - 1) / pageSize + 1;
            }
            result.put(ModelAttribute.pc.name(), pc);
            // 已选过滤条件
            List<Map<String, String>> filters = getFilter(params);
            result.put(ModelAttribute.selectedFilter.name(), filters);

            return result;
        } catch (Exception e) {

            logger.error("搜索发生异常。", e);
        }

        return Maps.newHashMap();
    }

    /**
     * 获取过滤条件 marketName=药材市场&属性名=属性值
     * 
     * @return
     */
    protected List<Map<String, String>> getFilter(Map<String, String[]> params) {
        if (params == null || params.isEmpty()) {

            return Lists.newArrayList();
        }

        Object filterText = getParam(params, HTTPParam.f.name());
        if (filterText == null) {

            return Lists.newArrayList();
        }

        try {
            filterText = URLDecoder.decode(filterText.toString(), "UTF-8");
        } catch (Exception e) {
            filterText = getParam(params, HTTPParam.f.name());
            logger.error("对过滤搜索参数解码失败!", e);
        }

        // 已选过滤条件
        List<Map<String, String>> filterList = Lists.newArrayList();
        if (StringUtils.isNotBlank(filterText.toString())) {
            Iterator<String> it =
                    Splitter.on("&").trimResults().split(filterText.toString()).iterator();
            while (it.hasNext()) {
                try {
                    Map<String, String> filterMap = Maps.newHashMap();
                    String pairs = it.next();
                    String[] kv = pairs.split("=");
                    if (kv.length < 2) {

                        continue;
                    }

                    String pCode = kv[0];
                    String pValue = kv[1];
                    if (StringUtils.isBlank(pCode) || StringUtils.isBlank(pValue)) {

                        continue;
                    }
                    if (HTTPParam.marketName.name().equals(pCode)) {
                        filterMap.put("code", pCode);
                        filterMap.put("name", "药材市场");
                        filterMap.put("value", pValue);
                    } else {
                        String[] temp = pValue.split("_");
                        filterMap.put("code", pCode);
                        filterMap.put("name", temp[0]);
                        filterMap.put("value", temp[1]);
                    }
                    // 追加到集合中
                    filterList.add(filterMap);
                } catch (Exception e) {

                    logger.error(e.getMessage());
                }
            }
        }
        return filterList;
    }

    /**
     * 获取map中key对应的value
     * 
     * @param params
     * @param key
     * @return
     */
    private Object getParam(Map<String, String[]> params, String key) {
        if (params == null || params.isEmpty()) {

            return null;
        }

        // 获取值
        String[] values = params.get(key);
        if (ArrayUtils.isNotEmpty(values)) {
            return values[0];
        }

        return null;
    }

}
