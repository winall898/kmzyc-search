package com.kmzyc.search.facade.response.transverter;

import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.vo.ProductItem;

public class PriceSelector {

    public void getPrice(ProductItem productItem, JSONObject hitJson, Channel channel) {

        if (null == productItem || null == hitJson || null == channel) {

            return;
        }

        // 销售价格
        double price = hitJson.getDoubleValue(DocFieldName.PRICE);
        productItem.setPrice(price);

        // 市场价格
        double mprice = hitJson.getDoubleValue(DocFieldName.MPRICE);
        productItem.setMprice(mprice);
    }

}
