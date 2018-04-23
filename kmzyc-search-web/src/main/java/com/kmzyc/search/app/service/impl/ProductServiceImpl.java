package com.kmzyc.search.app.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.app.service.ProductService;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.ShopFieldName;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getOperationCategorySql(int oid) {
        StringBuilder buf = new StringBuilder();
        List<Map<String, Object>> cates = getOperationCategoryTree(oid);
        if (null != cates && !cates.isEmpty()) {
            Iterator<Map<String, Object>> it = cates.iterator();
            final String or = " OR ";
            while (it.hasNext()) {
                Map<String, Object> m = it.next();
                if (null != m && !m.isEmpty()) { // EXEC_SQL
                    String sql = MapUtils.getString(m, "EXEC_SQL");
                    if (StringUtils.isNotBlank(sql)) {
                        buf.append("(");
                        buf.append(sql);
                        buf.append(")");
                        if (it.hasNext())
                            buf.append(or);
                    }
                }
            }
            String sql = buf.toString();
            if (sql.endsWith(or))
                sql = sql.substring(0, sql.length() - or.length());
            return sql;
        }
        return buf.toString();
    }

    @Override
    public Map<String, Map<String, String>> getAllOperationCategoryMap() {
        List<Map<String, String>> categories = getCategoryByChannel();
        if (null != categories && categories.size() > 0) {
            Map<String, Map<String, String>> result = Maps.newHashMap();
            for (Map<String, String> m : categories) {
                result.put(MapUtils.getString(m, "CATEGORY_ID"), m);
            }
            return result;
        }
        return Maps.newHashMap();
    }

    @Override
    public List<Map<String, Object>> getProductInfo(List<String> ids) {
        if (null == ids || ids.isEmpty()) {
            return Lists.newArrayList();
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(ids.size());
        for (String id : ids) {
            if (null == id)
                continue;
            int sid = Integer.parseInt(id);
            Map<String, Object> productInfo = getProductInfo(sid);
            if (null != productInfo && !productInfo.isEmpty()) {
                result.add(productInfo);
            }
        }
        return result;
    }

    private void extractSKUInfo(Map<String, Object> recodes, long skuId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select ps.PRODUCT_SKU_ID, ps.PRODUCT_SKU_CODE, ps.PRODUCT_ID, t.PRICE,t.BOTTOM_AMOUNT, ps.MARK_PRICE, p.UNIT ");
        sql.append(
                " from PRODUCT_SKU ps, PRODUCTMAIN p, (select ppl.PRODUCT_SKU_ID, min(ppl.PRICE) PRICE,min(ppl.BOTTOM_AMOUNT) BOTTOM_AMOUNT from product_price_ladder ppl group by ppl.PRODUCT_SKU_ID) t");
        sql.append(
                " where ps.PRODUCT_ID=p.PRODUCT_ID and ps.PRODUCT_SKU_ID = t.PRODUCT_SKU_ID(+) and p.STATUS=3 and ps.STATUS=1 and ps.PRODUCT_SKU_ID="
                        + skuId);
        try {
            Map<String, Object> skuInfo = jdbcTemplate.queryForMap(sql.toString());
            if (!skuInfo.isEmpty()) {
                String id = MapUtils.getString(skuInfo, "PRODUCT_SKU_ID");
                recodes.put(DocFieldName.ID, id);
                recodes.put(DocFieldName.PRODUCT_ID, MapUtils.getIntValue(skuInfo, "PRODUCT_ID"));
                recodes.put(DocFieldName.SPRICE, MapUtils.getDoubleValue(skuInfo, "PRICE"));
                recodes.put(DocFieldName.PRICE, MapUtils.getDoubleValue(skuInfo, "PRICE"));
                recodes.put(DocFieldName.MPRICE, MapUtils.getDoubleValue(skuInfo, "MARK_PRICE"));
                recodes.put(DocFieldName.SKUCODE, MapUtils.getString(skuInfo, "PRODUCT_SKU_CODE"));
                recodes.put(DocFieldName.BOTTOM_AMOUNT,
                        MapUtils.getIntValue(skuInfo, "BOTTOM_AMOUNT"));
                recodes.put(DocFieldName.UNIT, MapUtils.getString(skuInfo, "UNIT"));
            }
        } catch (DataAccessException e) {
            LOG.error("获取PRODUCT_SKU 表字段失败。SQL: " + sql.toString());
            throw e;
        }
    }

    private Map<String, Object> extractSKUSales(Map<String, Object> recodes, long skuid) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        int count = jdbcTemplate.queryForObject(
                "select count(1) as c from PRODUCT_SKU_QUANTITY where PRODUCT_SKU_ID=" + skuid,
                Integer.class);
        if (count > 0) {
            String sql =
                    "select SALE_QUANTITY from PRODUCT_SKU_QUANTITY where PRODUCT_SKU_ID=" + skuid;
            try {
                // 执行查询
                result = jdbcTemplate.queryForMap(sql);
                if (!result.isEmpty()) {
                    recodes.put(DocFieldName.SALES, MapUtils.getLongValue(result, "SALE_QUANTITY"));
                }
            } catch (DataAccessException e) {

                LOG.warn("获取商品销量失败。SQL: " + sql);
            }
        }

        return recodes;
    }

    private Map<String, Object> extractSKUStock(Map<String, Object> recodes, long skuId) {
        String sql =
                "select sum(STOCK_QUALITY) as QUALITY from PRODUCT_STOCK where SKU_ATTRIBUTE_ID="
                        + skuId;

        try {
            Map<String, Object> stockInfo = jdbcTemplate.queryForMap(sql);
            if (!stockInfo.isEmpty()) {
                recodes.put(DocFieldName.STOCK, MapUtils.getLongValue(stockInfo, "QUALITY"));
            }
        } catch (DataAccessException e) {
            LOG.warn("获取SKU库存量信息失败。SQL: " + sql);
            // throw e;
        }
        return recodes;
    }

    @SuppressWarnings("unchecked")
    private void extractSKUNavAttr(Map<String, Object> recodes, long skuId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select psa.CATEGORY_ATTR_ID, psa.CATEGORY_ATTR_NAME");
        sql.append(
                ",decode(cav.category_attr_value_id,null,psa.category_attr_value_id,cav.category_attr_value_id) category_attr_value_id");
        sql.append(
                ",decode(psa.new_attr,null,cav.category_attr_value,psa.new_attr) category_attr_value, ca.IS_NAV ");
        sql.append(" from PRODUCT_SKU_ATTR psa, CATEGORY_ATTR ca, CATEGORY_ATTR_VALUE cav");
        sql.append(
                " where psa.CATEGORY_ATTR_VALUE_ID=cav.CATEGORY_ATTR_VALUE_ID(+) and ca.CATEGORY_ATTR_ID = psa.CATEGORY_ATTR_ID and ca.IS_NAV = 1 and psa.PRODUCT_SKU_ID="
                        + skuId);

        try {
            List<Map<String, Object>> skuAttrInfo = jdbcTemplate.queryForList(sql.toString());
            if (!skuAttrInfo.isEmpty()) {
                // 所有SKU 属性名称
                List<String> attrName = Lists.newArrayList();
                // 所有SKU 属性值
                List<String> attrValue = Lists.newArrayList();
                // 所有SKU 属性名称和值
                List<String> attrValueCP = Lists.newArrayList();

                for (Map<String, Object> map : skuAttrInfo) {
                    if (null != map && !map.isEmpty()) {
                        // 属性名称
                        String cn = MapUtils.getString(map, "CATEGORY_ATTR_NAME");
                        attrName.add(cn);
                        // 属性值
                        String cv = MapUtils.getString(map, "CATEGORY_ATTR_VALUE");
                        attrValue.add(cv);

                        // 属性名称和值
                        attrValueCP.add(cn + "=" + cv);
                    }
                }

                recodes.put(DocFieldName.ATTRNAME, attrName);
                recodes.put(DocFieldName.ATTRVAL, attrValue);
                recodes.put(DocFieldName.ATTRVAL_CP, attrValueCP);
            }
        } catch (DataAccessException e) {
            LOG.error("获取SKU属性失败。SQL: " + sql);
            throw e;
        }
    }

    private void extractAllSKUAttr(Map<String, Object> recodes, final long skuId) {
        final List<String> attrNames = Lists.newArrayList();
        final List<String> attrValues = Lists.newArrayList();
        String sql =
                "select psa.CATEGORY_ATTR_ID, psa.CATEGORY_ATTR_NAME, decode(cav.category_attr_value_id,null,psa.category_attr_value_id,cav.category_attr_value_id) category_attr_value_id, decode(psa.new_attr,null,cav.category_attr_value,psa.new_attr) category_attr_value from PRODUCT_SKU_ATTR psa, CATEGORY_ATTR ca, CATEGORY_ATTR_VALUE cav where psa.CATEGORY_ATTR_VALUE_ID=cav.CATEGORY_ATTR_VALUE_ID(+) and ca.CATEGORY_ATTR_ID = psa.CATEGORY_ATTR_ID and psa.PRODUCT_SKU_ID=?";

        jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, skuId);

            }
        }, new RowMapper<Object>() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("CATEGORY_ATTR_NAME");
                String value = rs.getString("CATEGORY_ATTR_VALUE");
                attrNames.add(name);
                attrValues.add(value);
                return null;
            }
        });
        recodes.put("san_ss", attrNames);
        recodes.put("sav_cns", attrValues);
        recodes.put("sav_ss", attrValues);
    }

    private void extractProductmainInfo(Map<String, Object> recodes, int productId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select p.PROCUCT_NAME, p.PRODUCT_TITLE, p.PRODUCT_SUBTITLE, p.KEYWORD, p.PRODUCTHOT, p.BRAND_ID, b.BRAND_NAME, p.CREATE_TIME, p.UP_TIME, p.APPROVAL_TYPE,");
        sql.append(
                " p.APPROVAL_NO, p.CATEGORY_ID, p.MARKET_PRICE, p.SHOP_CODE, p.PRODUCT_NO, p.DRUG_CATE_ID, p.DRUG_CATE_CODE, p.CHANNEL, p.PRODUCT_TYPE from PRODUCTMAIN p, PROD_BRAND b");
        sql.append(" where p.STATUS=3 and p.BRAND_ID=b.BRAND_ID(+) and p.PRODUCT_ID=" + productId);

        try {
            Map<String, Object> pmainInfo = jdbcTemplate.queryForMap(sql.toString());
            if (!pmainInfo.isEmpty()) {
                String name = MapUtils.getString(pmainInfo, "PROCUCT_NAME");
                name = null == name ? name : name.trim();
                recodes.put(DocFieldName.PROCUCT_NAME, name);

                String title = MapUtils.getString(pmainInfo, "PRODUCT_TITLE");
                title = null == title ? title : title.trim();
                recodes.put(DocFieldName.PRODUCT_TITLE, title);

                String subTitle = MapUtils.getString(pmainInfo, "PRODUCT_SUBTITLE");
                subTitle = null == subTitle ? subTitle : subTitle.trim();
                recodes.put(DocFieldName.SUBTITLE, subTitle);

                recodes.put(DocFieldName.KEYWORD, MapUtils.getString(pmainInfo, "KEYWORD"));
                recodes.put(DocFieldName.PRODUCTHOT,
                        MapUtils.getLongValue(pmainInfo, "PRODUCTHOT"));

                String brand = MapUtils.getString(pmainInfo, "BRAND_NAME");
                brand = null == brand ? brand : brand.trim();
                recodes.put(DocFieldName.BRAND_NAME, brand);
                // recodes.put(MPRICE, MapUtils.getDoubleValue(pmainInfo,
                // "MARKET_PRICE"));
                Date ct = (Date) pmainInfo.get("CREATE_TIME");
                recodes.put(DocFieldName.CREATE_TIME, ct);
                Date ut = (Date) pmainInfo.get("UP_TIME");
                recodes.put(DocFieldName.UP_TIME, ut);
                recodes.put(DocFieldName.APPROVAL_TYPE,
                        MapUtils.getIntValue(pmainInfo, "APPROVAL_TYPE"));
                recodes.put(DocFieldName.APPROVAL_NO, MapUtils.getString(pmainInfo, "APPROVAL_NO"));
                recodes.put(DocFieldName.BRAND_ID, MapUtils.getInteger(pmainInfo, "BRAND_ID"));
                recodes.put(DocFieldName.SHOP_CODE, MapUtils.getString(pmainInfo, "SHOP_CODE"));
                recodes.put(DocFieldName.PRODUCT_NO, MapUtils.getString(pmainInfo, "PRODUCT_NO"));
                recodes.put(DocFieldName.DRUG_ID, MapUtils.getInteger(pmainInfo, "DRUG_CATE_ID"));
                recodes.put(DocFieldName.DRUG_CODE,
                        MapUtils.getString(pmainInfo, "DRUG_CATE_CODE"));
                recodes.put(DocFieldName.CHANNEL, MapUtils.getString(pmainInfo, "CHANNEL"));
                recodes.put("CATEGORY_ID", MapUtils.getInteger(pmainInfo, "CATEGORY_ID"));
                recodes.put(DocFieldName.PRODUCT_TYPE,
                        MapUtils.getInteger(pmainInfo, "PRODUCT_TYPE"));
            }
        } catch (DataAccessException e) {
            LOG.error("获取PRODUCTMAIN产品主表属性失败。SQL: " + sql);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private void extractProductAttrInfo(Map<String, Object> recodes, int productId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select pa.RELATE_ATTR_ID, pa.PRODUCT_ATTR_NAME, pa.PRODUCT_ATTR_TYPE, cav.CATEGORY_ATTR_VALUE from CATEGORY_ATTR_VALUE cav, PRODUCT_ATTR pa");
        sql.append(
                " where cav.CATEGORY_ATTR_VALUE_ID in (SELECT COLUMN_VALUE FROM table (splitByComma (pa.PRODUCT_ATTR_VALUE))) and pa.IS_SKU=0 and pa.IS_NAV=1 and pa.PRODUCT_ATTR_TYPE=1 and pa.INPUT_TYPE!=0 and pa.PRODUCT_ID="
                        + productId);
        // 设置当前线程使用的数据源
        try {

            List<Map<String, Object>> pmainInfo = jdbcTemplate.queryForList(sql.toString());
            if (!pmainInfo.isEmpty()) {

                List<String> attrName = (List<String>) recodes.get(DocFieldName.ATTRNAME);
                attrName = null == attrName ? new ArrayList<String>(pmainInfo.size()) : attrName;

                List<String> attrValue = (List<String>) recodes.get(DocFieldName.ATTRVAL);
                attrValue = null == attrValue ? new ArrayList<String>(pmainInfo.size()) : attrValue;

                List<String> attrValueCP = (List<String>) recodes.get(DocFieldName.ATTRVAL_CP);
                attrValueCP =
                        null == attrValueCP ? new ArrayList<String>(pmainInfo.size()) : attrValueCP;

                for (Map<String, Object> map : pmainInfo) {
                    String name = MapUtils.getString(map, "PRODUCT_ATTR_NAME");
                    String value = MapUtils.getString(map, "CATEGORY_ATTR_VALUE");
                    attrName.add(name);
                    attrValue.add(value);
                }
                // 所有SKU 属性名称
                recodes.put(DocFieldName.ATTRNAME, attrName);
                // 所有SKU 属性值
                recodes.put(DocFieldName.ATTRVAL, attrValue);
                attrValueCP.addAll(attrValue);
                recodes.put(DocFieldName.ATTRVAL_CP, attrValueCP);
            }
        } catch (DataAccessException e) {
            LOG.error("获取产品类目导航属性失败。SQL: " + sql);
            throw e;

        }
    }

    @SuppressWarnings("unchecked")
    private void operationAttrInfo(Map<String, Object> recodes, int productId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select pa.RELATE_ATTR_ID, pa.PRODUCT_ATTR_NAME, pa.PRODUCT_ATTR_TYPE from PRODUCT_ATTR pa");
        sql.append(" where pa.PRODUCT_ATTR_TYPE=3 and pa.PRODUCT_ID=" + productId);
        try {

            List<Map<String, Object>> pmainInfo = jdbcTemplate.queryForList(sql.toString());
            if (!pmainInfo.isEmpty()) {

                List<String> opname = (List<String>) recodes.get(DocFieldName.OPNAME);
                opname = null == opname ? new ArrayList<String>(pmainInfo.size()) : opname;

                List<String> opval = (List<String>) recodes.get(DocFieldName.OPVAL);
                opval = null == opval ? new ArrayList<String>(pmainInfo.size()) : opval;

                for (Map<String, Object> map : pmainInfo) {
                    String name = MapUtils.getString(map, "PRODUCT_ATTR_NAME");
                    opname.add(name);
                    opval.add(name);
                }

                recodes.put(DocFieldName.OPNAME, opname);
                recodes.put(DocFieldName.OPVAL, opval);
            }
        } catch (DataAccessException e) {
            LOG.error("获取产品运营属性失败。SQL: " + sql);
            throw e;
        }
    }

    private Map<String, Object> extractCategoryInfo(Map<String, Object> recodes, int categoryId) {
        List<Map<String, Object>> categorys = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("select CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME");
        sql.append(" from CATEGORYS");
        sql.append(" start with CATEGORY_ID = " + categoryId);
        sql.append(" connect by prior PARENT_ID = CATEGORY_ID");
        sql.append(" order by category_id desc");
        try {

            categorys = jdbcTemplate.queryForList(sql.toString());
            if (!categorys.isEmpty()) {
                String[] codes = new String[3];
                String[] names = new String[3];
                String[] ids = new String[3];
                for (int i = 0; i < categorys.size(); i++) {
                    Map<String, Object> map = categorys.get(i);
                    String id = MapUtils.getString(map, "CATEGORY_ID");
                    String code = MapUtils.getString(map, "CATEGORY_CODE");
                    String name = MapUtils.getString(map, "CATEGORY_NAME");
                    codes[i] = code;
                    names[i] = name;
                    ids[i] = id;
                }
                recodes.put(DocFieldName.PHYSICS_CODE, codes);
                recodes.put(DocFieldName.PHYSICS_NAME, names);
                recodes.put(DocFieldName.PHYSICS_ID, ids);
            }
        } catch (DataAccessException e) {
            LOG.error("获取产品类目信息失败。SQL: " + sql);
            throw e;
        }
        return recodes;
    }

    private Map<String, Object> extractSKUImage(Map<String, Object> recodes, long skuId) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        String sql =
                "select IMG_PATH1, IMG_PATH2, IMG_PATH3, IMG_PATH4, IMG_PATH5, IMG_PATH6, IMG_PATH7, IMG_PATH8, IMG_PATH9, IMG_PATH10 from PRODUCT_IMAGE where IS_DEFAULT=0 and SKU_ID="
                        + skuId;
        // 设置当前线程使用的数据源
        try {

            result = jdbcTemplate.queryForMap(sql);
            if (!result.isEmpty()) {
                Iterator<Entry<String, Object>> it = result.entrySet().iterator();
                String[] images = new String[result.size()];
                int i = 0;
                while (it.hasNext()) {
                    Entry<String, Object> entry = it.next();

                    // update by zhoulinhong on 20160411
                    if (null != entry.getValue()) {
                        images[i] = entry.getKey() + "=" + entry.getValue();
                        i++;
                    }
                }
                recodes.put(DocFieldName.IMAGE, images);
            }
        } catch (DataAccessException e) {
            LOG.warn("获取获取SKU图片失败。SQL: " + sql);
            // throw e;
        }
        return recodes;
    }

    private Map<String, Object> extractSKURank(Map<String, Object> recodes, long skuId) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        String sql =
                "select avg(POINT) as GRADE, count(APPRAISE_ID) as PNUM from PROD_APPRAISE where PRODUCT_SKU_ID="
                        + skuId;
        try {

            result = jdbcTemplate.queryForMap(sql);
            if (!result.isEmpty()) {
                recodes.put(DocFieldName.GRADE, MapUtils.getDouble(result, "GRADE"));
                recodes.put(DocFieldName.PNUM, MapUtils.getLong(result, "PNUM"));
            }
        } catch (DataAccessException e) {
            LOG.warn("获取产品评价信息失败。SQL: " + sql);
            // throw e;
        }
        return recodes;
    }

    private Map<String, Object> extractSKUBrowseCount(Map<String, Object> recodes, String skuCode) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        String sql =
                "select count(CONTENT_CODE) as BROWSE from BNES_BROWSING_HIS  where BROWSING_TYPe=1 and CONTENT_CODE='"
                        + skuCode + "'";
        try {

            result = jdbcTemplate.queryForMap(sql);
            if (!result.isEmpty()) {
                recodes.put(DocFieldName.BROWSE, MapUtils.getLongValue(result, "BROWSE"));
            }
        } catch (DataAccessException e) {
            LOG.warn("获取产品浏览次数信息失败。SQL: " + sql);
        }
        return recodes;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<String, Object> extractSupplierInfo(final Map<String, Object> recodes) {
        final int supplierId = MapUtils.getIntValue(recodes, DocFieldName.SHOP_CODE);
        String supSql =
                "select si.SUPPLIER_TYPE,si.SUPPLIER_ID,si.MARKET_ID,mm.MARKET_NAME from suppliers_info si,medicine_market mm where si.MARKET_ID = mm.MARKET_ID(+) and si.SUPPLIER_ID=?";

        jdbcTemplate.query(supSql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, supplierId);
            }
        }, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                recodes.put(DocFieldName.SUPTYPE, rs.getInt("SUPPLIER_TYPE"));
                recodes.put(DocFieldName.MARKET_ID, rs.getInt("MARKET_ID"));
                recodes.put(DocFieldName.MARKET_NAME, rs.getString("MARKET_NAME"));
                return null;
            }
        });
        return recodes;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<String, Object> extractShopInfo(final Map<String, Object> recodes) {
        final int supplierId = MapUtils.getIntValue(recodes, DocFieldName.SHOP_CODE);
        String shopSql = "select s.SHOP_ID, s.SHOP_NAME from shop_main s where s.SUPPLIER_ID=?";

        jdbcTemplate.query(shopSql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, supplierId);
            }
        }, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("SHOP_NAME");
                int id = rs.getInt("SHOP_ID");
                recodes.put(DocFieldName.SHOPNAME, name);
                recodes.put(DocFieldName.SHOPID, id);
                return null;
            }
        });
        return recodes;
    }

    private Map<String, Object> extractSKUShopgory(final Map<String, Object> recodes) {
        final int productId = MapUtils.getIntValue(recodes, DocFieldName.PRODUCT_ID);
        String shopgorySql =
                "select t.shop_category_id from SHOP_PRODUCT_CATEGORY t where t.product_id=?";
        final List<Integer> ids = Lists.newArrayList();

        jdbcTemplate.query(shopgorySql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, productId);
            }
        }, new RowMapper<Object>() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                int shopgory = rs.getInt("shop_category_id");
                ids.add(shopgory);

                return null;
            }
        });
        Integer[] gids = new Integer[ids.size()];
        recodes.put(DocFieldName.SHOPGORY, ids.toArray(gids));
        return recodes;
    }

    @Override
    public Map<String, Object> getProductInfo(long skuId) {
        Map<String, Object> recodes = new HashMap<String, Object>();
        // PRODUCT_SKU 表字段
        extractSKUInfo(recodes, skuId);
        // 判断sku对应的产品是否存在/有效
        if (recodes.isEmpty()) {

            return null;
        }
        // 获取SKU销量
        extractSKUSales(recodes, skuId);
        // 获取SKU图片
        extractSKUImage(recodes, skuId);
        // SKU属性表
        extractSKUNavAttr(recodes, skuId);
        extractAllSKUAttr(recodes, skuId);
        // 获取产品信息
        int productId = (Integer) recodes.get(DocFieldName.PRODUCT_ID);
        // PRODUCTMAIN产品主表
        extractProductmainInfo(recodes, productId);
        // 获取供应商和店铺信息 b2b
        extractSupplierInfo(recodes);
        extractShopInfo(recodes);
        // 获取产品类目导航属性
        extractProductAttrInfo(recodes, productId);
        // 获取产品运营属性
        operationAttrInfo(recodes, productId);
        // 获取SKU所属类目信息
        int categoryId = (Integer) recodes.remove("CATEGORY_ID");
        extractCategoryInfo(recodes, categoryId);
        // 获取SKU库存量信息
        extractSKUStock(recodes, skuId);
        // 获取SKU评价信息
        extractSKURank(recodes, skuId);
        // 获取SKU被浏览次数信息
        String skuCode = (String) recodes.get(DocFieldName.SKUCODE);
        if (StringUtils.isNotBlank(skuCode)) {
            extractSKUBrowseCount(recodes, skuCode);
        }
        // 获取SKU 店铺运营类目信息
        extractSKUShopgory(recodes);
        return recodes;
    }

    @Override
    public List<Map<String, Object>> getAllProductName() {
        List<Map<String, Object>> result = Lists.newArrayList();
        String sql = "select PROCUCT_NAME from PRODUCTMAIN where STATUS=3";

        result = jdbcTemplate.queryForList(sql);
        return result;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getProductCount() {
        String sql = "select count(PRODUCT_ID) from PRODUCTMAIN where STATUS=3";

        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public List<Map<String, String>> getB2BSugInfoByPage(final int pageNo, final int pageSize) {
        String sql =
                "SELECT PROCUCT_NAME, BRAND_NAME, ENG_NAME FROM (SELECT ROW_NUMBER() OVER(order by product_id desc) as rnum, p.procuct_name, b.brand_name, b.eng_name FROM PRODUCTMAIN p, PROD_BRAND b where p.STATUS=3 and b.brand_id = p.brand_id(+)) WHERE rnum between ? and ?";

        return jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, (pageNo - 1) * pageSize);
                ps.setInt(2, pageNo * pageSize);
            }
        }, new RowMapper<Map<String, String>>() {

            @Override
            public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, String> result = new HashMap<String, String>(3);
                result.put("PROCUCT_NAME", rs.getString("PROCUCT_NAME"));
                result.put("BRAND_NAME", rs.getString("BRAND_NAME"));
                result.put("ENG_NAME", rs.getString("ENG_NAME"));
                return result;
            }

        });
    }

    @Override
    public List<Map<String, Object>> getChildrenOprationCateGory(int parentId) {
        String sql =
                "select CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME, CHANNEL, EXEC_SQL from CATEGORYS t where t.is_phy=2 and t.status=1 and t.parent_id="
                        + parentId;

        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getOperationCategoryTree(int oid) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String sql =
                "select CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME, EXEC_SQL from CATEGORYS c start with CATEGORY_ID="
                        + oid + " connect by PARENT_ID = prior CATEGORY_ID";
        try {

            result = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            LOG.error("获取运营类目信息树失败! SQL: " + sql);
            LOG.error("oid: " + oid);
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getCategoryByChannel() {

        StringBuilder sql = new StringBuilder(
                "select distinct CATEGORY_ID, PARENT_ID, CATEGORY_CODE, CATEGORY_NAME, CHANNEL, EXEC_SQL, SORTNO, LEVEL");
        sql.append(" from CATEGORYS");
        sql.append(" connect by prior CATEGORY_ID=PARENT_ID");
        sql.append(" start with PARENT_ID=0 and STATUS=1 and IS_PHY=2");
        sql.append(" order by CATEGORY_ID");

        List<Map<String, String>> categories =
                jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, String>>() {

                    @Override
                    public Map<String, String> mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        Map<String, String> row = Maps.newHashMap();
                        row.put("CATEGORY_ID", rs.getString("CATEGORY_ID"));
                        row.put("CATEGORY_CODE", rs.getString("CATEGORY_CODE"));
                        row.put("CATEGORY_NAME", rs.getString("CATEGORY_NAME"));
                        row.put("EXEC_SQL", rs.getString("EXEC_SQL"));
                        row.put("SORTNO", rs.getString("SORTNO"));
                        row.put("LEVEL", rs.getString("LEVEL"));
                        row.put("PARENT_ID", rs.getString("PARENT_ID"));
                        return row;
                    }
                });
        return categories;
    }

    @Override
    public Map<String, Object> getShopInfo(final long id) {
        final Map<String, Object> result = Maps.newHashMap();
        String sql =
                "select s.SHOP_ID, s.SUPPLIER_ID, s.SHOP_TYPE, s.SHOP_NAME, s.SHOP_TITLE, s.LOGO_PATH, s.INTRODUCE, s.SHOP_SITE, s.SHOP_SEO_KEY, s.AUDIT_TIME, s.MANAGE_BRAND, s.SHOP_LEVEL, s.DEFAULT_DOMAIN_URL, s.SERVICE_QQ, s.SERVICE_TYPE from SHOP_MAIN s where s.STATUS = 1 and s.SHOP_ID=?";

        jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, id);
            }
        }, new RowMapper<Object>() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

                result.put(ShopFieldName.ID, rs.getString("SHOP_ID"));
                result.put(ShopFieldName.SID, rs.getString("SUPPLIER_ID"));
                result.put(ShopFieldName.SHOP_TYPE,
                        rs.getObject("SHOP_TYPE") == null ? null : rs.getInt("SHOP_TYPE"));
                result.put(ShopFieldName.SHOP_NAME, rs.getString("SHOP_NAME"));
                result.put(ShopFieldName.SHOP_TITLE, rs.getString("SHOP_TITLE"));
                result.put(ShopFieldName.LOGO_PATH, rs.getString("LOGO_PATH"));
                result.put(ShopFieldName.INTRODUCE, rs.getString("INTRODUCE"));
                result.put(ShopFieldName.DOMAIN, rs.getString("DEFAULT_DOMAIN_URL"));
                result.put(ShopFieldName.LEVEL,
                        rs.getObject("SHOP_LEVEL") == null ? null : rs.getInt("SHOP_LEVEL"));
                result.put(ShopFieldName.SHOP_SITE, rs.getString("SHOP_SITE"));
                result.put(ShopFieldName.SSK, rs.getString("SHOP_SEO_KEY"));
                result.put(ShopFieldName.AUDI_TIME, rs.getDate("AUDIT_TIME"));
                result.put(ShopFieldName.MANAGE_BRAND, rs.getString("MANAGE_BRAND"));
                result.put(ShopFieldName.CONTACT, rs.getString("SERVICE_QQ"));
                result.put(ShopFieldName.CONTACTYPE,
                        rs.getObject("SERVICE_TYPE") == null ? null : rs.getInt("SERVICE_TYPE"));
                return null;
            }

        });
        return result;
    }

    @Override
    public Map<String, Object> getCorporateInfo(final long id) {
        final Map<String, Object> result = Maps.newHashMap();
        String sql =
                "select t.CORPORATE_NAME, t.CORPORATE_LOCATION, t.PROVINCE, t.CITY, t.AREA from SUPPLIERS_INFO spi, COMMERCIAL_TENANT_BASIC_INFO t where spi.MERCHANT_ID=t.N_COMMERCIAL_TENANT_ID and spi.SUPPLIER_ID=?";

        jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, id);
            }
        }, new RowMapper<Object>() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                result.put(ShopFieldName.CORP_NAME, rs.getString("CORPORATE_NAME"));
                result.put(ShopFieldName.CORP_ADDR, rs.getString("CORPORATE_LOCATION"));
                result.put(ShopFieldName.PROVINCE, rs.getString("PROVINCE"));
                result.put(ShopFieldName.CITY, rs.getString("CITY"));
                result.put(ShopFieldName.AREA, rs.getString("AREA"));
                return null;
            }

        });
        return result;
    }

    @Override
    public Map<String, Object> getShopScore(final long id) {
        final Map<String, Object> result = Maps.newHashMap();
        String sql =
                "select d.ASSESS_TYPE, avg(d.ASSESS_SCORE) as SCORE from ORDER_ASSESS_DETAIL d, ORDER_ITEM OT, PRODUCTMAIN PM, PRODUCT_SKU PS where d.order_code = OT.Order_Code and PS.PRODUCT_ID = PM.PRODUCT_ID(+) AND OT.COMMODITY_SKU = PS.PRODUCT_SKU_CODE AND PM.SHOP_CODE = ? group by d.ASSESS_TYPE";

        jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, id + "");
            }
        }, new RowMapper<Object>() {

            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                String type = rs.getString("ASSESS_TYPE");
                if ("Assess_Type_one".equals(type)) {
                    result.put(ShopFieldName.DESC_SCORE, rs.getDouble("SCORE"));
                } else if ("Assess_Type_two".equals(type)) {
                    result.put(ShopFieldName.SPEED_SCORE, rs.getDouble("SCORE"));
                } else if ("Assess_Type_three".equals(type)) {
                    result.put(ShopFieldName.DIST_SCORE, rs.getDouble("SCORE"));
                } else if ("Assess_Type_four".equals(type)) {
                    result.put(ShopFieldName.SALE_SCORE, rs.getDouble("SCORE"));
                }
                return null;
            }

        });
        return result;
    }

    @Override
    public void batchAddShopWhole(List<Map<String, Object>> shopList) {
        // 插入sql
        String sql =
                "insert into SEARCH_SHOP_DATA_WHOLE(SHOP_ID,SUPPLIER_ID,SHOP_TYPE,SHOP_NAME,SHOP_TITLE,LOGO_PATH,"
                        + "INTRODUCE,SHOP_SITE,SHOP_SEO_KEY,AUDIT_TIME,MANAGE_BRAND,SHOP_LEVEL,DEFAULT_DOMAIN_URL,SERVICE_QQ,SERVICE_TYPE,"
                        + "CORPORATE_NAME,CORPORATE_LOCATION,PROVINCE,CITY,AREA,DESC_SCORE,SPEED_SCORE,DIST_SCORE,SALE_SCORE) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        final List<Map<String, Object>> tempList = shopList;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, tempList.get(i).get("id"));
                ps.setObject(2, tempList.get(i).get("sid"));
                ps.setObject(3, tempList.get(i).get("shopType"));
                ps.setObject(4, tempList.get(i).get("shopName"));
                ps.setObject(5, tempList.get(i).get("shopTitle"));
                ps.setObject(6, tempList.get(i).get("logoPath"));
                ps.setObject(7, tempList.get(i).get("introduce"));
                ps.setObject(8, tempList.get(i).get("shopSite"));
                ps.setObject(9, tempList.get(i).get("ssk"));
                ps.setObject(10, tempList.get(i).get("audiTime"));
                ps.setObject(11, tempList.get(i).get("manageBrand"));
                ps.setObject(12, tempList.get(i).get("level"));
                ps.setObject(13, tempList.get(i).get("domain"));
                ps.setObject(14, tempList.get(i).get("contact"));
                ps.setObject(15, tempList.get(i).get("contacType"));
                ps.setObject(16, tempList.get(i).get("corpName"));
                ps.setObject(17, tempList.get(i).get("corpAddr"));
                ps.setObject(18, tempList.get(i).get("province"));
                ps.setObject(19, tempList.get(i).get("city"));
                ps.setObject(20, tempList.get(i).get("area"));
                ps.setObject(21, tempList.get(i).get("descScore"));
                ps.setObject(22, tempList.get(i).get("speedScore"));
                ps.setObject(23, tempList.get(i).get("distScore"));
                ps.setObject(24, tempList.get(i).get("saleScore"));
            }

            @Override
            public int getBatchSize() {

                return tempList.size();
            }
        });
    }

    @Override
    public void batchUpdateShopWhole(List<Map<String, Object>> shopList) {
        // 插入sql
        String sql =
                "update SEARCH_SHOP_DATA_WHOLE set SUPPLIER_ID=?,SHOP_TYPE=?,SHOP_NAME=?,SHOP_TITLE=?,LOGO_PATH=?,"
                        + "INTRODUCE=?,SHOP_SITE=?,SHOP_SEO_KEY=?,AUDIT_TIME=?,MANAGE_BRAND=?,SHOP_LEVEL=?,DEFAULT_DOMAIN_URL=?,SERVICE_QQ=?,SERVICE_TYPE=?,"
                        + "CORPORATE_NAME=?,CORPORATE_LOCATION=?,PROVINCE=?,CITY=?,AREA=?,DESC_SCORE=?,SPEED_SCORE=?,DIST_SCORE=?,SALE_SCORE=? where SHOP_ID=? ";

        final List<Map<String, Object>> tempList = shopList;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, tempList.get(i).get("sid"));
                ps.setObject(2, tempList.get(i).get("shopType"));
                ps.setObject(3, tempList.get(i).get("shopName"));
                ps.setObject(4, tempList.get(i).get("shopTitle"));
                ps.setObject(5, tempList.get(i).get("logoPath"));
                ps.setObject(6, tempList.get(i).get("introduce"));
                ps.setObject(7, tempList.get(i).get("shopSite"));
                ps.setObject(8, tempList.get(i).get("ssk"));
                ps.setObject(9, tempList.get(i).get("audiTime"));
                ps.setObject(10, tempList.get(i).get("manageBrand"));
                ps.setObject(11, tempList.get(i).get("level"));
                ps.setObject(12, tempList.get(i).get("domain"));
                ps.setObject(13, tempList.get(i).get("contact"));
                ps.setObject(14, tempList.get(i).get("contacType"));
                ps.setObject(15, tempList.get(i).get("corpName"));
                ps.setObject(16, tempList.get(i).get("corpAddr"));
                ps.setObject(17, tempList.get(i).get("province"));
                ps.setObject(18, tempList.get(i).get("city"));
                ps.setObject(19, tempList.get(i).get("area"));
                ps.setObject(20, tempList.get(i).get("descScore"));
                ps.setObject(21, tempList.get(i).get("speedScore"));
                ps.setObject(22, tempList.get(i).get("distScore"));
                ps.setObject(23, tempList.get(i).get("saleScore"));
                ps.setObject(24, tempList.get(i).get("id"));
            }

            @Override
            public int getBatchSize() {

                return tempList.size();
            }
        });
    }

    @Override
    public void batchDelShopWhole(List<Long> shopIds) {

        if (null == shopIds || shopIds.isEmpty()) {

            return;
        }

        // 拼接ID字符串
        String strIds = concatStr(shopIds, ",");
        // 删除sql
        String sql = "delete from SEARCH_SHOP_DATA_WHOLE where SHOP_ID in (" + strIds + ")";

        // 执行删除
        jdbcTemplate.update(sql);
    }

    @Override
    public void batchAddB2bWhole(List<Map<String, Object>> productList) {
        // 插入sql
        String sql =
                "insert into SEARCH_B2B_DATA_WHOLE(PRODUCT_SKU_ID,PRODUCT_ID,PRODUCT_SKU_CODE,PRICE,MARK_PRICE,PROCUCT_NAME,"
                        + "PRODUCT_TITLE,PRODUCT_SUBTITLE,KEYWORD,PRODUCTHOT,BRAND_ID,BRAND_NAME,CREATE_TIME,UP_TIME,APPROVAL_TYPE,APPROVAL_NO,SHOP_CODE,"
                        + "PRODUCT_NO,DRUG_CATE_ID,DRUG_CATE_CODE,CHANNEL,PRODUCT_TYPE,SALE_QUANTITY,IMG_PATH,CATEGORY_ATTR_NAME_IS_NAV,CATEGORY_ATTR_VALUE_IS_NAV,"
                        + "CATEGORY_ATTR_NAME_IS_NAV_CP,CATEGORY_ATTR_NAME,CATEGORY_ATTR_VALUE,CATEGORY_ATTR_VALUE_SS,CATEGORY_ID,CATEGORY_CODE,CATEGORY_NAME,"
                        + "SUPPLIER_TYPE,SHOP_ID,SHOP_NAME,PRODUCT_ATTR_NAME_OP,PRODUCT_ATTR_NAME_OPVAL,QUALITY,GRADE,PNUM,BROWSE,SHOP_CATEGORY_ID,BOTTOM_AMOUNT,UNIT) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        final List<Map<String, Object>> tempList = productList;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @SuppressWarnings("unchecked")
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, tempList.get(i).get("id"));
                ps.setObject(2, tempList.get(i).get("prodId"));
                ps.setObject(3, tempList.get(i).get("skucode"));
                ps.setObject(4, tempList.get(i).get("price"));
                ps.setObject(5, tempList.get(i).get("mprice"));
                ps.setObject(6, tempList.get(i).get("prodName"));
                ps.setObject(7, tempList.get(i).get("prodTitle"));
                ps.setObject(8, tempList.get(i).get("subtitle"));
                ps.setObject(9, tempList.get(i).get("keyword"));
                ps.setObject(10, tempList.get(i).get("prodHot"));
                ps.setObject(11, tempList.get(i).get("brandId"));
                ps.setObject(12, tempList.get(i).get("brandName"));
                ps.setObject(13, tempList.get(i).get("cTime"));
                ps.setObject(14, tempList.get(i).get("upTime"));
                ps.setObject(15, tempList.get(i).get("approType"));
                ps.setObject(16, tempList.get(i).get("approNum"));
                ps.setObject(17, tempList.get(i).get("scode"));
                ps.setObject(18, tempList.get(i).get("pno"));
                ps.setObject(19, tempList.get(i).get("drugId"));
                ps.setObject(20, tempList.get(i).get("drugCode"));
                ps.setObject(21, tempList.get(i).get("channel"));
                ps.setObject(22, tempList.get(i).get("prodType"));
                ps.setObject(23, tempList.get(i).get("sales"));

                String[] images = (String[]) tempList.get(i).get("image");
                ps.setObject(24, concatStr(images, ","));

                List<Object> attrnames = (List<Object>) tempList.get(i).get("attrname");
                ps.setObject(25, concatObjStr(attrnames, ","));

                List<Object> attrvals = (List<Object>) tempList.get(i).get("attrval");
                ps.setObject(26, concatObjStr(attrvals, ","));

                List<Object> attrval_cps = (List<Object>) tempList.get(i).get("attrval_cp");
                ps.setObject(27, concatObjStr(attrval_cps, ","));

                List<Object> san_ss = (List<Object>) tempList.get(i).get("san_ss");
                ps.setObject(28, concatObjStr(san_ss, ","));

                List<Object> sav_cns = (List<Object>) tempList.get(i).get("sav_cns");
                ps.setObject(29, concatObjStr(sav_cns, ","));

                List<Object> sav_ss = (List<Object>) tempList.get(i).get("sav_ss");
                ps.setObject(30, concatObjStr(sav_ss, ","));

                String[] pcIds = (String[]) tempList.get(i).get("pcId");
                ps.setObject(31, concatStr(pcIds, ","));

                String[] pcCodes = (String[]) tempList.get(i).get("pcCode");
                ps.setObject(32, concatStr(pcCodes, ","));

                String[] pcNames = (String[]) tempList.get(i).get("pcName");
                ps.setObject(33, concatStr(pcNames, ","));

                ps.setObject(34, tempList.get(i).get("supType"));
                ps.setObject(35, tempList.get(i).get("shopId"));
                ps.setObject(36, tempList.get(i).get("shopName"));

                List<Object> opnames = (List<Object>) tempList.get(i).get("opname");
                ps.setObject(37, concatObjStr(opnames, ","));

                List<Object> opvals = (List<Object>) tempList.get(i).get("opval");
                ps.setObject(38, concatObjStr(opvals, ","));

                ps.setObject(39, tempList.get(i).get("stock"));
                ps.setObject(40, tempList.get(i).get("grade"));
                ps.setObject(41, tempList.get(i).get("pnum"));
                ps.setObject(42, tempList.get(i).get("browse"));

                Integer[] shopgorys = (Integer[]) tempList.get(i).get("shopgory");
                ps.setObject(43, concatStr(shopgorys, ","));

                ps.setObject(44, tempList.get(i).get("bottomAmount"));
                ps.setObject(45, tempList.get(i).get("unit"));
            }

            @Override
            public int getBatchSize() {

                return tempList.size();
            }
        });
    }

    @Override
    public void batchUpdateB2bWhole(List<Map<String, Object>> productList) {
        // 插入sql
        String sql =
                "update SEARCH_B2B_DATA_WHOLE set PRODUCT_ID=?,PRODUCT_SKU_CODE=?,PRICE=?,MARK_PRICE=?,PROCUCT_NAME=?,PRODUCT_TITLE=?,"
                        + "PRODUCT_SUBTITLE=?,KEYWORD=?,PRODUCTHOT=?,BRAND_ID=?,BRAND_NAME=?,CREATE_TIME=?,UP_TIME=?,APPROVAL_TYPE=?,APPROVAL_NO=?,SHOP_CODE=?,PRODUCT_NO=?,"
                        + "DRUG_CATE_ID=?,DRUG_CATE_CODE=?,CHANNEL=?,PRODUCT_TYPE=?,SALE_QUANTITY=?,IMG_PATH=?,CATEGORY_ATTR_NAME_IS_NAV=?,CATEGORY_ATTR_VALUE_IS_NAV=?,"
                        + "CATEGORY_ATTR_NAME_IS_NAV_CP=?,CATEGORY_ATTR_NAME=?,CATEGORY_ATTR_VALUE=?,CATEGORY_ATTR_VALUE_SS=?,CATEGORY_ID=?,CATEGORY_CODE=?,CATEGORY_NAME=?,"
                        + "SUPPLIER_TYPE=?,SHOP_ID=?,SHOP_NAME=?,PRODUCT_ATTR_NAME_OP=?,PRODUCT_ATTR_NAME_OPVAL=?,QUALITY=?,GRADE=?,PNUM=?,BROWSE=?,SHOP_CATEGORY_ID=?,BOTTOM_AMOUNT=?,UNIT=? "
                        + "where PRODUCT_SKU_ID=? ";

        final List<Map<String, Object>> tempList = productList;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @SuppressWarnings("unchecked")
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, tempList.get(i).get("prodId"));
                ps.setObject(2, tempList.get(i).get("skucode"));
                ps.setObject(3, tempList.get(i).get("price"));
                ps.setObject(4, tempList.get(i).get("mprice"));
                ps.setObject(5, tempList.get(i).get("prodName"));
                ps.setObject(6, tempList.get(i).get("prodTitle"));
                ps.setObject(7, tempList.get(i).get("subtitle"));
                ps.setObject(8, tempList.get(i).get("keyword"));
                ps.setObject(9, tempList.get(i).get("prodHot"));
                ps.setObject(10, tempList.get(i).get("brandId"));
                ps.setObject(11, tempList.get(i).get("brandName"));
                ps.setObject(12, tempList.get(i).get("cTime"));
                ps.setObject(13, tempList.get(i).get("upTime"));
                ps.setObject(14, tempList.get(i).get("approType"));
                ps.setObject(15, tempList.get(i).get("approNum"));
                ps.setObject(16, tempList.get(i).get("scode"));
                ps.setObject(17, tempList.get(i).get("pno"));
                ps.setObject(18, tempList.get(i).get("drugId"));
                ps.setObject(19, tempList.get(i).get("drugCode"));
                ps.setObject(20, tempList.get(i).get("channel"));
                ps.setObject(21, tempList.get(i).get("prodType"));
                ps.setObject(22, tempList.get(i).get("sales"));

                String[] images = (String[]) tempList.get(i).get("image");
                ps.setObject(23, concatStr(images, ","));

                List<Object> attrnames = (List<Object>) tempList.get(i).get("attrname");
                ps.setObject(24, concatObjStr(attrnames, ","));

                List<Object> attrvals = (List<Object>) tempList.get(i).get("attrval");
                ps.setObject(25, concatObjStr(attrvals, ","));

                List<Object> attrval_cps = (List<Object>) tempList.get(i).get("attrval_cp");
                ps.setObject(26, concatObjStr(attrval_cps, ","));

                List<Object> san_ss = (List<Object>) tempList.get(i).get("san_ss");
                ps.setObject(27, concatObjStr(san_ss, ","));

                List<Object> sav_cns = (List<Object>) tempList.get(i).get("sav_cns");
                ps.setObject(28, concatObjStr(sav_cns, ","));

                List<Object> sav_ss = (List<Object>) tempList.get(i).get("sav_ss");
                ps.setObject(29, concatObjStr(sav_ss, ","));

                String[] pcIds = (String[]) tempList.get(i).get("pcId");
                ps.setObject(30, concatStr(pcIds, ","));

                String[] pcCodes = (String[]) tempList.get(i).get("pcCode");
                ps.setObject(31, concatStr(pcCodes, ","));

                String[] pcNames = (String[]) tempList.get(i).get("pcName");
                ps.setObject(32, concatStr(pcNames, ","));

                ps.setObject(33, tempList.get(i).get("supType"));
                ps.setObject(34, tempList.get(i).get("shopId"));
                ps.setObject(35, tempList.get(i).get("shopName"));

                List<Object> opnames = (List<Object>) tempList.get(i).get("opname");
                ps.setObject(36, concatObjStr(opnames, ","));

                List<Object> opvals = (List<Object>) tempList.get(i).get("opval");
                ps.setObject(37, concatObjStr(opvals, ","));

                ps.setObject(38, tempList.get(i).get("stock"));
                ps.setObject(39, tempList.get(i).get("grade"));
                ps.setObject(40, tempList.get(i).get("pnum"));
                ps.setObject(41, tempList.get(i).get("browse"));

                Integer[] shopgorys = (Integer[]) tempList.get(i).get("shopgory");
                ps.setObject(42, concatStr(shopgorys, ","));

                ps.setObject(43, tempList.get(i).get("id"));
                ps.setObject(44, tempList.get(i).get("bottomAmount"));
                ps.setObject(45, tempList.get(i).get("unit"));
            }

            @Override
            public int getBatchSize() {

                return tempList.size();
            }
        });
    }

    @Override
    public void batchDelB2bWhole(List<Long> skuIds) {

        if (null == skuIds || skuIds.isEmpty()) {

            return;
        }

        // update by zhoulinhong on 20160428
        // 拆分skuIds
        List<List<Long>> listArr = new ArrayList<List<Long>>();
        // 获取被拆分的数组个数
        int arrSize = skuIds.size() % 500 == 0 ? skuIds.size() / 500 : skuIds.size() / 500 + 1;

        for (int i = 0; i < arrSize; i++) {
            List<Long> subList = new ArrayList<Long>();
            // 把指定索引数据放入到list中
            for (int j = 500 * i; j < 500 * i + 500; j++) {
                if (j < skuIds.size()) {
                    subList.add(skuIds.get(j));
                }
            }
            listArr.add(subList);
        }

        for (List<Long> idList : listArr) {
            // 拼接ID字符串
            String strIds = concatStr(idList, ",");
            // 删除sql
            String sql =
                    "delete from SEARCH_B2B_DATA_WHOLE where PRODUCT_SKU_ID in (" + strIds + ")";

            // 执行删除
            jdbcTemplate.update(sql);
        }
    }

    /**
     * 指定拼接符拼接id集合
     * 
     * @param ids
     * @param regex
     * @return
     */
    private String concatStr(List<Long> ids, String regex) {

        StringBuilder result = new StringBuilder();
        if (null == ids || ids.isEmpty() || StringUtils.isEmpty(regex)) {

            return null;
        }

        for (Long id : ids) {
            if (null != id) {
                result.append(id).append(regex);
            }
        }

        // 删除最后一个分隔符号
        if (-1 != result.lastIndexOf(regex)) {
            result.deleteCharAt(result.lastIndexOf(regex));
        }

        return result.toString();
    }

    /**
     * 指定拼接符拼接
     * 
     * @param strArr
     * @param regex
     * @return
     */
    private String concatStr(Object[] strArr, String regex) {

        StringBuilder result = new StringBuilder();
        if (null == strArr || strArr.length == 0 || StringUtils.isEmpty(regex)) {

            return null;
        }

        for (Object str : strArr) {
            if (null != str) {
                result.append(str).append(regex);
            }
        }

        // 删除最后一个分隔符号
        if (-1 != result.lastIndexOf(regex)) {
            result.deleteCharAt(result.lastIndexOf(regex));
        }

        return result.toString();
    }

    /**
     * 指定拼接符拼接id集合
     * 
     * @param ids
     * @param regex
     * @return
     */
    private String concatObjStr(List<Object> objs, String regex) {

        StringBuilder result = new StringBuilder();
        if (null == objs || objs.isEmpty() || StringUtils.isEmpty(regex)) {

            return null;
        }

        for (Object obj : objs) {
            if (null != obj) {
                result.append(obj).append(regex);
            }
        }

        // 删除最后一个分隔符号
        if (-1 != result.lastIndexOf(regex)) {
            result.deleteCharAt(result.lastIndexOf(regex));
        }

        return result.toString();
    }

    @Override
    public Map<String, Object> getB2bProductIndex(long id) {

        // 查询sql
        String sql = "select * from SEARCH_B2B_DATA_WHOLE where PRODUCT_SKU_ID = " + id;
        // 产品对象
        Map<String, Object> product = null;
        try {
            product = jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            LOG.error("获取产品索引信息失败。SQL: " + sql);
        }

        return product;
    }
}
