package com.kmzyc.search.facade.response.transverter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kmzyc.commons.enums.ProductUnitEnums;
import com.kmzyc.search.config.Configuration;
import com.kmzyc.search.facade.constants.APPResultKeys;
import com.kmzyc.search.facade.response.IResponseTransverter;
import com.kmzyc.search.facade.util.TagsUtil;
import com.kmzyc.search.facade.vo.Category;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.vo.Facter;
import com.kmzyc.search.vo.Facter.Field;

/**
 * app搜索结果处理类
 * 
 * @author KM
 *
 */
public class AppSearchResultTransverter implements IResponseTransverter {

    private static Logger logger = Logger.getLogger(AppSearchResultTransverter.class);

    private final Map<String, String> cateNameMap;
    private static final String FILED_NAME_FOR_SKU_ATTR = "sav_ss";

    public AppSearchResultTransverter(Map<String, String> cateNameMap) {
        this.cateNameMap = cateNameMap;
    }

    @Override
    public Map<String, Object> convert(SearchResponse searchResponse) {
        Map<String, Object> result = Maps.newHashMap();
        if (searchResponse == null) {
            return result;
        }
        long count = searchResponse.getHits().totalHits();
        if (count < 1) {
            return result;
        }
        result.put(APPResultKeys.COUNT, count);
        List<Map<String, Object>> prodList = Lists.newArrayList();
        for (SearchHit hit : searchResponse.getHits()) {
            JSONObject hitJson = JSONObject.parseObject(hit.sourceAsString());
            Map<String, Object> item = Maps.newHashMap();
            // 产品id
            item.put(APPResultKeys.ID, hitJson.getIntValue(DocFieldName.ID));

            // 产品名称
            String name = hitJson.getString(DocFieldName.PRODUCT_TITLE);

            // 产品的name值拼接上sku规格 20160314 需求423
            JSONArray skuAttrValues = hitJson.getJSONArray(FILED_NAME_FOR_SKU_ATTR);
            if (skuAttrValues != null && !skuAttrValues.isEmpty()) {
                StringBuilder valueStr = new StringBuilder();
                for (Object value : skuAttrValues) {
                    valueStr.append(" " + value.toString() + " ");
                }
                name = name + valueStr.toString();
            }
            item.put(APPResultKeys.NAME, name);

            item.put(APPResultKeys.MPRICE, hitJson.getDoubleValue(DocFieldName.MPRICE));

            item.put(APPResultKeys.PRICE, hitJson.getDoubleValue(DocFieldName.PRICE));

            JSONArray images = hitJson.getJSONArray(DocFieldName.IMAGE);
            if (null != images && !images.isEmpty()) {
                String host = Configuration.getString("picPath_B2B");
                for (Object image : images) {
                    if (null == image) {
                        continue;
                    }
                    String path = image.toString();
                    if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH6=")) {
                        int index = path.indexOf('=');
                        item.put(APPResultKeys.IMAGE100, host + path.substring(index + 1));
                    } else if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH5=")) {
                        int index = path.indexOf('=');
                        item.put(APPResultKeys.IMAGE170, host + path.substring(index + 1));
                    } else if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH4=")) {
                        int index = path.indexOf('=');
                        item.put(APPResultKeys.IMAGE240, host + path.substring(index + 1));
                    }
                }
            }
            String host = Configuration.getString("CSS_JS_PATH_B2B");
            Object temp = item.get(APPResultKeys.IMAGE100);
            if (null == temp) {
                item.put(APPResultKeys.IMAGE100, host + Configuration.getString("def.image100"));
            }
            temp = item.get(APPResultKeys.IMAGE170);
            if (null == temp) {
                item.put(APPResultKeys.IMAGE170, host + Configuration.getString("def.image170"));
            }
            // 获取商品标签
            List<String> productTags =
                    TagsUtil.getProductTags(hitJson.getIntValue(DocFieldName.ID));
            item.put("tags", productTags);

            // 起批量
            item.put(APPResultKeys.BOTTOM_AMOUNT, hitJson.getLongValue(DocFieldName.BOTTOM_AMOUNT));

            // 计量单位
            item.put(APPResultKeys.UNIT,
                    ProductUnitEnums.getNameByCode(hitJson.getString(DocFieldName.UNIT)));

            // 药材市场ID
            item.put(APPResultKeys.MARKET_ID, hitJson.getIntValue(DocFieldName.MARKET_ID));

            // 药材市场名称
            item.put(APPResultKeys.MARKET_NAME, hitJson.getString(DocFieldName.MARKET_NAME));

            prodList.add(item);

        }
        Aggregations aggs = searchResponse.getAggregations();
        if (!prodList.isEmpty() && null != aggs) {
            List<Category> cateList = getCategoryList(aggs);
            result.put("cateList", cateList);
            List<Facter> fs = getFacetFields(aggs);
            result.put("facterList", fs);
        }
        result.put(APPResultKeys.PRODUCTS, prodList);
        return result;
    }

    /**
     * 获取搜索结果中2，3运营类目
     * 
     * @param
     * @return
     */
    private List<Category> getCategoryList(Aggregations aggs) {
        List<Category> cateList = Lists.newArrayList();
        // 二级运营类目编码
        Set<String> secondCodeSet = getFacetValue(aggs, DocFieldName.SECOND_O_CODE);
        // 3级运营类目编码
        Set<String> thirdCodeSet = getFacetValue(aggs, DocFieldName.THIRD_O_CODE);

        if (secondCodeSet.isEmpty() || thirdCodeSet.isEmpty()) {
            return cateList;
        }
        // 运营类目编码与名称映射关系
        if (cateNameMap.isEmpty()) {
            logger.warn("无法生成搜索结果筛选类目。获取缓存内容为空 ");
            return cateList;
        }
        for (String second : secondCodeSet) {
            List<Category> children = Lists.newArrayList();
            for (String third : thirdCodeSet) {
                if (third.startsWith(second + "_")) {
                    Category child = new Category();
                    child.setCode(third);
                    String name = cateNameMap.get(third);
                    child.setName(name);
                    if (StringUtils.isNotBlank(name)) {
                        children.add(child);
                    } else {
                        logger.warn("third: " + third + "  second: " + second + " 名称为空");
                    }
                }
            }
            if (children.isEmpty()) {

                continue;
            }
            Category parent = new Category();
            parent.setCode(second);
            String name = cateNameMap.get(second);
            parent.setName(name);
            if (StringUtils.isNotBlank(name)) {
                parent.setChildren(children);
                cateList.add(parent);
            } else {
                logger.warn("second: " + second + " 名称为空");
            }
        }
        return cateList;
    }

    private Set<String> getFacetValue(Aggregations aggs, String fieldName) {
        Set<String> result = Sets.newHashSet();
        Terms terms = aggs.get(fieldName);
        if (terms != null && terms.getBuckets() != null) {
            for (Bucket bucket : terms.getBuckets()) {
                if (bucket.getDocCount() > 0) {
                    result.add(bucket.getKeyAsString());
                }
            }
        }
        return result;
    }

    /**
     * 获取搜索过滤条件列表
     * 
     * @param aggs
     * @return
     */
    public List<Facter> getFacetFields(Aggregations aggs) {
        List<Facter> result = Lists.newArrayList();
        if (aggs == null) {
            return result;
        }
        for (Aggregation aggr : aggs) {
            String fieldName = aggr.getName();
            Terms terms = (Terms) aggr;
            if (terms.getBuckets() == null || terms.getBuckets().isEmpty()) {
                continue;
            }
            if (DocFieldName.BRAND_NAME.equals(fieldName)) {
                Facter facter = new Facter();
                facter.setName("品牌");
                facter.setOrder(-100);
                facter.setCode(fieldName);
                List<Field> fields = Lists.newArrayList();
                for (Bucket bucket : terms.getBuckets()) {
                    Field f = facter.new Field();
                    f.setName(bucket.getKeyAsString());
                    f.setCode(DocFieldName.BRAND_NAME);
                    fields.add(f);
                }
                facter.setFields(fields);
                result.add(facter);
            }
        }
        return result;


    }



}
