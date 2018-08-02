package com.maxith.oauth.validator;


import com.maxith.oauth.entity.OauthClient;
import com.maxith.oauth.service.IOauthService;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 授权码模式验证器
 *
 * @author zhouyou
 * @date 2018/7/19 16:27
 **/
public class CodeClientDetailsValidator extends AbstractClientDetailsValidator {

    public CodeClientDetailsValidator(OAuthAuthzRequest oauthRequest, IOauthService iOauthService) {
        super(oauthRequest, iOauthService);
    }

    /**
     * grant_type="authorization_code"
     *  ?response_type=code&scope=read,write&client_id=[client_id]&redirect_uri=[redirect_uri]&state=[state]
     *
     * @param clientDetails
     * @return
     * @throws OAuthSystemException
     */
    @Override
    public OAuthResponse validateSelf(OauthClient clientDetails) throws OAuthSystemException {
        //验证回调
        if (invalidateRedirectURI(clientDetails)) {
            return invalidRedirectUriResponse();
        }

        //验证应用范围
        if (invalidateScope(clientDetails)) {
            return invalidScopeResponse();
        }

        //验证状态
        final String state = getState();
        if (StringUtils.isEmpty(state)) {
            logger.debug("Invalid 'state', it is required, but it is empty");
            return invalidStateResponse();
        }

        return null;
    }

    /**
     * 获取状态
     * @return
     */
    private String getState() {
        return ((OAuthAuthzRequest) oauthRequest).getState();
    }

    /**
     * 验证失败回调
     * @return
     * @throws OAuthSystemException
     */
    private OAuthResponse invalidStateResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                .setErrorDescription("Parameter 'state'  is required")
                .buildJSONMessage();
    }


}
