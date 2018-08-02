package com.maxith.oauth.handler.token;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.oauth.validator.AuthorizationCodeClientDetailsValidator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletResponse;

/**
 * 认证code方式获取认证令牌 处理器
 * <p>
 * 认证方式:authorization_code
 *
 * @author zhouyou
 * @date 2018/7/18 15:24
 **/
public class AuthorizationCodeTokenHandler extends AbstractOAuthTokenHandler {

    public AuthorizationCodeTokenHandler(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        super(iOauthService, tokenRequest, response);
    }

    @Override
    public boolean support(MyOAuthTokenRequest tokenRequest) {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.AUTHORIZATION_CODE.toString().equalsIgnoreCase(grantType);
    }

    /**
     * e.g.
     * /oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=redirect_uri
     */
    @Override
    public void handleAfterValidation() throws OAuthSystemException {

        //response token, always new
        responseToken();

        //remove code lastly
        removeCode();
    }

    private void removeCode() {
        final String code = tokenRequest.getCode();
        final boolean result = iOauthService.removeOauthCode(code, clientDetails());
        logger.debug("Remove code: {} result: {}", code, result);
    }

    private void responseToken() throws OAuthSystemException {
        AccessToken accessToken = iOauthService.retrieveAuthorizationCodeAccessToken(clientDetails(), tokenRequest.getCode());
        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        logger.debug("'authorization_code' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse(response, tokenResponse);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new AuthorizationCodeClientDetailsValidator(tokenRequest, iOauthService);
    }

}
