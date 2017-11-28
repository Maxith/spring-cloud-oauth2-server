package com.maxith.oauth.service.impl;


import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.mapper.OauthClientMapper;
import com.maxith.oauth.mapper.OauthCodeMapper;
import com.maxith.oauth.service.IOauthService;
import com.maxith.common.entity.BaseComponent;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.entity.OauthCode;
import com.maxith.oauth.interfaces.AuthenticationIdGenerator;
import com.maxith.oauth.mapper.AccessTokenMapper;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;

/**
 * Created by zhouyou on 2017/7/5.
 */
@Service
public class OauthServiceImpl extends BaseComponent implements IOauthService {

    @Resource
    private AccessTokenMapper accessTokenMapper;

    @Resource
    private OauthClientMapper oauthClientMapper;

    @Resource
    private OauthCodeMapper oauthCodeMapper;

    @Resource
    private AuthenticationIdGenerator authenticationIdGenerator;

    private OAuthIssuer oAuthIssuer;

    public OauthServiceImpl() {
        this.oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
    }

    @Override
    public OauthClient loadOauthClient(String clientId) {
        return oauthClientMapper.selectByClientId(clientId);
    }

    @Override
    public OauthCode saveAuthorizationCode(String username, String authCode, OauthClient clientDetails) {
        OauthCode oauthCode = new OauthCode();
        oauthCode.setCode(authCode);
        oauthCode.setUsername(username);
        oauthCode.setClientId(clientDetails.getClientId());

        oauthCodeMapper.insert(oauthCode);
        logger.debug("Save OauthCode: {}", oauthCode);
        return oauthCode;
    }

    @Override
    public String retrieveAuthCode(String username, OauthClient clientDetails) throws OAuthSystemException {
        final String clientId = clientDetails.getClientId();

        OauthCode oauthCode = oauthCodeMapper.findOauthCodeByUsernameClientId(username, clientId);
        if (oauthCode != null) {
            //Always delete exist
            logger.debug("OauthCode ({}) is existed, remove it and create a new one", oauthCode);
            oauthCodeMapper.deleteByPrimaryKey(oauthCode.getId());
        }
        //create a new one
        oauthCode = createOauthCode(username, clientDetails);

        return oauthCode.getCode();
    }

    @Override
    public String queryAccessToken(String username, OauthClient clientDetails) {
        AccessToken accessToken = accessTokenMapper.findAccessTokenWithoutAuthenticationId(clientDetails.getClientId(), username);
        if (accessToken != null) {
            return accessToken.getTokenId();
        }
        return null;
    }


    @Override
    public AccessToken retrieveAccessToken(String username, OauthClient clientDetails, Set<String> scopes, boolean includeRefreshToken) throws OAuthSystemException {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String clientId = clientDetails.getClientId();

        final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);

        AccessToken accessToken = accessTokenMapper.findAccessToken(clientId, username, authenticationId);
        if (accessToken == null) {
            accessToken = createAndSaveAccessToken(clientDetails, includeRefreshToken, username, authenticationId);
            logger.debug("Create a new AccessToken: {}", accessToken);
        }

        return accessToken;
    }

    //Always return new AccessToken, exclude refreshToken
    @Override
    public AccessToken retrieveNewAccessToken(String username, OauthClient clientDetails, Set<String> scopes) throws OAuthSystemException {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String clientId = clientDetails.getClientId();

        final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);

        AccessToken accessToken = accessTokenMapper.findAccessToken(clientId, username, authenticationId);
        if (accessToken != null) {
            logger.debug("Delete existed AccessToken: {}", accessToken);
            accessTokenMapper.deleteByPrimaryKey(accessToken.getId());
        }
        accessToken = createAndSaveAccessToken(clientDetails, false, username, authenticationId);
        logger.debug("Create a new AccessToken: {}", accessToken);

        return accessToken;
    }

    @Override
    public OauthCode loadOauthCode(String code, OauthClient clientDetails) {
        final String clientId = clientDetails.getClientId();
        return oauthCodeMapper.findOauthCode(code, clientId);
    }

    @Override
    public boolean removeOauthCode(String code, OauthClient clientDetails) {
        final OauthCode oauthCode = loadOauthCode(code, clientDetails);
        final int rows = oauthCodeMapper.deleteByPrimaryKey(oauthCode.getId());
        return rows > 0;
    }

    //Always return new AccessToken
    @Override
    public AccessToken retrieveAuthorizationCodeAccessToken(OauthClient clientDetails, String code) throws OAuthSystemException {
        final OauthCode oauthCode = loadOauthCode(code, clientDetails);
        final String username = oauthCode.getUsername();
        final String clientId = clientDetails.getClientId();

        final String authenticationId = authenticationIdGenerator.generate(clientId, username, null);

        AccessToken accessToken = accessTokenMapper.findAccessToken(clientId, username, authenticationId);
        if (accessToken != null) {
            logger.debug("Delete existed AccessToken: {}", accessToken);
            accessTokenMapper.deleteByPrimaryKey(accessToken.getId());
        }
        accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username, authenticationId);
        logger.debug("Create a new AccessToken: {}", accessToken);

        return accessToken;
    }

    //grant_type=password AccessToken
    @Override
    public AccessToken retrievePasswordAccessToken(OauthClient clientDetails, Set<String> scopes, String username) throws OAuthSystemException {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String clientId = clientDetails.getClientId();

        final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);
        AccessToken accessToken = accessTokenMapper.findAccessToken(clientId, username, authenticationId);

        boolean needCreate = false;
        if (accessToken == null) {
            needCreate = true;
            logger.debug("Not found AccessToken from repository, will create a new one, client_id: {}", clientId);
        } else if (accessToken.tokenExpired()) {
            logger.debug("Delete expired AccessToken: {} and create a new one, client_id: {}", accessToken, clientId);
            accessTokenMapper.deleteByPrimaryKey(accessToken.getId());
            needCreate = true;
        } else {
            logger.debug("Use existed AccessToken: {}, client_id: {}", accessToken, clientId);
        }

        if (needCreate) {
            accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username, authenticationId);
            logger.debug("Create a new AccessToken: {}", accessToken);
        }

        return accessToken;

    }


    //grant_type=client_credentials
    @Override
    public AccessToken retrieveClientCredentialsAccessToken(OauthClient clientDetails, Set<String> scopes) throws OAuthSystemException {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String clientId = clientDetails.getClientId();
        //username = clientId

        final String authenticationId = authenticationIdGenerator.generate(clientId, clientId, scope);
        AccessToken accessToken = accessTokenMapper.findAccessToken(clientId, clientId, authenticationId);

        boolean needCreate = false;
        if (accessToken == null) {
            needCreate = true;
            logger.debug("Not found AccessToken from repository, will create a new one, client_id: {}", clientId);
        } else if (accessToken.tokenExpired()) {
            logger.debug("Delete expired AccessToken: {} and create a new one, client_id: {}", accessToken, clientId);
            accessTokenMapper.deleteByPrimaryKey(accessToken.getId());
            needCreate = true;
        } else {
            logger.debug("Use existed AccessToken: {}, client_id: {}", accessToken, clientId);
        }

        if (needCreate) {
            //Ignore refresh_token
            accessToken = createAndSaveAccessToken(clientDetails, false, clientId, authenticationId);
            logger.debug("Create a new AccessToken: {}", accessToken);
        }

        return accessToken;

    }

    @Override
    public AccessToken loadAccessTokenByRefreshToken(String refreshToken, String clientId) {
        logger.debug("Load OauthClient by refreshToken: {} and clientId: {}", refreshToken, clientId);
        return accessTokenMapper.findAccessTokenByRefreshToken(refreshToken, clientId);
    }

    /*
    * Get AccessToken
    * Generate a new AccessToken from existed(exclude token,refresh_token)
    * Update access_token,refresh_token, expired.
    * Save and remove old
    * */
    @Override
    public AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId) throws OAuthSystemException {
        final AccessToken oldToken = loadAccessTokenByRefreshToken(refreshToken, clientId);

        AccessToken newAccessToken = oldToken.cloneMe();
        logger.debug("Create new AccessToken: {} from old AccessToken: {}", newAccessToken, oldToken);

        OauthClient details = oauthClientMapper.selectByClientId(clientId);
        newAccessToken.updateByClientDetails(details);

        Date now = new Date();
        final String authId = authenticationIdGenerator.generate(clientId, oldToken.getUsername(), null);
        newAccessToken.setAuthenticationId(authId);
        newAccessToken.setTokenId(oAuthIssuer.accessToken());
        newAccessToken.setRefreshToken(oAuthIssuer.refreshToken());
        newAccessToken.setCreateTime(now);
        newAccessToken.setCreateTime(now);

        accessTokenMapper.deleteByPrimaryKey(oldToken.getId());
        logger.debug("Delete old AccessToken: {}", oldToken);

        accessTokenMapper.insert(newAccessToken);
        logger.debug("Save new AccessToken: {}", newAccessToken);

        return newAccessToken;
    }

    @Override
    public boolean isExistedClientId(String clientId) {
        final OauthClient clientDetails = oauthClientMapper.selectByClientId(clientId);
        return clientDetails != null;
    }

    private AccessToken createAndSaveAccessToken(OauthClient clientDetails, boolean includeRefreshToken, String username, String authenticationId) throws OAuthSystemException {
        Date now = new Date();

        AccessToken accessToken = new AccessToken();
        accessToken.setClientId(clientDetails.getClientId());
        accessToken.setUsername(username);
        accessToken.setTokenId(oAuthIssuer.accessToken());
        accessToken.setAuthenticationId(authenticationId);
        accessToken.updateByClientDetails(clientDetails);
        accessToken.setCreateTime(now);
        accessToken.setCreateTime(now);

        if (includeRefreshToken) {
            accessToken.setRefreshToken(oAuthIssuer.refreshToken());
        }

        this.accessTokenMapper.insert(accessToken);
        logger.debug("Save AccessToken: {}", accessToken);
        return accessToken;
    }

    private OauthCode createOauthCode(String username, OauthClient clientDetails) throws OAuthSystemException {
        OauthCode oauthCode;
        final String authCode = oAuthIssuer.authorizationCode();

        logger.debug("Save authorizationCode '{}' of OauthClient '{}'", authCode, clientDetails);
        oauthCode = this.saveAuthorizationCode(username, authCode, clientDetails);
        return oauthCode;
    }
}
