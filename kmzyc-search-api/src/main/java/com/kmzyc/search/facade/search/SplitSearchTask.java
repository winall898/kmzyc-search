package com.kmzyc.search.facade.search;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.config.Configuration;
import com.kmzyc.search.facade.response.transverter.PriceSelector;
import com.kmzyc.search.facade.util.IndexClientUtil;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.facade.util.TagsUtil;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.vo.ProductItem;

/**
 * 分词查询
 * 
 * @author zhoulinhong
 * @since 20160614
 */
public class SplitSearchTask extends RecursiveTask<Map<String, Object>> {

    private static final long serialVersionUID = 1968163905924511405L;

    private static final Logger LOG = LoggerFactory.getLogger(SplitSearchTask.class);

    private Channel channel;
    private String keyword;
    private Map<String, Object> termInfo;
    private PriceSelector priceSelector;

    public SplitSearchTask(Channel channel, Map<String, Object> termInfo) {
        this.channel = channel;
        this.keyword = ParamUitl.escapeQueryChars(termInfo.get("text").toString());
        this.termInfo = termInfo;
        this.priceSelector = new PriceSelector();
    }

    @Override
    protected Map<String, Object> compute() {
        try {

            JSONObject queryJson = JSONObject.parseObject(
                    "{\"query\":{\"bool\":{\"must\":{\"query_string\":{}}}},\"post_filter\":{},\"aggs\":{}}");

            // 设置分页大小
            queryJson.put("size", 5);
            // 设置搜索关键字
            queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                    .getJSONObject("query_string").put("query", keyword);
            // 设置高亮
            setHighlight(queryJson);
            // 设置搜索条件
            setFunction(queryJson);

            SearchResponse searchResponse = IndexClientUtil.getInstance()
                    .getSearchResponse(channel.name(), channel.name(), queryJson.toJSONString());

            if (null == searchResponse || null == searchResponse.getHits()
                    || searchResponse.getHits().getTotalHits() <= 0) {

                return null;
            }

            // 命中的结果
            SearchHits searchHits = searchResponse.getHits();
            // 结果数量
            termInfo.put("count", searchResponse.getHits().getTotalHits());
            // 产品列表
            List<ProductItem> prodList = Lists.newArrayList();
            for (SearchHit searchHit : searchHits) {
                // 产品对象
                ProductItem productItem = new ProductItem();
                // 命中的对象转为json对象
                JSONObject hitJson = JSONObject.parseObject(searchHit.sourceAsString());
                // 高亮字段
                Map<String, HighlightField> highFields = searchHit.getHighlightFields();

                try {
                    // 产品ID
                    int id = hitJson.getIntValue(DocFieldName.ID);
                    productItem.setSkuId(id);

                    // 产品标题
                    String title = hitJson.getString(DocFieldName.PRODUCT_TITLE);
                    if (null != highFields.get(DocFieldName.PRODUCT_TITLE)) {
                        Text[] textArr = highFields.get(DocFieldName.PRODUCT_TITLE).getFragments();
                        if (null != textArr && textArr.length > 0) {
                            title = textArr[0].toString();
                        }
                    }
                    productItem.setTitle(title);

                    // 产品名称
                    String name = hitJson.getString(DocFieldName.PROCUCT_NAME);
                    productItem.setName(name);

                    // 产品副标题
                    String subTitle = hitJson.getString(DocFieldName.SUBTITLE);
                    productItem.setSubTitle(subTitle);

                    // 销售价格
                    priceSelector.getPrice(productItem, hitJson, channel);

                    // 商品类型
                    int prodType = hitJson.getIntValue(DocFieldName.PRODUCT_TYPE);
                    productItem.setProdType(prodType);

                    // SKU CODE
                    String skuCode = hitJson.getString(DocFieldName.SKUCODE);
                    productItem.setSkuCode(skuCode);

                    // 图片URL
                    Collection<Object> images = hitJson.getJSONArray(DocFieldName.IMAGE);
                    if (null != images) {
                        for (Object image : images) {
                            String path = image.toString();
                            if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH4=")) {
                                int index = path.indexOf('=');
                                productItem.setImage(path.substring(index + 1));
                            }
                        }
                    }

                    // 获取商品标签
                    List<String> productTags = TagsUtil.getProductTags(id);
                    productItem.setTags(productTags);

                    // 追加
                    prodList.add(productItem);
                } catch (Exception e) {

                    LOG.error("产品组装错误！", e);
                }
            }

            termInfo.put("productList", prodList);
            return termInfo;
        } catch (Exception e) {

            LOG.error("搜索:{} 失败!", keyword, e);
        }

        return null;
    }

    protected void setFunction(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        // 设置查询类型为edismax
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("use_dis_max", false);

        // 给查询字段分配权重
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("prodTitle^1");
        jsonArray.add("subtitle^0.5");
        jsonArray.add("prodName^0.2");
        jsonArray.add("brandName^0.2");
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("fields", jsonArray);

        // 促销打分权重
        queryJson.getJSONObject("query").getJSONObject("bool").put("should",
                JSONArray.parse("[{\"range\":{\"promotion\":{\"boost\":5,\"gt\":0}}}]"));

        // 设置搜索默认运算方式AND
        queryJson.getJSONObject("query").getJSONObject("bool").getJSONObject("must")
                .getJSONObject("query_string").put("default_operator", "AND");
    }

    protected void setHighlight(JSONObject queryJson) {

        if (null == queryJson) {

            return;
        }

        // 高亮标签前部
        JSONArray preTags = new JSONArray();
        preTags.add(Configuration.getString(channel.name() + ".es.high.light.simple.pre"));

        // 高亮标签尾部
        JSONArray postTags = new JSONArray();
        postTags.add(Configuration.getString(channel.name() + ".es.high.light.simple.post"));

        // 高亮字段
        JSONArray fields = new JSONArray();
        fields.add(JSONObject.parseObject("{\"" + DocFieldName.PRODUCT_TITLE
                + "\":{\"fragment_size\": 150,\"number_of_fragments\": 3}}"));

        JSONObject highlight = new JSONObject();
        highlight.put("pre_tags", preTags);
        highlight.put("post_tags", postTags);
        highlight.put("fields", fields);

        queryJson.put("highlight", highlight);
    }

}
