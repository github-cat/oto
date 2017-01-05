package com.changhong.oto.common.utils.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

//import system.constant.Constants;
//import system.service.IUserService;

/**
 * 
 *	类名称：SysUserFilter
 *	类描述：用户绑定
 *	创建人：
 *	创建时间：2016年11月10日上午9:24:39
 *	修改人：
 *	修改时间：
 *	修改备注：
 */
public class SysUserFilter extends PathMatchingFilter {

    //@Autowired
   // private IUserService userService;
    /**
     * 
     *	方法名：onPreHandle
     *	@param request
     *	@param response
     *	@param mappedValue
     *	@return
     *	@throws Exception
     *	返回类型：boolean
     *	说明：将用户放入请求中，并把CURRENT_USER值赋@CurrentUser参数对象
     *	创建人：
     * 	创建日期：2016年11月10日下午2:49:53
     *	修改人：
     *	修改日期：
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        //request.setAttribute(Constants.CURRENT_USER, userService.findByUsername(username));
        return true;
    }
}
