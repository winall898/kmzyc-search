package com.kmzyc.search.app.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kmzyc.promotion.app.vobject.PriceInfo;
import com.kmzyc.promotion.optimization.vo.PresellProductVO;
import com.kmzyc.promotion.remote.service.BaseProductRemoteService;
import com.kmzyc.promotion.remote.service.PresellInfoRemoteService;
import com.kmzyc.search.app.service.IOtherSystemService;

@Service
public class OtherSystemService implements IOtherSystemService {

    private static final Logger LOG = LoggerFactory.getLogger(OtherSystemService.class);

    @Resource
    private BaseProductRemoteService baseProductRemoteService;

    @Resource
    private PresellInfoRemoteService presellInfoRemoteService;

    @Override
    public Double promotionPrice(long skuid) {

        double price = -1;

        PriceInfo priceInfo = null;
        try {
            priceInfo = baseProductRemoteService.getCachePriceBySkuId(skuid);

            if (null != priceInfo && null != priceInfo.getFinalPrice()) {
                price = priceInfo.getFinalPrice().doubleValue();
            }
        } catch (Exception e) {
            LOG.error(
                    "调用产品远程接口baseProductRemoteService.getCachePriceBySkuId失败！channel:{} skuid:{} areaid:{}",
                    new Object[] {skuid});
            LOG.error("获取促销价格失败", e);
        }

        return price;
    }

    @Override
    public Double getPresellPrice(long skuid) {

        // 预售价格
        double price = -1;
        // 预售商品对象
        PresellProductVO presellProduct = null;
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
