package com.maxith.oauth.validator;


import com.maxith.common.tools.SpringUtil;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import com.maxith.system.entity.SystemUser;
import com.maxith.system.service.ISystemUserService;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Set;

/**
 * 用户名密码模式验证器
 *
 * @author zhouyou
 * @date 2018/7/19 16:29
 **/
public class PasswordClientDetailsValidator extends AbstractOauthTokenValidator {

    public PasswordClientDetailsValidator(MyOAuthTokenRequest oauthRequest, IOauthService iOauthService) {
        super(oauthRequest, iOauthService);
    }

    /**
     * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=password&scope=read,write&username=mobile&password=mobile
     *
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
     */
    @Override
    protected OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {

        //验证客户端是否支持该模式
        final String grantType = grantType();
        if (invalidateGrantType(clientDetails, grantType)) {
            return invalidGrantTypeResponse(grantType);
        }

        //验证客户端密钥
        if (invalidateClientSecret(clientDetails)) {
            return invalidClientSecretResponse();
        }

        //验证应用范围
        if (invalidateScope(clientDetails)) {
            return invalidScopeResponse();
        }

        //验证用户名密码
        if (invalidUsernamePassword()) {
            return invalidUsernamePasswordResponse();
        }

        return null;
    }

    /**
     * 验证用户名密码登陆
     * @return
     */
    protected boolean invalidUsernamePassword() {
        final String username = tokenRequest.getUsername();
        final String password = tokenRequest.getPassword();
        try {
            ISystemUserService iSystemUserService = SpringUtil.getBean(ISystemUserService.class);
            SystemUser user = iSystemUserService.selectUserByUsernameAndPassword(username,password);
            if (user == null){
                throw new Exception();
            }
        } catch (Exception e) {
            logger.debug("Login failed by username: " + username, e);
            return true;
        }
        return false;
    }

    /**
     * 验证用户名密码错误响应
     * @return
     * @throws OAuthSystemException
     */
    protected OAuthResponse invalidUsernamePasswordResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("用户名或密码错误!")
                .buildJSONMessage();
        response.addHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
        return response;
    }
}
