package com.maxith.oauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * OAuth 配置
 * @author zhouyou
 * @date 2018/7/18
 * @description
 **/
@Component("oAuthProperties")
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {

    /**
     * session 过期时间
     */
    private Long sessionOvertime;
    /**
     * 登陆页面
     */
    private String loginView;
    /**
     * 授权页面
     */
    private String approvalView;
    /**
     * 授权关键字
     */
    private String approvalKeyword;

    public String getLoginView() {
        return loginView;
    }

    public void setLoginView(String loginView) {
        this.loginView = loginView;
    }

    public Long getSessionOvertime() {
        return sessionOvertime;
    }

    public void setSessionOvertime(Long sessionOvertime) {
        this.sessionOvertime = sessionOvertime;
    }

    public String getApprovalView() {
        return approvalView;
    }

    public void setApprovalView(String approvalView) {
        this.approvalView = approvalView;
    }

    public String getApprovalKeyword() {
        return approvalKeyword;
    }

    public void setApprovalKeyword(String approvalKeyword) {
        this.approvalKeyword = approvalKeyword;
    }
}
