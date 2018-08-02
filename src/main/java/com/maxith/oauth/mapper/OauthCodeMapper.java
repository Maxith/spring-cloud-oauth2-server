package com.maxith.oauth.mapper;

import com.maxith.oauth.entity.OauthCode;
import org.apache.ibatis.annotations.Param;

/**
 * 认证code mybatis mapper
 *
 * @author zhouyou
 * @date 2018/7/19 16:00
 **/
public interface OauthCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OauthCode record);

    int insertSelective(OauthCode record);

    OauthCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OauthCode record);

    int updateByPrimaryKey(OauthCode record);

    OauthCode findOauthCode(@Param(value = "code") String code, @Param(value = "clientId") String clientId);

    OauthCode findOauthCodeByUsernameClientId(@Param(value = "username") String username, @Param(value = "clientId") String clientId);
}