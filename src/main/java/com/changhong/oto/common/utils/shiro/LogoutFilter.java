/**
 ***********************************************************************
 * All rights Reserved, Designed By changhong
 * @Title: LogoutFilter.java   
 * @Package com.changhong.oto.common.utils.shiro   
 * @Description: TODO   
 * @author: liuchengyong     
 * @date: 2016-12-13 下午2:04:55   
 * @version V1.0 
 * @Copyright: 2016 www.changhong.com Inc. All rights reserved. 
 ***********************************************************************
 */
package com.changhong.oto.common.utils.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName LogoutFilter
 * @Description TODO
 * @author liuchengyong
 * @Date 2016-12-13 下午2:04:55
 * @version 1.0.0
 */
public class LogoutFilter extends AdviceFilter {

    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    /**
     * The default redirect URL to where the user will be redirected after logout. The value is {@code "/"}, Shiro's
     * representation of the web application's context root.
     */
    public static final String DEFAULT_REDIRECT_URL = "/";

    /**
     * The URL is Logout address.
     */
    private String casServerLogoutUrl;

    /**
     * The URL to where the user will be redirected after logout.
     */
    private String redirectUrl = DEFAULT_REDIRECT_URL;

    /**
     * Acquires the currently executing {@link #getSubject(ServletRequest, ServletResponse)
     * subject}, a potentially Subject or request-specific
     * {@link #getRedirectUrl(ServletRequest, ServletResponse, Subject)
     * redirectUrl}, and redirects the end-user to that redirect url.
     * 
     * @param request the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @return {@code false} always as typically no further interaction should be done after user logout.
     * @throws Exception if there is any error.
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        HttpServletResponse resp = (HttpServletResponse) response;
        String casServerLogoutUrl = getCasServerLogoutUrl() + "?service=" + redirectUrl;
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        //issueRedirect(request, response, redirectUrl);
        resp.sendRedirect(casServerLogoutUrl);
        return false;
    }

    /**
     * Returns the currently executing {@link Subject}. This implementation merely defaults to calling
     * {@code SecurityUtils.}{@link SecurityUtils#getSubject() getSubject()}, but can be overridden by
     * subclasses for different retrieval strategies.
     * 
     * @param request the incoming Servlet request
     * @param response the outgoing Servlet response
     * @return the currently executing {@link Subject}.
     */
    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    /**
     * Issues an HTTP redirect to the specified URL after subject logout. This implementation simply calls
     * {@code WebUtils.}
     * {@link WebUtils#issueRedirect(ServletRequest, ServletResponse, String)
     * issueRedirect(request,response,redirectUrl)}.
     * 
     * @param request the incoming Servlet request
     * @param response the outgoing Servlet response
     * @param redirectUrl the URL to where the browser will be redirected immediately after Subject logout.
     * @throws Exception if there is any error.
     */
    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception {
        WebUtils.issueRedirect(request, response, redirectUrl);
    }

    /**
     * Returns the redirect URL to send the user after logout. This default implementation ignores the arguments and
     * returns the static configured {@link #getRedirectUrl() redirectUrl} property, but this method may be overridden
     * by subclasses to dynamically construct the URL based on the request or subject if necessary.
     * <p/>
     * Note: the Subject is <em>not</em> yet logged out at the time this method is invoked. You may access the Subject's
     * session if one is available and if necessary.
     * <p/>
     * Tip: if you need to access the Subject's session, consider using the {@code Subject.}
     * {@link Subject#getSession(boolean) getSession(false)} method to ensure a new session isn't created unnecessarily.
     * If a session would be created, it will be immediately stopped after logout, not providing any value and
     * unnecessarily taxing session infrastructure/resources.
     * 
     * @param request the incoming Servlet request
     * @param response the outgoing ServletResponse
     * @param subject the not-yet-logged-out currently executing Subject
     * @return the redirect URL to send the user after logout.
     */
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        return getRedirectUrl();
    }

    public String getCasServerLogoutUrl() {
        return casServerLogoutUrl;
    }

    public void setCasServerLogoutUrl(String casServerLogoutUrl) {
        this.casServerLogoutUrl = casServerLogoutUrl;
    }

    /**
     * Returns the URL to where the user will be redirected after logout. Default is the web application's context root,
     * i.e. {@code "/"}
     * 
     * @return the URL to where the user will be redirected after logout.
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * Sets the URL to where the user will be redirected after logout. Default is the web application's context root,
     * i.e. {@code "/"}
     * 
     * @param redirectUrl the url to where the user will be redirected after logout
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * 返回不带"/"带端口的网站根路径
     * 
     * @return 不带"/"带端口的网站根路径
     * @author fushihua
     */
    private String getWebBasePath(HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return basePath;
    }

    /**
     * 返回不带"/"不带端口的网站根路径
     * 
     * @return 不带"/"不带端口的网站根路径
     * @author fushihua
     */
    private String getWebBasePathNoPort(HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        return basePath;
    }
}
