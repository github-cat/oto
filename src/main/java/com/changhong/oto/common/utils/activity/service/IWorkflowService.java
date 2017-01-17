package com.changhong.oto.common.utils.activity.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;


/**
 * 
 * 类  名  称: IWorkflowService<br />
 * 描        述: 工作流服务<br />
 * 创  建  人: <br />
 * 创建时间: 2016年11月2日 下午2:39:04<br />
 * 修  改  人:<br /> 
 * 修改时间:<br />
 * 修改备注:<br />
 * 版       本: V1.0<br /> 
 *
 */
public interface IWorkflowService {
	/**
	 * 部署
	 * @param processName 规则名称
	 * @param inputStream 上传的zip包
	 */
	void newDeployment(String processName, InputStream inputStream);
	
	/**
	 * 流程定义列表查询
	 * @return
	 */
	List<Map<String,Object>> listProcessDefintion();
	
	
	/**
	 * 启动流程
	 * @param processKey
	 * @param processVarMap
	 */
	String startProcess(String processKey, HashMap<String, Object> processVarMap);
	
	/**
	 * 删除流程规则
	 * @param id
	 */
	void deleteProcess(String id);

	/**
	 * 获取流程任务页面信息
	 * @param assignee
	 * @return
	 */
	List<Map<String, Object>> getPersonalTask(String assignee,String group);
	
	
	/**
	 * 查看流程图
	 * @param processDefinitionId
	 * @return
	 */
	InputStream getResourceAsString(String processDefinitionId);
	
	
	/**
	 * 查询私有任务
	 * @param userId
	 * @return
	 */
	public List<Task> findPersonalTask(String userId);

	/**
	 * 查询流程定义对象
	 * @param taskId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByTaskId(String taskId);

	/**
	 * 获取当前活动的坐标
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getCurrentActivityCoordinates(String taskId);

	/**
	 * 通过任务ID获取到任务相关的表单信息
	 * @param taskId
	 * @return
	 */
	public TaskFormData getTaskFromData(String taskId);

	/**
	 * 通过任务ID获取到流程相关的业务对象ID
	 * @param taskId
	 * @return
	 */
	public String getBusinessObjId(String taskId);

	/**
	 * 完成任务
	 * @param taskId
	 */
	public void submitTask(String taskId, String outcome,String comment,Map<String, Object> vars);

	/**
	 * 获取指定任务的出口
	 * @param taskId
	 * @return
	 */
	public List<String> getOutGoingTransNames(String taskId);

	/**
	 * 查看当前任务相关的流程批注
	 * @param taskId
	 * @return
	 */
	public List<Comment> getProcessComments(String taskId);
	/**
	 * 
	 * 方  法  名: getImageStream<br /> 
	 * 描       述: 查看流程图<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: InputStream<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:33:57<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	InputStream getImageStream(String deploymentId, String imageName);
	/**
	 * 
	 * 方  法  名: getReadResource<br /> 
	 * 描       述:读取带跟踪的图片  当前节点流程图<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: void<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月28日 上午9:06:00<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	void getReadResource(String taskId, HttpServletResponse response)throws Exception;
	/**
	 * 
	 * 方  法  名: getHistoryActinst<br /> 
	 * 描       述:查询历史记录<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: void<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月28日 上午11:17:23<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	List<Comment> getHistoryActinst(String processInstanceId)throws Exception;
	/**
	 * 
	 * 方  法  名: queryProcessInstanceState<br /> 
	 * 描       述:查看流程状态<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: ProcessInstance<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年11月7日 上午9:18:13<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	ProcessInstance queryProcessInstanceState(String processInstanceId) throws Exception;
	
	
	/**
	 * 
	 * 方  法  名: queryTask<br /> 
	 * 描       述:通过当前流程taskId  查询对应数据<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: Task<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年11月7日 上午9:53:30<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	Task queryTask(String taskId)throws Exception;
	
	/**
	 * 
	 * 方  法  名: getCandidateTask<br /> 
	 * 描       述: 候选人查询任务<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: Map<String,Object><br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年11月14日 上午11:14:30<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	public Map<String, Object> getCandidateTask(String assignee);
	/**
	 * 
	 * 方  法  名: getGroupTask<br /> 
	 * 描       述:分组查询任务<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: Map<String,Object><br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年11月14日 上午11:17:11<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	public Map<String, Object> getGroupTask(String assignee);
	
	/**
	 * 
	 * 方  法  名: getAssigneeTask<br /> 
	 * 描       述:个人任务查询<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: Map<String,Object><br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年11月14日 上午11:27:31<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	public Map<String, Object> getAssigneeTask(String assignee);
}
