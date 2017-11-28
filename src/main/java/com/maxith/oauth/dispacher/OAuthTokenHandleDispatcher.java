
package com.maxith.oauth.dispacher;

import com.maxith.oauth.handler.token.AuthorizationCodeTokenHandler;
import com.maxith.oauth.handler.token.ClientCredentialsTokenHandler;
import com.maxith.oauth.handler.token.PasswordTokenHandler;
import com.maxith.oauth.handler.token.RefreshTokenHandler;
import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import com.maxith.common.entity.BaseComponent;
import com.maxith.oauth.handler.OAuthTokenHandler;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局认证处理分发器
 */
public class OAuthTokenHandleDispatcher extends BaseComponent {

    private List<OAuthTokenHandler> handlers = new ArrayList();

    private final MyOAuthTokenRequest tokenRequest;
    private final HttpServletResponse response;

    public OAuthTokenHandleDispatcher(MyOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        this.tokenRequest = tokenRequest;
        this.response = response;

        initialHandlers();
    }

    private void initialHandlers() {
        handlers.add(new AuthorizationCodeTokenHandler());
        handlers.add(new PasswordTokenHandler());
        handlers.add(new RefreshTokenHandler());

        handlers.add(new ClientCredentialsTokenHandler());

        logger.debug("Initialed '{}' OAuthTokenHandler(s): {}", handlers.size(), handlers);
    }


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
