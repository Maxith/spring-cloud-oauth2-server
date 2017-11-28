package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * code 与 客户端 匹配验证器
 */
public class CodeClientDetailsValidator extends AbstractClientDetailsValidator {

    public CodeClientDetailsValidator(OAuthAuthzRequest oauthRequest) {
        super(oauthRequest);
    }

    /*
    *  grant_type="authorization_code"
    *  ?response_type=code&scope=read,write&client_id=[client_id]&redirect_uri=[redirect_uri]&state=[state]
    * */
    @Override
    public OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {
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

        //validate state
        final String state = getState();
        if (StringUtils.isEmpty(state)) {
            logger.debug("Invalid 'state', it is required, but it is empty");
            return invalidStateResponse();
        }

        return null;
    }

    private String getState() {
        return ((OAuthAuthzRequest) oauthRequest).getState();
    }

    private OAuthResponse invalidStateResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                .setErrorDescription("Parameter 'state'  is required")
                .buildJSONMessage();
    }


}
