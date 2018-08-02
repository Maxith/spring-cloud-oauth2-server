package com.maxith.config.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 *  全局跨域请求handler
 *  @author zhouyou
 *  @date 2018/7/18 10:54
 **/
@ControllerAdvice
public class GlobalCrossDomainHandler extends AbstractJsonpResponseBodyAdvice {

    public GlobalCrossDomainHandler() {
        super("callback","jsonp");
    }
}
