package com.maxith.oauth.handler.token;


import com.maxith.oauth.handler.OAuthHandler;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.handler.OAuthTokenHandler;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * token 处理器 标准父类
 *
 */
public abstract class AbstractOAuthTokenHandler extends OAuthHandler implements OAuthTokenHandler {

    protected MyOAuthTokenRequest tokenRequest;
    protected HttpServletResponse response;
    protected String clientId() {
        return tokenRequest.getClientId();
    }

    protected abstract AbstractClientDetailsValidator getValidator();
    protected abstract void handleAfterValidation() throws OAuthProblemException, OAuthSystemException;

    protected boolean validateFailed() throws OAuthSystemException {
        AbstractClientDetailsValidator validator = getValidator();
        logger.debug("Use [{}] validate client: {}", validator, tokenRequest.getClientId());

        final OAuthResponse oAuthResponse = validator.validate();
        return checkAndResponseValidateFailed(oAuthResponse);
    }

    protected boolean checkAndResponseValidateFailed(OAuthResponse oAuthResponse) {
        if (oAuthResponse != null) {
            logger.debug("Validate OAuthAuthzRequest(client_id={}) failed", tokenRequest.getClientId());
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
            return true;
        }
        return false;
    }

    @Override
    public final void handle(MyOAuthTokenRequest tokenRequest, HttpServletResponse response) throws OAuthProblemException, OAuthSystemException {
        this.tokenRequest = tokenRequest;
        this.response = response;

        //validate
        if (validateFailed()) {
            return;
        }
        handleAfterValidation();
    }

}
