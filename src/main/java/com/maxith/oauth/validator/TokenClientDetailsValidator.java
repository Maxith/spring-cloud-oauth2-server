package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

/**
 * 令牌模式 验证器
 *
 * @author zhouyou
 * @date 2018/7/20 11:25
 **/
public class TokenClientDetailsValidator extends AbstractClientDetailsValidator {

    private final boolean validateClientSecret;

    public TokenClientDetailsValidator(OAuthAuthzRequest oauthRequest, IOauthService iOauthService) {
        this(oauthRequest, iOauthService, true);
    }

    public TokenClientDetailsValidator(OAuthAuthzRequest oauthRequest, IOauthService iOauthService, boolean validateClientSecret) {
        super(oauthRequest, iOauthService);
        this.validateClientSecret = validateClientSecret;
    }

    /**
     * grant_type="implicit"   -> response_type="token"
     * ?response_type=token&scope=read,write&client_id=[client_id]&client_secret=[client_secret]&redirect_uri=[redirect_uri]
     *
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
     */
    @Override
    public OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {

        //validate client_secret
        if (this.validateClientSecret) {
            if (invalidateClientSecret(clientDetails)) {
                return invalidClientSecretResponse();
            }
        }

        //validate redirect_uri
        if (invalidateRedirectURI(clientDetails)) {
            return invalidRedirectUriResponse();
        }

        //validate scope
        if (invalidateScope(clientDetails)) {
            return invalidScopeResponse();
        }
        return null;
    }
}
