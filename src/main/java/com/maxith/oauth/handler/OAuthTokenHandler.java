package com.maxith.oauth.handler;


import com.maxith.oauth.pojo.MyOAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletResponse;

/**
 * token处理器接口
 */
public interface OAuthTokenHandler {

    /**
     * 支持
     * @param tokenRequest
     * @return
     * @throws OAuthProblemException
     */
    boolean support(MyOAuthTokenRequest tokenRequest) throws OAuthProblemException;

    /**
     * 处理
     * @param tokenRequest
     * @param response
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    void handle(MyOAuthTokenRequest tokenRequest, HttpServletResponse response) throws OAuthProblemException, OAuthSystemException;

}