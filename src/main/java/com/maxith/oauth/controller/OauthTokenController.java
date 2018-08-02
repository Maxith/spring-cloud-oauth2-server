package com.maxith.oauth.controller;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.dispacher.OAuthTokenHandleDispatcher;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 令牌 控制器
 *
 * @author zhouyou
 * @date 2018/7/18 15:59
 **/
@Controller
@RequestMapping("/")
public class OauthTokenController {

    @Resource
    private IOauthService iOauthService;

    /**
     * 可处理的类型如下:
     * <p/>
     * grant_type=authorization_code
     * grant_type=password
     * grant_type=refresh_token
     * grant_type=client_credentials
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws OAuthSystemException
     */
    @RequestMapping("token")
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException {
        try {
            MyOAuthTokenRequest tokenRequest = new MyOAuthTokenRequest(request);

            OAuthTokenHandleDispatcher tokenHandleDispatcher = new OAuthTokenHandleDispatcher(iOauthService, tokenRequest, response);
            tokenHandleDispatcher.dispatch();

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
}
