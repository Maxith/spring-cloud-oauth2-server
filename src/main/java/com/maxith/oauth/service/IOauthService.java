package com.maxith.oauth.service;


import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.entity.OauthCode;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.util.Set;

/**
 * 认证服务service接口
 */
public interface IOauthService {

    /**
     * 加载应用对象
     * @param clientId
     * @return
     */
    OauthClient loadOauthClient(String clientId);

    /**
     * 保存认证code
     * @param authCode
     * @param clientDetails
     * @return
     */
    OauthCode saveAuthorizationCode(String username, String authCode, OauthClient clientDetails);

    /**
     * 检索认证code
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
     */
    String retrieveAuthCode(String username,OauthClient clientDetails) throws OAuthSystemException;


    /**
     * 查询认证token
     * @param username
     * @param clientDetails
     * @return
     */
    String queryAccessToken(String username,OauthClient clientDetails);
    /**
     * 检索认证token
     * @param clientDetails
     * @param scopes
     * @param includeRefreshToken
     * @return
     * @throws OAuthSystemException
     */
    AccessToken retrieveAccessToken(String username,OauthClient clientDetails, Set<String> scopes, boolean includeRefreshToken) throws OAuthSystemException;

    /**
     * 检索并返回一个新的认证 token
     * @param clientDetails
     * @param scopes
     * @return
     * @throws OAuthSystemException
     */
    AccessToken retrieveNewAccessToken(String username,OauthClient clientDetails, Set<String> scopes) throws OAuthSystemException;

    /**
     * 加载认证code
     * @param code
     * @param clientDetails
     * @return
     */
    OauthCode loadOauthCode(String code, OauthClient clientDetails);

    /**
     * 删除认证code
     * @param code
     * @param clientDetails
     * @return
     */
    boolean removeOauthCode(String code, OauthClient clientDetails);

    /**
     * 使用认证code获取认证令牌
     * @param clientDetails
     * @param code
     * @return
     * @throws OAuthSystemException
     */
    AccessToken retrieveAuthorizationCodeAccessToken(OauthClient clientDetails, String code) throws OAuthSystemException;

    /**
     * 使用密码认证获取认证令牌
     * @param clientDetails
     * @param scopes
     * @param username
     * @return
     * @throws OAuthSystemException
     */
    AccessToken retrievePasswordAccessToken(OauthClient clientDetails, Set<String> scopes, String username) throws OAuthSystemException;

    /**
     * 直接使用被授权的客户端获取认证令牌
     * @param clientDetails
     * @param scopes
     * @return
     * @throws OAuthSystemException
     */
    AccessToken retrieveClientCredentialsAccessToken(OauthClient clientDetails, Set<String> scopes) throws OAuthSystemException;

    /**
     * 使用刷新令牌获取认证令牌
     * @param refreshToken
     * @param clientId
     * @return
     */
    AccessToken loadAccessTokenByRefreshToken(String refreshToken, String clientId);

    /**
     * 使用刷新令牌获取新的认证令牌
     * @param refreshToken
     * @param clientId
     * @return
     * @throws OAuthSystemException
     */
    AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId) throws OAuthSystemException;

    /**
     * 检查clienId是否存在
     * @param clientId
     * @return
     */
    boolean isExistedClientId(String clientId);
}
