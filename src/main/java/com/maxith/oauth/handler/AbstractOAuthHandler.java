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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 *  抽象统一处理器
 *  @author zhouyou
 *  @date 2018/7/18 14:07
 **/
public abstract class AbstractOAuthHandler extends BaseComponent {

    /**
     * 客户端对象
     */
    private OauthClient clientDetails;

    /**
     * 获取客户端id
     * @return
     */
    protected abstract String clientId();

    /**
     * 数据层服务
     */
    protected IOauthService iOauthService;

    /**
     * 获取客户端对象
     * @return
     */
    protected OauthClient clientDetails() {
        if (clientDetails == null || iOauthService != null) {
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
     * @param queryOrJson True是查询消息,False是json消息
     * @return OAuthResponse
     * @throws OAuthSystemException
     */
    protected OAuthResponse createTokenResponse(AccessToken accessToken, boolean queryOrJson) throws OAuthSystemException {
        final OauthClient tempOauthClient = clientDetails();

        //构建令牌响应
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

    /**
     * 构造方法
     * @param iOauthService
     */
    public AbstractOAuthHandler(IOauthService iOauthService) {
        this.iOauthService = iOauthService;
    }
}
