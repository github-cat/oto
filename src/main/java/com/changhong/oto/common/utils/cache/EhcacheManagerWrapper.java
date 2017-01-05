/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.changhong.oto.common.utils.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * 
 *	类名称：SpringCacheManagerWrapper
 *	类描述：spring缓存
 *	创建人：
 *	创建时间：2016年11月10日上午9:42:29
 *	修改人：
 *	修改时间：
 *	修改备注：
 *  备注：shiro提供了缓存接口，根据具体用户需求做不通的缓存实现
 */
public class EhcacheManagerWrapper implements CacheManager {
    /*
     * 注入spring缓存实现
     */
    private org.springframework.cache.CacheManager cacheManager;
    public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    
    /**
     * 根据缓存name获取缓存对象Cache<K, V>
     */
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        org.springframework.cache.Cache springCache = cacheManager.getCache(name);
        return new EhcacheWrapper(springCache);
    }
    /**************************************静态类部类**************************************************/
    
    /**
     * 
     *	类名称：SpringCacheWrapper
     *	类描述：静态类部类
     *	创建人：
     *	创建时间：2016年11月10日上午9:48:30
     *	修改人：
     *	修改时间：
     *	修改备注：
     */
    static class EhcacheWrapper implements Cache {
        private org.springframework.cache.Cache springCache;

        EhcacheWrapper(org.springframework.cache.Cache springCache) {
            this.springCache = springCache;
        }
        /**
         * 根据缓存缓存Key获取Value
         */
        @Override
        public Object get(Object key) throws CacheException {
            Object value = springCache.get(key);
            if (value instanceof SimpleValueWrapper) {
                return ((SimpleValueWrapper) value).get();
            }
            return value;
        }

        @Override
        public Object put(Object key, Object value) throws CacheException {
            springCache.put(key, value);
            return value;
        }

        @Override
        public Object remove(Object key) throws CacheException {
            springCache.evict(key);
            return null;
        }

        @Override
        public void clear() throws CacheException {
            springCache.clear();
        }

        @Override
        public int size() {
            if(springCache.getNativeCache() instanceof Ehcache) {
                Ehcache ehcache = (Ehcache) springCache.getNativeCache();
                return ehcache.getSize();
            }
            throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");
        }

        @Override
        public Set keys() {
            if(springCache.getNativeCache() instanceof Ehcache) {
                Ehcache ehcache = (Ehcache) springCache.getNativeCache();
                return new HashSet(ehcache.getKeys());
            }
            throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
        }

        @Override
        public Collection values() {
            if(springCache.getNativeCache() instanceof Ehcache) {
                Ehcache ehcache = (Ehcache) springCache.getNativeCache();
                List keys = ehcache.getKeys();
                if (!CollectionUtils.isEmpty(keys)) {
                    List values = new ArrayList(keys.size());
                    for (Object key : keys) {
                        Object value = get(key);
                        if (value != null) {
                            values.add(value);
                        }
                    }
                    return Collections.unmodifiableList(values);
                } else {
                    return Collections.emptyList();
                }
            }
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        }
    }
}
