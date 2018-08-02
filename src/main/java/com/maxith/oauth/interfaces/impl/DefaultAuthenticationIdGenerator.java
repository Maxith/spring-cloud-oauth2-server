package com.maxith.oauth.interfaces.impl;


import com.maxith.common.tools.CryptUtils;
import com.maxith.oauth.interfaces.AuthenticationIdGenerator;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认认证id生成器
 * 使用md5加密
 *
 * @author zhouyou
 * @date 2018/7/19 15:54
 **/
@Service
public class DefaultAuthenticationIdGenerator implements AuthenticationIdGenerator {

    @Override
    public String generate(String clientId, String username, String scope) {
        Map<String, String> map = new HashMap<>(3);
        map.put(OAuth.OAUTH_CLIENT_ID, clientId);
        //check it is client only
        if (!clientId.equals(username)) {
            map.put(OAuth.OAUTH_USERNAME, username);
        }
        if (!OAuthUtils.isEmpty(scope)) {
            map.put(OAuth.OAUTH_SCOPE, scope);
        }
        return CryptUtils.generateDigestWithMD5(map.toString());
    }
}
