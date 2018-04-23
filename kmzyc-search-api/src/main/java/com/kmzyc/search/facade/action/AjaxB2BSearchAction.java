package com.kmzyc.search.facade.action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.util.IndexClientUtil;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.facade.vo.Category;
import com.kmzyc.search.facade.vo.ReturnResult;
import com.kmzyc.search.param.DocFieldName;

/**
 * CMS异步查询运营类目接口
 * 
 * @author zhoulinhong
 * @since 20160525
 */
@Controller
public class AjaxB2BSearchAction extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AjaxB2BSearchAction.class);

    /**
     * 获取商品运营类目
     * 
     * @author zhoulinhong
     * @since 20160525
     */
    @ResponseBody
    @RequestMapping("/**/getSKUCategory")
    public String getSKUCategory() {

        ReturnResult<Map<String, Map<String, Object>>> returnResult = new ReturnResult<>();

        final String skuId = this.getParameter("skuId");
        String callback = getParameter("callback");

        if (StringUtils.isBlank(skuId)) {

            if (logger.isDebugEnabled()) {

                logger.debug("无法获取请求中skuId参数值。");
            }
            returnResult.setCode("0");
            returnResult.setMessage("无法获取请求中skuId参数值");

            // 返回结果信息
            String json = JSON.toJSONString(returnResult);
            if (!StringUtils.isBlank(callback)) {
                json = callback + "(" + json + ")";
            }

            return json;
        } else {
            if (logger.isDebugEnabled()) {

                logger.debug("查询商品运营类目信息请求中skuId参数值为：" + skuId);
            }

            String json = "";
            Cache<Object, Object> cache = CacheUtil.getCache("sku.category.cache", false);
            try {

                json = cache.get(skuId, new Callable<String>() {

                    @Override
                    public String call() throws Exception {

                        // 获取运营类目，并缓存
                        return getCategory(skuId);
                    }
                }).toString();
            } catch (ExecutionException e) {

                logger.error("获取skuId：" + skuId + "的运营类目信息失败！", e);
            }

            if (!StringUtils.isBlank(callback)) {

                json = callback + "(" + json + ")";
            }

            // 结果返回
            return json;
        }
    }

    /**
     * 根据渠道获取运营类目
     * 
     * @param skuId
     * @param channel
     * @return
     */
    @SuppressWarnings("unchecked")
    private String getCategory(final String skuId) {

        ReturnResult<Map<Integer, Map<String, Object>>> returnResult = new ReturnResult<>();

        try {
            // 获取运营类目
            Map<Integer, Object> resultMap = getResult(skuId);
            if (null == resultMap || resultMap.isEmpty()) {

                returnResult.setCode("404");
                returnResult.setMessage("无运营类目信息");
            } else {

                Map<Integer, Map<String, Object>> infoMap =
                        (Map<Integer, Map<String, Object>>) resultMap.get(0);
                returnResult.setReturnObject(infoMap);
                returnResult.setCode("200");
                returnResult.setMessage("OK");
            }
        } catch (Exception e) {

            returnResult.setCode("0");
            returnResult.setMessage("获取数据失败！");
            logger.error("获取skuId：" + skuId + "的运营类目信息失败！", e);
        }

        // 结果返回
        return JSONObject.toJSONString(returnResult);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Object> getResult(String skuId) {

        if (StringUtils.isBlank(skuId)) {

            logger.warn("skuId错误，无法获取搜索对象");
            return null;
        }

        Map<Integer, Object> resultMap = Maps.newHashMap();

        // 获取渠道下所有运营类目集合
        final Map<Integer, Category> categories =
                memCache.getPublishedOperationCategory(Channel.b2b.name());

        try {
            // 获取对应的索引数据
            Map<String, Object> index = IndexClientUtil.getInstance()
                    .queryIndexById(Channel.b2b.name(), Channel.b2b.name(), skuId);

            if (null == index || index.isEmpty()) {

                return null;
            }

            Map<Integer, Map<String, Object>> infoMap = Maps.newHashMap();
            resultMap.put(0, infoMap);

            // 3级
            Collection<String> thirdCode = null;
            if (null != index.get(DocFieldName.THIRD_O_CODE)) {
                thirdCode = (Collection<String>) index.get(DocFieldName.THIRD_O_CODE);
                // 获取类目信息
                getInfo(thirdCode, infoMap, categories);
            }

            if (infoMap == null || infoMap.isEmpty()) {
                // 2级
                Collection<String> secondCode = null;
                if (null != index.get(DocFieldName.SECOND_O_CODE)) {
                    secondCode = (Collection<String>) index.get(DocFieldName.SECOND_O_CODE);
                    // 获取类目信息
                    getInfo(secondCode, infoMap, categories);
                }
            }

            if (infoMap == null || infoMap.isEmpty()) {
                // 1级
                Collection<String> firstCode = null;
                if (null != index.get(DocFieldName.FIRST_O_CODE)) {
                    firstCode = (Collection<String>) index.get(DocFieldName.FIRST_O_CODE);
                    // 获取类目信息
                    getInfo(firstCode, infoMap, categories);
                }
            }

        } catch (Exception e) {
            logger.error("获取skuId：" + skuId + "的运营类目信息失败！", e);
        }

        return resultMap;
    }

    private void getInfo(Collection<String> codes, Map<Integer, Map<String, Object>> infoMap,
            final Map<Integer, Category> categories) {
        if (null != codes && !codes.isEmpty()) {
            // 取第一个
            String code = codes.iterator().next();
            List<String> ids = Splitter.on("_").splitToList(code);
            for (int i = 0; i < ids.size(); i++) {
                Map<String, Object> map = Maps.newHashMap();
                int oid = Integer.parseInt(ids.get(i));
                map.put("oid", oid);
                Category c = categories.get(oid);
                if (null == c) {
                    continue;
                }
                map.put("name", c.getName());
                infoMap.put(i, map);
            }
        }
    }

    /**
     * portal获取指定类别的商品
     * 
     * @zhoulinhong
     * @since 20160525
     */
    @ResponseBody
    @RequestMapping(value = {"/10/products", "/b2b/products"}, produces = {"application/json"})
    public String portalGetB2BProducts() {

        ReturnResult<Map<String, Object>> result = new ReturnResult<>();
        try {
            String sc = getParameter("sc");

            // 商品类别
            String type = getParameter("type");
            if (StringUtils.isBlank(sc) || StringUtils.isBlank(type)) {

                result.setCode("404");
                result.setMessage("传递参数错误（搜索条件和商品类别不能为空）。搜索失败！");
                result.setReturnObject(Collections.emptyMap());
                return JSON.toJSONString(result);
            }

            // 搜索日志记录
            searchLog.info("搜索关键词：" + sc);

            // 商品类别
            int productType = Integer.parseInt(type);

            // 默认搜索第一页
            int pageNo = 1;
            String pc = getParameter("pc");
            if (StringUtils.isNotBlank(pc)) {
                pageNo = Integer.parseInt(pc);
            }

            // 默认每页返回8个商品
            int pageSize = 8;
            String ps = getParameter("ps");
            if (StringUtils.isNotBlank(ps)) {
                pageSize = Integer.parseInt(ps);
            }

            // 搜索语句
            String queryString = getQueryString(pageNo, pageSize, productType, sc);

            SearchHits searchHits = IndexClientUtil.getInstance()
                    .queryIndexByConditions(Channel.b2b.name(), Channel.b2b.name(), queryString);


            if (null != searchHits && searchHits.getTotalHits() > 0) {

                // 组装产品数据格式
                List<Map<String, Object>> products = getProducts(searchHits.getHits());
                Map<String, Object> value = Maps.newHashMap();
                value.put("ps", ps);
                value.put("pc", pc);
                value.put("products", products);
                value.put("count", searchHits.getTotalHits());
                result.setCode("200");
                result.setMessage("搜索成功！");
                result.setReturnObject(value);
                return JSON.toJSONString(result);
            }
        } catch (Exception e) {

            logger.error("搜索失败！", e);
            result.setCode("404");
            result.setMessage("搜索失败！");
            result.setReturnObject(Collections.emptyMap());
            return JSON.toJSONString(result);
        }

        result.setCode("200");
        result.setMessage("商品为空");
        result.setReturnObject(Collections.emptyMap());
        return JSON.toJSONString(result);
    }

    /**
     * 搜索语句
     * 
     * @param pc
     * @param ps
     * @param type
     * @param sc
     * @return
     */
    private String getQueryString(int pc, int ps, int type, String sc) {

        JSONObject queryJson = JSONObject.parseObject("{\"query\":{\"query_string\":{}}}");

        // 分页
        int start = (pc - 1) * ps;
        start = start <= 0 ? 0 : start;
        queryJson.put("from", start);
        queryJson.put("size", ps);

        // 查询条件
        StringBuilder queryText = new StringBuilder(
                DocFieldName.PRODUCT_TITLE + ":" + ParamUitl.escapeQueryChars(sc));
        queryText.append(" OR ");
        queryText.append(DocFieldName.PRODUCT_NO + ":" + sc);
        queryJson.getJSONObject("query").getJSONObject("query_string").put("query",
                queryText.toString());

        // 过滤
        JSONArray filterJson = new JSONArray();
        // TODO APP端过滤掉预售商品的搜索
        // TODO add by zhoulinhong on 20160828
        filterJson.add(
                JSONObject.parse("{\"bool\": {\"must_not\": {\"term\": {\"promotion\": \"2\"}}}}"));

        filterJson.add(JSONObject.parseObject(
                "{\"term\": {\"" + DocFieldName.PRODUCT_TYPE + "\": \"" + type + "\"}}"));
        queryJson.put("post_filter", JsonUtil.jsonPut(new JSONObject(), "and", filterJson));

        // 返回字段
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(DocFieldName.ID);
        jsonArray.add(DocFieldName.PRODUCT_TITLE);
        jsonArray.add(DocFieldName.SKUCODE);
        jsonArray.add(DocFieldName.PRICE);
        jsonArray.add(DocFieldName.MPRICE);
        jsonArray.add(DocFieldName.IMAGE);
        jsonArray.add(DocFieldName.PROMOTION);
        queryJson.put("fields", jsonArray);

        // 设置搜索默认运算方式
        queryJson.getJSONObject("query").getJSONObject("query_string").put("default_operator",
                "AND");

        return queryJson.toJSONString();
    }

    private List<Map<String, Object>> getProducts(SearchHit[] docs) {

        if (null == docs || docs.length <= 0) {

            return Collections.emptyList();
        }

        List<Map<String, Object>> products = Lists.newArrayList();
        for (SearchHit doc : docs) {

            // 商品对象
            Map<String, Object> product = Maps.newHashMap();
            // 返回字段值
            Map<String, SearchHitField> fields = doc.fields();

            // 商品ID
            String id = null;
            if (null != fields.get(DocFieldName.ID)) {
                id = fields.get(DocFieldName.ID).getValue().toString();
            }
            product.put("skuid", id);

            // 商品标题
            String title = null;
            if (null != fields.get(DocFieldName.PRODUCT_TITLE)) {
                title = fields.get(DocFieldName.PRODUCT_TITLE).getValue();
            }
            product.put("title", title);

            // 商品编号
            String pno = null;
            if (null != fields.get(DocFieldName.SKUCODE)) {
                pno = fields.get(DocFieldName.SKUCODE).getValue();
            }
            product.put("skuCode", pno);

            // 商品图片
            Collection<Object> images = null;
            if (null != fields.get(DocFieldName.IMAGE)) {
                images = fields.get(DocFieldName.IMAGE).getValues();
            }
            // default value;
            product.put("image", "");
            if (null != images && !images.isEmpty()) {
                for (Object image : images) {
                    String path = image.toString();
                    if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH7=")) {
                        int index = path.indexOf("=");
                        product.put("image", path.substring(index + 1));
                    }
                }
            }

            // 销售价格
            double price = 0.0;
            if (null != fields.get(DocFieldName.PRICE)) {
                price = Double.parseDouble(fields.get(DocFieldName.PRICE).getValue().toString());
            }
            product.put("price", price);

            // 市场价格
            double mprice = 0.0;
            if (null != fields.get(DocFieldName.MPRICE)) {
                mprice = Double.parseDouble(fields.get(DocFieldName.MPRICE).getValue().toString());
            }
            product.put("mprice", mprice);

            products.add(product);
        }

        // 结果返回
        return products;
    }
}
