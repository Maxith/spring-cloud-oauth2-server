package com.maxith.oauth.interfaces;

/**
 * 认证id生成器接口
 *
 * @author zhouyou
 * @date 2018/7/19 15:53
 **/
public interface AuthenticationIdGenerator {
    /**
     * 生成认证id
     *
     * @param clientId
     * @param username
     * @param scope
     * @return
     */
    String generate(String clientId, String username, String scope);
}