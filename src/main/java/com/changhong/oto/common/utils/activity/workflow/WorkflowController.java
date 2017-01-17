package com.changhong.oto.common.utils.activity.workflow;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.changhong.oto.common.utils.activity.service.IWorkflowService;
import com.changhong.oto.common.utils.activity.utils.WorkflowConst;

@Controller
@RequestMapping("/workflow")
public class WorkflowController {
	@Autowired
	private IWorkflowService workflowService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
    ProcessEngineFactoryBean processEngine;
	@Autowired
    ProcessEngineConfiguration processEngineConfiguration;
	@Autowired
	private TaskService taskService;
	
	
	/**
	 * 
	 * 方  法  名: getPersonalTask<br /> 
	 * 描       述:流程任务管理<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: ModelAndView<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:27:07<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("task")
	public String getPersonalTask(@RequestParam("type") String type,Model model) {
//		String assignee = UserContext.getUser().getName();
		String assignee = "";
		List<Map<String, Object>> personalTaskList = new ArrayList<Map<String, Object>>();
		List<Map<String,Object>> pTask = workflowService.getPersonalTask(assignee,assignee);
		for (Map<String, Object> map : pTask) {
			if (type.equals(map.get(WorkflowConst.CLASS_TYPE))) {
				personalTaskList.add(map);
			}
		}
		model.addAttribute("pTask", personalTaskList);
		return "workflow/task";
	}
	
	
	@RequestMapping("/viewTaskForm")
	public String viewTask(String formUrl) {
		
		return "forward:"+formUrl;
	}
	
	
	
	/**
	 * 
	 * 方  法  名: submitTask<br /> 
	 * 描       述:任务办理<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: String<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:11:32<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("submitTask")
	public String submitTask(String taskId,String outcome,String comment){
//		System.out.println("outcome:" + outcome);
//		System.out.println("comment:" + comment);
//		workflowService.submitTask(taskId,outcome,comment);
		return "redirect:/workflow/task";
	}
	/**
	 * 
	 * 方  法  名: deleteProcess<br /> 
	 * 描       述:删除部署流程<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: void<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:11:25<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/deleteProcess")
	public String deleteProcess(String id){
		
		workflowService.deleteProcess(id);
		
		return "redirect:/workflow/listUI";
		
	}
	
	
	/**
	 * 
	 * 方  法  名: listUI<br /> 
	 * 描       述:流程部署管理<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: ModelAndView<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:25:51<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("listUI")
	public String listUI(Model model) {
		List<Map<String,Object>> pds = workflowService.listProcessDefintion();
		model.addAttribute("pds", pds);
		return "workflow/processDefinition";
	}

	/**
	 * 
	 * 方  法  名: newDeployment<br /> 
	 * 描       述:上传zip包 部署<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: Object<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:26:01<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/newDeployment")
	public String newDeployment(String processName, @RequestParam MultipartFile processFile) {
		try {
			workflowService.newDeployment(processName,processFile.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/workflow/listUI";
	}
	
	
	/**
	 * 
	 * 方  法  名: viewImage<br /> 
	 * 描       述:查看流程图(方式一)<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: String<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:35:31<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("viewImage")
	public String viewImage(String deploymentId,String imageName,HttpServletResponse resp){
		InputStream  in = workflowService.getImageStream(deploymentId,imageName);
		try {
			OutputStream out = resp.getOutputStream();
			// 把图片的输入流程写入resp的输出流中
			byte[] b = new byte[1024];
			for (int len = -1; (len= in.read(b))!=-1; ) {
				out.write(b, 0, len);
			}
			// 关闭流
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * 方  法  名: viewImage1<br /> 
	 * 描       述:查看流程图(方式二)<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: void<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月31日 上午9:47:58<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/viewImage1")
	@ResponseBody
	public void viewImage1(String processDefinitionId, HttpServletResponse response) {
		InputStream inputStream= null;
		OutputStream outputStream= null;
		try {
			inputStream = workflowService.getResourceAsString(processDefinitionId);
			
			outputStream = response.getOutputStream();
			
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(outputStream!=null){
				IOUtils.closeQuietly(outputStream);
			}
			if(inputStream!=null){
				IOUtils.closeQuietly(inputStream);
			}
		}
		
	}
	
	
	/**
	 * 
	 * 方  法  名: viewCurrentImage<br /> 
	 * 描       述:查看当前流程图(方式一)<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: String<br />
	 * 创  建  人:<br /> 
	 * 创建时间: 2016年10月27日 下午2:41:10<br /> 
	 * 修  改  人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("viewCurrentImage")
	public String viewCurrentImage(String taskId,Model model,HttpServletResponse resp){
		ProcessDefinition pd = workflowService.getProcessDefinitionByTaskId(taskId);
		
		// 1. 获取流程部署ID
		model.addAttribute("deploymentId", pd.getId());
		// 2. 获取流程图片的名称
//		model.addAttribute("imageName", pd.getDiagramResourceName());
		// 3.获取当前活动的坐标
		Map<String,Object> currentActivityCoordinates = workflowService.getCurrentActivityCoordinates(taskId);
		model.addAttribute("currentActivityCoordinates", currentActivityCoordinates);
		return "workflow/image";
	}
	
	
	  /**
	   * 
	   * 方  法  名: readResource<br /> 
	   * 描       述:读取带跟踪的图片  当前节点流程图(方式二)<br />
	   * @return<br />
	   * @throws<br />
	   * 返回类型: void<br />
	   * 创  建  人:<br /> 
	   * 创建时间: 2016年10月27日 下午6:04:14<br /> 
	   * 修  改  人:<br />
	   * 修改时间:<br />
	   * 修改备注:<br />
	   *
	   */
    @RequestMapping( "/processImage")
    public void readResource(String taskId, HttpServletResponse response)
            throws Exception {
    	workflowService.getReadResource(taskId, response);
        
    }
}
