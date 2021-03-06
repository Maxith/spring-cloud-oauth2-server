package com.maxith.oauth.validator;


import com.maxith.common.entity.BaseComponent;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 抽象客户端验证器
 *
 * @author zhouyou
 * @date 2018/7/19 16:11
 **/
public abstract class AbstractClientDetailsValidator extends BaseComponent {

    private OauthClient clientDetails;

    protected IOauthService iOauthService;

    protected OAuthRequest oauthRequest;

    protected AbstractClientDetailsValidator(OAuthRequest oauthRequest, IOauthService iOauthService) {
        this.oauthRequest = oauthRequest;
        this.iOauthService = iOauthService;
    }

    /**
     * 自我验证
     *
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
     */
    protected abstract OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException;

    protected OauthClient clientDetails() {
        if (clientDetails == null) {
            clientDetails = iOauthService.loadOauthClient(oauthRequest.getClientId());
        }
        return clientDetails;
    }

    /**
     * 验证应用范围
     *
     * @param clientDetails
     * @return
     */
    protected boolean invalidateScope(OauthClient clientDetails) {
        final Set<String> scopes = oauthRequest.getScopes();
        return scopes.isEmpty() || excludeScopes(scopes, clientDetails);
    }

    /**
     * 验证客户端回调
     *
     * @param clientDetails
     * @return
     */
    protected boolean invalidateRedirectURI(OauthClient clientDetails) {
        final String redirectURI = oauthRequest.getRedirectURI();
        if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
            logger.debug("Invalid redirect_uri '{}', client_id = '{}'", redirectURI, clientDetails.getClientId());
            return true;
        }
        return false;
    }

    /**
     * 验证是否支持认证类型
     *
     * @param clientDetails
     * @param grantType
     * @return
     */
    protected boolean invalidateGrantType(OauthClient clientDetails, String grantType) {
        if (!clientDetails.getGrantTypes().contains(grantType)) {
            logger.debug("Invalid grant_type '{}', client_id = '{}'", grantType, clientDetails.getClientId());
            return true;
        }
        return false;
    }

    /**
     * 验证客户端密钥
     *
     * @param clientDetails
     * @return
     */
    protected boolean invalidateClientSecret(OauthClient clientDetails) {
        final String clientSecret = oauthRequest.getClientSecret();
        if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
            logger.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret, clientDetails.getClientId());
            return true;
        }
        return false;
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
        final String clientDetailsScope = clientDetails.getScope();
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
     *
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
