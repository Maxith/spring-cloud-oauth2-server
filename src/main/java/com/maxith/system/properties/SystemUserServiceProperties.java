package com.maxith.system.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统用户服务feign地址配置对象
 * @author zhouyou
 * @date 2018/7/18
 * @description
 **/
@Component
@ConfigurationProperties(prefix = "api.system-user")
public class SystemUserServiceProperties {

    /**
     * feign服务名称
     */
    private String feignServiceName;
    /**
     * 按名称查询
     */
    private String selectByName;
    /**
     * 按名称和密码查询
     */
    private String selectByNameAndPass;
    /**
     * 更新时间
     */
    private String updateLoginTime;

    public String getFeignServiceName() {
        return feignServiceName;
    }

    public void setFeignServiceName(String feignServiceName) {
        this.feignServiceName = feignServiceName;
    }

    public String getSelectByName() {
        return selectByName;
    }

    public void setSelectByName(String selectByName) {
        this.selectByName = selectByName;
    }

    public String getSelectByNameAndPass() {
        return selectByNameAndPass;
    }

    public void setSelectByNameAndPass(String selectByNameAndPass) {
        this.selectByNameAndPass = selectByNameAndPass;
    }

    public String getUpdateLoginTime() {
        return updateLoginTime;
    }

    public void setUpdateLoginTime(String updateLoginTime) {
        this.updateLoginTime = updateLoginTime;
    }
}
