package com.maxith.oauth.mapper;


import com.maxith.oauth.entity.OauthClient;

public interface OauthClientMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OauthClient record);

    int insertSelective(OauthClient record);

    OauthClient selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OauthClient record);

    int updateByPrimaryKey(OauthClient record);

    OauthClient selectByClientId(String clientId);
}