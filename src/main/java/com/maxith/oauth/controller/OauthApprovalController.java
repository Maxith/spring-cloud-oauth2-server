package com.maxith.oauth.controller;

import com.maxith.common.ApplicatonConstants;
import com.maxith.common.entity.BaseComponent;
import com.maxith.common.entity.JsonResult;
import com.maxith.common.tools.WebRequestUtils;
import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;

/**
 * 授权
 * Created by zhouyou on 2017/10/17.
 */
@Controller
@RequestMapping("/approval")
public class OauthApprovalController extends BaseComponent {

    @Resource
    private IOauthService iOauthService;

    /**
     * 授权页面
     *
     * @param response
     * @param client_id
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletResponse response, String client_id) throws OAuthSystemException, ServletException, IOException {
        ModelAndView model = new ModelAndView("approval/index");

        try {
            //获取应用信息
            OauthClient oauthClient = iOauthService.loadOauthClient(client_id);
            if (oauthClient == null) {
                throw OAuthProblemException.error("未找到应用信息");
            }
            //将应用id放入session
            WebRequestUtils.setAttribute4Session(ApplicatonConstants.SESSION_CLIENT_ID, oauthClient.getClientId());

            model.addObject("clientInfo", oauthClient);
            return model;
        } catch (OAuthProblemException e) {
            //exception
            OAuthResponse oAuthResponse = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_FOUND)
                    .location(e.getRedirectUri())
                    .error(e)
                    .buildJSONMessage();
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
        }
        return null;
    }
}
