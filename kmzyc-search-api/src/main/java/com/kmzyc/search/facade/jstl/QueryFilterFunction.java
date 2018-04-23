package com.kmzyc.search.facade.jstl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.kmzyc.search.facade.constants.SupplierType;
import com.kmzyc.search.param.HTTPParam;

/**
 * 搜索过滤参数处理JSTL标签
 * 
 * @author river
 * 
 */
public class QueryFilterFunction {
    private static final Logger LOG = LoggerFactory.getLogger(QueryFilterFunction.class);

    /**
     * 添加过滤参数
     * 
     * @param queryString
     * @param paramName
     * @param paramValue
     * @return
     */
    public static String addFilter(String queryString, String paramName, String paramValue) {

        queryString = StringEscapeUtils.unescapeHtml4(queryString);
        paramName = StringEscapeUtils.unescapeHtml4(paramName);
        paramValue = StringEscapeUtils.unescapeHtml4(paramValue);

        String regex = "(\\?|&+)(" + HTTPParam.f + ")=([^&]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(queryString);
        if (matcher.find()) {
            try {
                String matchString = matcher.group();
                String filter = matchString.substring(matchString.indexOf("=") + 1);

                String newFilterParam = URLDecoder.decode(filter, "UTF-8");
                if (StringUtils.isBlank(newFilterParam)) {
                    newFilterParam += paramName + "=" + paramValue;
                } else {
                    newFilterParam += "&" + paramName + "=" + paramValue;
                }
                String newParamVal = getNewParam(matchString.substring(0, 1), newFilterParam);
                queryString = queryString.replace(matchString, newParamVal);
            } catch (UnsupportedEncodingException e) {
                LOG.error("对过滤参数编码转换失败！", e);
            }
        } else {
            try {
                String filter = paramName + "=" + paramValue;
                queryString = QueryStringFunction.add(queryString, HTTPParam.f.name(),
                        URLEncoder.encode(filter, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                LOG.error("对过滤参数编码转换失败！", e);
            }
        }
        return queryString;
    }

    private static String getNewParam(String prefix, String newFilterParam) {
        StringBuilder newParamVal = new StringBuilder();
        if ("?".equals(prefix)) {
            newParamVal.append("?");
        }
        if (StringUtils.isNotBlank(newFilterParam)) {
            if (newParamVal.length() == 0) {
                newParamVal.append("&");
            }
            try {
                newParamVal.append(HTTPParam.f).append("=")
                        .append(URLEncoder.encode(newFilterParam, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                LOG.error("对过滤参数编码转换失败！", e);
            }
        }
        return newParamVal.toString();
    }

    /**
     * 删除过滤参数
     * 
     * @param queryString
     * @param paramName
     * @return
     */
    public static String delFilter(String queryString, String paramName) {
        queryString = StringEscapeUtils.unescapeHtml4(queryString);
        paramName = StringEscapeUtils.unescapeHtml4(paramName);

        Pattern pattern = Pattern.compile("(\\?|&+)(" + HTTPParam.f + ")=([^&]*)");
        Matcher matcher = pattern.matcher(queryString);
        if (matcher.find()) {
            try {
                // &f=a%3Da%26b%3Dc
                String matchString = matcher.group();
                // a%3Da%26b%3Dc
                String filter = matchString.substring(matchString.indexOf("=") + 1);

                // a=a&b=c
                String filterParams = URLDecoder.decode(filter, "UTF-8");

                Iterator<String> it = Splitter.on("&").trimResults().split(filterParams).iterator();
                StringBuilder newFilterParams = new StringBuilder();
                while (it.hasNext()) {
                    String pairs = it.next();
                    if (!pairs.startsWith(paramName + "=")) {
                        newFilterParams.append(pairs);
                        if (it.hasNext()) {
                            newFilterParams.append("&");
                        }
                    }
                }
                String newParamVal =
                        getNewParam(matchString.substring(0, 1), newFilterParams.toString());
                queryString = queryString.replace(matchString, newParamVal);
            } catch (UnsupportedEncodingException e) {
                LOG.error("对过滤参数编码转换失败！", e);
            }
        }
        return queryString;
    }

    /**
     * 删除指定参数名称与值.过滤参数值有可能出现相同的过滤参数名称. a=a&b=c&a=b
     * 
     * @param queryString
     * @param paramName
     * @param paramValue
     * @return
     */
    public static String delFilter(String queryString, String paramName, String paramValue) {
        queryString = StringEscapeUtils.unescapeHtml4(queryString);
        paramName = StringEscapeUtils.unescapeHtml4(paramName);
        paramValue = StringEscapeUtils.unescapeHtml4(paramValue);

        Pattern pattern = Pattern.compile("(\\?|&+)(" + HTTPParam.f + ")=([^&]*)");
        Matcher matcher = pattern.matcher(queryString);
        if (matcher.find()) {
            try {
                // &f=a%3Da%26b%3Dc
                String matchString = matcher.group();
                // a%3Da%26b%3Dc
                String filter = matchString.substring(matchString.indexOf("=") + 1);

                // a=a&b=c
                String filterParams = URLDecoder.decode(filter, "UTF-8");

                Iterator<String> it = Splitter.on("&").trimResults().split(filterParams).iterator();
                StringBuilder newFilterParams = new StringBuilder();
                while (it.hasNext()) {
                    String pairs = it.next();
                    if (!pairs.equals(paramName + "=" + paramValue)) {
                        newFilterParams.append(pairs);
                        if (it.hasNext()) {
                            newFilterParams.append("&");
                        }
                    }
                }
                String newParamVal =
                        getNewParam(matchString.substring(0, 1), newFilterParams.toString());
                queryString = queryString.replace(matchString, newParamVal);
            } catch (UnsupportedEncodingException e) {
                LOG.error("对过滤参数编码转换失败！", e);
            }
        }
        return queryString;
    }

    /**
     * 判断是否包含指定过滤参数
     * 
     * @param queryString
     * @param filterParam
     * @return
     */
    public static boolean contant(String queryString, String paramName) {
        queryString = StringEscapeUtils.unescapeHtml4(queryString);
        paramName = StringEscapeUtils.unescapeHtml4(paramName);

        Pattern pattern = Pattern.compile("(\\?|&+)(" + HTTPParam.f + ")=([^&]*)");
        Matcher matcher = pattern.matcher(queryString);
        if (matcher.find()) {
            try {
                String matcherString = matcher.group();
                String filter = matcherString.substring(matcherString.indexOf("=") + 1);

                String filterParams = URLDecoder.decode(filter, "UTF-8");
                Iterator<String> it = Splitter.on("&").trimResults().split(filterParams).iterator();
                new StringBuilder();
                while (it.hasNext()) {
                    String pairs = it.next();
                    String prefix = paramName + "=";
                    if (pairs.startsWith(paramName + "=")) {
                        return pairs.length() > prefix.length();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LOG.error("对过滤参数编码转换失败！", e);
            }

        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String en = URLEncoder.encode("a=a&b=c", "UTF-8");
        String en2 = URLEncoder.encode(en, "UTF-8");
        System.out.println(en + "\t" + en2);
        String u = "search?t=1";
        System.out.println(addFilter(u, "d", "d"));
        System.out.println(delFilter(delFilter(u, "d"), "b"));
        URLEncoder.encode("a=a&b=c&a=1", "UTF-8");
        u = "search?f=stock%3D1-*";
        System.out.println(contant(u, "stock"));
        System.out.println("supplier_type=" + SupplierType.T3.getCode());
    }
}
