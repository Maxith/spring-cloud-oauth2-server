package com.maxith.oauth.handler.authorize;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
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
 */
public class TokenAuthorizeHandler extends AbstractAuthorizeHandler{

    public TokenAuthorizeHandler(MyOAuthAuthzRequest oauthRequest, HttpServletResponse response, String loginView, String approvalView, String approvalKeyword) {
        super(oauthRequest, response, loginView, approvalView, approvalKeyword);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new TokenClientDetailsValidator(oauthRequest, false);
    }

    /*
    *  response token
    *
    *  If it is the first loggerged or first approval , always return newly AccessToken
    *  Always exclude refresh_token
    *
    * */
    @Override
    protected void handleResponse() throws OAuthSystemException, IOException {

        if (forceNewAccessToken()) {
            forceTokenResponse();
        } else {
            AccessToken accessToken = iOauthService.retrieveAccessToken(username(),clientDetails(), oauthRequest.getScopes(), false);

            if (accessToken.tokenExpired()) {
                expiredTokenResponse(accessToken);
            } else {
                normalTokenResponse(accessToken);
            }
        }
    }

    private void forceTokenResponse() throws OAuthSystemException {
        AccessToken accessToken = iOauthService.retrieveNewAccessToken(username(),clientDetails(), oauthRequest.getScopes());
        normalTokenResponse(accessToken);
    }

    private void normalTokenResponse(AccessToken accessToken) throws OAuthSystemException {

        final OAuthResponse oAuthResponse = createTokenResponse(accessToken, true);
        logger.debug("'token' response: {}", oAuthResponse);

        WebUtils.writeOAuthQueryResponse(response, oAuthResponse);
    }

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
