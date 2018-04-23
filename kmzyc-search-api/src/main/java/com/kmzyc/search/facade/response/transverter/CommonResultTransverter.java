package com.kmzyc.search.facade.response.transverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.kmzyc.commons.enums.ProductUnitEnums;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.facade.response.IResponseTransverter;
import com.kmzyc.search.facade.util.TagsUtil;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.vo.ProductItem;

public class CommonResultTransverter implements IResponseTransverter {

    private Channel channel;

    public CommonResultTransverter(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Map<String, Object> convert(SearchResponse searchResponse) {
        if (searchResponse == null) {
            return Maps.newHashMap();
        }

        long count = searchResponse.getHits().totalHits();
        if (count < 1) {
            return Maps.newHashMap();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("count", count);
        // 产品信息
        List<ProductItem> prodList = new ArrayList<ProductItem>();
        for (SearchHit hit : searchResponse.getHits()) {
            ProductItem item = new ProductItem();
            JSONObject hitJson = JSONObject.parseObject(hit.sourceAsString());

            // 产品id
            item.setSkuId(hitJson.getIntValue(DocFieldName.ID));

            // 产品标题
            Map<String, HighlightField> highMap = hit.getHighlightFields();
            if (highMap != null && !highMap.isEmpty()) {
                item.setTitle(highMap.get(DocFieldName.PRODUCT_TITLE).getFragments()[0].string());
            } else {
                item.setTitle(hitJson.getString(DocFieldName.PRODUCT_TITLE));
            }

            // 产品名称
            item.setName(hitJson.getString(DocFieldName.PROCUCT_NAME));

            // 产品副标题
            item.setSubTitle(hitJson.getString(DocFieldName.SUBTITLE));

            // skuCode
            item.setSkuCode(hitJson.getString(DocFieldName.SKUCODE));

            // 销售价格
            item.setPrice(hitJson.getDoubleValue(DocFieldName.PRICE));
            // 市场价格
            item.setMprice(hitJson.getDoubleValue(DocFieldName.MPRICE));

            // 图片
            JSONArray imageJsonArray = hitJson.getJSONArray(DocFieldName.IMAGE);
            if (null != imageJsonArray) {
                for (int i = 0; i < imageJsonArray.size(); i++) {
                    String path = imageJsonArray.getString(i);
                    if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH4=")) {
                        item.setImage(path.substring(path.indexOf("=") + 1));
                    }
                }
            }
            // 店铺id
            item.setShopId(hitJson.getString(DocFieldName.SHOPID));
            // 商品类型
            item.setProdType(hitJson.getIntValue(DocFieldName.PRODUCT_TYPE));

            // 获取供应商类型
            Integer supType = hitJson.getIntValue(DocFieldName.SUPTYPE);
            if (supType != null) {
                item.setSupType(supType.toString());
            }

            // 设置店铺名称
            if (SupplierType.T2.getCode() == supType) {
                item.setShopName(hitJson.getString(DocFieldName.SHOPNAME));
            } else if (supType != 0) {
                item.setShopName(SupplierType.$(supType).getTitle());
            }

            // 获取商品标签
            List<String> productTags = TagsUtil.getProductTags(item.getSkuId());
            item.setTags(productTags);

            // 获取SKU属性
            String attrs = getAllSKUattr(hitJson);
            item.setAttrVals(attrs);

            // 起批量
            item.setBottomAmount(hitJson.getLongValue(DocFieldName.BOTTOM_AMOUNT));

            // 计量单位
            item.setUnit(ProductUnitEnums.getNameByCode(hitJson.getString(DocFieldName.UNIT)));

            // 药材市场ID
            item.setMarketId(hitJson.getIntValue(DocFieldName.MARKET_ID));

            // 药材市场名字
            item.setMarketName(hitJson.getString(DocFieldName.MARKET_NAME));

            prodList.add(item);
        }
        result.put("productList", prodList);

        return result;
    }


    private String getAllSKUattr(JSONObject hitJson) {
        JSONArray attrs = hitJson.getJSONArray("sav_ss");
        if (attrs != null && !attrs.isEmpty()) {
            return Joiner.on("").join(attrs);
        }
        return "";
    }
}
