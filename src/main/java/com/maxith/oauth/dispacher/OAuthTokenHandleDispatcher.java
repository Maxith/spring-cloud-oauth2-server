
package com.maxith.oauth.dispacher;

import com.maxith.oauth.handler.token.AuthorizationCodeTokenHandler;
import com.maxith.oauth.handler.token.ClientCredentialsTokenHandler;
import com.maxith.oauth.handler.token.PasswordTokenHandler;
import com.maxith.oauth.handler.token.RefreshTokenHandler;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.common.entity.BaseComponent;
import com.maxith.oauth.handler.OAuthTokenHandler;
import com.maxith.oauth.service.IOauthService;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局认证处理调度器
 *
 * @author zhouyou
 * @date 2018/7/18 15:54
 **/
public class OAuthTokenHandleDispatcher extends BaseComponent {

    private List<OAuthTokenHandler> handlers = new ArrayList<>();

    private final MyOAuthTokenRequest tokenRequest;
    private final HttpServletResponse response;

    public OAuthTokenHandleDispatcher(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        this.tokenRequest = tokenRequest;
        this.response = response;

        //初始化处理器
        initialHandlers(iOauthService, tokenRequest, response);
    }

    /**
     * 初始化处理器
     *
     * @param iOauthService
     * @param tokenRequest
     * @param response
     */
    private void initialHandlers(IOauthService iOauthService, MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        handlers.add(new AuthorizationCodeTokenHandler(iOauthService, tokenRequest, response));
        handlers.add(new PasswordTokenHandler(iOauthService, tokenRequest, response));
        handlers.add(new RefreshTokenHandler(iOauthService, tokenRequest, response));

        handlers.add(new ClientCredentialsTokenHandler(iOauthService, tokenRequest, response));

        logger.debug("Initialed '{}' OAuthTokenHandler(s): {}", handlers.size(), handlers);
    }

    /**
     * 任务调度
     *
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    public void dispatch() throws OAuthProblemException, OAuthSystemException {
        for (OAuthTokenHandler handler : handlers) {
            if (handler.support(tokenRequest)) {
                logger.debug("Found '{}' handle MyOAuthTokenRequest: {}", handler, tokenRequest);
                handler.handle(tokenRequest, response);
                return;
            }
        }
        throw new IllegalStateException("Not found 'OAuthTokenHandler' to handle MyOAuthTokenRequest: " + tokenRequest);
    }
}
