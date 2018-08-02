package com.maxith.oauth.handler.authorize;


import com.maxith.common.ApplicatonConstants;
import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.handler.AbstractOAuthHandler;
import com.maxith.oauth.pojo.MyOAuthAuthzRequest;
import com.maxith.oauth.properties.OAuthProperties;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抽象认证响应器
 *
 * @author zhouyou
 * @date 2018/7/18 14:06
 **/
public abstract class AbstractAuthorizeHandler extends AbstractOAuthHandler {

    protected MyOAuthAuthzRequest oauthRequest;
    protected HttpServletResponse response;

    protected OAuthProperties oAuthProperties;

    /**
     * 获取验证器
     *
     * @return
     */
    protected abstract AbstractClientDetailsValidator getValidator();

    /**
     * 处理自定义响应内容
     *
     * @throws OAuthSystemException
     * @throws IOException
     */
    protected abstract void handleResponse() throws OAuthSystemException, IOException;

    @Override
    protected String clientId() {
        return oauthRequest.getClientId();
    }

    protected String username() {
        if (oauthRequest.getSystemUser() != null){
            return oauthRequest.getSystemUser().getGid();
        }
        return null;
    }

    /**
     * 字段验证
     *
     * @return
     * @throws OAuthSystemException
     */
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

    /**
     * 用户是否已登陆
     *
     * @return
     */
    protected boolean isUserAuthenticated() {
        final Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    /**
     * 是否post请求
     *
     * @return
     */
    protected boolean isPost() {
        return RequestMethod.POST.name().equalsIgnoreCase(oauthRequest.request().getMethod());
    }

    /**
     * 是否需要用户登陆
     *
     * @return
     */
    protected boolean isNeedUserLogin() {
        return !isUserAuthenticated() && !isPost();
    }

    /**
     * 重定向到登陆页面
     *
     * @return
     * @throws ServletException
     * @throws IOException
     */
    protected boolean goLogin() throws ServletException, IOException {
        if (isNeedUserLogin()) {
            //go to login
            logger.debug("Forward to Oauth login by client_id '{}'", oauthRequest.getClientId());
            final HttpServletRequest request = oauthRequest.request();

            request.getRequestDispatcher(oAuthProperties.getLoginView())
                    .forward(request, response);
            return true;
        }
        return false;
    }

    /**
     * 是否已授权
     *
     * @return
     */
    protected boolean isApprovaled() {
        final String accessToken = iOauthService.queryAccessToken(username(), clientDetails());
        final boolean isApprovale = Boolean.parseBoolean(oauthRequest.getParam(ApplicatonConstants.IS_APPROVAL));
        return accessToken != null || isApprovale;
    }

    /**
     * 重定向到授权页面
     *
     * @return
     * @throws ServletException
     * @throws IOException
     */
    protected boolean goApproval() throws ServletException, IOException {
        if (!isApprovaled() && clientDetails().getTrusted() == 1) {
            //go to approval
            logger.debug("Go to oauth_approval, clientId: '{}'", clientDetails().getClientId());
            final HttpServletRequest request = oauthRequest.request();
            request.getRequestDispatcher(oAuthProperties.getApprovalView())
                    .forward(request, response);
            return true;
        }
        return false;
    }

    /**
     * 登陆 或 授权 失败,重定向到客户端的回掉地址,并携带错误消息
     *
     * @return
     * @throws OAuthSystemException
     */
    protected boolean goErrorCallback() throws OAuthSystemException {
        // 检查 是否登录 是否授权 不通过则返回授权失败
        if (!isUserAuthenticated() || !isApprovaled()) {
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

    /**
     * 处理
     *
     * @throws OAuthSystemException
     * @throws ServletException
     * @throws IOException
     */
    public void handle() throws OAuthSystemException, ServletException, IOException {
        //字段验证
        if (validateFailed()) {
            return;
        }

        //检查是否需要登陆
        if (goLogin()) {
            return;
        }

        //检查是否需要授权
        if (goApproval()) {
            return;
        }

        //检查是否授权成功
        if (goErrorCallback()) {
            return;
        }

        //处理响应
        handleResponse();
    }

    public AbstractAuthorizeHandler(IOauthService iOauthService, MyOAuthAuthzRequest oauthRequest,
                                    HttpServletResponse response, OAuthProperties oAuthProperties) {
        super(iOauthService);
        this.oauthRequest = oauthRequest;
        this.response = response;
        this.oAuthProperties = oAuthProperties;
    }
}
