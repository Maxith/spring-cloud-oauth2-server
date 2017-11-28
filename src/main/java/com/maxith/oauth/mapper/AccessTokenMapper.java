package com.maxith.oauth.mapper;


import com.maxith.oauth.entity.AccessToken;
import org.apache.ibatis.annotations.Param;

public interface AccessTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccessToken record);

    int insertSelective(AccessToken record);

    AccessToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccessToken record);

    int updateByPrimaryKey(AccessToken record);

    AccessToken findAccessToken(@Param(value = "clientId") String clientId, @Param(value = "username") String username, @Param(value = "authenticationId") String authenticationId);

    AccessToken findAccessTokenByRefreshToken(@Param(value = "refreshToken") String refreshToken, @Param(value = "clientId") String clientId);

    AccessToken findAccessTokenWithoutAuthenticationId(@Param(value = "clientId") String clientId, @Param(value = "username") String username);
}