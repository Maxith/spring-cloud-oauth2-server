package com.maxith.common.tools;


import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * web工具
 *
 * @author zhouyou
 * @date 2018/7/18 10:30
 **/
public class WebUtils {

    private WebUtils() {}

    /**
     * 获取request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request;
    }

    /**
     * 获取response对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        return response;
    }

    /**
     * 获取session
     *
     * @return
     */
    public static Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session;
    }

    /**
     * 从request对象中获取值
     *
     * @param name
     * @return
     */
    public static Object getAttributeFromRequest(String name) {
        HttpServletRequest request = getRequest();
        return request.getAttribute(name);
    }

    /**
     * 从session中获取值
     *
     * @param name
     * @return
     */
    public static Object getAttributeFromSession(String name) {
        Session session = getSession();
        return session.getAttribute(name);
    }

    /**
     * 向request对象中写入值
     *
     * @param name
     * @param value
     */
    public static void setAttribute4Request(String name, Object value) {
        HttpServletRequest request = getRequest();
        request.setAttribute(name, value);
    }

    /**
     * 向session中写入值
     *
     * @param name
     * @param value
     */
    public static void setAttribute4Session(String name, Object value) {
        Session session = getSession();
        session.setAttribute(name, value);
    }

    /**
     * 写出json信息
     *
     * @param response
     * @param oAuthResponse
     */
    public static void writeOAuthJsonResponse(HttpServletResponse response, OAuthResponse oAuthResponse) {
        final int responseStatus = oAuthResponse.getResponseStatus();
        try {
            final Map<String, String> headers = oAuthResponse.getHeaders();
            for (String key : headers.keySet()) {
                response.addHeader(key, headers.get(key));
            }
            response.setContentType(OAuth.ContentType.JSON);
            response.setStatus(responseStatus);

            final PrintWriter out = response.getWriter();
            out.print(oAuthResponse.getBody());
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Write OAuthResponse error", e);
        }
    }

    /**
     * 重定向并写出认证信息
     *
     * @param response
     * @param oAuthResponse
     */
    public static void writeOAuthQueryResponse(HttpServletResponse response, OAuthResponse oAuthResponse) {
        final String locationUri = oAuthResponse.getLocationUri();
        try {

            final Map<String, String> headers = oAuthResponse.getHeaders();
            for (String key : headers.keySet()) {
                response.addHeader(key, headers.get(key));
            }

            response.setStatus(oAuthResponse.getResponseStatus());
            response.sendRedirect(locationUri);

        } catch (IOException e) {
            throw new IllegalStateException("Write OAuthResponse error", e);
        }
    }


    /**
     * 获取客户端真实 ip
     *
     * @param request
     * @return
     */
    public static String retrieveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isUnAvailableIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnAvailableIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnAvailableIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isUnAvailableIp(String ip) {
        return (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip));
    }
}