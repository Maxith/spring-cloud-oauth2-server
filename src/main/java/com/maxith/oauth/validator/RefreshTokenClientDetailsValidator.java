package com.maxith.oauth.validator;


import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * 令牌刷新模式验证器
 *
 * @author zhouyou
 * @date 2018/7/20 10:46
 **/
public class RefreshTokenClientDetailsValidator extends AbstractOauthTokenValidator {

    public RefreshTokenClientDetailsValidator(MyOAuthTokenRequest tokenRequest, IOauthService iOauthService) {
        super(tokenRequest, iOauthService);
    }

    /**
     * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=refresh_token&refresh_token=b36f4978-a172-4aa8-af89-60f58abe3ba1
     *
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
     */
    @Override
    protected OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {

        //validate grant_type
        final String grantType = grantType();
        if (invalidateGrantType(clientDetails, grantType)) {
            return invalidGrantTypeResponse(grantType);
        }

        //validate client_secret
        if (invalidateClientSecret(clientDetails)) {
            return invalidClientSecretResponse();
        }

        //validate refresh_token
        final String refreshToken = tokenRequest.getRefreshToken();
        AccessToken accessToken = iOauthService.loadAccessTokenByRefreshToken(refreshToken, oauthRequest.getClientId());
        if (accessToken == null || accessToken.refreshTokenExpired()) {
            logger.debug("Invalid refresh_token: '{}'", refreshToken);
            return invalidRefreshTokenResponse(refreshToken);
        }

        return null;
    }

    private OAuthResponse invalidRefreshTokenResponse(String refreshToken) throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid refresh_token: " + refreshToken)
                .buildJSONMessage();
    }
}
