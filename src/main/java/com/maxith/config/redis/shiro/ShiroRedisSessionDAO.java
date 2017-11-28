package com.maxith.config.redis.shiro;

import com.maxith.common.tools.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * shiro redis session缓存
 * Created by zhouyou on 2017/10/17.
 */
@Component
public class ShiroRedisSessionDAO extends CachingSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(ShiroRedisSessionDAO.class);

    private static final String SHIRO_REDIS_SESSION = "shiro-redis-session:";

    @Resource
    private RedisTemplate<String, byte[]> redisTemplate;

    @Override
    protected void doUpdate(Session session) {
        this.saveSession(session);
    }

    @Override
    protected void doDelete(Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        redisTemplate.delete(SHIRO_REDIS_SESSION + session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("session id is null");
            return null;
        }
        Session s = (Session)SerializeUtils.deserialize(redisTemplate.opsForValue().get(SHIRO_REDIS_SESSION + sessionId));
        return s;
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }

        String key = SHIRO_REDIS_SESSION + session.getId();
        byte[] value = SerializeUtils.serialize(session);
        redisTemplate.opsForValue().set(key, value);
    }
}
