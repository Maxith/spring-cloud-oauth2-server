package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.entity.OauthCode;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * 客户端授权码验证器
 *
 * @author zhouyou
 * @date 2018/7/19 16:17
 **/
public class AuthorizationCodeClientDetailsValidator extends AbstractOauthTokenValidator {

    public AuthorizationCodeClientDetailsValidator(MyOAuthTokenRequest oauthRequest, IOauthService iOauthService) {
        super(oauthRequest, iOauthService);
    }

    /**
     * /oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=redirect_uri
     */
    @Override
    protected OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {

        //验证客户端是否支持该类型
        final String grantType = grantType();
        if (invalidateGrantType(clientDetails,grantType)) {
            return invalidGrantTypeResponse(grantType);
        }

        //验证客户端密钥
        if (invalidateClientSecret(clientDetails)) {
            return invalidClientSecretResponse();
        }

        //验证回调地址
        if (invalidateRedirectURI(clientDetails)) {
            return invalidRedirectUriResponse();
        }

        //验证授权码
        String code = getCode();
        OauthCode oauthCode = iOauthService.loadOauthCode(code, clientDetails());
        if (oauthCode == null) {
            logger.debug("Invalid code '{}', client_id = '{}'", code, clientDetails.getClientId());
            return invalidCodeResponse(code);
        }

        return null;
    }

    /**
     * 授权码验证失败回调
     *
     * @param code
     * @return
     * @throws OAuthSystemException
     */
    private OAuthResponse invalidCodeResponse(String code) throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid code '" + code + "'")
                .buildJSONMessage();
    }

    /**
     * 获取授权码
     *
     * @return
     */
    private String getCode() {
        return ((MyOAuthTokenRequest) oauthRequest).getCode();
    }
}
