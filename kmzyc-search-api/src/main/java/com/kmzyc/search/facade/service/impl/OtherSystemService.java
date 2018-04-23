package com.kmzyc.search.facade.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.kmzyc.cms.remote.service.ViewProductInfoRemoteService;
import com.kmzyc.product.remote.service.CategoryRemoteService;
import com.kmzyc.product.remote.service.SaleRankRemoteService;
import com.kmzyc.promotion.app.vobject.PriceInfo;
import com.kmzyc.promotion.remote.service.BaseProductRemoteService;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.cache.CacheUtil;
import com.kmzyc.search.facade.service.IOtherSystemService;
import com.kmzyc.search.vo.ProductItem;
import com.pltfm.app.vobject.Category;

@Service
public class OtherSystemService implements IOtherSystemService {

    private static final Logger LOG = LoggerFactory.getLogger(OtherSystemService.class);

    @Resource
    private BaseProductRemoteService baseProductRemoteService;

    @Resource
    private SaleRankRemoteService saleRankService;

    @Resource
    private ViewProductInfoRemoteService viewProductInfoRemoteService;

    @Resource
    private CategoryRemoteService categoryRemoteService;

    private static Cache<Object, Object> mainCache =
            CacheUtil.getCache("main", 1000, 12, TimeUnit.HOURS, true);

    @Override
    public Double promotionPrice(long skuid) {

        double price = -1;

        PriceInfo priceInfo = null;
        try {
            // 获取价格
            priceInfo = baseProductRemoteService.getCachePriceBySkuId(skuid);

            if (null != priceInfo && priceInfo.getFinalPrice() != null) {

                price = priceInfo.getFinalPrice().doubleValue();
            }
        } catch (Exception e) {

            LOG.error("调用产品远程接口baseProductRemoteService.getCachePriceBySkuId失败！skuid：" + skuid, e);
        }

        return price;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductItem> getHotSearchProducts() {

        try {
            return (List<ProductItem>) mainCache.get("b2b:Hot.Search.Products",
                    new Callable<Object>() {

                        @Override
                        public Object call() throws Exception {
                            List<Map<String, Object>> products = getHotProducts();

                            if (null == products || products.isEmpty()) {
                                LOG.warn("无法获取热门搜索商品.");

                                return Lists.newArrayList();
                            }

                            List<ProductItem> hotSearchPaoduct = Lists.newArrayList();
                            int i = 0;
                            for (Map<String, Object> m : products) {
                                ProductItem item = new ProductItem();
                                int skuid = MapUtils.getIntValue(m, "productSkuId");
                                item.setSkuId(skuid);
                                item.setImage(MapUtils.getString(m, "imgPath"));
                                double price = MapUtils.getDoubleValue(m, "price");
                                double promPrice = promotionPrice(skuid);
                                if (promPrice > 0) {
                                    price = promPrice;
                                }
                                item.setPrice(price);
                                item.setName(MapUtils.getString(m, "productName"));
                                item.setCode(MapUtils.getString(m, "productSkuCode"));
                                hotSearchPaoduct.add(item);
                                i++;
                                if (i == 6) {
                                    break;
                                }
                            }

                            return hotSearchPaoduct;
                        }
                    });
        } catch (ExecutionException e) {

            LOG.error("无法获取热门搜索商品.", e);
        }

        return Lists.newArrayList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getHotProducts() {
        String key = "b2b.Hot.Products";
        try {
            return (List<Map<String, Object>>) mainCache.get(key, new Callable<Object>() {

                @Override
                public Object call() {

                    List<Map<String, Object>> result = Lists.newArrayList();
                    try {

                        result = saleRankService.findSectionsHotSell(Channel.b2b.name());
                        if (null == result) {

                            result = Lists.newArrayList();
                            LOG.warn("获取热门商品为空");
                        }
                    } catch (Exception e) {

                        LOG.error("调用产品远程接口saleRankService.findSectionsHotSell失败！", e);
                    }

                    return result;
                }
            });
        } catch (ExecutionException e) {

            LOG.error("无法获取热门搜索商品.", e);
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> getProductTags(final int productId) {

        List<String> result = null;

        try {

            result = viewProductInfoRemoteService.queryProductTags(productId);

        } catch (Exception e) {

            LOG.error(
                    "调用远程接口viewProductInfoRemoteService.queryProductTags失败！productId:" + productId,
                    e);
        }

        return result;
    }

    @Override
    public String getExecSql(long oid) {

        Category category = null;

        try {
            category = categoryRemoteService.findByCategoryId(oid);

            if (null != category) {

                return category.getExecSql();
            }
        } catch (Exception e) {

            LOG.error("调用产品远程接口categoryBusiService.findByCategoryId失败！ OID:" + oid, e);
        }

        return null;
    }
}
