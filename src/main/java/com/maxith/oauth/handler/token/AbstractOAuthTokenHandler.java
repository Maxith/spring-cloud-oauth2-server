package com.maxith.oauth.handler.token;


import com.maxith.common.tools.WebUtils;
import com.maxith.oauth.handler.AbstractOAuthHandler;
import com.maxith.oauth.handler.OAuthTokenHandler;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.oauth.service.IOauthService;
import com.maxith.oauth.validator.AbstractClientDetailsValidator;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * 抽象 token 处理器
 *
 * @author zhouyou
 * @date 2018/7/18 15:13
 **/
public abstract class AbstractOAuthTokenHandler extends AbstractOAuthHandler implements OAuthTokenHandler {

    protected MyOAuthTokenRequest tokenRequest;
    protected HttpServletResponse response;

    @Override
    protected String clientId() {
        return tokenRequest.getClientId();
    }

    /**
     * 获取验证器
     * @return
     */
    protected abstract AbstractClientDetailsValidator getValidator();

    /**
     * 验证后处理
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    protected abstract void handleAfterValidation() throws OAuthProblemException, OAuthSystemException;

    /**
     * 验证是否通过
     * @return
     * @throws OAuthSystemException
     */
    protected boolean validateFailed() throws OAuthSystemException {
        AbstractClientDetailsValidator validator = getValidator();
        logger.debug("Use [{}] validate client: {}", validator, tokenRequest.getClientId());

        final OAuthResponse oAuthResponse = validator.validate();
        return checkAndResponseValidateFailed(oAuthResponse);
    }

    /**
     * 验证并响应错误信息
     * @param oAuthResponse
     * @return
     */
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
        //validate
        if (validateFailed()) {
            return;
        }
        handleAfterValidation();
    }

    public AbstractOAuthTokenHandler(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest,
                                     HttpServletResponse response) {
        super(iOauthService);
        this.tokenRequest = tokenRequest;
        this.response = response;
    }
}
