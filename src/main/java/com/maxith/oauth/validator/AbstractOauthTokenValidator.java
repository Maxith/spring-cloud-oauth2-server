package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Set;

/**
 * 全局抽象token验证器
 *
 * @author zhouyou
 * @date 2018/7/19 16:13
 **/
public abstract class AbstractOauthTokenValidator extends AbstractClientDetailsValidator {

    protected MyOAuthTokenRequest tokenRequest;

    protected AbstractOauthTokenValidator(MyOAuthTokenRequest tokenRequest, IOauthService iOauthService) {
        super(tokenRequest, iOauthService);
        this.tokenRequest = tokenRequest;
    }

    /**
     * 获取认证类型
     * @return
     */
    protected String grantType() {
        return tokenRequest.getGrantType();
    }

    /**
     * 认证失败响应
     * @param grantType
     * @return
     * @throws OAuthSystemException
     */
    protected OAuthResponse invalidGrantTypeResponse(String grantType) throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid grant_type '" + grantType + "'")
                .buildJSONMessage();
    }
}
