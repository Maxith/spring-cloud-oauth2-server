package com.maxith.config.oauth;

import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 *  shiro框架的令牌
 *  @author zhouyou
 *  @date 2018/7/18 16:27
 **/
public class OAuthShiroToken implements RememberMeAuthenticationToken {

    private static final long serialVersionUID = 8587854556973099598L;

    /**
     * 认证令牌
     */
    private String accessToken;

    /**
     * 用户id
     * <br/>
     * 即用户名
     */
    private String userId;

    /**
     * 资源id
     */
    private String resourceId;

    /**
     * 开启"记住我" 模式
     * 默认关闭
     */
    private boolean rememberMe = false;

    public OAuthShiroToken(String accessToken, String resourceId) {
        this.accessToken = accessToken;
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public OAuthShiroToken setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public OAuthShiroToken setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }
}
