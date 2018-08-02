package com.maxith.oauth.handler.token;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.oauth.validator.PasswordClientDetailsValidator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletResponse;

/**
 * 使用密码登陆的方式直接获取认证令牌
 * <p>
 * 认证方式:password
 *
 * @author zhouyou
 * @date 2018/7/18 15:40
 **/
public class PasswordTokenHandler extends AbstractOAuthTokenHandler {

    public PasswordTokenHandler(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        super(iOauthService, tokenRequest, response);
    }

    @Override
    public boolean support(MyOAuthTokenRequest tokenRequest) {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.PASSWORD.toString().equalsIgnoreCase(grantType);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new PasswordClientDetailsValidator(tokenRequest, iOauthService);
    }

    /**
     * e.g.
     * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=password&scope=read,write&username=mobile&password=mobile
     * <p/>
     * 直接返回未过期的令牌
     * 如果令牌已过期,则返回新的令牌,包括刷新令牌
     * <p/>
     * {"token_type":"Bearer","expires_in":33090,"refresh_token":"976aeaf7df1ee998f98f15acd1de63ea","access_token":"7811aff100eb7dadec132f45f1c01727"}
     */
    @Override
    public void handleAfterValidation() throws OAuthSystemException {

        AccessToken accessToken = iOauthService.retrievePasswordAccessToken(clientDetails(),
                tokenRequest.getScopes(), tokenRequest.getUsername());
        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        logger.debug("'password' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse(response, tokenResponse);

    }
}
