package com.maxith.oauth.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 认证令牌 对象
 *
 * @author zhouyou
 * @date 2018/7/18 16:02
 **/
public class AccessToken implements Serializable {

    public static final String BEARER_TYPE = "Bearer";

    /**
     * 默认的 refresh_token 的有效时长: 30天
     */
    public final static long REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

    /**
     * 默认的 access_token 的有效时长: 12小时
     */
    public final static long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;


    protected static long ONE_THOUSAND = 1000L;

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 令牌id
     */
    private String tokenId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 认证id
     */
    private String authenticationId;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 令牌类型
     */
    private String tokenType = BEARER_TYPE;
    /**
     * 令牌过期时间
     */
    private Long tokenExpiredSeconds = ACCESS_TOKEN_VALIDITY_SECONDS;
    /**
     * 刷新令牌过期时间
     */
    private Long refreshTokenExpiredSeconds = REFRESH_TOKEN_VALIDITY_SECONDS;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId == null ? null : tokenId.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId == null ? null : authenticationId.trim();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType == null ? null : tokenType.trim();
    }

    public Long getTokenExpiredSeconds() {
        return tokenExpiredSeconds;
    }

    public void setTokenExpiredSeconds(Long tokenExpiredSeconds) {
        this.tokenExpiredSeconds = tokenExpiredSeconds;
    }

    public Long getRefreshTokenExpiredSeconds() {
        return refreshTokenExpiredSeconds;
    }

    public void setRefreshTokenExpiredSeconds(Long refreshTokenExpiredSeconds) {
        this.refreshTokenExpiredSeconds = refreshTokenExpiredSeconds;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 令牌是否过期
     *
     * @return
     */
    public boolean tokenExpired() {
        final long time = createTime.getTime() + (this.tokenExpiredSeconds * ONE_THOUSAND);
        return time < System.currentTimeMillis();
    }

    /**
     * 刷新令牌是否过期
     *
     * @return
     */
    public boolean refreshTokenExpired() {
        final long time = createTime.getTime() + (this.refreshTokenExpiredSeconds * ONE_THOUSAND);
        return time < System.currentTimeMillis();
    }

    /**
     * 获取令牌过期时间
     *
     * @return
     */
    public long currentTokenExpiredSeconds() {
        if (tokenExpired()) {
            return -1;
        }
        final long time = createTime.getTime() + (this.tokenExpiredSeconds * ONE_THOUSAND);
        return (time - System.currentTimeMillis()) / ONE_THOUSAND;
    }

    /**
     * 更新令牌时间
     *
     * @param clientDetails
     * @return
     */
    public AccessToken updateByClientDetails(OauthClient clientDetails) {
        this.clientId = clientDetails.getClientId();

        final Long accessTokenValidity = clientDetails.getAccessTokenValidity();
        if (accessTokenValidity != null && accessTokenValidity > 0) {
            this.tokenExpiredSeconds = accessTokenValidity;
        }

        final Long refreshTokenValidity = clientDetails.getRefreshTokenValidity();
        if (refreshTokenValidity != null && refreshTokenValidity > 0) {
            this.refreshTokenExpiredSeconds = refreshTokenValidity;
        }

        return this;
    }

    @Override
    public String toString() {
        return "{" +
                "tokenId='" + tokenId + '\'' +
                ", username='" + username + '\'' +
                ", clientId='" + clientId + '\'' +
                ", authenticationId='" + authenticationId + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", tokenExpiredSeconds=" + tokenExpiredSeconds +
                ", refreshTokenExpiredSeconds=" + refreshTokenExpiredSeconds +
                '}';
    }

    /**
     * Clone
     * Exclude token, refresh_token, authenticationId, expired
     *
     * @return New AccessToken instance
     */
    public AccessToken cloneMe() {
        AccessToken token = new AccessToken();
        token.setUsername(username);
        token.setClientId(clientId);
        token.setTokenType(tokenType);
        return token;
    }
}