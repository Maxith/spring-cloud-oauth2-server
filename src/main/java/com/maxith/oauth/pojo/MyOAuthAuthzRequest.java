package com.maxith.oauth.pojo;

import com.maxith.common.ApplicatonConstants;
import com.maxith.common.tools.WebUtils;
import com.maxith.system.entity.SystemUser;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.servlet.http.HttpServletRequest;

/**
 *  自定义OAuth协议认证响应对象
 *  @author zhouyou
 *  @date 2018/7/18 10:02
 **/
public class MyOAuthAuthzRequest extends OAuthAuthzRequest {

    public MyOAuthAuthzRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        super(request);
    }

    /**
     * 判断响应的类型是否为CODE
     * @return
     */
    public boolean isCode() {
        return ResponseType.CODE.name().equalsIgnoreCase(this.getResponseType());
    }

    /**
     * 判断响应的类型是否为TOKEN
     * @return
     */
    public boolean isToken() {
        return ResponseType.TOKEN.name().equalsIgnoreCase(this.getResponseType());
    }

    /**
     * 获取 request 对象
     * @return
     */
    public HttpServletRequest request() {
        return this.request;
    }

    /**
     * 获取系统用户
     * @return
     */
    public SystemUser getSystemUser() {
        return (SystemUser) WebUtils.getAttributeFromSession(ApplicatonConstants.SESSION_USER);
    }
}
