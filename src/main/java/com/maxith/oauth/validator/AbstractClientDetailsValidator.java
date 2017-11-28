package com.maxith.oauth.validator;


import com.maxith.oauth.service.IOauthService;
import com.maxith.common.entity.BaseComponent;
import com.maxith.common.tools.SpringUtil;
import com.maxith.oauth.entity.OauthClient;
import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 全局抽象客户端认证器
 */
public abstract class AbstractClientDetailsValidator extends BaseComponent {

    private OauthClient clientDetails;

    protected IOauthService iOauthService = SpringUtil.getBean(IOauthService.class);

    protected OAuthRequest oauthRequest;

    protected AbstractClientDetailsValidator(OAuthRequest oauthRequest) {
        this.oauthRequest = oauthRequest;
    }

    protected abstract OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException;

    protected OauthClient clientDetails() {
        if (clientDetails == null) {
            clientDetails = iOauthService.loadOauthClient(oauthRequest.getClientId());
        }
        return clientDetails;
    }

    protected OAuthResponse invalidClientErrorResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                .setErrorDescription("Invalid client_id '" + oauthRequest.getClientId() + "'")
                .buildJSONMessage();
    }

    protected OAuthResponse invalidRedirectUriResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                .setErrorDescription("Invalid redirect_uri '" + oauthRequest.getRedirectURI() + "'")
                .buildJSONMessage();
    }


    protected OAuthResponse invalidScopeResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_SCOPE)
                .setErrorDescription("Invalid scope '" + oauthRequest.getScopes() + "'")
                .buildJSONMessage();
    }


    protected boolean excludeScopes(Set<String> scopes, OauthClient clientDetails) {
        final String clientDetailsScope = clientDetails.getScope();          //read write
        for (String scope : scopes) {
            if (!clientDetailsScope.contains(scope)) {
                logger.debug("Invalid scope - OauthClient scopes '{}' exclude '{}'", clientDetailsScope, scope);
                return true;
            }
        }
        return false;
    }

    protected OAuthResponse invalidClientSecretResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                .setErrorDescription("Invalid client_secret by client_id '" + oauthRequest.getClientId() + "'")
                .buildJSONMessage();
    }

    /**
     * 验证
     * @return
     * @throws OAuthSystemException
     */
    public final OAuthResponse validate() throws OAuthSystemException {
        final OauthClient details = clientDetails();
        if (details == null) {
            return invalidClientErrorResponse();
        }

        return validateSelf(details);
    }
}
