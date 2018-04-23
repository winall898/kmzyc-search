package com.kmzyc.search.facade.action;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.constants.APPResultKeys;
import com.kmzyc.search.facade.service.IAppESSearchService;
import com.kmzyc.search.facade.service.IOtherSystemService;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.facade.vo.ReturnResult;
import com.kmzyc.search.facade.vo.Suggest;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;

/**
 * 手机端商品搜索接口
 * 
 * @author river
 * 
 */
@Controller
@RequestMapping("/app")
public class AppSearchAction extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppSearchAction.class);

    public static final int DEF_PAGE_SIZE = 10;

    @Resource(name = "appESSearchService")
    private IAppESSearchService appESSearchService;

    @Resource(name = "otherSystemService")
    private IOtherSystemService otherSystemService;

    /**
     * 关键字搜索
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/appKeywordSearch")
    @ResponseBody
    public String appKeywordSearch() {
        Map<String, Object> result = Maps.newHashMap();
        ReturnResult<Map<String, Object>> returnResult = new ReturnResult<>();
        try {
            ParamUtil util = new ParamUtil();
            Map<String, Object> params = util.extractKWQueryReq();
            if (params == null || params.isEmpty()) {
                returnResult.setCode("404");
                returnResult.setMessage("请求参数为空！");
                returnResult.setReturnObject(result);
                return JSON.toJSONString(returnResult);
            }
            if (params.get("paramsError") != null) {
                returnResult.setCode("404");
                returnResult.setMessage(params.get("paramsError").toString());
                returnResult.setReturnObject(result);
                return JSON.toJSONString(returnResult);
            }

            // 搜索日志记录
            if (params.get(HTTPParam.q.name()) != null
                    && StringUtils.isNotBlank(params.get(HTTPParam.q.name()).toString())) {

                searchLog.info("搜索关键词：" + params.get(HTTPParam.q.name()).toString());
            }

            int pagination = MapUtils.getIntValue(params, "pageNo");
            params.remove("pageNo");
            int size;
            params.remove("size");

            result = appESSearchService.keywordSearch(params);
            result.put(APPResultKeys.PAGINATION, pagination);
            Object products = result.get(APPResultKeys.PRODUCTS);
            if (null == products) {
                size = 0;
            } else {
                List<Map<String, Object>> ps = (List<Map<String, Object>>) products;
                size = ps.size();
            }
            result.put(APPResultKeys.SIZE, size); // 当前页商品数
            if (size == 0) {
                returnResult.setCode("404");
                returnResult.setMessage("搜索失败。无数据！");
            } else {
                returnResult.setCode("200");
                returnResult.setMessage("搜索成功");
            }
        } catch (Exception e) {
            returnResult.setCode("404");
            returnResult.setMessage("搜索失败");
            logger.error("处理APP搜索时发生异常", e);
        }
        returnResult.setReturnObject(result);
        return JSON.toJSONString(returnResult);
    }

    /**
     * 类目搜索
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/appCategorySearch")
    @ResponseBody
    public String appCategorySearch() {
        Map<String, Object> result = Maps.newHashMap();
        ReturnResult<Map<String, Object>> returnResult = new ReturnResult<>();
        try {
            ParamUtil util = new ParamUtil();
            Map<String, Object> params = util.extractCGQueryReq(returnResult);
            if (params.isEmpty()) {
                return JSON.toJSONString(returnResult);
            }
            int pagination = MapUtils.getIntValue(params, "pageNo");
            params.remove("pageNo");
            int size;
            params.remove("size");

            result = appESSearchService.appCategorySearch(params);
            result.put(APPResultKeys.PAGINATION, pagination);
            Object products = result.get(APPResultKeys.PRODUCTS);
            if (null == products) {
                size = 0;
            } else {
                List<Map<String, Object>> ps = (List<Map<String, Object>>) products;
                size = ps.size();
            }
            result.put(APPResultKeys.SIZE, size); // 当前页商品数
            if (size == 0) {
                returnResult.setCode("404");
                returnResult.setMessage("搜索失败。无数据！");
            } else {
                returnResult.setCode("200");
                returnResult.setMessage("搜索成功");
            }
        } catch (NumberFormatException e) {
            returnResult.setCode("404");
            returnResult.setMessage("搜索失败。传入OID参数非数字格式。");
            logger.error("处理APP搜索时发生异常", e);
        } catch (Exception e) {
            returnResult.setCode("404");
            returnResult.setMessage("搜索失败。");
            logger.error("处理APP搜索时发生异常", e);
        }
        returnResult.setReturnObject(result);
        return JSON.toJSONString(returnResult);
    }

    @RequestMapping(value = "/suggest", produces = {"application/json"})
    @ResponseBody
    public String appSuggest(@RequestParam String q, @RequestParam String c) {
        String prefix = q;
        String channelVal = c;
        String callback = getParameter("callback");

        Map<String, Object> result = Maps.newHashMap();
        result.put("result", "");
        if (StringUtils.isBlank(prefix) || StringUtils.isBlank(channelVal)) {
            String json = JSON.toJSONString(result);
            if (!StringUtils.isBlank(callback)) {
                return callback + "(" + json + ")";
            }
        }

        prefix = prefix.toLowerCase();

        try {
            final Channel channel = Channel.valueOf(channelVal.toLowerCase());
            Cache<Object, Object> cache = CacheUtil.getCache(channel.name() + ".suggest.search",
                    3000, 12, TimeUnit.HOURS, false);
            Object sugs = cache.get(prefix, new Callable<List<Suggest>>() {

                @Override
                public List<Suggest> call() throws Exception {

                    return appESSearchService.appSuggestSearch(channel, getRequest());
                }
            });
            result.put("result", sugs);

        } catch (Exception e) {
            logger.error("APP SUGGEST搜索获取数据失败!搜索词条为：" + prefix, e);
        }
        String json = JSON.toJSONString(result);
        if (!StringUtils.isBlank(callback)) {
            return callback + "(" + json + ")";
        }
        return json;
    }

    class ParamUtil {

        /**
         * 获取关键词搜索参数
         * 
         * @param request
         * @return
         */
        public Map<String, Object> extractKWQueryReq() {

            Map<String, Object> params = Maps.newHashMap();

            JSONObject info = getJsonObject();
            if (null == info) {

                return Collections.emptyMap();
            }

            String keyword = info.getString(HTTPParam.kw.name());

            String qPara = "*:*";


            if (!StringUtils.isBlank(keyword)) {
                qPara = ParamUitl.escapeQueryChars(keyword);
            }

            params.put("q", qPara); // 关键字

            decodeJson(info, params);


            String shopid = info.getString(HTTPParam.shopid.name());
            String shopname = info.getString(HTTPParam.shopname.name());

            if (StringUtils.isNotBlank(shopid) && StringUtils.isNotBlank(shopname)) {
                params.put("paramsError", "搜索失败。 请求参数shopid和shopname不可同时查询!");
                return params;
            }

            String stock = info.getString(HTTPParam.stock.name());
            String stype = info.getString(HTTPParam.suptype.name());
            String ptype = info.getString(HTTPParam.ptype.name());
            String skucode = info.getString(HTTPParam.skucode.name());
            String brandId = info.getString(HTTPParam.brandId.name());
            String brandName = info.getString(HTTPParam.brandName.name());
            String price = info.getString(HTTPParam.price.name());
            String shopgory = info.getString(HTTPParam.shopgory.name());
            String c3 = info.getString(HTTPParam.c3.name());
            // wn add, 2016.4.6
            String c2 = info.getString(HTTPParam.c2.name());

            if (StringUtils.isNotBlank(stock)) {
                params.put(DocFieldName.STOCK, ParamUitl.escapeQueryChars(stock));
            }
            if (StringUtils.isNotBlank(shopid)) {
                params.put(DocFieldName.SHOPID, ParamUitl.escapeQueryChars(shopid));
            }
            if (StringUtils.isNotBlank(shopname)) {
                params.put(DocFieldName.SHOPNAME, ParamUitl.escapeQueryChars(shopname));
            }
            if (StringUtils.isNotBlank(stype)) {
                params.put(DocFieldName.SUPTYPE, ParamUitl.escapeQueryChars(stype));
            }
            if (StringUtils.isNotBlank(ptype)) {
                params.put("ptype", ParamUitl.escapeQueryChars(ptype));
            }
            if (StringUtils.isNotBlank(skucode)) {
                params.put(DocFieldName.SKUCODE, ParamUitl.escapeQueryChars(skucode));
            }
            if (StringUtils.isNotBlank(brandId)) {
                params.put(DocFieldName.BRAND_ID, ParamUitl.escapeQueryChars(brandId));
            }
            if (StringUtils.isNotBlank(price)) {
                params.put(DocFieldName.PRICE, ParamUitl.escapeQueryChars(price));
            }
            if (StringUtils.isNotBlank(shopgory)) {
                params.put(DocFieldName.SHOPGORY, ParamUitl.escapeQueryChars(shopgory));
            }
            // wn add, 2016.4.6
            if (StringUtils.isNotBlank(c2)) {
                params.put(DocFieldName.SECOND_O_CODE, ParamUitl.escapeQueryChars(c2));
            }
            if (StringUtils.isNotBlank(c3)) {
                params.put(DocFieldName.THIRD_O_CODE, ParamUitl.escapeQueryChars(c3));
            }
            if (StringUtils.isNotBlank(brandName)) {
                params.put(DocFieldName.BRAND_NAME, ParamUitl.escapeQueryChars(brandName));
            }

            return params;
        }

        /**
         * 获取类目搜索请求参数
         * 
         * @param request
         * @return
         * @throws NumberFormatException
         * @throws Exception
         */
        public Map<String, Object> extractCGQueryReq(
                ReturnResult<Map<String, Object>> returnResult) {

            Map<String, Object> params = Maps.newHashMap();

            JSONObject info = getJsonObject();
            if (null == info) {

                return Collections.emptyMap();
            }

            String oid = info.getString(HTTPParam.oid.name());
            if (StringUtils.isBlank(oid)) {
                returnResult.setCode("404");
                returnResult.setMessage("搜索失败。 请求参数oid为空。");
                return params;
            }

            String query = otherSystemService.getExecSql(Long.parseLong(oid));
            if (StringUtils.isBlank(query)) {
                returnResult.setCode("404");
                returnResult.setMessage("搜索失败。 该运营类目没有对应的搜索语句，请检查运营类目SQL字段设置。");
                return params;
            }
            params.put("q", query); // 关键字

            decodeJson(info, params);
            // 默认促销商品排在前面
            Object sort = params.get("sort");
            if (null == sort) {
                params.put("sort", DocFieldName.PROMOTION + " desc");
            }
            return params;
        }

        private void decodeJson(JSONObject info, Map<String, Object> params) {
            int pageNum = info.getIntValue("pageNo");
            if (pageNum < 1) {
                pageNum = 1;
            }

            int pageSize = info.getIntValue("pageNum");
            if (pageSize < 1) {
                pageSize = DEF_PAGE_SIZE;
            }

            params.put("pageNo", pageNum);
            params.put("size", pageSize);
            int start = (pageNum - 1) * pageSize;
            start = start < 0 ? 0 : start;
            params.put("start", start); // 起始偏移量
            params.put("rows", pageSize); // 搜索每页的产品数量

            String sort = info.getString(HTTPParam.sort.name()); // 排序
            if (StringUtils.isNotBlank(sort)) {
                params.put("sort", sort);
            }
        }

    }

    /**
     * 获取json对象
     * 
     * @param request
     * @return
     */
    protected JSONObject getJsonObject() {
        JSONObject json = null;
        try {
            String code = URLDecoder.decode(getParameter(HTTPParam.json.name()), "utf-8");
            json = JSONObject.parseObject(code);
        } catch (Exception e) {
            logger.error("手机接口json数据错误：" + e.getMessage(), e);
        }
        return json;
    }

}
