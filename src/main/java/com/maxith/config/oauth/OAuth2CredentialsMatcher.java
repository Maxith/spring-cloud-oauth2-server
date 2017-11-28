package com.maxith.config.oauth;


import com.maxith.common.tools.CryptUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * shiro 认证器
 *
 * @author zhouyou
 */
public class OAuth2CredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //加密传入的密码
        Object tokenCredentials = CryptUtils.generateDigestWithMD5(String.valueOf(usernamePasswordToken.getPassword()));

        Object accountCredentials = this.getCredentials(info);

        return this.equals(tokenCredentials,accountCredentials);
    }
}
