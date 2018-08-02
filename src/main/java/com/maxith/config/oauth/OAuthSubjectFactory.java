package com.maxith.config.oauth;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * OAuth协议实现shiro认证工厂
 *
 * @author zhouyou
 * @date 2018/7/18 10:53
 **/
public class OAuthSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {

        boolean authenticated = context.isAuthenticated();

        if (authenticated) {

            AuthenticationToken token = context.getAuthenticationToken();

            if (token != null && token instanceof OAuthShiroToken) {
                OAuthShiroToken oAuth2Token = (OAuthShiroToken) token;
                if (oAuth2Token.isRememberMe()) {
                    context.setAuthenticated(false);
                }
            }
        }

        return super.createSubject(context);
    }
}
