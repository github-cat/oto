package com.changhong.oto.common.utils.activity.domain;

import java.util.Date;

import org.activiti.engine.repository.Model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 
 * 类  名  称: ActivitiModel<br />
 * 描        述: 封装Model属性值TODO<br />
 * 创  建  人: <br />
 * 创建时间: 2016年11月2日 下午2:24:25<br />
 * 修  改  人:<br /> 
 * 修改时间:<br />
 * 修改备注:<br />
 * 版       本: V1.0<br /> 
 *
 */
public class ActivitiModel {
	String id;
	String name;
	String key;
	String description;
	Date createTime;
	
	
	
	
	
	
	public static ActivitiModel ActivitiModelFactory(Model model) {
		ActivitiModel activitiModel = new ActivitiModel();
		activitiModel.setId(model.getId());
		activitiModel.setKey(model.getKey());
		activitiModel.setName(model.getName());
		activitiModel.setCreateTime(model.getCreateTime());
		JSONObject parse = (JSONObject)JSON.parse(model.getMetaInfo());
		activitiModel.setDescription(parse.getString("description"));
		return activitiModel;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
