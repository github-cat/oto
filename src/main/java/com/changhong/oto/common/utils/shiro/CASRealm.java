/**
 ***********************************************************************
 * All rights Reserved, Designed By changhong
 * @Title: CASRealm.java   
 * @Package com.changhong.oto.common.utils.shiro   
 * @Description: TODO   
 * @author: liuchengyong     
 * @date: 2016-12-13 下午1:22:28   
 * @version V1.0 
 * @Copyright: 2016 www.changhong.com Inc. All rights reserved. 
 ***********************************************************************
 *//*
package com.changhong.oto.common.utils.shiro;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

*//**
 * @ClassName CASRealm
 * @Description TODO
 * @author liuchengyong
 * @Date 2016-12-13 下午1:22:28
 * @version 1.0.0
 *//*
public class CASRealm extends CasRealm {
	private static final Logger log = LoggerFactory.getLogger(CASRealm.class);

	@Autowired
	protected UserService userService;

	*//**
	 * 认证回调函数,登录时调用.
	 *//*
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		CasToken casToken = (CasToken) authcToken;
		if (authcToken == null) {
			return null;
		}
		String ticket = (String) casToken.getCredentials();
		if (StringUtils.isEmpty(ticket)) {
			return null;
		}
		TicketValidator ticketValidator = ensureTicketValidator();
		Assertion casAssertion = null;
		try {
			casAssertion = ticketValidator.validate(ticket, getCasService());

			AttributePrincipal casPrincipal = casAssertion.getPrincipal();
			String userName = casPrincipal.getName();

			User user = userService.findOne("loginName", userName);
			if (user != null) {
				if (user.getStatus().equals("disabled")) {
					throw new DisabledAccountException();
				}
				Map attributes = casPrincipal.getAttributes();
				casToken.setUserId(userName);
				String rememberMeAttributeName = getRememberMeAttributeName();
				String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);
				boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
				if (isRemembered){
					casToken.setRememberMe(true);
				}
				ShiroUser shiroUser = new ShiroUser(user.getLoginName(), user.getName(), user.getPassword(), user.getId(),user.getOrganizations() == null ? null : user.getOrganizations());
				// List<Object> principals = Arrays.asList(new Object[] { userName, shiroUser });
				PrincipalCollection principalCollection = new SimplePrincipalCollection(shiroUser, getName());
				return new SimpleAuthenticationInfo(principalCollection, ticket);
			}
		} catch (TicketValidationException e) {
			throw new CasAuthenticationException((new StringBuilder()).append("Unable to validate ticket [").append(ticket).append("]").toString(),e);
		}
		return null;
	}

	*//**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 *//*
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = userService.findOne("loginName", shiroUser.getLoginName());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Role role : user.getRoles()) {
			// 基于Role的权限信息
			info.addRole(role.getName());
			for (Permission permission : role.getPermissions()) {
				// 基于Permission的权限信息
				info.addStringPermission(permission.getName());
			}
		}
		for (Permission permission : user.getPermissions()) {
			info.addStringPermission(permission.getName());
		}
		// 超级用户可以赋权超级权限
		if (userService.isSupervisor(user)) {
			info.addStringPermission("admin");
		}
		log.info("当前登录用户" + shiroUser.getLoginName() + "所拥有的权限：" + info.getStringPermissions());
		return info;
	}

	*//**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 *//*
	public static class ShiroUser extends com.techstar.modules.shiro.domain.ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Set<Organization> organizations;
		private Map<String, Object> cacheMap;

		public ShiroUser(String loginName, String name, String password, String id, Set<Organization> organizations) {
			super(id, loginName, password, name);
			this.organizations = organizations;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap == null) {
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

		public Set<Organization> getOrganizations() {
			return this.organizations;
		}

		*//**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 *//*
		@Override
		public String toString() {
			return this.getLoginName();
		}

		*//**
		 * 重载equals,只计算loginName;
		 *//*
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this, "loginName");
		}

		*//**
		 * 重载equals,只比较loginName
		 *//*
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj, "loginName");
		}
	}
}
*/