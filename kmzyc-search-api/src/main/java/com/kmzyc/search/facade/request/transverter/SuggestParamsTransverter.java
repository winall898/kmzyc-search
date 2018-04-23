package com.kmzyc.search.facade.request.transverter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.facade.request.IRequestTransverter;
import com.kmzyc.search.facade.util.ParamUitl;
import com.kmzyc.search.util.PinyinUtil;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * SUGGEST搜索参数处理类
 * 
 * @author river
 * 
 */
public class SuggestParamsTransverter implements IRequestTransverter {

    private static final Logger LOG = Logger.getLogger(SuggestParamsTransverter.class);

    // 中文正则
    private static final Pattern CN_PATTERN = Pattern.compile("[\u4e00-\u9fa5],{0,}");

    private final Channel channel;

    public SuggestParamsTransverter(Channel channel) {

        this.channel = channel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convert(Object param) {
        if (param == null) {
            return null;
        }
        Map<String, String[]> searchParams = (Map<String, String[]>) param;

        String prefix = searchParams.get("q")[0].toLowerCase();
        String pinyin = prefix;
        try {
            // 将查询词转成拼音
            // 多音字只需取一种读音
            pinyin = PinyinUtil.getNormalPy(prefix)[0];
        } catch (BadHanyuPinyinOutputFormatCombination e) {

            LOG.error("对词条 " + prefix + " 进行拼音转换失败。", e);
        }
        // 保留字符进行转义
        pinyin = ParamUitl.escapeQueryChars(pinyin);
        // 由于查询会按查询词条的空格将查询词条分隔成两条词条进行查询，
        // 所以将查询词条中的空格替换成*
        pinyin = pinyin.replaceAll("[\\s]+", "*");


        JSONObject queryJson =
                JSONObject.parseObject("{\"query\":{\"query_string\":{}},\"post_filter\":{}}");

        // 查询字段
        JSONArray queryArray = new JSONArray();
        queryArray.add("source");
        queryArray.add("lowterm");
        queryArray.add("py");
        queryArray.add("jp");
        queryJson.getJSONObject("query").getJSONObject("query_string").put("fields", queryArray);

        // 查询关键字
        queryJson.getJSONObject("query").getJSONObject("query_string").put("query", pinyin + "*");

        // 排序
        JSONArray sortArray = new JSONArray();
        sortArray.add(JSONObject.parseObject("{\"order\": \"asc\"}"));
        sortArray.add(JSONObject.parseObject("{\"count\": \"desc\"}"));
        queryJson.put("sort", sortArray);

        // 过滤
        JSONArray filterArray = new JSONArray();
        filterArray.add(JSONObject.parseObject(
                "{\"term\": {\"channel\": \"" + channel.name().toUpperCase() + "\"}}"));

        Matcher m = CN_PATTERN.matcher(prefix);
        if (m.find()) {
            // 如果查询词中包含中文，再进行前缀过滤。
            prefix = prefix.replaceAll("[^\u4e00-\u9fa5]+", "*");
            filterArray.add(
                    JSONObject.parseObject("{\"wildcard\": {\"lowterm\": \"" + prefix + "*\"}}"));
        }
        queryJson.getJSONObject("post_filter").put("and", filterArray);

        // 返回字段
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("id");
        jsonArray.add("source");
        queryJson.put("fields", jsonArray);

        return queryJson.toJSONString();
    }

}
