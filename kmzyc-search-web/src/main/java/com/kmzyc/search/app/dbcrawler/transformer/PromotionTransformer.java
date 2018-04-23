package com.kmzyc.search.app.dbcrawler.transformer;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.promotion.app.vobject.PriceInfo;
import com.kmzyc.promotion.optimization.vo.PresellProductVO;
import com.kmzyc.promotion.remote.service.BaseProductRemoteService;
import com.kmzyc.promotion.remote.service.PresellInfoRemoteService;
import com.kmzyc.search.app.util.SpringBeanUtil;

/**
 * 促销价格处理
 * 
 * @author KM
 *
 */
public class PromotionTransformer implements ITransformer {

    private static final Logger LOG = LoggerFactory.getLogger(PromotionTransformer.class);

    @Override
    public Map<String, Object> process(Map<String, Object> map, String crawlerName) {
        Long skuId = MapUtils.getLong(map, "id");
        if (null == skuId) {
            return map;
        }

        // 数据库原始价格
        Object original = map.remove("price");
        Double origPrice = null == original ? 0.0 : Double.parseDouble(original.toString());
        // 销售价格
        map.put("price", origPrice);
        // 价格COPY
        map.put("sprice", origPrice);
        // 默认普通商品
        map.put("promotion", 0);

        // 获取预售价格，判断商品是否参加预售(预售优先于促销)
        double pm_price = getPresellPrice(skuId);
        if (pm_price > 0) {
            map.put("price", pm_price);
            map.put("promotion", 2);
        } else {
            // 获取促销价格，判断商品是否参加促销
            Double promPrice = getPromotionPrice(skuId, crawlerName);
            if (promPrice > 0) {
                map.put("price", promPrice);
                map.put("promotion", 1);
            }
        }
        return map;
    }

    /**
     * 从REDIS中查询商品是否参与促销
     * 
     * @param skuid
     * @return
     */
    private Double getPromotionPrice(long skuid, String scope) {
        double price = -1;
        BaseProductRemoteService baseProductRemoteService =
                SpringBeanUtil.getBean("baseProductRemoteService", BaseProductRemoteService.class);

        try {
            PriceInfo priceInfo = baseProductRemoteService.getCachePriceBySkuId(skuid);
            if (null != priceInfo && priceInfo.getFinalPrice() != null) {
                price = priceInfo.getFinalPrice().doubleValue();
            }
        } catch (Exception e) {
            LOG.error(
                    "调用产品远程接口baseProductRemoteService.getCachePriceBySkuId失败！channel:{} skuid:{} areaid:{}",
                    new Object[] {scope, skuid, null});
            LOG.error("", e);
        }
        return price;
    }

    /**
     * 获取预售价格
     * 
     * @author zhoulinhong
     * @param skuid
     * @return
     */
    private Double getPresellPrice(long skuid) {
        double price = -1;

        // 预售商品对象
        PresellProductVO presellProduct = null;
        PresellInfoRemoteService presellInfoRemoteService =
                SpringBeanUtil.getBean("presellInfoRemoteService", PresellInfoRemoteService.class);
        try {
            presellProduct = presellInfoRemoteService.getPresellInfo(skuid);
            if (null != presellProduct && null != presellProduct.getPresellPrice()) {

                price = presellProduct.getPresellPrice().doubleValue();
            }
        } catch (Exception e) {

            LOG.error("获取预售价格失败", e);
        }
        return price;
    }
}
