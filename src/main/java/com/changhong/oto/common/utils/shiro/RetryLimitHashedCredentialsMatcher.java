package com.changhong.oto.common.utils.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

/**
 * 
 *	类名称：RetryLimitHashedCredentialsMatcher
 *	类描述：凭证匹配器
 *	创建人：
 *	创建时间：2016年11月10日上午9:20:30
 *	修改人：
 *	修改时间：
 *	修改备注：
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
	
    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }
    /**
     * 
     *	方法名：doCredentialsMatch
     *	@param token
     *	@param info
     *	@return
     *	返回类型：boolean
     *	说明：
     *	创建人：
     * 	创建日期：2016年11月10日上午9:19:16
     *	修改人：
     *	修改日期：
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //密码输入错误限制
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }
        //调用父类doCredentialsMatch方法进行凭证匹配操作
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
