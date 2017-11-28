package com.maxith.oauth.interfaces;

/**
 * 认证id生成器接口
 */
public interface AuthenticationIdGenerator {

    String generate(String clientId, String username, String scope);

}