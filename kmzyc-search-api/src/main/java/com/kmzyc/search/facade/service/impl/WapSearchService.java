package com.kmzyc.search.facade.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.request.transverter.WapCSParamsTransverter;
import com.kmzyc.search.facade.request.transverter.WapParamsTransverter;
import com.kmzyc.search.facade.response.transverter.WapSearchResultTransverter;
import com.kmzyc.search.facade.service.IWAPSearchService;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.util.MemCache;
import com.kmzyc.search.searcher.Searcher;

@Component
public class WapSearchService implements IWAPSearchService {

    protected static final Logger LOG = LoggerFactory.getLogger(WapSearchService.class);

    @Resource(name = "memCache")
    protected MemCache memCache;

    @Resource(name = "searchDBDataService")
    protected SearchDBDataService searchDBDataService;

    @Override
    public Map<String, Object> keyWordSearch(HttpServletRequest request) {

        try {

            Searcher kwSearcher =
                    new Searcher(Channel.b2b.name(), Channel.b2b.name(), new WapParamsTransverter(),
                            new WapSearchResultTransverter(
                                    memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase()),
                                    request));

            // 返回查询结果
            return kwSearcher.search(request);
        } catch (Exception e) {

            LOG.error("WAP 关键字搜索失败.", e);
        }

        return null;
    }

    @Override
    public Map<String, Object> categorySearch(HttpServletRequest request) {

        try {
            String oid = request.getParameter("oid");
            if (StringUtils.isBlank(oid)) {

                LOG.error("wap 获取运营ID为：[" + oid + "] 的搜索SQL为空，无法进行搜索。");
                return Maps.newHashMap();
            }


            Map<String, String> paramMap =
                    searchDBDataService.getOperationCategorySql(Integer.parseInt(oid));
            String query = paramMap.get("sql");
            if (LOG.isDebugEnabled())
                LOG.debug("wap 运营ID为：[" + oid + "] 的搜索SQL为：" + query);
            if (StringUtils.isBlank(query)) {

                return Maps.newHashMap();
            }

            Searcher kwSearcher = new Searcher(Channel.b2b.name(), Channel.b2b.name(),
                    new WapCSParamsTransverter(query),
                    new WapSearchResultTransverter(
                            memCache.getOpraCategoryMap(Channel.b2b.name().toLowerCase()),
                            request));

            // 返回查询结果
            return kwSearcher.search(request);
        } catch (Exception e) {
            LOG.error("wap 运营类目搜索获取数据失败!", e);
        }
        return Maps.newHashMap();
    }
}
