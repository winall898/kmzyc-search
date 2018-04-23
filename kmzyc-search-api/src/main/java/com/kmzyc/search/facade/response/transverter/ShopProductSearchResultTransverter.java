package com.kmzyc.search.facade.response.transverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.highlight.HighlightField;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.kmzyc.commons.enums.ProductUnitEnums;
import com.kmzyc.search.facade.config.Configuration;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.facade.response.IResponseTransverter;
import com.kmzyc.search.facade.util.TagsUtil;
import com.kmzyc.search.facade.vo.Category;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.vo.Facter;
import com.kmzyc.search.vo.Facter.Field;
import com.kmzyc.search.vo.ProductItem;

public class ShopProductSearchResultTransverter extends PriceSelector
        implements IResponseTransverter {

    private static Logger logger = Logger.getLogger(ShopProductSearchResultTransverter.class);

    private final Map<String, String> cateNameMap;

    public ShopProductSearchResultTransverter(Map<String, String> cateNameMap) {

        this.cateNameMap = cateNameMap;
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
                    if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH5=")) {
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

            item.setPnum(hitJson.getIntValue(DocFieldName.PNUM));
            item.setSales(hitJson.getIntValue(DocFieldName.SALES));

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
        if (!prodList.isEmpty()) {
            Aggregations aggs = searchResponse.getAggregations();
            result.put("cateList", getCategoryList(aggs));
            result.put("facterList", getFacetFields(aggs, count));
        }
        return result;
    }

    /**
     * 获取搜索结果中2，3运营类目
     * 
     * @param qrs
     * @return
     */
    private List<Category> getCategoryList(Aggregations aggs) {
        List<Category> cateList = new ArrayList<Category>();
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
            List<Category> children = new ArrayList<Category>();
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
            if (children.isEmpty())
                continue;
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
        Set<String> result = new HashSet<String>();
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
     * @param count
     * @return
     */
    public List<Facter> getFacetFields(Aggregations aggs, long count) {
        List<Facter> result = new ArrayList<Facter>();
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
                result.add(getBrandNameFacter(terms, fieldName));

                // 获取搜索结果属性的facet结果，并排序
            } else if (DocFieldName.ATTRVAL_CP.equals(fieldName)) {
                setAttrvalFacter(terms, fieldName, result);
            }
        }
        // 排序
        sortFacterList(result);
        for (int i = 0; i < result.size(); i++) {
            Facter f = result.get(i);
            List<Field> fieldList = f.getFields();
            sortFieldList(fieldList);
            f.setFields(fieldList);
            result.set(i, f);
        }
        // 加入价格过滤
        int size = Configuration.getInt("b2b.price.view.num", 8);
        if (count > size) {
            String priceStr = Configuration.getString("b2b.price.range");
            if (StringUtils.isNotBlank(priceStr)) {
                String[] priceArray = priceStr.split(",");
                List<Field> fieldList = new ArrayList<Facter.Field>(priceArray.length);
                Facter facter = new Facter();
                facter.setName("价格");
                for (int i = 0; i < priceArray.length; i++) {
                    String name = priceArray[i];
                    Field field = facter.new Field();
                    field.setCode("price");
                    field.setName(name);
                    fieldList.add(field);
                }
                facter.setFields(fieldList);
                result.add(facter);
            }
        }
        return result;
    }

    /**
     * 获取搜索结果品牌列表
     * 
     * @param terms
     * @param fieldName
     * @return
     */
    private Facter getBrandNameFacter(Terms terms, String fieldName) {
        Facter facter = new Facter();
        facter.setName("品牌");
        facter.setOrder(-100);
        facter.setCode(fieldName);
        List<Field> fields = new ArrayList<Facter.Field>();
        for (Bucket bucket : terms.getBuckets()) {
            Field f = facter.new Field();
            f.setName(bucket.getKeyAsString());
            f.setCode(DocFieldName.BRAND_NAME);
            fields.add(f);
        }
        facter.setFields(fields);
        return facter;
    }

    /**
     * 获取搜索结果属性的facet结果，并排序
     * 
     * @param terms
     * @param fieldName
     * @param result
     */
    private void setAttrvalFacter(Terms terms, String fieldName, List<Facter> result) {
        Map<String, List<String>> attrList = new HashMap<String, List<String>>();
        for (Bucket bucket : terms.getBuckets()) {
            String name = bucket.getKeyAsString();
            if (StringUtils.isNotBlank(name) && name.contains("=")) {
                String[] tempArr = name.split("=");
                List<String> values = attrList.get(tempArr[0]);
                if (values == null) {
                    values = new ArrayList<String>();
                }
                values.add(tempArr[1]);
                attrList.put(tempArr[0], values);
            }
        }
        if (attrList.isEmpty()) {
            return;
        }
        Map<String, Integer> parent = new HashMap<String, Integer>();
        Map<String, Map<String, Integer>> children = new HashMap<String, Map<String, Integer>>();
        Iterator<Entry<String, List<String>>> it = attrList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List<String>> entry = it.next();
            Facter facter = new Facter();
            facter.setName(entry.getKey());
            Integer sortNo = parent.get(entry.getKey());
            if (null == sortNo)
                sortNo = entry.getValue().size();
            facter.setOrder(sortNo);
            facter.setCode(DocFieldName.ATTRNAME);
            List<String> values = entry.getValue();
            if (values.size() > 1) {
                Map<String, Integer> childSort = children.get(entry.getKey());
                if (null == childSort)
                    childSort = new HashMap<String, Integer>(0);
                List<Field> fields = new ArrayList<Facter.Field>();
                for (int i = 0; i < values.size(); i++) {
                    Field f = facter.new Field();
                    f.setName(values.get(i));
                    f.setCode(DocFieldName.ATTRVAL);
                    Integer sn = childSort.get(values.get(i));
                    if (null == sn)
                        sn = i;
                    f.setOrder(sn);
                    fields.add(f);
                }
                facter.setFields(fields);
                result.add(facter);
            }
        }
    }


    private String getAllSKUattr(JSONObject hitJson) {
        JSONArray attrs = hitJson.getJSONArray("sav_ss");
        if (attrs != null && !attrs.isEmpty()) {
            return Joiner.on("").join(attrs);
        }
        return "";

    }

    private void sortFacterList(List<Facter> list) {
        Collections.sort(list, new Comparator<Facter>() {
            @Override
            public int compare(Facter f1, Facter f2) {
                return f1.getOrder() - f2.getOrder();
            }
        });
    }

    private void sortFieldList(List<Field> list) {
        Collections.sort(list, new Comparator<Field>() {
            @Override
            public int compare(Field f1, Field f2) {
                return f1.getOrder() - f2.getOrder();
            }
        });
    }



}
