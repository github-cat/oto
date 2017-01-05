/**
 ***********************************************************************
 * All rights Reserved, Designed By changhong
 * @Title: RedisSessionDao.java   
 * @Package com.changhong.oto.common.utils.shiro   
 * @Description: TODO   
 * @author: liuchengyong     
 * @date: 2016-12-11 上午9:51:44   
 * @version V1.0 
 * @Copyright: 2016 www.changhong.com Inc. All rights reserved. 
 ***********************************************************************
 */
package com.changhong.oto.common.utils.shiro;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @ClassName RedisSessionDao
 * @Description TODO
 * @author liuchengyong
 * @Date 2016-12-11 上午9:51:44
 * @version 1.0.0
 */
public class RedisSessionDao extends CachingSessionDAO {
	
	
    private RedisTemplate<Serializable, Session> redisTemplate;
    
    @Resource(name = "redisTemplate")
    private ValueOperations<Serializable, Session> valueOps;

    @Override
    protected void doUpdate(Session session) {
        valueOps.set(session.getId(), session);
    }
    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        redisTemplate.delete(session.getId());
    }
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        valueOps.set(sessionId, session);
        return sessionId;
    }
    @Override
    protected Session doReadSession(Serializable sessionId) {
        return valueOps.get(sessionId);
    }
} 
