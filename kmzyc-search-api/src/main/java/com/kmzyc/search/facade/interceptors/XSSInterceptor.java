package com.kmzyc.search.facade.interceptors;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Maps;

/**
 * XSS攻击拦截器: 用户原始输入不处理，对输出到页面的内容进行XSS攻击过滤。
 * 
 * @author river
 * 
 */
public class XSSInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(XSSInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (null != modelAndView) {
            try {
                ModelMap modelMap = modelAndView.getModelMap();
                Iterator<Entry<String, Object>> it = modelMap.entrySet().iterator();
                Map<String, String> escapeMap = Maps.newHashMap();
                while (it.hasNext()) {
                    Entry<String, Object> entry = it.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (null != value && value instanceof String) {
                        String temp = value.toString();
                        temp = ESAPI.encoder().encodeForHTML(temp);
                        temp = xssEncode(temp);
                        // temp = ESAPI.encoder().encodeForJavaScript(temp);
                        escapeMap.put(key, temp);
                    }
                    if (null != value && "recommends".equals(key)) {
                        String query = MapUtils.getString(modelMap, "keyword");
                        List<Map<String, Object>> recommends = (List<Map<String, Object>>) value;
                        for (Map<String, Object> info : recommends) {
                            int start = MapUtils.getIntValue(info, "start");
                            int end = MapUtils.getIntValue(info, "end");
                            String warpQuery = warpQuery(query, start, end);
                            info.put("warpQuery", warpQuery);
                        }
                    }
                }
                modelMap.putAll(escapeMap);
            } catch (Exception e) {
                LOG.error("XSS攻击过滤出现异常：", e);
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * <b>手机</b><del>中</del><b>的</b><del>洗衣机fdfdfdsfsdf</del>
     * 
     * @param query
     * @param start
     * @param end
     * @return
     */
    private String warpQuery(String query, int start, int end) {
        StringBuilder target = new StringBuilder();
        if (start > 0) {
            target.append("<del>");
            target.append(ESAPI.encoder().encodeForHTML(query.subSequence(0, start).toString()));
            target.append("</del>");
        }
        target.append("<b>"
                + ESAPI.encoder().encodeForHTML(query.subSequence(start, end).toString()) + "</b>");
        if (end < query.length()) {
            target.append("<del>");
            target.append(ESAPI.encoder()
                    .encodeForHTML(query.subSequence(end, query.length()).toString()));
            target.append("</del>");
        }
        return target.toString();
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符 目前xssProject对注入代码要求是必须开始标签和结束标签(如<script></script>
     * )正确匹配才能解析，否则报错；因此只能替换调xssProject换为自定义实现
     * 
     * @param s
     * @return
     */
    public static String xssEncode(String s) {

        if (s == null || s.isEmpty()) {
            return s;
        }

        String result = stripXSS(s);
        if (null != result) {
            result = escape(result);
        }

        return result;

    }

    private static String stripXSS(String value) {
        if (value != null) {
            // Avoid null characters
            value = value.replaceAll("", "");
            // Avoid anything between script tags
            Pattern scriptPattern =
                    Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("</iframe>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<iframe(.*?)>",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

    public static String escape(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append('＞');// 全角大于号
                    break;
                case '<':
                    sb.append('＜');// 全角小于号
                    break;
                default:
                    sb.append(c);
                    break;
            }

        }
        return sb.toString();
    }
}
