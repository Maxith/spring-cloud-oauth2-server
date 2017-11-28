package com.maxith.oauth.controller;


import com.maxith.common.ApplicatonConstants;
import com.maxith.oauth.handler.authorize.CodeAuthorizeHandler;
import com.maxith.oauth.handler.authorize.TokenAuthorizeHandler;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.common.entity.BaseComponent;
import com.maxith.common.tools.WebUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一认证入口
 */
@Controller
@RequestMapping("/")
public class OauthAuthorizeController extends BaseComponent implements EnvironmentAware {

    private Environment env;

    /**
     * Must handle the grant_type as follow:
     * grant_type="authorization_code" -> response_type="code"
     * ?response_type=code&scope=read,write&client_id=[client_id]&redirect_uri=[redirect_uri]&state=[state]
     * <p/>
     * grant_type="implicit"   -> response_type="token"
     * ?response_type=token&scope=read,write&client_id=[client_id]&client_secret=[client_secret]&redirect_uri=[redirect_uri]
     * <p/>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, ServletException, IOException {

        String loginView = env.getProperty(ApplicatonConstants.OAUTH_LOGIN_VIEW);
        String approvalView = env.getProperty(ApplicatonConstants.OAUTH_APPROVAL_VIEW);
        String approvalKeyword = env.getProperty(ApplicatonConstants.OAUTH_APPROVAL_KEYWORD);
        try {
            MyOAuthAuthzRequest MyOAuthAuthzRequest = new MyOAuthAuthzRequest(request);

            if (MyOAuthAuthzRequest.isCode()) {
                CodeAuthorizeHandler codeAuthorizeHandler = new CodeAuthorizeHandler(MyOAuthAuthzRequest, response, loginView, approvalView, approvalKeyword);
                logger.debug("Go to  response_type = 'code' handler: {}", codeAuthorizeHandler);
                codeAuthorizeHandler.handle();

            } else if (MyOAuthAuthzRequest.isToken()) {
                TokenAuthorizeHandler tokenAuthorizeHandler = new TokenAuthorizeHandler(MyOAuthAuthzRequest, response, loginView, approvalView, approvalKeyword);
                logger.debug("Go to response_type = 'token' handler: {}", tokenAuthorizeHandler);
                tokenAuthorizeHandler.handle();

            } else {
                unsupportResponseType(MyOAuthAuthzRequest, response);
            }

        } catch (OAuthProblemException e) {
            //exception
            OAuthResponse oAuthResponse = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_FOUND)
                    .location(e.getRedirectUri())
                    .error(e)
                    .buildJSONMessage();
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
        }

    }

    /**
     * 不支持的认证类型
     *
     * @param MyOAuthAuthzRequest
     * @param response
     * @throws OAuthSystemException
     */
    private void unsupportResponseType(MyOAuthAuthzRequest MyOAuthAuthzRequest, HttpServletResponse response) throws OAuthSystemException {
        final String responseType = MyOAuthAuthzRequest.getResponseType();
        logger.debug("Unsupport response_type '{}' by client_id '{}'", responseType, MyOAuthAuthzRequest.getClientId());

        OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE)
                .setErrorDescription("Unsupport response_type '" + responseType + "'")
                .buildJSONMessage();
        WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}