/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.changhong.oto.common.utils.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * 
 * @ClassName RedisCacheManagerWrapper
 * @Description TODO
 * @author liuchengyong
 * @Date 2016-12-14 下午1:56:25
 * @version 1.0.0
 */
public class RedisCacheManagerWrapper implements CacheManager {
	
	private static final Logger LOGGER = Logger
			.getLogger(RedisCacheManagerWrapper.class);
	
    private org.springframework.data.redis.cache.RedisCacheManager cacheManager;
    
    public void setCacheManager(org.springframework.data.redis.cache.RedisCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
	@Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        org.springframework.cache.Cache cache = cacheManager.getCache(name);
        return new RedisCacheWrapper<K,V>(cache);
    }
    
    /**
     * 
     * @ClassName RedisCacheWrapper
     * @Description TODO
     * @author liuchengyong
     * @Date 2016-12-14 下午1:56:46
     * @version 1.0.0
     * @param <V>
     */
	static class RedisCacheWrapper<K,V> implements Cache<K,V> {
    	
        private org.springframework.cache.Cache cache;

        RedisCacheWrapper(org.springframework.cache.Cache cache) {
            this.cache = cache;
        }
        
        @Override
        public void clear() throws CacheException {
        	cache.clear();
        }
        
		/* (非 Javadoc)
		 * Description:
		 * @see org.apache.shiro.cache.Cache#size()
		 */
		@Override
		public int size() {
			LOGGER.debug("RedisCacheManagerWrapper size()方法被调用");
			return 0;
		}
		
		/* (非 Javadoc)
		 * Description:
		 * @see org.apache.shiro.cache.Cache#keys()
		 */
		@Override
		public Set<K> keys() {
			LOGGER.debug("RedisCacheManagerWrapper keys()方法被调用");
			return Collections.emptySet();
		}
		/* (非 Javadoc)
		 * Description:
		 * @see org.apache.shiro.cache.Cache#values()
		 */
		@Override
		public Collection<V> values() {
			LOGGER.debug("RedisCacheManagerWrapper values()方法被调用");
			// TODO Auto-generated method stub
			return Collections.emptyList();
		}

		/* (非 Javadoc)
		 * Description:
		 * @see org.apache.shiro.cache.Cache#get(java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public V get(K key) throws CacheException {
			// TODO Auto-generated method stub
			ValueWrapper wrapper = cache.get(key);
    		return wrapper == null ? null : (V) wrapper.get();
		}

		/* (非 Javadoc)
		 * Description:
		 * @see org.apache.shiro.cache.Cache#put(java.lang.Object, java.lang.Object)
		 */
		@Override
		public V put(K key, V value) throws CacheException {
			// TODO Auto-generated method stub
			cache.put(key, value);
            return value;
		}
		
		/* (非 Javadoc)
		 * Description:
		 * @see org.apache.shiro.cache.Cache#remove(java.lang.Object)
		 */
		@Override
		public V remove(K key) throws CacheException {
			// TODO Auto-generated method stub
			cache.evict(key);
			return null;
		}
    }    
}
