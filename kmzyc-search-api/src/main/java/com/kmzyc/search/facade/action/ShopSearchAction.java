package com.kmzyc.search.facade.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.config.Configuration;
import com.kmzyc.search.facade.constants.Constants;
import com.kmzyc.search.facade.request.transverter.SuggestParamsTransverter;
import com.kmzyc.search.facade.response.transverter.SuggestResultTransverter;
import com.kmzyc.search.facade.service.IB2BESSearchService;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.util.IndexClientUtil;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.facade.vo.Shop;
import com.kmzyc.search.facade.vo.SupplierInfo;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;
import com.kmzyc.search.param.ShopFieldName;
import com.kmzyc.search.searcher.Searcher;
import com.kmzyc.search.vo.ProductItem;

/**
 * 商铺搜索
 * 
 * @author zhoulinhong
 * @since 20160519
 */
@RequestMapping("/10/shop")
@Controller
public class ShopSearchAction extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(ShopSearchAction.class);

    private static final int PAGESIZE = Configuration.getInt("shop.search.result.pagesize", 8);

    @Resource(name = "searchDBDataService")
    protected SearchDBDataService searchDBDataService;

    @Resource(name = "b2bESSearchService")
    protected IB2BESSearchService b2bESSearchService;

    private void setChannel() {
        String key = Configuration.getString(Constants.requestChannel);
        setAttribute(key, "b2b");
    }

    /**
     * 店铺搜索
     */
    @RequestMapping("/items")
    public ModelAndView shopSearch() {
        setChannel();
        ModelAndView view = new ModelAndView();
        try {
            addViewObject(view);
            JSONObject queryJson = getQueryShopJson();
            SearchResponse searchResponse = IndexClientUtil.getInstance().getSearchResponse(
                    Channel.shop.name(), Channel.shop.name(), JSONObject.toJSONString(queryJson));
            if (null != searchResponse) {
                SearchHits searchHits = searchResponse.getHits();
                if (null != searchHits && null != searchHits.getHits()) {
                    // 搜索结果总数
                    view.addObject("shopCount", searchHits.getTotalHits());
                    if (searchHits.getHits().length > 0) {
                        List<Shop> shops = getShops(searchHits.getHits());
                        getProducts(shops);
                        view.addObject("shopList", shops);
                        view.addObject("pageCount", (searchHits.getTotalHits() + 1) / PAGESIZE);
                    }
                }
            }
        } catch (Exception e) {

            LOG.error("店铺搜索失败！", e);
        }

        view.setViewName("b2b/shops");
        return view;
    }

    private void addViewObject(ModelAndView view) {
        String q = getParameter("kw");
        view.addObject("keyword", q);

        String pageNum = getParameter("p");
        int pn = 1;
        if (StringUtils.isNotBlank(pageNum)) {
            pn = Integer.parseInt(pageNum);
        }
        view.addObject("pageNo", pn);

        String st = getParameter("st");
        if (StringUtils.isBlank(st)) {
            st = "0";
        }
        view.addObject("shopType", st);
    }

    @RequestMapping(value = "/suggest", produces = {"application/json"})
    @ResponseBody
    public String suggest() {

        setChannel();

        final String prefix = getParameter("q");

        if (StringUtils.isBlank(prefix)) {

            return "";
        }

        String callback = getParameter("callback");
        String json = "";
        try {

            // 店铺提示词缓存
            Cache<Object, Object> cache =
                    CacheUtil.getCache("shop.suggest.search", 3000, 12, TimeUnit.HOURS, false);

            Object jsonObj = cache.get(prefix, new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    // 提示词查询
                    Searcher suggestSearcher = new Searcher(Channel.suggest.name(),
                            Channel.suggest.name(), new SuggestParamsTransverter(Channel.shop),
                            new SuggestResultTransverter());

                    Map<String, String[]> searchParams = Maps.newHashMap();
                    searchParams.put("q", new String[] {prefix});

                    Map<String, Object> resultMap = suggestSearcher.search(searchParams);
                    if (null == resultMap || resultMap.isEmpty() || resultMap.get("count") == null
                            || Integer.parseInt(resultMap.get("count").toString()) <= 0) {

                        return "";
                    }

                    return resultMap.get("list");
                }
            });

            if (StringUtils.isNotBlank(callback) && jsonObj != null) {

                json = callback + "(" + JSONObject.toJSONString(jsonObj) + ")";
            }
        } catch (Exception e) {

            LOG.error("shop SUGGEST搜索获取数据失败!搜索词条为：" + prefix, e);
        }

        return json;
    }

    /**
     * 默认通用店铺-商品搜索
     */
    @RequestMapping("/search")
    public ModelAndView search() {

        ModelAndView view = new ModelAndView();
        view.setViewName("b2b/shop/normalShop");

        String shopId = getParameter(HTTPParam.shopid.name());
        if (StringUtils.isEmpty(shopId)) {
            LOG.error("店铺类目搜索传递的店铺ID参数为空,无法进行搜索.");
            view.setViewName("errors/error_notfound");
            return view;
        }

        // 搜索日志记录
        String kw = getParameter(HTTPParam.kw.name());
        if (StringUtils.isNotBlank(kw)) {
            searchLog.info("搜索关键词：" + kw);
        }

        // 查询供应商信息
        SupplierInfo supplierInfo =
                searchDBDataService.getSupplierInfoByShopId(Integer.parseInt(shopId));
        if (supplierInfo == null) {
            LOG.error("供应商不存在！");
            view.setViewName("errors/error_notfound");
            return view;
        }

        SearchConfig config = SearchConfig.build(getParameterMap()).setFacet(true).setEdismax(true)
                .setEnableElevation(false).setForceElevation(false).setHighlight(false);

        Map<String, Object> result = b2bESSearchService.shopProductsSearch(config);

        // 供应商信息
        result.put("supplierInfo", supplierInfo);

        // 店铺类目ID
        String soid = getParameter(HTTPParam.soid.name());
        if (StringUtils.isNotBlank(soid) && StringUtils.isBlank(kw)) {
            result.put("isKeyword", false);
            result.put("categoryName",
                    searchDBDataService.getShopCategoryNameById(Integer.parseInt(soid)));
        } else {
            result.put("isKeyword", true);
            result.put(HTTPParam.kw.name(), kw);
        }

        // 获取店铺名称用于页面title描述
        setAttribute("title", supplierInfo.getShopName());
        setAttribute("keywords", supplierInfo.getShopName() + "价格，" + supplierInfo.getShopName()
                + "说明书，" + supplierInfo.getShopName() + "效果，" + supplierInfo.getShopName() + "作用");

        addModel(view, result);
        return view;
    }

    protected List<Shop> getShops(SearchHit[] searchHits) {

        if (null == searchHits || searchHits.length <= 0) {

            return null;
        }

        List<Shop> shops = Lists.newArrayList();
        for (SearchHit searchHit : searchHits) {

            Map<String, SearchHitField> fields = searchHit.getFields();
            if (null == fields || fields.isEmpty()) {

                continue;
            }

            // 店铺ID
            String id = null;
            if (null != fields.get(ShopFieldName.ID)) {

                id = fields.get(ShopFieldName.ID).getValue().toString();
            }

            // 店铺名称
            String name = null;
            if (null != fields.get(ShopFieldName.SHOP_NAME)) {

                name = fields.get(ShopFieldName.SHOP_NAME).getValue();
            }


            Map<String, HighlightField> hightlightMap = searchHit.getHighlightFields();

            if (null != hightlightMap && !hightlightMap.isEmpty()) {
                HighlightField highlightField = hightlightMap.get(ShopFieldName.SHOP_NAME);
                if (null != highlightField) {
                    Text[] textArr = highlightField.getFragments();
                    if (null != textArr && textArr.length > 0) {
                        name = textArr[0].toString();
                    }
                }
            }

            // 店铺类型
            int shopType = 0;
            if (null != fields.get(ShopFieldName.SHOP_TYPE)
                    && null != fields.get(ShopFieldName.SHOP_TYPE).getValue()) {

                shopType =
                        Integer.parseInt(fields.get(ShopFieldName.SHOP_TYPE).getValue().toString());
            }

            // 地址
            String corpAddr = null;
            if (null != fields.get(ShopFieldName.CORP_ADDR)) {

                corpAddr = fields.get(ShopFieldName.CORP_ADDR).getValue();
            }

            // 品牌
            String manageBrand = null;
            if (null != fields.get(ShopFieldName.MANAGE_BRAND)) {

                manageBrand = fields.get(ShopFieldName.MANAGE_BRAND).getValue();
            }

            if (null != hightlightMap && !hightlightMap.isEmpty()) {
                HighlightField highlightField = hightlightMap.get(ShopFieldName.MANAGE_BRAND);
                if (null != highlightField) {
                    Text[] textArr = highlightField.getFragments();
                    if (null != textArr && textArr.length > 0) {
                        manageBrand = textArr[0].toString();
                    }
                }
            }

            // 评分
            double descScore = 0.0;
            if (null != fields.get(ShopFieldName.DESC_SCORE)
                    && null != fields.get(ShopFieldName.DESC_SCORE).getValue()) {

                descScore = Double
                        .parseDouble(fields.get(ShopFieldName.DESC_SCORE).getValue().toString());
            }

            // 评分
            double distScore = 0.0;
            if (null != fields.get(ShopFieldName.DIST_SCORE)
                    && null != fields.get(ShopFieldName.DIST_SCORE).getValue()) {

                distScore = Double
                        .parseDouble(fields.get(ShopFieldName.DIST_SCORE).getValue().toString());
            }

            // 评分
            double saleScore = 0.0;
            if (null != fields.get(ShopFieldName.SALE_SCORE)
                    && null != fields.get(ShopFieldName.SALE_SCORE).getValue()) {

                saleScore = Double
                        .parseDouble(fields.get(ShopFieldName.SALE_SCORE).getValue().toString());
            }

            // 评分
            double speedScore = 0.0;
            if (null != fields.get(ShopFieldName.SPEED_SCORE)
                    && null != fields.get(ShopFieldName.SPEED_SCORE).getValue()) {

                speedScore = Double
                        .parseDouble(fields.get(ShopFieldName.SPEED_SCORE).getValue().toString());
            }

            // 店铺渠道
            String shopSite = null;
            if (null != fields.get(ShopFieldName.SHOP_SITE)) {

                shopSite = fields.get(ShopFieldName.SHOP_SITE).getValue();
            }

            Shop shop = new Shop();
            shop.setId(id);
            shop.setCorpAddr(corpAddr);
            shop.setDescScore(descScore);
            shop.setManageBrand(manageBrand);
            shop.setDistScore(distScore);
            shop.setName(name);
            shop.setSaleScore(saleScore);
            shop.setSite(shopSite);
            shop.setSpeedScore(speedScore);
            shop.setType(shopType);

            if (shopType == 1) {
                shop.setTypeName("旗舰店");
            } else if (shopType == 2) {
                shop.setTypeName("专营店");
            } else if (shopType == 3) {
                shop.setTypeName("专卖店");
            }

            double comprehensive = (descScore + distScore + saleScore + speedScore) / 4;
            shop.setComprehensive(comprehensive);

            // 联系详细地址
            String contact = null;
            if (null != fields.get(ShopFieldName.CONTACT)) {

                contact = fields.get(ShopFieldName.CONTACT).getValue();
            }
            shop.setContact(contact);

            // 联系类别
            int contacType = 0;
            if (null != fields.get(ShopFieldName.CONTACTYPE)
                    && null != fields.get(ShopFieldName.CONTACTYPE).getValue()) {

                contacType = Integer
                        .parseInt(fields.get(ShopFieldName.CONTACTYPE).getValue().toString());
            }
            shop.setContacType(contacType);

            // 省
            String province = null;
            if (null != fields.get(ShopFieldName.PROVINCE)) {

                province = fields.get(ShopFieldName.PROVINCE).getValue();
            }
            shop.setContact(province);

            // 市
            String city = null;
            if (null != fields.get(ShopFieldName.CITY)) {

                city = fields.get(ShopFieldName.CITY).getValue();
            }
            shop.setContact(city);

            // 区
            String area = null;
            if (null != fields.get(ShopFieldName.AREA)) {

                area = fields.get(ShopFieldName.AREA).getValue();
            }
            shop.setContact(area);

            // 集合追加
            shops.add(shop);
        }

        return shops;
    }

    protected JSONObject getQueryShopJson() {

        JSONObject queryJson = JSONObject.parseObject("{\"query\":{\"query_string\":{}}}");

        setStartRows(queryJson);
        setFunction(queryJson);
        setReturnField(queryJson);
        setHighlight(queryJson);

        String shopType = getParameter("st");
        if (StringUtils.isNotBlank(shopType) && !"0".equals(shopType)) {
            JSONArray filterJson = new JSONArray();
            filterJson.add(JSONObject.parseObject(
                    "{\"term\":{\"" + ShopFieldName.SHOP_TYPE + "\":\"" + shopType + "\"}}"));

            queryJson.put("post_filter", JsonUtil.jsonPut(new JSONObject(), "and", filterJson));
        }

        // 搜索关键字
        String kw = getParameter("kw");
        if (StringUtils.isBlank(kw)) {
            kw = "";
        } else {

            // 搜索日志记录
            searchLog.info("搜索关键词：" + kw);

            // 用于页面title描述
            setAttribute("title", kw + " - 店铺搜索 - 康美中药城");

            String escapeKw = ParamUitl.escapeQueryChars(kw);
            kw = StringUtils.isBlank(escapeKw) ? "" : escapeKw;
        }

        queryJson.getJSONObject("query").getJSONObject("query_string").put("query", kw);

        return queryJson;
    }

    protected void setStartRows(JSONObject queryJson) {
        if (null == queryJson) {

            return;
        }

        String pageNum = getParameter("p");
        int pn = 1;
        if (StringUtils.isNotBlank(pageNum)) {
            pn = Integer.parseInt(pageNum);
        }

        String pageSize = getParameter("ps");
        int ps = PAGESIZE;
        if (StringUtils.isNotBlank(pageSize)) {
            ps = Integer.parseInt(pageSize);
        }

        int start = (pn - 1) * ps;
        start = start <= 0 ? 0 : start;

        queryJson.put("from", start);
        queryJson.put("size", ps);
    }

    protected void setFunction(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        // 设置查询类型为edismax
        queryJson.getJSONObject("query").getJSONObject("query_string").put("use_dis_max", false);

        // 给查询字段分配权重
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("shopName^1");
        jsonArray.add("manageBrand^0.8");
        jsonArray.add("shopTitle^0.3");
        jsonArray.add("ssk");
        jsonArray.add("introduce");
        queryJson.getJSONObject("query").getJSONObject("query_string").put("fields", jsonArray);

        // 设置搜索默认运算方式default_operator=AND
        queryJson.getJSONObject("query").getJSONObject("query_string").put("default_operator",
                "AND");
    }

    protected void setReturnField(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(ShopFieldName.ID);
        jsonArray.add(ShopFieldName.SHOP_NAME);
        jsonArray.add(ShopFieldName.SHOP_TYPE);
        jsonArray.add(ShopFieldName.CORP_ADDR);
        jsonArray.add(ShopFieldName.MANAGE_BRAND);
        jsonArray.add(ShopFieldName.DESC_SCORE);
        jsonArray.add(ShopFieldName.DIST_SCORE);
        jsonArray.add(ShopFieldName.SALE_SCORE);
        jsonArray.add(ShopFieldName.SPEED_SCORE);
        jsonArray.add(ShopFieldName.SHOP_SITE);
        jsonArray.add(ShopFieldName.CONTACT);
        jsonArray.add(ShopFieldName.CONTACTYPE);
        jsonArray.add(ShopFieldName.PROVINCE);
        jsonArray.add(ShopFieldName.CITY);
        jsonArray.add(ShopFieldName.AREA);

        queryJson.put("fields", jsonArray);
    }

    protected void setHighlight(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        // 高亮标签前部
        JSONArray preTags = new JSONArray();
        preTags.add(Configuration.getString("shop.es.high.light.simple.pre"));

        // 高亮标签尾部
        JSONArray postTags = new JSONArray();
        postTags.add(Configuration.getString("shop.es.high.light.simple.post"));

        // 高亮字段
        JSONArray fields = new JSONArray();
        fields.add(JSONObject.parseObject("{\"" + ShopFieldName.SHOP_NAME
                + "\":{\"fragment_size\": 150,\"number_of_fragments\": 3}}"));
        fields.add(JSONObject.parseObject("{\"" + ShopFieldName.MANAGE_BRAND
                + "\":{\"fragment_size\": 150,\"number_of_fragments\": 3}}"));

        JSONObject highlight = new JSONObject();
        highlight.put("pre_tags", preTags);
        highlight.put("post_tags", postTags);
        highlight.put("fields", fields);

        queryJson.put("highlight", highlight);
    }

    protected void getProducts(List<Shop> shops) {
        Map<String, ProductTask> tasks = Maps.newHashMap();
        for (Shop shop : shops) {
            try {
                ProductTask task = new ProductTask(shop);
                task.fork();
                tasks.put(shop.getId(), task);
            } catch (Exception e) {
                LOG.error("获取{}店铺的商品失败！", shop.getName(), e);
            }
        }

        for (Shop shop : shops) {
            try {
                ProductTask task = tasks.get(shop.getId());
                List<ProductItem> products = task.join();
                shop.setProducts(products);
            } catch (Exception e) {
                LOG.error("获取{}店铺的商品失败！", shop.getName(), e);
            }
        }
    }

    class ProductTask extends RecursiveTask<List<ProductItem>> {

        /**
         * 
         */
        private static final long serialVersionUID = -7489274137574364342L;
        Shop shop;

        public ProductTask(Shop shop) {
            this.shop = shop;
        }

        @Override
        protected List<ProductItem> compute() {

            JSONObject queryJson = JSONObject.parseObject("{\"query\":{}}");

            // 查询条件
            queryJson.getJSONObject("query").put("term", JSONObject
                    .parseObject("{\"" + DocFieldName.SHOPID + "\": \"" + shop.getId() + "\"}"));

            // 分页
            queryJson.put("from", 0);
            queryJson.put("size", Configuration.getInt("shop.product.pagesize", 5));

            // 返回字段
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(DocFieldName.ID);
            jsonArray.add(DocFieldName.PRODUCT_TITLE);
            jsonArray.add(DocFieldName.SUBTITLE);
            jsonArray.add(DocFieldName.PRICE);
            jsonArray.add(DocFieldName.MPRICE);
            jsonArray.add(DocFieldName.IMAGE);
            queryJson.put("fields", jsonArray);

            // 排序
            JSONArray sortArray = new JSONArray();
            sortArray.add(JSONObject.parseObject("{\"sales\": \"desc\"}"));
            queryJson.put("sort", sortArray);

            // TODO PC端过滤掉预售商品的搜索
            // TODO add by zhoulinhong on 20160828
            JSONArray filterJson = new JSONArray();
            filterJson.add(JSONObject
                    .parse("{\"bool\": {\"must_not\": {\"term\": {\"promotion\": \"2\"}}}}"));
            queryJson.put("post_filter", JsonUtil.jsonPut(new JSONObject(), "and", filterJson));

            try {

                SearchHits searchHits = IndexClientUtil.getInstance().queryIndexByConditions(
                        Channel.b2b.name(), Channel.b2b.name(), queryJson.toJSONString());

                if (null == searchHits || searchHits.getTotalHits() <= 0) {

                    return null;
                }

                // 产品集合
                List<ProductItem> products = Lists.newArrayList();
                for (SearchHit searchHit : searchHits.getHits()) {

                    Map<String, SearchHitField> fields = searchHit.getFields();

                    ProductItem productItem = new ProductItem();

                    // 产品ID
                    int id = 0;
                    if (null != fields.get(DocFieldName.ID)) {

                        id = Integer.parseInt(fields.get(DocFieldName.ID).getValue().toString());
                    }
                    productItem.setSkuId(id);

                    // 产品标题
                    String title = null;
                    if (null != fields.get(DocFieldName.PRODUCT_TITLE)) {

                        title = fields.get(DocFieldName.PRODUCT_TITLE).getValue();
                    }
                    productItem.setTitle(title);

                    // 产品副标题
                    String subTitle = null;
                    if (null != fields.get(DocFieldName.SUBTITLE)) {

                        subTitle = fields.get(DocFieldName.SUBTITLE).getValue();
                    }
                    productItem.setSubTitle(subTitle);

                    // 销售价格
                    double price = 0.0;
                    if (null != fields.get(DocFieldName.PRICE)) {

                        price = Double
                                .parseDouble(fields.get(DocFieldName.PRICE).getValue().toString());
                    }
                    productItem.setPrice(price);

                    // 市场价格
                    double mprice = 0.0;
                    if (null != fields.get(DocFieldName.MPRICE)) {

                        mprice = Double
                                .parseDouble(fields.get(DocFieldName.MPRICE).getValue().toString());
                    }
                    productItem.setMprice(mprice);

                    // 图片URL
                    if (null != fields.get(DocFieldName.IMAGE)) {
                        Collection<Object> images = fields.get(DocFieldName.IMAGE).getValues();
                        if (null != images) {
                            for (Object image : images) {
                                String path = image.toString();
                                if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH5=")) {
                                    int index = path.indexOf("=");
                                    productItem.setImage(path.substring(index + 1));
                                }
                            }
                        }
                    }

                    products.add(productItem);
                }

                return products;

            } catch (Exception e) {

                LOG.error("获取{}的商品失败！", shop.getName(), e);
            }

            return null;
        }

    }

}
