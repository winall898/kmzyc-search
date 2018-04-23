package com.kmzyc.search.app.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.app.index.AddToOprationTask;
import com.kmzyc.search.app.index.CategoryIndexTask;
import com.kmzyc.search.app.index.IndexTaskManager;
import com.kmzyc.search.app.service.IOtherSystemService;
import com.kmzyc.search.app.service.IndexService;
import com.kmzyc.search.app.service.ProductService;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.ShopFieldName;

@Service("indexService")
public class IndexServiceImpl implements IndexService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Resource(name = "productService")
    private ProductService productService;

    @Resource(name = "otherSystemService")
    private IOtherSystemService otherSystemService;

    @Override
    public void addB2bIndex(List<Long> ids) {

        if (null == ids || ids.isEmpty()) {

            LOG.error("新增产品索引失败，产品id集合为空。");
            return;
        }

        // 产品索引信息Map
        Map<String, JSONObject> docsMap = Maps.newHashMap();

        // 产品信息Map
        List<Map<String, Object>> productList = Lists.newArrayList();

        for (Long id : ids) {
            // 获取商品信息
            Map<String, Object> productInfo = null;
            try {
                productInfo = productService.getProductInfo(id);
            } catch (Exception e) {
                LOG.error("获取产品信息失败. SKUID: " + id, e);
                continue;
            }

            if (null == productInfo || productInfo.isEmpty()) {

                LOG.error("获取产品信息为空. SKUID: " + id);
                continue;
            }

            // 对象属性复制给新的map
            Map<String, Object> productMap = Maps.newHashMap(productInfo);

            // 获取商品skuId
            long skuId = MapUtils.getLongValue(productMap, DocFieldName.ID);

            // 获取预售价格，判断商品是否参加预售(预售优先于促销)
            double pm_price = otherSystemService.getPresellPrice(skuId);
            if (pm_price > 0) {
                productMap.put(DocFieldName.PRICE, pm_price);
                productMap.put(DocFieldName.PROMOTION, 2);
            } else {
                // 获取促销价格，判断商品是否参加促销活动
                pm_price = otherSystemService.promotionPrice(skuId);
                // 判断是否参加促销活动
                if (pm_price > 0) {
                    productMap.put(DocFieldName.PRICE, pm_price);
                    productMap.put(DocFieldName.PROMOTION, 1);
                }
            }

            // 产品索引文档数据
            docsMap.put(id.toString(), JSONObject.parseObject(JSONObject.toJSONString(productMap)));

            // 产品集合
            productList.add(productInfo);
        }

        // 判断集合是否为空
        if (docsMap.isEmpty() || productList.isEmpty()) {

            return;
        }

        // 创建索引
        try {

            LOG.info("创建索引开始,id:{}", Arrays.toString(ids.toArray()));

            // 创建索引
            IndexClientUtil.getInstance().batchAddIndex(Channel.b2b.name(), Channel.b2b.name(),
                    docsMap);
            // 新增数据库产品全量信息
            List<Long> skuIds = Lists.newArrayList();
            for (Map<String, Object> product : productList) {

                skuIds.add(MapUtils.getLongValue(product, DocFieldName.ID));
            }
            // 新增产品全量表数据
            productService.batchAddB2bWhole(productList);

            LOG.info("创建索引成功,id:{}", Arrays.toString(ids.toArray()));
        } catch (Exception e) {
            LOG.error("SKU IDs: {}", new Object[] {Arrays.toString(ids.toArray())});
            LOG.error("创建索引失败。", e);

        }

        // 创建运营类目
        for (Object skuId : docsMap.keySet().toArray()) {
            try {
                // 运营类目处理
                IndexTaskManager.invokeTask(new AddToOprationTask(Long.parseLong(skuId.toString()),
                        Channel.valueOf(Channel.b2b.name())));
            } catch (Exception e) {

                LOG.error("创建SKU{}运营类目索引失败。", skuId.toString(), e);
            }
        }
    }

    @Override
    public void updateB2bIndex(List<Long> ids) {

        if (null == ids || ids.isEmpty()) {

            LOG.error("修改产品索引失败，产品id集合为空。");
            return;
        }

        // 产品索引信息Map
        Map<String, JSONObject> docsMap = Maps.newHashMap();

        // 产品信息Map
        List<Map<String, Object>> productList = Lists.newArrayList();

        for (Long id : ids) {
            // 获取商品信息
            Map<String, Object> productInfo = null;
            try {

                productInfo = productService.getProductInfo(id);
            } catch (Exception e) {

                LOG.error("获取产品信息失败. SKUID: " + id, e);
                continue;
            }

            if (null == productInfo || productInfo.isEmpty()) {

                LOG.error("获取产品信息为空. SKUID: " + id);
                continue;
            }

            // 对象属性复制给新的map
            Map<String, Object> productMap = Maps.newHashMap(productInfo);

            // 获取产品skuId
            long skuId = MapUtils.getLongValue(productInfo, DocFieldName.ID);

            // 获取预售价格，判断商品是否参加预售(预售优先于促销)
            double pm_price = otherSystemService.getPresellPrice(skuId);
            if (pm_price > 0) {
                productMap.put(DocFieldName.PRICE, pm_price);
                productMap.put(DocFieldName.PROMOTION, 2);
            } else {
                // 获取促销价格，判断商品是否参加促销活动
                pm_price = otherSystemService.promotionPrice(skuId);
                // 判断是否参加促销活动
                if (pm_price > 0) {
                    productMap.put(DocFieldName.PRICE, pm_price);
                    productMap.put(DocFieldName.PROMOTION, 1);
                }
            }

            // 产品索引文档数据
            docsMap.put(id.toString(), JSONObject.parseObject(JSONObject.toJSONString(productMap)));

            // 产品集合
            productList.add(productInfo);
        }

        // 判断集合是否为空
        if (docsMap.isEmpty() || productList.isEmpty()) {

            return;
        }

        // 修改索引
        try {

            LOG.info("修改索引开始,id:{}", Arrays.toString(ids.toArray()));

            // 修改索引
            IndexClientUtil.getInstance().batchUpdateIndex(Channel.b2b.name(), Channel.b2b.name(),
                    docsMap);
            // 修改数据库产品全量信息
            productService.batchUpdateB2bWhole(productList);

            LOG.info("修改索引成功,id:{}", Arrays.toString(ids.toArray()));
        } catch (Exception e) {
            LOG.error("SKU IDs: {}", new Object[] {Arrays.toString(ids.toArray())});
            LOG.error("修改索引失败。", e);
        }
    }

    @Override
    public void deleteB2bIndex(List<Long> ids) {

        if (null == ids || ids.isEmpty()) {

            LOG.error("删除产品索引数据失败，产品id集合为空。");
            return;
        }

        try {

            LOG.info("开始删除索引:{}", Arrays.toString(ids.toArray()));

            List<String> docIds = new ArrayList<String>();
            for (Long id : ids) {
                if (null != id) {
                    docIds.add(id.toString());
                }
            }
            // 批量删除产品索引数据
            IndexClientUtil.getInstance().batchDeleteIndex(Channel.b2b.name(), Channel.b2b.name(),
                    docIds);
            // 批量删除产品全量表数据
            productService.batchDelB2bWhole(ids);

            LOG.info("删除索引成功,id:{}", Arrays.toString(ids.toArray()));
        } catch (Exception e) {
            LOG.error("删除{}索引失败。", Arrays.toString(ids.toArray()), e);
            LOG.error("产品IDS: " + Arrays.toString(ids.toArray()));
        }

    }

    @Override
    public void createOperationCategoryTree() {
        Map<String, Map<String, String>> categories = productService.getAllOperationCategoryMap();
        if (null == categories || categories.isEmpty()) {

            LOG.error("无法获取运营类目信息，创建运营类目索引失败!");
            return;
        }

        Iterator<Entry<String, Map<String, String>>> it = categories.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Entry<String, Map<String, String>> entry = it.next();
                String id = entry.getKey();
                new CategoryIndexTask(id, categories).optCategory();
            } catch (Exception e) {
                LOG.error("创建运营类目信息索引发生异常。", e);
            }
        }
    }

    @Override
    public void addShopIndex(List<Long> ids) {

        if (null == ids || ids.isEmpty()) {

            LOG.error("新增店铺索引失败，店铺id集合为空。");
            return;
        }

        // 店铺列表
        List<Map<String, Object>> shopList = Lists.newArrayList();

        // 店铺索引数据
        Map<String, JSONObject> shopIndexs = Maps.newHashMap();

        for (long id : ids) {

            // 店铺信息
            Map<String, Object> shop = productService.getShopInfo(id);
            if (null == shop || shop.isEmpty()) {

                continue;
            }

            long sid = MapUtils.getLongValue(shop, ShopFieldName.SID);

            // 店铺评分
            Map<String, Object> score = productService.getShopScore(sid);
            if (null != score && !score.isEmpty()) {

                shop.putAll(score);
            }

            // 公司信息
            Map<String, Object> corp = productService.getCorporateInfo(sid);
            if (null != corp && !corp.isEmpty()) {
                shop.putAll(corp);
            }

            // 店铺列表数据追加
            shopList.add(shop);

            // 店铺索引数据追加
            shopIndexs.put(shop.get(ShopFieldName.ID).toString(),
                    JSONObject.parseObject(JSONObject.toJSONString(shop)));
        }

        if (null != shopList && !shopList.isEmpty()) {

            // 新增店铺索引
            IndexClientUtil.getInstance().batchAddIndex(Channel.shop.name(), Channel.shop.name(),
                    shopIndexs);

            // 批量新增店铺全量表数据
            productService.batchAddShopWhole(shopList);

            LOG.info("创建店铺索引成功,id:{}", Arrays.toString(ids.toArray()));
        }

    }

    @Override
    public void delShopIndex(List<Long> ids) {

        if (null == ids || ids.isEmpty()) {

            LOG.error("删除店铺索引失败，店铺id集合为空。");
            return;
        }

        try {

            List<String> shopIds = new ArrayList<String>(ids.size());
            for (Long id : ids) {
                if (null != id) {
                    shopIds.add(id.toString());
                }
            }

            // 批量删除店铺索引数据
            IndexClientUtil.getInstance().batchDeleteIndex(Channel.shop.name(), Channel.shop.name(),
                    shopIds);

            // 批量删除店铺全量表数据
            productService.batchDelShopWhole(ids);

            LOG.info("删除店铺索引成功,id:{}", Arrays.toString(ids.toArray()));
        } catch (Exception e) {

            LOG.error("删除SHOP索引失败。", e);
            LOG.error("产品IDS: " + Arrays.toString(ids.toArray()));
        }
    }

    @Override
    public void updateShopIndex(List<Long> ids) {

        if (null == ids || ids.isEmpty()) {

            LOG.error("修改店铺索引失败，店铺id集合为空。");
            return;
        }

        // 店铺列表
        List<Map<String, Object>> shopList = Lists.newArrayList();

        // 店铺索引数据
        Map<String, JSONObject> shopIndexs = Maps.newHashMap();

        for (long id : ids) {

            // 店铺信息
            Map<String, Object> shop = productService.getShopInfo(id);
            if (null == shop || shop.isEmpty()) {

                continue;
            }

            long sid = MapUtils.getLongValue(shop, ShopFieldName.SID);

            // 店铺评分
            Map<String, Object> score = productService.getShopScore(sid);
            if (null != score && !score.isEmpty()) {

                shop.putAll(score);
            }

            // 公司信息
            Map<String, Object> corp = productService.getCorporateInfo(sid);
            if (null != corp && !corp.isEmpty()) {
                shop.putAll(corp);
            }

            // 店铺列表数据追加
            shopList.add(shop);

            // 店铺索引数据追加
            shopIndexs.put(shop.get(ShopFieldName.ID).toString(),
                    JSONObject.parseObject(JSONObject.toJSONString(shop)));
        }

        if (null != shopList && !shopList.isEmpty()) {

            // 修改店铺索引
            IndexClientUtil.getInstance().batchUpdateIndex(Channel.shop.name(), Channel.shop.name(),
                    shopIndexs);

            // 批量修改店铺全量表数据
            productService.batchUpdateShopWhole(shopList);

            LOG.info("修改店铺索引成功,id:{}", Arrays.toString(ids.toArray()));
        }
    }

    @Override
    public void updatePromotionIndex(List<Object> ids, Channel channel, boolean flag) {

        if (null == ids || ids.isEmpty() || null == channel) {

            return;
        }

        // 促销产品索引数据
        Map<String, JSONObject> productIndexs = Maps.newHashMap();

        // 指定促销商品更新
        for (Object id : ids) {

            if (null == id) {

                continue;
            }

            double pm_price = 0;
            try {

                // 获取商品促销价格
                long skuId = Long.parseLong(id.toString());
                pm_price = otherSystemService.promotionPrice(skuId);

                if (pm_price > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(DocFieldName.PRICE, pm_price);
                    jsonObject.put(DocFieldName.PROMOTION, 1);

                    productIndexs.put(id.toString(), jsonObject);
                }
            } catch (Exception e) {

                LOG.error("更新促销索引价格失败。id:" + id, e);
            }
        }

        // 参加促销活动,进行促销索引价格更新
        if (null != productIndexs && !productIndexs.isEmpty()) {
            IndexClientUtil.getInstance().batchUpdateIndex(channel.name(), channel.name(),
                    productIndexs);
        }

    }

    @Override
    public void updatePromotionIndex(Map<String, Map<String, Object>> data, Channel channel) {

        if (null == data || data.isEmpty() || null == channel) {

            return;
        }

        // 产品索引数据
        Map<String, JSONObject> productIndexs = Maps.newHashMap();

        try {

            // 遍历Map,组装产品索引更新集合
            for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {

                if (StringUtils.isBlank(entry.getKey()) || null == entry.getValue()) {

                    continue;
                }

                JSONObject jsonObject = new JSONObject();

                Map<String, Object> dataMap = entry.getValue();
                // 获取价格
                if (null != dataMap.get("price")) {

                    jsonObject.put(DocFieldName.PRICE,
                            Double.parseDouble(dataMap.get("price").toString()));
                }

                // 获取商品类别（0普通商品，1促销商品，2预售商品）
                int dataType = 0;
                if (null != dataMap.get("dataType")) {

                    dataType = Integer.parseInt(dataMap.get("dataType").toString());
                }
                jsonObject.put(DocFieldName.PROMOTION, dataType);

                productIndexs.put(entry.getKey(), jsonObject);
            }

            // 参加促销活动,进行促销索引价格更新
            if (null != productIndexs && !productIndexs.isEmpty()) {

                IndexClientUtil.getInstance().batchUpdateIndex(channel.name(), channel.name(),
                        productIndexs);
            }

        } catch (Exception e) {

            LOG.error("更新促销索引价格失败。", e);
        }
    }
}
