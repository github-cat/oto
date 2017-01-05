package com.changhong.oto.common.utils.shiro.aop;

import java.util.List;
import java.util.Map;

public interface DataParameterRequest {
	
	//数据权限
	Map<String,String> getParameters();
	
	//字段级权限
	List<String> getFieldList();
}
