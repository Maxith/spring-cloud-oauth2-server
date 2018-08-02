package com.maxith.oauth.controller;


import com.maxith.common.entity.BaseComponent;
import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.handler.authorize.CodeAuthorizeHandler;
import com.maxith.oauth.handler.authorize.TokenAuthorizeHandler;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.oauth.properties.OAuthProperties;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一认证 控制器
 *
 * @author zhouyou
 * @date 2018/7/18 15:46
 **/
@Controller
@RequestMapping("/")
public class OauthAuthorizeController extends BaseComponent {

    @Resource
    private IOauthService iOauthService;

    @Resource
    private OAuthProperties oAuthProperties;

    /**
     * 必须处理grant_type，如下所示:
     * <br>
     * grant_type="authorization_code" -> response_type="code"
     * ?response_type=code&scope=read,write&client_id=[client_id]&redirect_uri=[redirect_uri]&state=[state]
     * <br>
     * grant_type="implicit"   -> response_type="token"
     * ?response_type=token&scope=read,write&client_id=[client_id]&client_secret=[client_secret]&redirect_uri=[redirect_uri]
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, ServletException, IOException {

        try {
            MyOAuthAuthzRequest oAuthAuthzRequest = new MyOAuthAuthzRequest(request);

            if (oAuthAuthzRequest.isCode()) {
                CodeAuthorizeHandler codeAuthorizeHandler = new CodeAuthorizeHandler(iOauthService, oAuthAuthzRequest, response, oAuthProperties);
                logger.debug("Go to  response_type = 'code' handler: {}", codeAuthorizeHandler);
                codeAuthorizeHandler.handle();

            } else if (oAuthAuthzRequest.isToken()) {
                TokenAuthorizeHandler tokenAuthorizeHandler = new TokenAuthorizeHandler(iOauthService, oAuthAuthzRequest, response, oAuthProperties);
                logger.debug("Go to response_type = 'token' handler: {}", tokenAuthorizeHandler);
                tokenAuthorizeHandler.handle();

            } else {
                unsupportResponseType(oAuthAuthzRequest, response);
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
     * @param request
     * @param response
     * @throws OAuthSystemException
     */
    private void unsupportResponseType(MyOAuthAuthzRequest request, HttpServletResponse response) throws OAuthSystemException {
        final String responseType = request.getResponseType();

        logger.debug("Unsupport response_type '{}' by client_id '{}'", responseType, request.getClientId());

        OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE)
                .setErrorDescription("Unsupport response_type '" + responseType + "'")
                .buildJSONMessage();

        WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
    }
}