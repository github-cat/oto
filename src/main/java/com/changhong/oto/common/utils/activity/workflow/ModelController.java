package com.changhong.oto.common.utils.activity.workflow;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.changhong.oto.common.utils.activity.domain.ActivitiModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * 类 名 称: ModelController<br />
 * 描 述: 流程模型控制器TODO<br />
 * 创 建 人: <br />
 * 创建时间: 2016年11月2日 下午1:53:54<br />
 * 修 改 人:<br />
 * 修改时间:<br />
 * 修改备注:<br />
 * 版 本: V1.0<br />
 *
 */
@Controller
@RequestMapping("/workflow/model")
public class ModelController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 
	 * 方 法 名: index<br />
	 * 描 述:模型导航页面<br />
	 * @return<br />
	 * @throws<br /> 
	 * 返回类型: ModelAndView<br />
	 * 创 建 人:<br />
	 * 创建时间: 2016年11月2日 下午1:54:17<br />
	 * 修 改 人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("workflow/modeler-list");
		List<Model> models = repositoryService.createModelQuery().list();
		ArrayList<ActivitiModel> activitiModels = new ArrayList<ActivitiModel>();
		for (Model model : models) {
			activitiModels.add(ActivitiModel.ActivitiModelFactory(model));
		}
		mav.addObject("list", activitiModels);
		return mav;

	}

	/**
	 * 模型列表
	 */
	// @RequestMapping("/list")
	// @ResponseBody
	// public List<Model> modelList() {
	// List<Model> list = repositoryService.createModelQuery().list();
	// return list;
	// }
	/**
	 * 
	 * 方 法 名: modelList<br />
	 * 描 述:<br />
	 * 
	 * @return<br />
	 * @throws<br /> 
	 * 返回类型: String<br />
	 * 创 建 人:<br />
	 * 创建时间: 2016年11月2日 下午1:56:19<br />
	 * 修 改 人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/modelEdit")
	public String modelEdit() {
		return "workflow/modeler-edit";
	}

	/**
	 * 
	 * 方 法 名: create<br />
	 * 描 述:创建模型<br />
	 * @return<br />
	 * @throws<br />
	 * 返回类型: void<br />
	 * 创 建 人:<br />
	 * 创建时间: 2016年11月2日 下午2:34:15<br />
	 * 修 改 人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/create")
	public void create(@RequestParam("name") String name,@RequestParam("key") String key,@RequestParam("description") String description,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// 创建编辑的节点
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace",
					"http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			// 创建模型对象
			Model modelData = repositoryService.newModel();

			// 封装模型对象
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,description);

			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			// 保存模型对象
			repositoryService.saveModel(modelData); // 把数据放入model表

			// 保存该模型的编辑信息（用户编辑回显数据的一些细节！！）
			repositoryService.addModelEditorSource(modelData.getId(),
					editorNode.toString().getBytes("utf-8")); // 把数据放入act_ge_bytearray

			// response.sendRedirect(request.getContextPath() +
			// "/service/editor?id=" + modelData.getId());
			// 使用模型信息，打开Activiti-modeler编辑器
			response.sendRedirect(request.getContextPath()
					+ "/modeler.jsp?modelId=" + modelData.getId());
		} catch (Exception e) {
			logger.error("创建模型失败：", e);
		}
	}

	/**
	 * 
	 * 方 法 名: deploy<br />
	 * 描 述:根据Model部署流程<br />
	 * @return<br />
	 * @throws<br /> 
	 * 返回类型: String<br />
	 * 创 建 人:<br />
	 * 创建时间: 2016年11月2日 下午2:34:34<br />
	 * 修 改 人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/deploy/{modelId}")
	public String deploy(@PathVariable("modelId") String modelId,
			RedirectAttributes redirectAttributes) {
		try {
			// 获取模型信息
			Model modelData = repositoryService.getModel(modelId);
			// 读取模型的编辑器内容
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData
							.getId()));
			byte[] bpmnBytes = null;

			// 解析编辑器的内容，把resultful数据转变为一个bpmn模型对象
			BpmnModel model = new BpmnJsonConverter()
					.convertToBpmnModel(modelNode);
			// 再使用BpmnXML转换器，把内容解析为bpmn结构的xml
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);

			// 以二进制方式，读取部署信息
			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment()
					.name(modelData.getName())
					.addString(processName, new String(bpmnBytes)).deploy();
			redirectAttributes.addFlashAttribute("message", "部署成功，部署ID="
					+ deployment.getId());
		} catch (Exception e) {
			logger.error("根据模型部署流程失败：modelId={}", modelId, e);
		}
		return "redirect:/workflow/model";
	}

	/**
	 * 
	 * 方 法 名: deleteModel<br />
	 * 描 述:删除模型<br />
	 * @return<br />
	 * @throws<br /> 
	 * 返回类型: void<br />
	 * 创 建 人:<br />
	 * 创建时间: 2016年10月26日 上午11:57:22<br />
	 * 修 改 人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping("/deleteModel")
	public String deleteModel(String id) {
		repositoryService.deleteModel(id);
		return "redirect:/workflow/model";
	}

	/**
	 * 
	 * 方 法 名: export<br />
	 * 描 述:导出model的xml文件<br />
	 * @return<br />
	 * @throws<br /> 
	 * 返回类型: void<br />
	 * 创 建 人:<br />
	 * 创建时间: 2016年11月2日 下午2:34:44<br />
	 * 修 改 人:<br />
	 * 修改时间:<br />
	 * 修改备注:<br />
	 *
	 */
	@RequestMapping(value = "export/{modelId}")
	public void export(@PathVariable("modelId") String modelId,
			HttpServletResponse response) {
		try {
			Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService
					.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, response.getOutputStream());
			String filename = bpmnModel.getMainProcess().getId()
					+ ".bpmn20.xml";
			response.setHeader("Content-Disposition", "attachment; filename="
					+ filename);
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("导出model的xml文件失败：modelId={}", modelId, e);
		}
	}

}