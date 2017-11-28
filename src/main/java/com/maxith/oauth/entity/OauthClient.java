package com.maxith.oauth.entity;

import org.apache.oltu.oauth2.common.domain.client.BasicClientInfo;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用详情
 */
public class OauthClient extends BasicClientInfo implements Serializable {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 权限范围
     */
    private String scope;
    /**
     * 客户端所支持的授权模式(grant_type),
     * 至少一个, 多个值时使用逗号(,)分隔, 如: password,refresh_token
     */
    private String grantTypes;
    /**
     * access_token 的有效时长, 单位: 秒.
     * 若不填或为空(null)则使用默认值: 12小时
     */
    private Long accessTokenValidity;
    /**
     * refresh_token的 有效时长, 单位: 秒
     * 若不填或为空(null)则使用默认值: 30天
     */
    private Long refreshTokenValidity;
    /**
     * 该 客户端是否为授信任的,
     * 若为信任的,, 则在 grant_type = authorization_code 时将跳过用户同意/授权 步骤
     * 0,不信任,1 信任
     */
    private Byte trusted = 0;
    /**
     * 逻辑删除的标识, 1表示已经删除
     */
    private Byte archived = 0;

    private Date createTime;

    private Date updateTime;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Long getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Long accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Long getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Long refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public Byte getTrusted() {
        return trusted;
    }

    public void setTrusted(Byte trusted) {
        this.trusted = trusted;
    }

    public Byte getArchived() {
        return archived;
    }

    public void setArchived(Byte archived) {
        this.archived = archived;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean supportRefreshToken() {
        return this.grantTypes != null && this.grantTypes.contains(GrantType.REFRESH_TOKEN.toString());
    }
}