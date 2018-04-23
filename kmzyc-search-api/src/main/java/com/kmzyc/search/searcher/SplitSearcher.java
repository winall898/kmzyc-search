package com.kmzyc.search.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.config.Configuration;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.facade.util.IndexClientUtil;
import com.kmzyc.search.facade.util.JsonUtil;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.facade.util.TagsUtil;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.vo.ProductItem;

public class SplitSearcher {

    private final Channel channel;
    private final String keyword;
    private final Map<String, Object> termInfo;

    public SplitSearcher(Channel channel, Map<String, Object> termInfo) {
        this.channel = channel;
        this.keyword = ParamUitl.escapeQueryChars(termInfo.get("text").toString());
        this.termInfo = termInfo;
    }

    public Map<String, Object> search() {
        JSONObject queryJson = JSONObject
                .parseObject("{\"query\":{\"query_string\":{\"default_operator\": \"AND\"}}}");
        // 获取查询字段权重配置
        String qf = Configuration.getString(channel.name() + ".es.query.fields");
        String[] fields = null;
        if (StringUtils.isNotBlank(qf)) {
            fields = qf.split(" ");
        } else {
            fields = new String[] {"text"};
        }
        queryJson.getJSONObject("query").getJSONObject("query_string").put("fields", fields);
        queryJson.getJSONObject("query").getJSONObject("query_string").put("query", keyword);
        queryJson.getJSONObject("query").getJSONObject("query_string").put("use_dis_max", false);
        queryJson.put("highlight", getHighlight());
        SearchResponse searchResponse = IndexClientUtil.getInstance()
                .getSearchResponse(channel.name(), channel.name(), queryJson.toJSONString());
        long count = searchResponse.getHits().totalHits();
        if (count < 1) {
            return null;
        }
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

            // 产品主题
            item.setSubTitle(hitJson.getString(DocFieldName.SUBTITLE));

            // 商品类型
            item.setProdType(hitJson.getIntValue(DocFieldName.PRODUCT_TYPE));

            // 店铺id
            item.setShopId(hitJson.getString(DocFieldName.SHOPID));

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

            // 获取商品标签
            List<String> productTags = TagsUtil.getProductTags(item.getSkuId());
            item.setTags(productTags);

            // 获取商品库存量
            item.setStock(hitJson.getLongValue(DocFieldName.STOCK));

            prodList.add(item);
        }
        termInfo.put("productList", prodList);
        return termInfo;
    }


    protected JSONObject getHighlight() {
        JSONObject highJson = new JSONObject();
        highJson.put("pre_tags", new String[] {"<font color=\'red\'>"});
        highJson.put("post_tags", new String[] {"</font>"});
        highJson.put("fields", JsonUtil.jsonPut(new JSONObject(), "prodTitle", new JSONObject()));
        return highJson;
    }

}
