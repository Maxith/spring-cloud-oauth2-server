package com.maxith.oauth.mapper;


import com.maxith.oauth.entity.OauthClient;

/**
 * 客户端mybatis mapper
 *
 * @author zhouyou
 * @date 2018/7/19 15:59
 **/
public interface OauthClientMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OauthClient record);

    int insertSelective(OauthClient record);

    OauthClient selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OauthClient record);

    int updateByPrimaryKey(OauthClient record);

    OauthClient selectByClientId(String clientId);
}