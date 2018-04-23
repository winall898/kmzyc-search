package com.kmzyc.search.facade.interceptors;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kmzyc.search.facade.annotation.WapResource;
import com.kmzyc.search.facade.util.MobileUtil;

/**
 * 拦截搜索请求，判断访问来源是否为移动端。 如果是移动端请求，重定向到WAP处理器处理搜索
 * 
 * @author river
 * 
 */
public class WapVisitInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            String channel = (String) request.getAttribute("req_target");
            if (!"b2b".equals(channel)) // 非kmb2b请求不进行判断
            {
                return true;
            }
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            WapResource wapResource = method.getAnnotation(WapResource.class);
            if (null != wapResource) {
                String servletPath = request.getServletPath();
                if (servletPath.startsWith("/wap")) // wap请求不进行判断
                {
                    return true;
                }
                boolean fromMobile = MobileUtil.isFromMobile(request);
                if (fromMobile) {
                    String resource = wapResource.value();
                    request.getRequestDispatcher(resource).forward(request, response);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}
