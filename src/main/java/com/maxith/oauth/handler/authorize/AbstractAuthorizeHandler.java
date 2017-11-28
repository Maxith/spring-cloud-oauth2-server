package com.maxith.oauth.handler.authorize;


import com.maxith.common.ApplicatonConstants;
import com.maxith.common.tools.WebRequestUtils;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.handler.OAuthHandler;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import com.maxith.common.tools.WebUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抽象认证响应器
 */
public abstract class AbstractAuthorizeHandler extends OAuthHandler {

    protected MyOAuthAuthzRequest oauthRequest;
    protected HttpServletResponse response;

    protected final String loginView;
    protected final String approvalView;
    protected final String approvalKeyword;

    public AbstractAuthorizeHandler(MyOAuthAuthzRequest oauthRequest, HttpServletResponse response, String loginView, String approvalView, String approvalKeyword) {
        this.oauthRequest = oauthRequest;
        this.response = response;
        this.loginView = loginView;
        this.approvalView = approvalView;
        this.approvalKeyword = approvalKeyword;
    }

    protected abstract AbstractClientDetailsValidator getValidator();

    protected String clientId() {
        return oauthRequest.getClientId();
    }
    protected String username(){
        return oauthRequest.getSystemUser().getGid();
    }

    protected boolean validateFailed() throws OAuthSystemException {
        AbstractClientDetailsValidator validator = getValidator();
        logger.debug("Use [{}] validate client: {}", validator, oauthRequest.getClientId());

        final OAuthResponse oAuthResponse = validator.validate();
        return checkAndResponseValidateFailed(oAuthResponse);
    }

    protected boolean checkAndResponseValidateFailed(OAuthResponse oAuthResponse) {
        if (oAuthResponse != null) {
            logger.debug("Validate OAuthAuthzRequest(client_id={}) failed", oauthRequest.getClientId());
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
            return true;
        }
        return false;
    }

    protected boolean isUserAuthenticated() {
        final Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    protected boolean isPost() {
        return RequestMethod.POST.name().equalsIgnoreCase(oauthRequest.request().getMethod());
    }

    protected boolean isNeedUserLogin() {
        return !isUserAuthenticated() && !isPost();
    }

    protected boolean goLogin() throws ServletException, IOException {
        if (isNeedUserLogin()) {
            //go to login
            logger.debug("Forward to Oauth login by client_id '{}'", oauthRequest.getClientId());
            final HttpServletRequest request = oauthRequest.request();
            request.getRequestDispatcher(loginView)
                    .forward(request, response);
            return true;
        }
        return false;
    }

    protected boolean isApprovaled(){
        final String accessToken = iOauthService.queryAccessToken(username(),clientDetails());
        final boolean isApprovale = Boolean.parseBoolean(oauthRequest.getParam(ApplicatonConstants.IS_APPROVAL));
        return accessToken != null || isApprovale;
    }

    protected boolean goApproval() throws ServletException, IOException {
        if (!isApprovaled() && clientDetails().getTrusted() == 1) {
            //go to approval
            logger.debug("Go to oauth_approval, clientId: '{}'", clientDetails().getClientId());
            final HttpServletRequest request = oauthRequest.request();
            request.getRequestDispatcher(approvalView)
                    .forward(request, response);
            return true;
        }
        return false;
    }

    protected boolean goErrorCallback() throws ServletException, IOException, OAuthSystemException {
        // 检查 是否登录 是否授权 不通过则返回授权失败
        if ( !isUserAuthenticated() || !isApprovaled()){
            final OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OAuthError.CodeResponse.ACCESS_DENIED)
                    .setErrorDescription("User denied access")
                    .location(clientDetails().getRedirectUri())
                    .setState(oauthRequest.getState())
                    .buildQueryMessage();
            logger.debug("'ACCESS_DENIED' response: {}", oAuthResponse);

            WebUtils.writeOAuthQueryResponse(response, oAuthResponse);

            //user logout when deny
            final Subject subject = SecurityUtils.getSubject();
            subject.logout();
            logger.debug("After 'ACCESS_DENIED' call logout. user: {}", subject.getPrincipal());
            return true;
        }
        return false;
    }

    public void handle() throws OAuthSystemException, ServletException, IOException {
        //validate
        if (validateFailed()) {
            return;
        }

        //Check need usr login
        if (goLogin()) {
            return;
        }

        // Check approval
        if (goApproval()) {
            return;
        }

        //检查是否授权成功
        if(goErrorCallback()){
            return;
        }

        //handle response
        handleResponse();
    }

    //Handle custom response content
    protected abstract void handleResponse() throws OAuthSystemException, IOException;
}
