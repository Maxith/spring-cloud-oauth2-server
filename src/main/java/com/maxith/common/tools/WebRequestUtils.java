package com.maxith.common.tools;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 用这个类必须在web.xml中配置
 * <p>
 * <listener>
 * <listener-class>
 * org.springframework.web.context.request.RequestContextListener
 * </listener-class>
 * </listener>
 */
public class WebRequestUtils {
    public static Session getSession() {
        Subject subject= SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session;
    }

    public static <T> T getAttributeFromRequest(String name) {
        HttpServletRequest request = getRequest();
        Object object = request.getAttribute(name);
        @SuppressWarnings("unchecked")
        T t = (T) object;
        return t;
    }

    public static void setAttribute4Request(String name, Object value) {
        HttpServletRequest request = getRequest();
        request.setAttribute(name, value);
    }

    public static void setAttribute4Session(String name, Object value) {
        Session session = getSession();
        session.setAttribute(name, value);
    }

    public static <T> T getAttributeFromSession(Class<T> clazz, String name) {
        Session session = getSession();
        Object object = session.getAttribute(name);
        T t = (T) object;
        return t;
    }
    public static Object getAttributeFromSession(String name) {
        Session session = getSession();
        return session.getAttribute(name);
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request;
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        return response;
    }

    public static String getIp() {
        HttpServletRequest request =getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}