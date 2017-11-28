package com.maxith.config.jdbc;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis扫描接口
 * Created by zhouyou on 2017/5/12.
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)//注意，由于MapperScannerConfigurer执行的比较早，所以必须有此注解
public class MyBatisMapperScannerConfig {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.maxith.*.mapper");
        return mapperScannerConfigurer;
    }
}
