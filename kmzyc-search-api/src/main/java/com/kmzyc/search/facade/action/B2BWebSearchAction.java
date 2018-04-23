package com.kmzyc.search.facade.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.annotation.WapResource;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.constants.ESSortParam;
import com.kmzyc.search.facade.constants.JSPResource;
import com.kmzyc.search.facade.service.IB2BESSearchService;
import com.kmzyc.search.facade.service.IOtherSystemService;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.util.CategoryUtil;
import com.kmzyc.search.facade.vo.Brand;
import com.kmzyc.search.facade.vo.Category;
import com.kmzyc.search.facade.vo.Suggest;
import com.kmzyc.search.param.HTTPParam;
import com.kmzyc.search.param.ModelAttribute;

/**
 * 请求处理类
 * 
 * @author river
 * 
 */
@Controller("b2bSearchAction")
@RequestMapping("/10")
public class B2BWebSearchAction extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(B2BWebSearchAction.class);

    @Resource(name = "searchDBDataService")
    protected SearchDBDataService searchDBDataService;

    @Resource(name = "b2bESSearchService")
    protected IB2BESSearchService b2bESSearchService;

    @Resource(name = "otherSystemService")
    private IOtherSystemService otherSystemService;

    private void setViewName(ModelAndView view, String name) {
        String viewName = getViewName(Channel.b2b, name);
        view.setViewName(viewName);
    }

    private SearchConfig getConfig(boolean facet, boolean edismax, boolean elevation,
            boolean forceEle, boolean highlight) {
        SearchConfig config = SearchConfig.build(getParameterMap()).setFacet(facet)
                .setEdismax(edismax).setEnableElevation(elevation).setForceElevation(forceEle)
                .setHighlight(highlight);
        return config;
    }

    @RequestMapping("/sensitive")
    public ModelAndView sensitive() {
        String viewName = getViewName(Channel.b2b, JSPResource.unmatchSearch);
        ModelAndView view = new ModelAndView(viewName);
        view.addObject(ModelAttribute.keyword.name(), getParameter(HTTPParam.kw.name()));
        return view;
    }

    /**
     * 关键字搜索
     * 
     * @return
     */
    @RequestMapping("/search")
    @WapResource("/wap/kwsearch")
    public ModelAndView keywordSearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.searchPage);

        String keyword = getParameter(HTTPParam.kw.name());
        if (StringUtils.isBlank(keyword)) {
            LOG.warn("搜索关键字为空！");
            setViewName(view, JSPResource.unmatchSearch);
            return view;
        }

        // 搜索日志记录
        searchLog.info("搜索关键词：" + keyword);

        Map<String, Object> result = Maps.newHashMap();
        try {
            SearchConfig config = getConfig(true, true, true, false, true);

            result = b2bESSearchService.keywordSearch(keyword, config);

            if (!haveProducts(result)) {
                if (!isFilteQuery()) {
                    setViewName(view, JSPResource.unmatchSearch);
                }

                // 拆词改AND操作为OR操作搜索
                List<Map<String, Object>> splitSearchResult =
                        b2bESSearchService.splitSearch(keyword);
                if (!splitSearchResult.isEmpty()) {
                    if (null == result) {
                        result = Maps.newHashMap();
                    }
                    result.put(ModelAttribute.recommends.name(), splitSearchResult);
                    setViewName(view, JSPResource.relatedSearch);
                }
            }

            // 生成面包屑路径
            String prefix = "search?kw=" + keyword;
            getBreadMapAndHot1(result, prefix);

            // 用于页面title描述
            setAttribute("title", keyword + " - 商品搜索 - 康美中药城");
            setAttribute("keywords",
                    keyword + "价格，" + keyword + "说明书，" + keyword + "效果，" + keyword + "作用");
            // 关键词过长，省略显示处理
            if (keyword.length() > 10) {
                setAttribute("showKeyword", keyword.substring(0, 10) + "...");
            } else {
                setAttribute("showKeyword", keyword);
            }
        } catch (Exception e) {
            LOG.error("关键搜索发生异常.搜索词为：" + getParameter(HTTPParam.kw.name()), e);
            view.setViewName(getErrorViewName());
        }
        addModel(view, result);
        return view;

    }

    /**
     * 物理类目搜索
     * 
     * @return
     */
    @RequestMapping("/categorySearch")
    public ModelAndView categorySearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.searchPage);

        if (!containts(HTTPParam.cid.name())) {
            LOG.warn("搜索物理类目ID为空！");
            setViewName(view, JSPResource.unmatchSearch);
            return view;
        }
        Map<String, Object> result = Maps.newHashMap();
        try {
            SearchConfig config = getConfig(true, true, false, false, false);
            // 默认促销排序前面
            if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
                config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
            }
            result = b2bESSearchService.categorySearch(config);
            if (!haveProducts(result)) {
                if (!isFilteQuery()) {
                    setViewName(view, JSPResource.unmatchSearch);
                }
            }

            String cid = getParameter(HTTPParam.cid.name());
            if (StringUtils.isNotBlank(cid)) {
                // 生成面包屑路径
                String prefix = "categorySearch?cid=" + cid;
                getBreadMapAndHot1(result, prefix);

                // 获取类目名称用于页面title描述
                String categoryName =
                        searchDBDataService.getCategoryNameById(Integer.parseInt(cid));
                if (StringUtils.isNotBlank(categoryName)) {
                    setAttribute("title", categoryName + " - 商品搜索 - 康美中药城");
                    setAttribute("keywords", categoryName + "价格，" + categoryName + "说明书，"
                            + categoryName + "效果，" + categoryName + "作用");
                }
            }
        } catch (Exception e) {
            LOG.error("获取物理类目:{} 搜索结果失败!", getParameter(HTTPParam.cid.name()), e);
            view.setViewName(getErrorViewName());
        }
        addModel(view, result);
        return view;
    }

    /**
     * 运营类目搜索
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/operCateSearch")
    @WapResource("/wap/categorySearch")
    public Object operCateSearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.categorySearchPage);

        String oid = getParameter(HTTPParam.oid.name());
        if (StringUtils.isBlank(oid)) {
            LOG.warn("获取运营ID为：[{}] 的搜索SQL为空，无法进行搜索。", oid);
            return view;
        }

        Map<String, Object> result = null;
        try {
            SearchConfig config = getConfig(true, true, false, false, false);
            // 默认促销排序前面
            if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
                config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
            }
            Map<String, Object> resultMap = Maps.newHashMap();
            Map<String, Object> tdkParamMap = Maps.newHashMap();
            resultMap = b2bESSearchService.operCateSearch(oid, config);
            result = (Map<String, Object>) resultMap.get("result");
            if (null == result) {
                result = Maps.newHashMap();
            }
            tdkParamMap = (Map<String, Object>) resultMap.get("tdkParam_Map");
            if (null != tdkParamMap) {
                String categoryName = (String) tdkParamMap.get("c_name");
                String categoryDesc = (String) tdkParamMap.get("c_desc2");

                // 用于页面title描述
                if (StringUtils.isNotBlank(categoryName)) {
                    setAttribute("title", categoryName + " - 商品搜索 - 康美中药城");
                    setAttribute("keywords", categoryName + "价格，" + categoryName + "说明书，"
                            + categoryName + "效果，" + categoryName + "作用");
                }
                setAttribute("categoryDesc", categoryDesc);
            }

            if (!haveProducts(result)) {
                if (!isFilteQuery()) {
                    setViewName(view, JSPResource.unmatchSearch);
                }
            }
            getBreadMapAndHot2(result, oid);
        } catch (Exception e) {
            LOG.error("运营类目搜索结果处理时发生异常.运营类目ID: " + oid, e);
            view.setViewName(getErrorViewName());
        }

        addModel(view, result);
        return view;
    }

    /**
     * 品牌搜索
     * 
     * @return
     */
    @RequestMapping("/brandSearch")
    public ModelAndView brandSearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.searchPage);

        if (!containts(HTTPParam.bid.name())) {
            setViewName(view, JSPResource.unmatchSearch);
            return view;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            SearchConfig config = getConfig(true, true, false, false, false);
            // 默认促销排序前面
            if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
                config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
            }

            result = b2bESSearchService.brandSearch(config);

            if (!haveProducts(result)) {
                if (!isFilteQuery()) {
                    setViewName(view, JSPResource.unmatchSearch);
                }
            }

            String bid = getParameter(HTTPParam.bid.name());
            if (StringUtils.isNotBlank(bid)) {
                // 生成面包屑路径
                String prefix = "brandSearch?bid=" + bid;
                getBreadMapAndHot1(result, prefix);

                // 获取品牌名称用于页面title描述
                Brand brand = searchDBDataService.getBrandDetails(Integer.parseInt(bid));
                if (brand != null && StringUtils.isNotBlank(brand.getBrandName())) {
                    setAttribute("title", brand.getBrandName() + " - 商品搜索 - 康美中药城");
                    setAttribute("keywords", brand.getBrandName() + "价格，" + brand.getBrandName()
                            + "说明书，" + brand.getBrandName() + "效果，" + brand.getBrandName() + "作用");
                }
            }
        } catch (Exception e) {
            LOG.error("获取品牌搜索数据失败!品牌ID：" + getParameter(HTTPParam.bid.name()), e);
            view.setViewName(getErrorViewName());
        }
        addModel(view, result);
        return view;
    }

    /**
     * 品牌主页搜索 brandPage
     */
    @RequestMapping("/brandWebSearch")
    public ModelAndView brandWebSearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.brandPage);

        if (!containts(HTTPParam.bid.name())) {
            setViewName(view, JSPResource.unmatchSearch);
            return view;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            SearchConfig config = getConfig(true, true, false, false, false);
            // 默认促销排序前面
            if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
                config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
            }
            String bid = getParameter(HTTPParam.bid.name());
            result = b2bESSearchService.brandWebSearch(bid, config);

            if (null != result) {
                Brand b = (Brand) result.remove("brand_info");
                if (null == b) {
                    LOG.error("获取品基本数据失败!品牌ID：" + bid);
                    setViewName(view, JSPResource.unmatchSearch);
                } else {
                    result.put(ModelAttribute.brand.name(), b);

                    // 获取品牌名称用于页面title描述
                    if (StringUtils.isNotBlank(b.getBrandName())) {
                        setAttribute("title", b.getBrandName() + " - 商品搜索 - 康美中药城");
                        setAttribute("keywords", b.getBrandName() + "价格，" + b.getBrandName()
                                + "说明书，" + b.getBrandName() + "效果，" + b.getBrandName() + "作用");
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("获取品基本数据失败!品牌ID：" + getParameter(HTTPParam.bid.name()), e);
            view.setViewName(getErrorViewName());
        }
        addModel(view, result);
        return view;
    }

    /**
     * suggest搜索
     * 
     * @return
     */
    @RequestMapping(value = "/suggest", produces = {"application/json"})
    @ResponseBody
    public String suggestSearch() {

        if (!containts(HTTPParam.q.name()))
            return "";

        String callback = getParameter("callback");
        String json = "";
        try {
            Cache<Object, Object> cache = CacheUtil.getCache(Channel.b2b.name() + ".suggest.search",
                    3000, 12, TimeUnit.HOURS, false);
            final String prefix = getParameter(HTTPParam.q.name()).toLowerCase();
            Object jsonObj = cache.get(prefix, new Callable<List<Suggest>>() {

                @Override
                public List<Suggest> call() throws Exception {
                    SearchConfig config = SearchConfig.build(getParameterMap());
                    config.getConfig().put("q", new String[] {prefix});
                    List<Suggest> result = b2bESSearchService.suggestSearch(config);
                    return result;
                }
            });

            if (!StringUtils.isBlank(callback) && jsonObj != null) {

                json = callback + "(" + JSONObject.toJSONString(jsonObj) + ")";
            }
        } catch (Exception e) {

            LOG.error("SUGGEST搜索获取数据失败!搜索词条为：" + getParameter(HTTPParam.q.name()), e);
        }

        return json;
    }

    /**
     * 根据店铺编码搜索店铺商品
     * 
     * @return
     */
    @RequestMapping("/shopSearch")
    public ModelAndView shopSearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.searchPage);
        if (!containts(HTTPParam.sc.name())) {
            setViewName(view, JSPResource.unmatchSearch);
            return view;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            SearchConfig config = getConfig(true, true, false, false, false);
            // 默认促销排序前面
            if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
                config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
            }
            result = b2bESSearchService.shopSearch(config);

            if (!haveProducts(result)) {
                if (!isFilteQuery()) {
                    setViewName(view, JSPResource.unmatchSearch);
                }
            }


            String shopCode = getParameter(HTTPParam.sc.name());
            if (StringUtils.isNotBlank(shopCode)) {
                // 生成面包屑路径
                String prefix = "shopSearch?sc=" + shopCode;
                getBreadMapAndHot1(result, prefix);

                // 获取店铺名称用于页面title描述
                String shopName = searchDBDataService.getShopNameId(Integer.parseInt(shopCode));
                if (StringUtils.isNotBlank(shopName)) {
                    setAttribute("title", shopName + "-商品搜索-康美中药城");
                    setAttribute("keywords", shopName + "价格，" + shopName + "说明书，" + shopName + "效果，"
                            + shopName + "作用");
                }
            }
        } catch (Exception e) {
            LOG.error("获取商铺搜索数据失败!商铺CODE：" + getParameter(HTTPParam.sc.name()), e);
            view.setViewName(getErrorViewName());
        }
        addModel(view, result);
        return view;
    }

    /**
     * 店铺商品搜索
     * 
     * @return
     */
    @RequestMapping("/shopProducts")
    public ModelAndView shopProducts() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.shopProduct);

        String shopid = getParameter(HTTPParam.shopid.name());
        if (StringUtils.isEmpty(shopid)) {
            LOG.warn("店铺类目搜索传递的店铺ID参数为空,无法进行搜索.");
            return view;
        }

        // 搜索日志记录
        String kw = getParameter(HTTPParam.kw.name());
        if (StringUtils.isNotBlank(kw)) {
            searchLog.info("搜索关键词：" + kw);
        }

        SearchConfig config = getConfig(true, true, false, false, false);
        // 默认促销排序前面
        if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
            config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
        }

        Map<String, Object> result = b2bESSearchService.shopProductsSearch(config);
        result.put(ModelAttribute.shopid.name(), shopid);

        // 获取店铺名称用于页面title描述
        String shopName = searchDBDataService.getShopNameId(Integer.parseInt(shopid));
        if (StringUtils.isNotBlank(shopName)) {
            setAttribute("title", shopName);
            setAttribute("keywords",
                    shopName + "价格，" + shopName + "说明书，" + shopName + "效果，" + shopName + "作用");
        }

        addModel(view, result);
        return view;
    }

    private void getBreadMapAndHot1(Map<String, Object> result, String prefix) {

        if (StringUtils.isBlank(prefix)) {

            return;
        }
        try {
            if (result == null) {

                result = Maps.newHashMap();
            }

            Map<String, String> breadMap = getBreadVal(prefix, Channel.b2b);
            result.put(ModelAttribute.breadMap.name(), breadMap);
        } catch (Exception e) {
            LOG.error("无法获取热门搜索商品或面包屑。", e);
        }
    }

    private void getBreadMapAndHot2(Map<String, Object> result, String oid) {

        if (StringUtils.isBlank(oid)) {

            return;
        }
        try {
            if (result == null) {

                result = Maps.newHashMap();
            }

            // 筛选类目列表
            List<Category> cateList = new CategoryUtil().getFixCategoryList(Channel.b2b.name(),
                    NumberUtils.toInt(oid));
            result.put(ModelAttribute.cateList.name(), cateList);

            Map<String, String> breadMap = getOperaBreadVal(oid, Channel.b2b);
            result.put(ModelAttribute.breadMap.name(), breadMap);
        } catch (Exception e) {
            LOG.error("无法获取热门搜索商品或面包屑。", e);
        }
    }

}
