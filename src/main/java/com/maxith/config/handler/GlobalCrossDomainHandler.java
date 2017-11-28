package com.maxith.config.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 全局跨域请求handler
 * Created by zhouyou on 2017/6/6.
 */
@ControllerAdvice
public class GlobalCrossDomainHandler extends AbstractJsonpResponseBodyAdvice {

    public GlobalCrossDomainHandler() {
        super("callback","jsonp");
    }
}
