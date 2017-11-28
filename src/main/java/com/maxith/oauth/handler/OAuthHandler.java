package com.maxith.oauth.handler;

import com.maxith.oauth.entity.AccessToken;
import com.maxith.oauth.service.IOauthService;
import com.maxith.common.entity.BaseComponent;
import com.maxith.common.tools.SpringUtil;
import com.maxith.oauth.entity.OauthClient;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * 抽象全局认证处理器
 *
 */
@Component
public abstract class OAuthHandler extends BaseComponent {

    private OauthClient clientDetails;

    protected abstract String clientId();

    protected IOauthService iOauthService;

    public OAuthHandler() {
        this.iOauthService = SpringUtil.getBean(IOauthService.class);
    }

    protected OauthClient clientDetails() {
        if (clientDetails == null) {
            final String clientId = clientId();
            clientDetails = iOauthService.loadOauthClient(clientId);
            logger.debug("Load OauthClient: {} by clientId: {}", clientDetails, clientId);
        }
        return clientDetails;
    }

    /**
     * 创建令牌响应
     *
     * @param accessToken AccessToken
     * @param queryOrJson True is QueryMessage, false is JSON message
     * @return OAuthResponse
     * @throws OAuthSystemException
     */
    protected OAuthResponse createTokenResponse(AccessToken accessToken, boolean queryOrJson) throws OAuthSystemException {
        final OauthClient tempOauthClient = clientDetails();

        final OAuthASResponse.OAuthTokenResponseBuilder builder = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .location(tempOauthClient.getRedirectUri())
                .setAccessToken(accessToken.getTokenId())
                .setExpiresIn(String.valueOf(accessToken.currentTokenExpiredSeconds()))
                .setTokenType(accessToken.getTokenType());

        final String refreshToken = accessToken.getRefreshToken();
        if (StringUtils.isNotEmpty(refreshToken)) {
            builder.setRefreshToken(refreshToken);
        }

        return queryOrJson ? builder.buildQueryMessage() : builder.buildJSONMessage();
    }
}
