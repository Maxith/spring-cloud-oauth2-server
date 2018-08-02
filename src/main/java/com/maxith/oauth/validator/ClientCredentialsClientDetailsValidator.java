package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import java.util.Set;

/**
 * 信任的客户端验证器
 *
 * @author zhouyou
 * @date 2018/7/19 16:21
 **/
public class ClientCredentialsClientDetailsValidator extends AbstractOauthTokenValidator {

    public ClientCredentialsClientDetailsValidator(MyOAuthTokenRequest oauthRequest, IOauthService iOauthService) {
        super(oauthRequest, iOauthService);
    }

    /**
     * /oauth/token?client_id=credentials-client&client_secret=credentials-secret&grant_type=client_credentials&scope=read write
     *
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
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

        //验证应用范围
        if (invalidateScope(clientDetails)) {
            return invalidScopeResponse();
        }

        return null;
    }


}
