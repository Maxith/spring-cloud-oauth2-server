package com.maxith.oauth.handler.authorize;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.oauth.validator.CodeClientDetailsValidator;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * code 认证处理器
 */
public class CodeAuthorizeHandler extends AbstractAuthorizeHandler {

    public CodeAuthorizeHandler(MyOAuthAuthzRequest oauthRequest, HttpServletResponse response, String loginView, String approvalView, String approvalKeyword) {
        super(oauthRequest, response, loginView, approvalView, approvalKeyword);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new CodeClientDetailsValidator(oauthRequest);
    }


    //response code
    @Override
    protected void handleResponse() throws OAuthSystemException, IOException {
        final OauthClient clientDetails = clientDetails();
        final String authCode = iOauthService.retrieveAuthCode(username() ,clientDetails);

        final OAuthResponse oAuthResponse = OAuthASResponse
                .authorizationResponse(oauthRequest.request(), HttpServletResponse.SC_OK)
                .location(clientDetails.getRedirectUri())
                .setCode(authCode)
                .buildQueryMessage();
        logger.debug(" 'code' response: {}", oAuthResponse);

        WebUtils.writeOAuthQueryResponse(response, oAuthResponse);
    }
}
