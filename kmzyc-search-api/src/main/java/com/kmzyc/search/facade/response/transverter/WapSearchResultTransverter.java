package com.kmzyc.search.facade.response.transverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.facade.response.IResponseTransverter;
import com.kmzyc.search.facade.vo.Category;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;
import com.kmzyc.search.vo.Facter;
import com.kmzyc.search.vo.Facter.Field;
import com.kmzyc.search.vo.ProductItem;

/**
 * wap搜索结果处理类
 * 
 * @author zhoulinhong
 * @since 20160614
 * 
 */
public class WapSearchResultTransverter implements IResponseTransverter {

    private static Logger LOG = LoggerFactory.getLogger(WapSearchResultTransverter.class);

    private final Map<String, String> cateNameMap;
    private final HttpServletRequest request;

    public WapSearchResultTransverter(Map<String, String> cateNameMap, HttpServletRequest request) {
        this.cateNameMap = cateNameMap;
        this.request = request;
    }

    @Override
    public Map<String, Object> convert(SearchResponse searchResponse) {

        if (null == searchResponse) {

            return null;
        }

        SearchHits searchHits = searchResponse.getHits();

        if (null == searchHits || searchHits.getTotalHits() <= 0) {

            return null;
        }

        Map<String, Object> resultValue = new HashMap<String, Object>();
        resultValue.put("count", searchHits.getTotalHits());

        // 产品信息列表
        List<ProductItem> prodList = Lists.newArrayList();
        for (SearchHit searchHit : searchHits.getHits()) {
            try {
                // 返回字段
                Map<String, SearchHitField> fields = searchHit.getFields();
                if (null == fields) {

                    continue;
                }

                // 产品对象
                ProductItem productItem = new ProductItem();

                // 产品ID
                int id = 0;
                if (null != fields.get(DocFieldName.ID)) {
                    id = Integer.parseInt(fields.get(DocFieldName.ID).getValue().toString());
                }
                productItem.setSkuId(id);

                // 产品标题
                String title = null;
                if (null != fields.get(DocFieldName.PRODUCT_TITLE)) {

                    title = fields.get(DocFieldName.PRODUCT_TITLE).getValue();
                }
                productItem.setTitle(title);

                // 产品名称
                String name = null;
                if (null != fields.get(DocFieldName.PROCUCT_NAME)) {

                    name = fields.get(DocFieldName.PROCUCT_NAME).getValue();
                }
                productItem.setName(name);

                // 产品副标题
                String subTitle = null;
                if (null != fields.get(DocFieldName.SUBTITLE)) {

                    subTitle = fields.get(DocFieldName.SUBTITLE).getValue();
                }
                productItem.setSubTitle(subTitle);

                // 销售价格
                double price = 0;
                if (null != fields.get(DocFieldName.PRICE)) {
                    price = Double
                            .parseDouble(fields.get(DocFieldName.PRICE).getValue().toString());
                }
                productItem.setPrice(price);

                // 市场价格
                double mprice = 0;
                if (null != fields.get(DocFieldName.MPRICE)) {
                    mprice = Double
                            .parseDouble(fields.get(DocFieldName.MPRICE).getValue().toString());
                }
                productItem.setMprice(mprice);

                // SKU CODE
                String skuCode = null;
                if (null != fields.get(DocFieldName.SKUCODE)) {

                    skuCode = fields.get(DocFieldName.SKUCODE).getValue();
                }
                productItem.setSkuCode(skuCode);

                // 图片URL
                if (null != fields.get(DocFieldName.IMAGE)) {
                    Collection<Object> images = fields.get(DocFieldName.IMAGE).getValues();
                    if (null != images) {
                        for (Object image : images) {
                            String path = image.toString();
                            if (StringUtils.isNotBlank(path) && path.startsWith("IMG_PATH3=")) {
                                int index = path.indexOf("=");
                                productItem.setImage(path.substring(index + 1));
                            }
                        }
                    }
                }

                // 店铺ID
                String shopId = null;
                if (null != fields.get(DocFieldName.SHOPID)) {

                    shopId = fields.get(DocFieldName.SHOPID).getValue();
                }
                productItem.setShopId(shopId);

                // 商品类型
                int prodType = 0;
                if (null != fields.get(DocFieldName.PRODUCT_TYPE)) {
                    prodType = Integer
                            .parseInt(fields.get(DocFieldName.PRODUCT_TYPE).getValue().toString());
                }
                productItem.setProdType(prodType);

                // 获取供应商类型
                int supType = 0;
                if (null != fields.get(DocFieldName.SUPTYPE)) {
                    supType = Integer
                            .parseInt(fields.get(DocFieldName.SUPTYPE).getValue().toString());
                    productItem.setSupType(String.valueOf(supType));
                }

                if (SupplierType.T2.getCode() == supType) {
                    if (null != fields.get(DocFieldName.SHOPNAME)) {
                        String shopName = fields.get(DocFieldName.SHOPNAME).getValue();
                        productItem.setShopName(shopName);
                    }
                } else {
                    if (supType != 0) {
                        String shopName = SupplierType.$(supType).getTitle();
                        productItem.setShopName(shopName);
                    }
                }

                // 获取SKU属性
                String attrs = getAllSKUattr(fields);
                productItem.setAttrVals(attrs);

                // 促销类型（0普通 1促销 2预售）
                int promotion = 0;
                if (null != fields.get(DocFieldName.PROMOTION)) {
                    promotion = Integer
                            .parseInt(fields.get(DocFieldName.PROMOTION).getValue().toString());
                }
                productItem.setPromotion(promotion);

                prodList.add(productItem);
            } catch (Exception e) {

                LOG.error("获取商品信息异常！", e);
            }
        }

        resultValue.put("productList", prodList);
        if (!prodList.isEmpty())

        {
            List<Category> cateList = getCategoryList(searchResponse.getAggregations());
            resultValue.put("cateList", cateList);
            List<Facter> facterList = getFacetFields(searchResponse.getAggregations(),
                    MapUtils.getLongValue(resultValue, "count"));
            resultValue.put("facterList", facterList);
        }

        return resultValue;
    }

    /**
     * 获取搜索结果中2，3运营类目
     * 
     * @param qrs
     * @return
     */
    public List<Category> getCategoryList(Aggregations aggregations) {
        if (null == aggregations) {

            return null;
        }

        // 分类统计
        List<Category> cateList = new ArrayList<Category>();
        // 二级运营类目编码
        Set<String> secondCodeSet = getFacetValue(aggregations, DocFieldName.SECOND_O_CODE);
        // 3级运营类目编码
        Set<String> thirdCodeSet = getFacetValue(aggregations, DocFieldName.THIRD_O_CODE);

        if (secondCodeSet.isEmpty() || thirdCodeSet.isEmpty()) {

            return cateList;
        }

        // 运营类目编码与名称映射关系
        if (null == cateNameMap || cateNameMap.isEmpty()) {

            LOG.warn("无法生成搜索结果筛选类目。获取缓存内容为空 ");
            return cateList;
        }

        // 获取选中的类别
        Set<String> temp = Sets.newHashSet();
        String cateText = request.getParameter(HTTPParam.c3.name());
        if (StringUtils.isNotBlank(cateText)) {
            Iterator<String> cates =
                    Splitter.on(",").trimResults().omitEmptyStrings().split(cateText).iterator();
            while (cates.hasNext()) {
                temp.add(cates.next());
            }
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
                        if (temp.contains(third)) {
                            child.setSelected(true);
                        }
                        children.add(child);
                    } else {
                        LOG.warn("third: " + third + "  second: " + second + " 名称为空");
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
                LOG.warn("second: " + second + " 名称为空");
            }
        }
        return cateList;
    }

    private Set<String> getFacetValue(Aggregations aggregations, String fieldName) {
        // 二级运营类目编码
        Terms terms = aggregations.get(fieldName);
        if (null == terms || null == terms.getBuckets()) {

            return null;
        }

        // 分组结果
        Set<String> result = new HashSet<String>();
        for (Bucket bucket : terms.getBuckets()) {
            if (bucket.getDocCount() > 0) {
                result.add(bucket.getKeyAsString());
            }
        }

        return result;
    }

    /**
     * 获取搜索过滤条件列表
     * 
     * @param qrs
     * @return
     */
    public List<Facter> getFacetFields(Aggregations aggregations, long count) {

        if (null == aggregations || aggregations.asList().size() <= 0) {

            return null;
        }

        // 返回分组集合
        List<Facter> result = new ArrayList<Facter>();
        // 获取选中的品牌
        Set<String> temp = Sets.newHashSet();
        String brandText = request.getParameter(HTTPParam.brandName.name());
        if (StringUtils.isNotBlank(brandText)) {
            Iterator<String> brands =
                    Splitter.on(",").trimResults().omitEmptyStrings().split(brandText).iterator();
            while (brands.hasNext()) {
                temp.add(brands.next());
            }
        }

        // 获取选中的价格
        String priceText = request.getParameter(HTTPParam.price.name());
        // 获取选中的商家
        String supplyText = request.getParameter("supply");
        // 获取选中的类目
        String c3Text = request.getParameter("c3");

        List<Field> fieldList = new ArrayList<Facter.Field>();

        for (Aggregation aggr : aggregations) {
            Terms terms = (Terms) aggr;
            if (terms.getBuckets() == null || terms.getBuckets().isEmpty()) {

                continue;
            }

            String fieldName = terms.getName();
            // 价格、商家条件选中排序等处理
            if (DocFieldName.PRICE.equals(fieldName)) {
                Facter facter = new Facter();
                facter.setName("价格");
                Field field = facter.new Field();
                field.setCode("price");
                field.setName(priceText);
                fieldList.add(field);
                facter.setFields(fieldList);
                result.add(facter);
            }

            if (DocFieldName.SUPTYPE.equals(fieldName)) { // 获取搜索结果商家列表
                Facter facter = new Facter();
                facter.setName("商家");
                Field field = facter.new Field();
                field.setCode("supply");
                field.setName(supplyText);
                fieldList.add(field);
                facter.setFields(fieldList);
                result.add(facter);
            }
            // 获取搜索结果类目列表
            if (DocFieldName.THIRD_O_CODE.equals(fieldName)) {
                Facter facter = new Facter();
                facter.setName("类目");
                Field field = facter.new Field();
                field.setCode("c3");
                field.setName(c3Text);
                fieldList.add(field);
                facter.setFields(fieldList);
                result.add(facter);
            }
        }

        // 排序
        sortFacterList(result);
        for (int i = 0; i < result.size(); i++) {
            Facter facter = result.get(i);
            List<Field> fieldListTmp = facter.getFields();
            sortFieldList(fieldListTmp);
            facter.setFields(fieldListTmp);
            result.set(i, facter);
        }

        return result;
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

    private String getAllSKUattr(Map<String, SearchHitField> fields) {
        if (null == fields) {

            return "";
        }

        if (null == fields.get("sav_ss")) {

            return "";
        }

        Collection<Object> attrs = fields.get("sav_ss").getValues();
        if (null != attrs && !attrs.isEmpty()) {
            return Joiner.on("").join(attrs);
        }

        return "";
    }
}
