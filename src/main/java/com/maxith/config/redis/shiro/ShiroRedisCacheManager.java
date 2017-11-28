package com.maxith.config.redis.shiro;

import com.maxith.common.entity.BaseComponent;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * shiro redis 缓存
 * Created by zhouyou on 2017/10/11.
 */
@Component
public class ShiroRedisCacheManager extends BaseComponent implements CacheManager {

    private static final String REDIS_SHIRO_CACHE = "shiro-redis-cache:";

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        logger.debug("获取名称为: " + name + " 的RedisCache实例");

        Cache c = caches.get(name);

        if (c == null) {

            // create a new cache instance
            c = new ShiroCache<>(REDIS_SHIRO_CACHE, redisTemplate);

            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }
}
