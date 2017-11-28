package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import java.util.Set;

/**
 * 密码登录客户端验证器
 */
public class PasswordClientDetailsValidator extends AbstractOauthTokenValidator {

    public PasswordClientDetailsValidator(MyOAuthTokenRequest oauthRequest) {
        super(oauthRequest);
    }

    /*
    * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=password&scope=read,write&username=mobile&password=mobile
    * */
    @Override
    protected OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {

        //validate grant_type
        final String grantType = grantType();
        if (invalidateGrantType(clientDetails, grantType)) {
            return invalidGrantTypeResponse(grantType);
        }

        //validate client_secret
        if (invalidateClientSecret(clientDetails)) {
            return invalidClientSecretResponse();
        }

        //validate scope
        if (invalidateScope(clientDetails)) {
            return invalidScopeResponse();
        }

        //validate username,password
        if (invalidUsernamePassword()) {
            return invalidUsernamePasswordResponse();
        }

        return null;
    }

    private boolean invalidateGrantType(OauthClient clientDetails, String grantType) throws OAuthSystemException {
        if (!clientDetails.getGrantTypes().contains(grantType)) {
            logger.debug("Invalid grant_type '{}', client_id = '{}'", grantType, clientDetails.getClientId());
            return true;
        }
        return false;
    }

    private boolean invalidateScope(OauthClient clientDetails) throws OAuthSystemException {
        final Set<String> scopes = oauthRequest.getScopes();
        return scopes.isEmpty() || excludeScopes(scopes, clientDetails);
    }

    private boolean invalidateClientSecret(OauthClient clientDetails) throws OAuthSystemException {
        final String clientSecret = oauthRequest.getClientSecret();
        if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
            logger.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret, clientDetails.getClientId());
            return true;
        }
        return false;
    }


}
