package com.maxith.oauth.handler.token;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.oauth.validator.RefreshTokenClientDetailsValidator;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletResponse;

/**
 * 使用刷新令牌直接获取认证令牌 处理器
 * <p>
 * 认证方式:refresh_token
 *
 * @author zhouyou
 * @date 2018/7/18 15:43
 **/
public class RefreshTokenHandler extends AbstractOAuthTokenHandler {

    public RefreshTokenHandler(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        super(iOauthService, tokenRequest, response);
    }

    @Override
    public boolean support(MyOAuthTokenRequest tokenRequest) {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.REFRESH_TOKEN.toString().equalsIgnoreCase(grantType);
    }

    /**
     * e.g.
     * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=refresh_token&refresh_token=b36f4978-a172-4aa8-af89-60f58abe3ba1
     *
     * @throws OAuthProblemException
     */
    @Override
    public void handleAfterValidation() throws OAuthSystemException {

        final String refreshToken = tokenRequest.getRefreshToken();
        AccessToken accessToken = iOauthService.changeAccessTokenByRefreshToken(refreshToken, tokenRequest.getClientId());

        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        logger.debug("'refresh_token' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse(response, tokenResponse);

    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new RefreshTokenClientDetailsValidator(tokenRequest, iOauthService);
    }

}
