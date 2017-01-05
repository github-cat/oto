package com.changhong.oto.common.utils.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

//import system.entity.User;
//import system.service.IUserService;

/**
 * 
 *	类名称：MyRealm
 *	类描述：自定义Realm：进行数据源配置
 *	创建人：
 *	创建时间：2016-6-24上午11:17:38
 *	修改人：
 *	修改时间：
 *	修改备注：
 */
public class UserRealm extends AuthorizingRealm {

    //@Autowired
    //private IUserService userService;
    
    /**
	 * 
	 *	方法名：doGetAuthorizationInfo
	 *	@return
	 *	throws Exception
	 *	返回类型：AuthorizationInfo
	 *	说明：授权认证：则为当前登录的用户授予角色和权限
	 *	创建人：
	 * 	创建日期：2016-5-24上午11:53:03
	 *	修改人：
	 *	修改日期：
	 *  备注：	1.只有需要验证权限时才会调用, 授权查询回调函数, 
	 * 			2.进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
	 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try {
			//authorizationInfo.setRoles(userService.findRoles(username));
			//authorizationInfo.setStringPermissions(userService.findPermissions(username));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return authorizationInfo;
    }
    /**
	 * 
	 *	方法名：doGetAuthenticationInfo
	 * 	@param token
	 *	@return
	 *	throws Exception
	 *	返回类型：AuthenticationInfo
	 *	说明：用户登录认证
	 *	创建人：
	 * 	创建日期：2016-5-24上午11:53:03
	 *	修改人：
	 *	修改日期：
	 *  备注：	1.当用户执行登录时user.login(token),则调用该方法
	 *  	  	2.从数据库查询出来的账号名和密码,与用户输入的账号和密码对比
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	
    	
      //  String username = (String)token.getPrincipal();

       // User user = null;
		try {
			//user = userService.findByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}

      /*  if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }*/

       /* if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }*/

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                "admin", //用户名
                "d3c59d25033dbf980d29554025c23a75", //密码（数据库：加密密码）
                ByteSource.Util.bytes("admin"+"8d78869f470951332959580424d4bf4f"),//salt=username+salt //同样规则生成的加密密码=用户名+盐（私）
                getName()  //realm name
        );
        
        SimpleAuthenticationInfo authenticationInfo1 = new SimpleAuthenticationInfo(
                "admin", //用户名
                "d3c59d25033dbf980d29554025c23a75", //密码（数据库：加密密码）
                getName()  //realm name
        );
        
        System.out.println("认证##########################################");
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
