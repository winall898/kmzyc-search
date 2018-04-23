package com.kmzyc.search.facade.action;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.param.DocFieldName;
import com.kmzyc.search.param.HTTPParam;

public class SearchConfig {
    private boolean facet;
    private boolean enableElevation;
    private boolean forceElevation;
    private boolean highlight;
    private boolean isEdismax;

    private final Map<String, String[]> config;

    private SearchConfig(Map<String, String[]> httpParameters) {
        config = Maps.newHashMap(httpParameters);
    }

    public boolean isFacet() {
        return facet;
    }

    public SearchConfig setFacet(boolean facet) {
        this.facet = facet;
        config.put("facet", new String[] {String.valueOf(this.isFacet())});
        return this;
    }

    public boolean isEnableElevation() {
        return enableElevation;
    }

    public SearchConfig setEnableElevation(boolean enableElevation) {
        this.enableElevation = enableElevation;
        config.put("enableElevation", new String[] {String.valueOf(this.isEnableElevation())});
        return this;
    }

    public boolean isForceElevation() {
        return forceElevation;
    }

    public SearchConfig setForceElevation(boolean forceElevation) {
        this.forceElevation = forceElevation;
        config.put("forceElevation", new String[] {String.valueOf(this.isForceElevation())});
        return this;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public SearchConfig setHighlight(boolean highlight) {
        this.highlight = highlight;
        config.put("highlight", new String[] {String.valueOf(this.isHighlight())});
        return this;
    }

    public boolean isEdismax() {
        return isEdismax;
    }

    public SearchConfig setEdismax(boolean isEdismax) {
        this.isEdismax = isEdismax;
        config.put("edismax", new String[] {String.valueOf(this.isEdismax())});
        return this;
    }

    public static SearchConfig build(Map<String, String[]> httpParameters) {

        SearchConfig searchconfig = new SearchConfig(httpParameters);

        StringBuilder queryText = new StringBuilder();

        // 关键字
        String[] keyword = searchconfig.getConfig().remove(HTTPParam.kw.name());
        if (ArrayUtils.isNotEmpty(keyword) && StringUtils.isNotBlank(keyword[0])) {
            queryText.append(ParamUitl.escapeQueryChars(keyword[0]));
        }
        // 1级运营类目
        String[] c1 = searchconfig.getConfig().remove(HTTPParam.c1.name());
        if (ArrayUtils.isNotEmpty(c1) && StringUtils.isNotBlank(c1[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.FIRST_O_CODE + ":" + c1[0]);
        }
        // 2级运营类目
        String[] c2 = searchconfig.getConfig().remove(HTTPParam.c2.name());
        if (ArrayUtils.isNotEmpty(c2) && StringUtils.isNotBlank(c2[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.SECOND_O_CODE + ":" + c2[0]);
        }
        // 3级运营类目
        String[] c3 = searchconfig.getConfig().remove(HTTPParam.c3.name());
        if (ArrayUtils.isNotEmpty(c3) && StringUtils.isNotBlank(c3[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.THIRD_O_CODE + ":" + c3[0]);
        }
        // 物理类目
        String[] cid = searchconfig.getConfig().remove(HTTPParam.cid.name());
        if (ArrayUtils.isNotEmpty(cid) && StringUtils.isNotBlank(cid[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.PHYSICS_ID + ":" + cid[0]);
        }
        // 品牌id
        String[] bid = searchconfig.getConfig().remove(HTTPParam.bid.name());
        if (ArrayUtils.isNotEmpty(bid) && StringUtils.isNotBlank(bid[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.BRAND_ID + ":" + bid[0]);
        }
        // 店铺编码
        String[] sc = searchconfig.getConfig().remove(HTTPParam.sc.name());
        if (ArrayUtils.isNotEmpty(sc) && StringUtils.isNotBlank(sc[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.SHOP_CODE + ":" + sc[0]);
        }

        // 店铺ID
        String[] shopid = searchconfig.getConfig().remove(HTTPParam.shopid.name());
        if (ArrayUtils.isNotEmpty(shopid) && StringUtils.isNotBlank(shopid[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.SHOPID + ":" + shopid[0]);
        }
        // 店铺运营类目ID
        String[] soid = searchconfig.getConfig().remove(HTTPParam.soid.name());
        if (ArrayUtils.isNotEmpty(soid) && StringUtils.isNotBlank(soid[0])) {
            // 设置OID查询参数
            Iterator<String> oidList = Splitter.on("_").trimResults().split(soid[0]).iterator();
            StringBuilder idText = new StringBuilder(DocFieldName.SHOPGORY + ":(");
            while (oidList.hasNext()) {
                idText.append(oidList.next());
                if (oidList.hasNext()) {
                    idText.append(" OR ");
                }
            }
            idText.append(")");
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(idText.toString());
        }

        // 运营类首字第拼音
        String[] py = searchconfig.getConfig().remove(HTTPParam.py.name());
        if (ArrayUtils.isNotEmpty(py) && StringUtils.isNotBlank(py[0])) {
            if (StringUtils.isNotBlank(queryText.toString())) {
                queryText.append(" AND ");
            }
            queryText.append(DocFieldName.OCNAME_PY + ":" + py[0].toLowerCase());
        }

        searchconfig.getConfig().put("q", new String[] {queryText.toString()});

        searchconfig.getConfig().put("facet",
                new String[] {String.valueOf(searchconfig.isFacet())});
        searchconfig.getConfig().put("edismax",
                new String[] {String.valueOf(searchconfig.isEdismax())});
        searchconfig.getConfig().put("enableElevation",
                new String[] {String.valueOf(searchconfig.isEnableElevation())});
        searchconfig.getConfig().put("forceElevation",
                new String[] {String.valueOf(searchconfig.isForceElevation())});
        searchconfig.getConfig().put("highlight",
                new String[] {String.valueOf(searchconfig.isHighlight())});
        return searchconfig;
    }

    public Map<String, String[]> getConfig() {
        return config;
    }
}
