package com.maxith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 单点登录微服务
 *
 * @author zhouyou
 * @date 2018/7/18 10:45
 **/
@ServletComponentScan
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableAspectJAutoProxy
public class Oauth2Application {

    /**
     * 服务入口
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
