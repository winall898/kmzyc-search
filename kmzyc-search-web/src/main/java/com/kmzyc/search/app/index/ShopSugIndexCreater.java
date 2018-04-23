package com.kmzyc.search.app.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.app.dao.SuggestItemDAO;
import com.kmzyc.search.app.model.SuggestItem;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.app.util.MD5Util;
import com.kmzyc.search.app.util.PinyinUtil;
import com.kmzyc.search.app.util.SpringBeanUtil;
import com.kmzyc.search.app.util.SuggestTermFilter;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.ShopFieldName;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 提示词索引库生成器
 * 
 * @author zhoulinhong
 * @since 20160523
 *
 */
public class ShopSugIndexCreater {

    private static final Logger LOG = LoggerFactory.getLogger(ShopSugIndexCreater.class);

    private static final int ROWS = 1000;

    private SuggestItemDAO suggestItemDAO = null;

    private Channel channel;

    public ShopSugIndexCreater(Channel channel) {
        this.channel = channel;
        suggestItemDAO = SpringBeanUtil.getBean("suggestItemDAO", SuggestItemDAO.class);
    }

    public void run() {

        for (int i = 0;; i++) {
            // 查询商品所有索引，获取名称和品牌，创建SUGGEST索引
            SearchHits searchHits = getDocuments(i);
            if (null == searchHits || searchHits.getHits().length <= 0) {

                break;
            }

            // 创建店铺提示词索引
            creatIndex(searchHits.getHits());
        }
    }

    private SearchHits getDocuments(int number) {

        JSONObject queryJson = JSONObject.parseObject("{\"query\":{\"match_all\":{}}}");

        // 返回字段
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(ShopFieldName.SHOP_NAME);
        jsonArray.add(ShopFieldName.MANAGE_BRAND);
        queryJson.put("fields", jsonArray);

        // 分页返回
        queryJson.put("from", number * ROWS);
        queryJson.put("size", ROWS);

        try {

            SearchHits searchHits = IndexClientUtil.getInstance().queryIndexByConditions(
                    channel.name(), channel.name(), queryJson.toJSONString());

            return searchHits;
        } catch (Exception e) {

            LOG.error("获取索引失败。query: " + queryJson.toJSONString(), e);
            return null;
        }
    }

    private void creatIndex(SearchHit[] docList) {

        if (null == docList || docList.length <= 0) {

            return;
        }

        List<String> names = Lists.newArrayList();
        List<String> brands = Lists.newArrayList();

        for (SearchHit doc : docList) {
            Map<String, SearchHitField> fields = doc.getFields();
            if (null == fields || fields.isEmpty()) {

                continue;
            }

            // 店铺名称
            String shopName = null;
            if (null != fields.get(ShopFieldName.SHOP_NAME)) {

                shopName = fields.get(ShopFieldName.SHOP_NAME).getValue();
            }
            if (StringUtils.isNotBlank(shopName)) {

                names.add(shopName);
            }

            // 店铺品牌
            String brandName = null;
            if (null != fields.get(ShopFieldName.MANAGE_BRAND)) {

                brandName = fields.get(ShopFieldName.MANAGE_BRAND).getValue();
            }
            if (StringUtils.isNotBlank(brandName)) {

                brands.add(brandName);
            }
        }

        // 店铺名称
        Map<String, JSONObject> docsMap = createDoc(names);

        if (null == docsMap) {

            docsMap = Maps.newHashMap();
        }

        // 品牌
        Map<String, JSONObject> docs = createDoc(brands);
        if (MapUtils.isNotEmpty(docs)) {
            docsMap.putAll(docs);
        }

        try {

            if (null != docsMap && !docsMap.isEmpty()) {

                // 批量创建索引数据
                IndexClientUtil.getInstance().batchAddIndex(Channel.suggest.name(),
                        Channel.suggest.name(), docsMap);
            }
        } catch (Exception e) {

            LOG.error("创建suggest索引失败。", e);
        }
    }

    private Map<String, JSONObject> createDoc(List<String> datas) {

        Map<String, JSONObject> docsMap = Maps.newHashMap();

        // 获取搜索系统中已设定排序权重的搜索词条
        Map<String, SuggestItem> oldTerms = getQueryTerms();

        // 分词处理器
        TermCreater creater = TermCreater.instance;

        for (String data : datas) {

            // 过滤重名
            if (SuggestTermFilter.getFilter(channel).checkAndPut(data)) {

                continue;
            }

            // 分词处理
            Set<String> terms = creater.createTerm(data);

            if (null == terms || terms.isEmpty()) {

                continue;
            }

            for (String term : terms) {

                // 索引集合
                createDocument(term, oldTerms, docsMap);
            }
        }

        return docsMap;
    }

    /**
     * 获取搜索系统中已设定排序权重的搜索词条
     * 
     * @return
     */
    private Map<String, SuggestItem> getQueryTerms() {

        Map<String, SuggestItem> result = Maps.newHashMap();
        List<SuggestItem> termList = suggestItemDAO.findListByExample(new SuggestItem());
        if (termList.isEmpty()) {
            return result;
        }

        result = new HashMap<String, SuggestItem>(termList.size());
        for (int i = 0; i < termList.size(); i++) {
            SuggestItem term = termList.get(i);
            result.put(term.getSource(), term);
        }

        return result;
    }

    /**
     * 创建索引对象
     * 
     * @param name
     * @param terms
     * @param docsMap
     */
    private void createDocument(String name, Map<String, SuggestItem> terms,
            Map<String, JSONObject> docsMap) {

        if (StringUtils.isBlank(name)) {

            return;
        }

        if (null == terms) {

            terms = Maps.newHashMap();
        }

        if (null == docsMap) {

            docsMap = Maps.newHashMap();
        }

        // 索引对象
        JSONObject doc = new JSONObject();

        try {

            String id = MD5Util.getMD5Str(name);
            if (SuggestTermFilter.getFilter(channel).checkAndPut(id)) {

                return;
            }

            doc.put("id", id);
            doc.put("channel", channel.name().toUpperCase());

            SuggestItem qt = null;
            if (terms.containsKey(name)) {
                qt = terms.remove(name);
            }
            doc.put("source", name);

            String lowName = name.toLowerCase();
            doc.put("lowterm", lowName);

            String[] py = null;
            try {
                py = PinyinUtil.getNormalPy(lowName);
            } catch (Exception e) {
                py = new String[] {""};
            }
            // 拼音PY
            doc.put("py", py);

            String[] jp = null;
            try {
                jp = PinyinUtil.getJianPy(lowName);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                jp = new String[] {""};
            }
            // 简拼JP
            doc.put("jp", jp);

            if (null != qt) {
                // 搜索量
                int count = qt.getCount();
                doc.put("count", count);
                // 排序权重
                int order = qt.getSort();
                doc.put("order", order);
            } else {
                // 搜索量
                doc.put("count", 0);
                // 排序权重
                doc.put("order", name.length());
            }

            if (null != doc && !doc.isEmpty()) {

                docsMap.put(id, doc);
            }
        } catch (Exception e) {

            LOG.error("创建索引文档失败。原始词条为：" + name, e);
        }
    }
}
