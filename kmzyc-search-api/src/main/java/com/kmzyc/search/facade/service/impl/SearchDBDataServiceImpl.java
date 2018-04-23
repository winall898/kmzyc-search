package com.kmzyc.search.facade.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.facade.dao.CategorysDao;
import com.kmzyc.search.facade.dao.ShopMainDao;
import com.kmzyc.search.facade.service.SearchDBDataService;
import com.kmzyc.search.facade.vo.Brand;
import com.kmzyc.search.facade.vo.SupplierInfo;

@Service("searchDBDataService")
public class SearchDBDataServiceImpl implements SearchDBDataService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchDBDataServiceImpl.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private CategorysDao categorysDao;

    @Resource
    private ShopMainDao shopMainDao;

    @Override
    public Map<String, String> getOperationCategorySql(int oid) {
        Map<String, String> paramMap = Maps.newHashMap();
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
                        if (it.hasNext()) {
                            buf.append(or);
                        }
                    }

                    String categoryId = MapUtils.getString(m, "CATEGORY_ID");
                    if (categoryId.equals(String.valueOf(oid))) {
                        paramMap.put("c_name", MapUtils.getString(m, "CATEGORY_NAME"));
                        paramMap.put("c_keyword", MapUtils.getString(m, "CATEGORY_KEYWORD"));
                        paramMap.put("c_desc2", MapUtils.getString(m, "CATEGORY_DESC"));
                    }
                }
            }
            String sql = buf.toString();
            if (sql.endsWith(or)) {
                sql = sql.substring(0, sql.length() - or.length());
            }
            paramMap.put("sql", sql);
        }
        return paramMap;
    }

    @Override
    public Map<String, Map<String, String>> getAllOperationCategoryMap() {

        List<Map<String, String>> categories = getCategoryByChannel();
        if (null != categories && !categories.isEmpty()) {
            Map<String, Map<String, String>> result = Maps.newHashMap();
            for (Map<String, String> m : categories) {
                result.put(MapUtils.getString(m, "CATEGORY_ID"), m);
            }
            return result;
        }
        return Maps.newHashMap();
    }

    @Override
    public Map<String, List<Map<String, String>>> getCategoryGroupByLevel() {

        List<Map<String, String>> categories = getCategoryByChannel();
        if (null != categories && !categories.isEmpty()) {
            Map<String, List<Map<String, String>>> result = Maps.newHashMap();
            for (Map<String, String> m : categories) {
                String leavel = MapUtils.getString(m, "LEVEL");
                List<Map<String, String>> values = result.get(leavel);
                if (null == values) {
                    values = Lists.newArrayList();
                    result.put(leavel, values);
                }
                values.add(m);
            }
            return result;
        }

        return Collections.emptyMap();
    }

    @Override
    public List<Map<String, Object>> getOperationCategoryTree(int oid) {
        List<Map<String, Object>> result = Lists.newArrayList();
        // 增加TDK字段
        String sql =
                "select CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME, EXEC_SQL,CATEGORY_TITLE,CATEGORY_KEYWORD,CATEGORY_DESC from CATEGORYS c start with CATEGORY_ID="
                        + oid + " connect by PARENT_ID = prior CATEGORY_ID";
        try {
            // 执行查询
            result = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {

            LOG.error("获取运营类目信息树失败! SQL: " + sql, e);
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Brand getBrandDetails(final int brandID) {
        StringBuilder sql = new StringBuilder(
                "select BRAND_ID, BRAND_NAME, CONTACT_INFO, PAVILION_PIC_PATH, INTRODUCE_FILE_PATH, CERTIFICATE_HONOR, LOGO_PATH, DES");
        sql.append(" from PROD_BRAND where BRAND_ID = ? and IS_VALID=1");

        // 执行查询
        List<Brand> result = jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, brandID);
            }
        }, new RowMapper() {

            @Override
            public Brand mapRow(ResultSet rs, int arg1) throws SQLException {
                Brand brand = new Brand();
                brand.setBrandId(rs.getInt("BRAND_ID"));
                brand.setBrandName(rs.getString("BRAND_NAME"));
                brand.setLogoPath(rs.getString("LOGO_PATH"));
                brand.setDes(rs.getString("DES"));

                String contactInfo = rs.getString("CONTACT_INFO");
                brand.setContactInfo(contactInfo);
                // 品牌详情页的联系方式（多个以分号隔开）
                if (StringUtils.isNotBlank(contactInfo)) {
                    List<String> contactInfoList = Lists.newArrayList();
                    if (contactInfo.indexOf(';') > -1) {
                        String[] infoArray = contactInfo.split(";");
                        for (String info : infoArray) {
                            contactInfoList.add(info);
                        }
                    } else {
                        contactInfoList.add(contactInfo);
                    }
                    brand.setContactList(contactInfoList);
                }

                brand.setPavilionPicPath(rs.getString("PAVILION_PIC_PATH"));

                String introduceFilePath = rs.getString("INTRODUCE_FILE_PATH");
                brand.setIntroduceFilePath(introduceFilePath);
                // 品牌详情页的介绍文件或图片
                if (StringUtils.isNotBlank(introduceFilePath)) {
                    List<String> introducePics = Lists.newArrayList();
                    if (introduceFilePath.endsWith(".swf")) {
                        // 视频:introducePics.add(introduceFilePath);
                    } else if (introduceFilePath.indexOf(';') > -1) { // 多张图片
                        String[] fileArray = introduceFilePath.split(";");
                        for (String info : fileArray) {
                            introducePics.add(info);
                        }
                    } else { // 单张图片
                        introducePics.add(introduceFilePath);
                    }
                    brand.setIntroducePics(introducePics);
                }

                // 品牌详情页的荣誉证书（多个以分号隔开）
                String certificateHonor = rs.getString("CERTIFICATE_HONOR");
                brand.setCertificateHonor(certificateHonor);

                if (StringUtils.isNotBlank(certificateHonor)) {
                    List<String> honorList = Lists.newArrayList();
                    if (certificateHonor.indexOf(';') > -1) {
                        String[] infoArray = certificateHonor.split(";");
                        for (String info : infoArray) {
                            honorList.add(info);
                        }
                    } else {
                        honorList.add(certificateHonor);
                    }
                    brand.setHonorList(honorList);
                }

                return brand;
            }
        });
        if (null != result && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, String>> getCategoryByChannel() {

        StringBuilder sql = new StringBuilder(
                "select distinct CATEGORY_ID, PARENT_ID, CATEGORY_CODE, CATEGORY_NAME, CHANNEL, EXEC_SQL, SORTNO, LEVEL");
        sql.append(" from CATEGORYS");
        sql.append(" connect by prior CATEGORY_ID=PARENT_ID");
        sql.append(" start with PARENT_ID=0 and STATUS=1 and IS_PHY=2");
        sql.append(" order by CATEGORY_ID");

        try {
            // 返回结果
            return jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, String>>() {

                @Override
                public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, String> row = Maps.newHashMap();
                    row.put("CATEGORY_ID", rs.getString("CATEGORY_ID"));
                    row.put("CATEGORY_CODE", rs.getString("CATEGORY_CODE"));
                    row.put("CATEGORY_NAME", rs.getString("CATEGORY_NAME"));
                    row.put("CHANNEL", rs.getString("CHANNEL"));
                    row.put("EXEC_SQL", rs.getString("EXEC_SQL"));
                    row.put("SORTNO", rs.getString("SORTNO"));
                    row.put("LEVEL", rs.getString("LEVEL"));
                    row.put("PARENT_ID", rs.getString("PARENT_ID"));
                    return row;
                }
            });
        } catch (Exception e) {

            LOG.error("根据渠道查询类目失败！", e);
        }

        return Collections.emptyList();
    }

    @Override
    public String getCategoryNameById(Integer categoryId) {

        if (null == categoryId) {

            LOG.error("查询类目失败，类目ID不能为空！");
            return null;
        }

        // 类目名称
        String categoryName = null;
        try {
            // 执行查询
            categoryName = categorysDao.getCategoryNameById(categoryId);
        } catch (Exception e) {

            LOG.error("查询类目失败，查询sql异常！", e);
        }

        return categoryName;
    }

    @Override
    public String getShopNameId(Integer shopId) {

        if (null == shopId) {

            LOG.error("查询店铺名称失败，店铺ID不能为空！");
            return null;
        }

        // 店铺名称
        String shopName = null;
        try {
            // 执行查询
            shopName = shopMainDao.getShopNameId(shopId);
        } catch (Exception e) {

            LOG.error("查询店铺名称失败，查询sql异常！", e);
        }

        return shopName;
    }

    @Override
    public SupplierInfo getSupplierInfoByShopId(Integer shopId) {
        if (null == shopId) {

            LOG.error("查询供应商失败，店铺ID不能为空！");
            return null;
        }

        // 供应商
        SupplierInfo supplierInfo = null;
        try {
            // 执行查询
            supplierInfo = shopMainDao.getSupplierInfoByShopId(shopId);
        } catch (Exception e) {

            LOG.error("查询供应商失败，查询sql异常！", e);
        }

        return supplierInfo;
    }

    @Override
    public String getShopCategoryNameById(Integer shopCategoryId) {
        if (null == shopCategoryId) {

            LOG.error("查询店铺类目名称失败，店铺类目ID不能为空！");
            return null;
        }

        // 店铺类目名称
        String categoryName = null;
        try {
            // 执行查询
            categoryName = categorysDao.getShopCategoryNameById(shopCategoryId);
        } catch (Exception e) {

            LOG.error("查询店铺类目名称失败，查询sql异常！", e);
        }

        return categoryName;
    }

}
