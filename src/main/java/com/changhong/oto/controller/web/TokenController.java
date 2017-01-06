package com.changhong.oto.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.changhong.oto.common.utils.AjaxResult;

@Controller
@RequestMapping(value = "/web/token")
public class TokenController {

	@RequestMapping(value = "/setDeviceToken")
	public @ResponseBody AjaxResult setDeviceToken(@RequestBody JSONObject obj) {
		//Session s = SecurityUtils.getSubject().getSession();
		//System.out.println(s.getId()+"--------------------------------------------");
		AjaxResult ajaxResult = null;
		return ajaxResult;
	}
	
	@RequestMapping(value = "/add")
	public @ResponseBody AjaxResult add() {
		//Session s = SecurityUtils.getSubject().getSession();
		//System.out.println(s.getId()+"--------------------------------------------");
		AjaxResult ajaxResult = new AjaxResult("正常返回", "正常返回");
		return ajaxResult;
	}
	
	
	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}
}
