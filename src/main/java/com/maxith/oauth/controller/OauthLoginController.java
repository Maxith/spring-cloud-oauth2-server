package com.maxith.oauth.controller;

import com.maxith.common.ApplicatonConstants;
import com.maxith.common.entity.BaseComponent;
import com.maxith.common.entity.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录 控制器
 *
 * @author zhouyou
 * @date 2018/7/18 15:58
 **/
@Controller
@RequestMapping("/login")
public class OauthLoginController extends BaseComponent {

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("login/login");
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping("/passwordLogin")
    public JsonResult login(String username, String password) {
        JsonResult result = new JsonResult();
        try {
            //设置登录信息
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setCode(ApplicatonConstants.APP_ERROR_CODE);
            result.setMsg(ex.getMessage());
        }
        return result;
    }
}
