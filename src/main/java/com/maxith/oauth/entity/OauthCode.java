package com.maxith.oauth.entity;

/**
 *  密码模式
 *  @author zhouyou
 *  @date 2018/7/18 16:34
 **/
public class OauthCode {

    private Integer id;
    /**
     * 密码
     */
    private String code;
    /**
     * 用户名
     */
    private String username;
    /**
     * 客户端id
     */
    private String clientId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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
}