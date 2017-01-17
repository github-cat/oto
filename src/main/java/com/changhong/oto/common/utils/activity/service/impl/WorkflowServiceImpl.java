package com.changhong.oto.common.utils.activity.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.changhong.oto.common.utils.activity.service.IWorkflowService;
import com.changhong.oto.common.utils.activity.utils.CommUtil;
import com.changhong.oto.common.utils.activity.utils.WorkflowConst;

@Service
public class WorkflowServiceImpl implements IWorkflowService{
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessEngineFactoryBean processEngine;
	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;

	@Override
	public void newDeployment(String processName, InputStream inputStream) {
		// 创建发布对象
		DeploymentBuilder builder = repositoryService.createDeployment();
		//配置
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		builder.name(processName).addZipInputStream(zipInputStream);
		//应用配置
		builder.deploy();
	}
	
	@Override
	public List<Map<String, Object>> listProcessDefintion() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//封装
		List<ProcessDefinition> pds = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().desc()
		.orderByProcessDefinitionVersion().desc().list();
		for (ProcessDefinition pd : pds) {
			list.add(CommUtil.obj2map(pd, new String[]{
					"id","name","key","version","resourceName","diagramResourceName","description","deploymentId"
			}));
		}
		return list;
	}
	
	
	@Override
	public String startProcess(String processKey,HashMap<String, Object> processVarMap) {
		
		String businessKey = processVarMap.get(WorkflowConst.CLASS_TYPE)+"."+processVarMap.get(WorkflowConst.OBJ_ID);
		ProcessInstance ProcessInstance = runtimeService.startProcessInstanceByKey(processKey,businessKey,processVarMap);
		return ProcessInstance.getId();
		
	}
	/**
	 * 删除流程规则
	 */
	@Override
	public void deleteProcess(String id) {
		
		repositoryService.deleteDeployment(id, true);
	}
	
	
	/**查看流程图
	 * 
	 */
	@Override
	public InputStream getResourceAsString(String processDefinitionId) {
		return repositoryService.getProcessDiagram(processDefinitionId);
	}
	
	
	@Override
	public Map<String, Object> getCandidateTask(String assignee) {
		Map<String, Object> taskMap = null;
		List<Task> candidatetasks = taskService.createTaskQuery().taskCandidateUser(assignee).orderByTaskCreateTime().desc().list();
		for (Task task : candidatetasks) {
			List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
			if (identityLinksForTask!=null) {
				for (IdentityLink identityLink : identityLinksForTask) {
					if (StringUtils.isNotBlank(assignee)&&assignee.equals(identityLink.getUserId())) {
						task.setAssignee(identityLink.getUserId());
					}
				}
			}
			taskMap = CommUtil.obj2map(task, new String[]{
					"id","name","assignee","createTime","description","formKey"
			});
			// 获取表单键
//		String formKey =  getTaskFormKey(task);
//		taskMap.put("formKey", formKey);
			
			String classType = getClassType(task);  
			// 首字母小写
			classType = classType.substring(0,1).toLowerCase()+classType.substring(1);
			taskMap.put(WorkflowConst.CLASS_TYPE, classType);
			
			Long objId = getObjId(task);
			taskMap.put(WorkflowConst.OBJ_ID, objId);
		}
		return taskMap;
	}
	
	@Override
	public Map<String, Object> getGroupTask(String group) {
		Map<String, Object> taskMap = null;
		List<Task> grouptasks = taskService.createTaskQuery().taskCandidateGroup(group).orderByTaskCreateTime().desc().list();
		for (Task task : grouptasks) {
			List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
			if (identityLinksForTask!=null) {
				for (IdentityLink identityLink : identityLinksForTask) {
					if (StringUtils.isNotBlank(group)&&group.equals(identityLink.getUserId())) {
						task.setAssignee(identityLink.getUserId());
					}
				}
			}
			taskMap = CommUtil.obj2map(task, new String[]{
					"id","name","assignee","createTime","description","formKey"
			});
			// 获取表单键
//		String formKey =  getTaskFormKey(task);
//		taskMap.put("formKey", formKey);
			
			String classType = getClassType(task);  
			// 首字母小写
			classType = classType.substring(0,1).toLowerCase()+classType.substring(1);
			taskMap.put(WorkflowConst.CLASS_TYPE, classType);
			
			Long objId = getObjId(task);
			taskMap.put(WorkflowConst.OBJ_ID, objId);
		}
		return taskMap;
	}
	
	@Override
	public Map<String, Object> getAssigneeTask(String assignee) {
		Map<String, Object> taskMap = null;
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
		if (tasks.size()!=0) {
			for (Task task : tasks) {
				taskMap = CommUtil.obj2map(task, new String[]{
						"id","name","assignee","createTime","description","formKey"
				});
				// 获取表单键
//			String formKey =  getTaskFormKey(task);
//			taskMap.put("formKey", formKey);
				
				String classType = getClassType(task);  
				// 首字母小写
				classType = classType.substring(0,1).toLowerCase()+classType.substring(1);
				taskMap.put(WorkflowConst.CLASS_TYPE, classType);
				
				Long objId = getObjId(task);
				taskMap.put(WorkflowConst.OBJ_ID, objId);
			}
		}
		return taskMap;
	}
	
	/**
	 * 获取流程任务页面信息
	 */
	@Override
	public List<Map<String, Object>> getPersonalTask(String assignee,String group) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> assigneeTask = getAssigneeTask(assignee);
		if (assigneeTask!=null) {
			list.add(assigneeTask);
		}
		
		Map<String, Object> candidateTask = getCandidateTask(assignee);
		if (candidateTask!=null) {
			list.add(candidateTask);
		}
		Map<String, Object> groupTask = getGroupTask(group);
		if (groupTask!=null) {
			list.add(groupTask);
		}
		return list;
	}
	
	
	private Long getObjId(Task task) {
		String taskId = task.getId();
		return taskService.getVariable(taskId, WorkflowConst.OBJ_ID, Long.class);
	}

	private String getClassType(Task task) {
		String taskId = task.getId();
		return taskService.getVariable(taskId , WorkflowConst.CLASS_TYPE, String.class);
	}

	@Override
	public List<Task> findPersonalTask(String userId) {
		return taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
	}

	@Override
	public ProcessDefinition getProcessDefinitionByTaskId(String taskId) {
		// 1. 得到task
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 2. 通过task对象的pdid获取流程定义对象
		ProcessDefinition pd = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		return pd;
	}

	@Override
	public Map<String, Object> getCurrentActivityCoordinates(String taskId) {
		Map<String, Object> coordinates = new HashMap<String, Object>();
		// 1. 获取到当前活动的ID
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		String currentActivitiId = pi.getActivityId();
		// 2. 获取到流程定义
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		// 3. 使用流程定义通过currentActivitiId得到活动对象
		ActivityImpl activity =  pd.findActivity(currentActivitiId);
		// 4. 获取活动的坐标
		coordinates.put("x", activity.getX());
		coordinates.put("y", activity.getY());
		coordinates.put("width", activity.getWidth());
		coordinates.put("height", activity.getHeight());
		return coordinates;
	}

	@Override
	public TaskFormData getTaskFromData(String taskId) {
		return formService.getTaskFormData(taskId);
	}

	@Override
	public String getBusinessObjId(String taskId) {
		//1  获取任务对象
		Task task  =  taskService.createTaskQuery().taskId(taskId).singleResult();
		//2  通过任务对象获取流程实例
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		//3 通过流程实例获取“业务键”
		String businessKey = pi.getBusinessKey();
		//4 拆分业务键，拆分成“业务对象名称”和“业务对象ID”的数组 
		// a=b  LeaveBill.1
		String objId = null;
		if(StringUtils.isNotBlank(businessKey)){
			objId = businessKey.split("\\.")[1];
		}
		return objId;
	}

	@Override
	public void submitTask(String taskId, String outcome, String comment,Map<String, Object> vars) {
		// 由于流程用户上下文对象是线程独立的，所以要在需要的位置设置，要保证设置和获取操作在同一个线程中
		Authentication.setAuthenticatedUserId(""/*UserContext.getUser().getName()*/);
		// 添加批注信息
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.addComment(taskId, task.getProcessInstanceId(),outcome,comment);
		// 完成任务
		taskService.complete(taskId,vars);
	}

	@Override
	public List<String> getOutGoingTransNames(String taskId) {
		List<String> transNames = new ArrayList<>();
		// 1.获取流程定义
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		// 2.获取流程实例
		ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult(); 
		// 3.通过流程实例查找当前活动的ID
		String activitiId = pi.getActivityId();
		// 4.通过活动的ID在流程定义中找到对应的活动对象
		ActivityImpl activity = pd.findActivity(activitiId);
		// 5.通过活动对象找当前活动的所有出口
		 List<PvmTransition> transitions =  activity.getOutgoingTransitions();
		// 6.提取所有出口的名称，封装成集合
		 for (PvmTransition trans : transitions) {
			 String transName = (String) trans.getProperty("name");
			 if(StringUtils.isNotBlank(transName)){
				 transNames.add(transName);
			 }
		}
		 if(transNames.size()==0){
			 transNames.add(WorkflowConst.OUT_NAME);
		 }
		return transNames;
	}

	@Override
	public List<Comment> getProcessComments(String taskId) {
		List<Comment> historyCommnets = new ArrayList<>();
//		 1) 获取流程实例的ID
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
//==========================================================================================================================		
		ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
//      2）通过流程实例查询所有的(用户任务类型)历史活动   
		List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery().processInstanceId(pi.getId()).activityType("userTask").list();
		HistoricActivityInstanceQuery processInstanceId = historyService.createHistoricActivityInstanceQuery().processInstanceId(pi.getId());
//      3）查询每个历史任务的批注
//		for (HistoricActivityInstance hai : hais) {
//			String historytaskId = hai.getTaskId();
//			List<Comment> comments = taskService.getTaskComments(historytaskId);
//			List<Comment> comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
//			// 4）如果当前任务有批注信息，添加到集合中
//			if(comments!=null && comments.size()>0){
//				historyCommnets.addAll(comments);
//			}
//		}
//======================================================================================================		
		List<Comment> comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
		// 4）如果当前任务有批注信息，添加到集合中
		if(comments!=null && comments.size()>0){
			historyCommnets.addAll(comments);
		}
//      5）返回
		 return historyCommnets;
	}
	
	
	/**
	 * 查看流程图
	 */
	@Override
	public InputStream getImageStream(String deploymentId, String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}

	@Override
	public void getReadResource(String taskId, HttpServletResponse response)throws Exception {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(task.getProcessInstanceId());
        // 不使用spring请使用下面的两行代码
//    ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//    Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        //获取图片流,并解决中文乱码问题
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds,
        		new ArrayList<String>(),"粗体","粗体","粗体",null,2.0);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
	}


	@Override
	public List<Comment> getHistoryActinst(String processInstanceId) throws Exception {
		List<Comment> historyCommnets = new ArrayList<>();
		List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);
		if(taskComments!=null && taskComments.size()>0){
			historyCommnets.addAll(taskComments);
		}
		return historyCommnets;
		
	}


	@Override
	public ProcessInstance queryProcessInstanceState(String processInstanceId) throws Exception {
		return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}


	@Override
	public Task queryTask(String taskId) throws Exception {
		return  taskService.createTaskQuery().taskId(taskId).singleResult();
	}


	


	

}
