package com.kmzyc.search.facade.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.service.IWAPSearchService;
import com.kmzyc.search.facade.util.CategoryUtil;
import com.kmzyc.search.facade.vo.Category;

/**
 * wap搜索接口. 是否直接调用 WebSearchAction 对应搜索方法返回结果？
 * 
 * @author river
 * 
 */
@Controller
@RequestMapping("/wap")
public class WapSearchAction extends BaseController {
    protected static final Logger LOG = LoggerFactory.getLogger(WapSearchAction.class);

    @Resource(name = "wapSearchService")
    protected IWAPSearchService wapSearchService;

    @RequestMapping("/kwsearch")
    public ModelAndView keyWordSearch() {
        ModelAndView view = new ModelAndView();

        String keyword = getParameter("kw");
        if (StringUtils.isBlank(keyword)) {
            view.setViewName("wap/unmatchSearch");
            return view;
        }

        // 搜索日志记录
        searchLog.info("搜索关键词：" + keyword);

        try {

            Map<String, Object> result = Maps.newHashMap();
            result = wapSearchService.keyWordSearch(getRequest());

            if (!haveProducts(result)) {
                view.setViewName("wap/unmatchSearch");
                return view;
            }
            addModel(view, result);
        } catch (Exception e) {
            LOG.error("wap 关键搜索发生异常.搜索词为：" + getParameter("kw"), e);
        }

        String href = prefixHref("/wap/kwsearch?kw=" + getParameter("kw"));
        String price = getParameter("price");
        String supply = getParameter("supply");
        if (price != null)
            href += "&price=" + price;
        if (supply != null)
            href += "&supply=" + supply;
        view.addObject("href", href);
        view.setViewName("wap/wapSearchPage");
        return view;
    }

    @RequestMapping(value = "/ajax/kwsearch", produces = {"application/json"})
    @ResponseBody
    public String ajaxKeyWordSearch() {
        ModelAndView view = keyWordSearch();
        Map<String, Object> model = view.getModel();
        Map<String, Object> result = Maps.newHashMap();
        if (null != model) {
            result.put("code", 200);
            result.put("message", "搜索成功");
            Map<String, Object> data = Maps.newHashMap();
            data.put("pageNo", model.get("pageNo"));
            data.put("pageSize", model.get("pageSize"));
            data.put("pageCount", model.get("pageCount"));
            data.put("skuCount", model.get("skuCount"));
            data.put("productList", model.get("productList"));
            result.put("data", data);
        } else {
            result.put("code", 404);
            result.put("message", "搜索失败");
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("/categorySearch")
    public ModelAndView operCateSearch() {
        ModelAndView view = new ModelAndView();
        String oid = getParameter("oid"); // 获取运营类目ID
        if (StringUtils.isBlank(oid)) {
            view.setViewName("wap/categorySearchPage");
            return view;
        }
        try {
            Map<String, Object> result = Maps.newHashMap();
            result = wapSearchService.categorySearch(getRequest());

            if (!haveProducts(result)) {
                if (isFilteQuery()) {
                    view.setViewName("wap/unmatchSearch");
                    return view;
                }
            }
            // 筛选类目列表
            List<Category> cateList = new CategoryUtil().getFixCategoryList(Channel.b2b.name(),
                    NumberUtils.toInt(oid));
            result.put("cateList", cateList);
            addModel(view, result);
        } catch (Exception e) {
            LOG.error("wap 运营类目搜索发生异常.oid：" + oid, e);
        }
        String href = prefixHref("/wap/categorySearch?oid=" + getParameter("oid"));
        view.addObject("href", href);
        String categoryName = getCategoryName(Integer.parseInt(getRequest().getParameter("oid")));
        view.addObject("categoryName", categoryName);
        view.setViewName("wap/categorySearchPage");
        return view;
    }

    @RequestMapping(value = "/ajax/categorySearch", produces = {"application/json"})
    @ResponseBody
    public String ajaxOperCateSearch() {
        ModelAndView view = operCateSearch();
        Map<String, Object> model = view.getModel();
        Map<String, Object> result = Maps.newHashMap();
        if (null != model) {
            result.put("code", 200);
            result.put("message", "搜索成功");
            Map<String, Object> data = Maps.newHashMap();
            data.put("pageNo", model.get("pageNo"));
            data.put("pageSize", model.get("pageSize"));
            data.put("pageCount", model.get("pageCount"));
            data.put("skuCount", model.get("skuCount"));
            data.put("productList", model.get("productList"));
            result.put("data", data);
        } else {
            result.put("code", 404);
            result.put("message", "搜索失败");
        }
        return JSON.toJSONString(result);
    }

    private String prefixHref(String prefix) {
        StringBuilder buf = new StringBuilder(prefix);
        if (null != getParameter("brand")) {
            buf.append("&brand=" + getParameter("brand"));
        }
        if (null != getParameter("c3")) {
            buf.append("&c3=" + getParameter("c3"));
        }
        if (null != getParameter("c2")) {
            buf.append("&c2=" + getParameter("c2"));
        }
        return buf.toString();
    }

    private String getCategoryName(int oid) {
        Map<Integer, Category> opercate =
                memCache.getPublishedOperationCategory(Channel.b2b.name());
        if (null == opercate) {
            LOG.warn("无法获取所有CMS发布的运营类目信息");
            return "";
        }
        if (null != opercate.get(oid)) {
            return opercate.get(oid).getName();
        }
        return "";
    }

}
