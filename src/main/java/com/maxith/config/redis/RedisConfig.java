package com.maxith.config.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.maxith.oauth.properties.OAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 配置
 *
 * @author zhouyou
 * @date 2018/7/18 10:57
 **/
@Configuration
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate,
                                     @Qualifier("oAuthProperties") OAuthProperties oAuthProperties) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(oAuthProperties.getSessionOvertime());
        return cacheManager;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //使用fastjson格式化数据
        template.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        return template;
    }
}