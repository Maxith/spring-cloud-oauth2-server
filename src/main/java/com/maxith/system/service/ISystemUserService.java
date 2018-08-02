package com.maxith.system.service;


import com.maxith.system.entity.SystemUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 系统用户service
 *
 * @author zhouyou
 * @date 2018/7/18 10:44
 **/
@FeignClient(name = "${api.system-user.feign-service-name}")
public interface ISystemUserService {
    /**
     * 使用用户名查询
     *
     * @param username
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "${api.system-user.select-by-name}", consumes = "application/json")
    SystemUser selectSystemUserByUsername(@RequestParam(value = "username") String username);

    /**
     * 使用用户名和密码查询
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "${api.system-user.select-by-name-and-pass}", consumes = "application/json")
    SystemUser selectUserByUsernameAndPassword(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);

    /**
     * 更新用户登录时间
     *
     * @param gid
     */
    @RequestMapping(method = RequestMethod.POST, value = "${api.system-user.update-login-time}", consumes = "application/json")
    void updateLoginTime(@RequestParam(value = "gid") String gid);
}
