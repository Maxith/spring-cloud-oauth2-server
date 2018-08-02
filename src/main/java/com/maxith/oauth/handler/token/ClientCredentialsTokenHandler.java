package com.maxith.oauth.handler.token;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.oauth.validator.ClientCredentialsClientDetailsValidator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletResponse;

/**
 * 受信任的客户端直接获取认证令牌 处理器
 * <p>
 * 认证方式:client_credentials
 *
 * @author zhouyou
 * @date 2018/7/18 15:32
 **/
public class ClientCredentialsTokenHandler extends AbstractOAuthTokenHandler {

    public ClientCredentialsTokenHandler(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        super(iOauthService, tokenRequest, response);
    }

    @Override
    public boolean support(MyOAuthTokenRequest tokenRequest) {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.CLIENT_CREDENTIALS.toString().equalsIgnoreCase(grantType);
    }

    /**
     * e.g.
     * /oauth/token?client_id=credentials-client&client_secret=credentials-secret&grant_type=client_credentials&scope=read write
     * <p/>
     * 直接返回已存在的令牌
     * 如果令牌已过期,则返回新的令牌
     * <p/>
     * {"access_token":"38187f32-e4fb-4650-8e4a-99903b66f20e","token_type":"bearer","expires_in":7}
     */
    @Override
    public void handleAfterValidation() throws OAuthSystemException {

        AccessToken accessToken = iOauthService.retrieveClientCredentialsAccessToken(clientDetails(),
                tokenRequest.getScopes());
        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        logger.debug("'client_credentials' response: {}", tokenResponse);

        WebUtils.writeOAuthJsonResponse(response, tokenResponse);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new ClientCredentialsClientDetailsValidator(tokenRequest, iOauthService);
    }
}
