package com.maxith.oauth.handler.authorize;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.oauth.properties.OAuthProperties;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.oauth.validator.TokenClientDetailsValidator;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一令牌认证处理器
 *
 * @author zhouyou
 * @date 2018/7/18 14:05
 **/
public class TokenAuthorizeHandler extends AbstractAuthorizeHandler {

    public TokenAuthorizeHandler(IOauthService iOauthService, MyOAuthAuthzRequest oauthRequest, HttpServletResponse response, OAuthProperties oAuthProperties) {
        super(iOauthService, oauthRequest, response, oAuthProperties);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new TokenClientDetailsValidator(oauthRequest, iOauthService, false);
    }

    @Override
    protected void handleResponse() throws OAuthSystemException {
        if (forceNewAccessToken()) {
            forceTokenResponse();
        } else {
            AccessToken accessToken = iOauthService.retrieveAccessToken(username(), clientDetails(), oauthRequest.getScopes(), false);

            if (accessToken.tokenExpired()) {
                expiredTokenResponse(accessToken);
            } else {
                normalTokenResponse(accessToken);
            }
        }
    }

    /**
     * 新令牌响应
     *
     * @throws OAuthSystemException
     */
    private void forceTokenResponse() throws OAuthSystemException {
        AccessToken accessToken = iOauthService.retrieveNewAccessToken(username(), clientDetails(), oauthRequest.getScopes());
        normalTokenResponse(accessToken);
    }

    /**
     * 普通响应
     *
     * @param accessToken
     * @throws OAuthSystemException
     */
    private void normalTokenResponse(AccessToken accessToken) throws OAuthSystemException {

        final OAuthResponse oAuthResponse = createTokenResponse(accessToken, true);
        logger.debug("'token' response: {}", oAuthResponse);

        WebUtils.writeOAuthQueryResponse(response, oAuthResponse);
    }

    /**
     * 失效的响应
     *
     * @param accessToken
     * @throws OAuthSystemException
     */
    private void expiredTokenResponse(AccessToken accessToken) throws OAuthSystemException {
        final OauthClient clientDetails = clientDetails();
        logger.debug("AccessToken {} is expired", accessToken);

        final OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                .setErrorDescription("access_token '" + accessToken.getTokenId() + "' expired")
                .setErrorUri(clientDetails.getRedirectUri())
                .buildJSONMessage();

        WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
    }

    private boolean forceNewAccessToken() {
        final OauthClient clientDetails = clientDetails();
        if (clientDetails.getTrusted() == 1) {
            return false;
        } else {
            return false;
        }
    }
}
