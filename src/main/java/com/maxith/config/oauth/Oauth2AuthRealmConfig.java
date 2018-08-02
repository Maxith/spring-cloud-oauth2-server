package com.maxith.config.oauth;


import com.maxith.common.ApplicatonConstants;
import com.maxith.common.tools.CryptUtils;
import com.maxith.common.tools.WebUtils;
import com.maxith.system.entity.SystemUser;
import com.maxith.system.service.ISystemUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限登陆器
 *
 * @author zhouyou
 * @date 2018/7/18 10:47
 **/
@Configuration
public class Oauth2AuthRealmConfig extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oauth2AuthRealmConfig.class);

    @Autowired
    private ISystemUserService iSystemUserService;

    public Oauth2AuthRealmConfig() {
        this.setCredentialsMatcher(new OAuth2CredentialsMatcher());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //null usernames are invalid
        if (principalCollection == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principalCollection);

        SystemUser user = iSystemUserService.selectSystemUserByUsername(username);
        if (user == null) {
            final String message = "没找到用户名为 [" + username + "] 的用户!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(message);
            }

            // Rethrow any SQL errors as an authorization exception
            throw new AuthorizationException(message);
        }
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        password = CryptUtils.generateDigestWithMD5(password);

        SystemUser user = iSystemUserService.selectUserByUsernameAndPassword(username, password);

        if (user == null) {
            WebUtils.setAttribute4Request(ApplicatonConstants.LOGIN_ERROR,
                    ApplicatonConstants.LOGIN_ERRER_MSG);
            throw new AuthenticationException(ApplicatonConstants.LOGIN_ERRER_MSG);
        }

        //将用户放入session
        SystemUser tempUser = (SystemUser) WebUtils.getAttributeFromSession(ApplicatonConstants.SESSION_USER);
        if (tempUser == null || user.getGid().equals(tempUser.getGid())) {
            WebUtils.setAttribute4Session(ApplicatonConstants.SESSION_USER, user);
        }

        iSystemUserService.updateLoginTime(user.getGid());
        return new SimpleAuthenticationInfo(user, password, this.getName());
    }
}
