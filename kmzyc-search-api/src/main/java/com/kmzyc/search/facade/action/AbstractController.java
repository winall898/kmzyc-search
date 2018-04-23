package com.kmzyc.search.facade.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.kmzyc.search.facade.interceptors.XSSInterceptor;

public abstract class AbstractController {

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    protected String getParameter(String paramName) {
        if (StringUtils.isBlank(paramName)) {
            return "";
        }

        return XSSInterceptor.xssEncode(getRequest().getParameter(paramName));
    }

    protected Object getAttribute(String paramName) {
        if (StringUtils.isBlank(paramName)) {
            return null;
        }
        return getRequest().getAttribute(paramName);
    }

    protected void setAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    protected Map<String, String[]> getParameterMap() {
        return getRequest().getParameterMap();
    }

    protected String getRequestURI() {
        return getRequest().getRequestURI();
    }

    protected String getQueryString() {
        return getRequest().getQueryString();
    }
}
