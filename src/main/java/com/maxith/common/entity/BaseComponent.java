package com.maxith.common.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础对象,用于控制日志输出
 */
public abstract class BaseComponent {
    public Logger logger;

    public BaseComponent() {
        logger = LoggerFactory.getLogger((this.getClass()));
    }
}
