package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import java.util.Set;

/**
 * token 与 客户端 匹配验证器
 */
public class TokenClientDetailsValidator extends AbstractClientDetailsValidator {

    private final boolean validateClientSecret;

    public TokenClientDetailsValidator(OAuthAuthzRequest oauthRequest) {
        this(oauthRequest, true);
    }

    public TokenClientDetailsValidator(OAuthAuthzRequest oauthRequest, boolean validateClientSecret) {
        super(oauthRequest);
        this.validateClientSecret = validateClientSecret;
    }

    /*
     * grant_type="implicit"   -> response_type="token"
     * ?response_type=token&scope=read,write&client_id=[client_id]&client_secret=[client_secret]&redirect_uri=[redirect_uri]
    * */
    @Override
    public OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {

        //validate client_secret
        if (this.validateClientSecret) {
            final String clientSecret = oauthRequest.getClientSecret();
            if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
                return invalidClientSecretResponse();
            }
        }

        //validate redirect_uri
        final String redirectURI = oauthRequest.getRedirectURI();
        if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
            logger.debug("Invalid redirect_uri '{}' by response_type = 'code', client_id = '{}'", redirectURI, clientDetails.getClientId());
            return invalidRedirectUriResponse();
        }

        //validate scope
        final Set<String> scopes = oauthRequest.getScopes();
        if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
            return invalidScopeResponse();
        }
        return null;
    }


}
