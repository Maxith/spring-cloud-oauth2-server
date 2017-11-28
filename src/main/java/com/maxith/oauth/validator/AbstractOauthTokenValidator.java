package com.maxith.oauth.validator;


import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 全局抽象token验证器
 */
public abstract class AbstractOauthTokenValidator extends AbstractClientDetailsValidator {

    protected MyOAuthTokenRequest tokenRequest;

    protected AbstractOauthTokenValidator(MyOAuthTokenRequest tokenRequest) {
        super(tokenRequest);
        this.tokenRequest = tokenRequest;
    }

    protected String grantType() {
        return tokenRequest.getGrantType();
    }


    protected OAuthResponse invalidGrantTypeResponse(String grantType) throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid grant_type '" + grantType + "'")
                .buildJSONMessage();
    }


    //true is invalided
    protected boolean invalidUsernamePassword() {
        final String username = tokenRequest.getUsername();
        final String password = tokenRequest.getPassword();
        try {
            //FIXME 用户登录
            //SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (Exception e) {
            logger.debug("Login failed by username: " + username, e);
            return true;
        }
        return false;
    }

    protected OAuthResponse invalidUsernamePasswordResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("用户名或密码错误!")
                .buildJSONMessage();
        response.setHeaders(new HashMap(){
            {
                put("Content-type", "text/html;charset=UTF-8");
            }
        });
        return response;
    }

}
