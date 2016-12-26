package com.wbliu.SSM002.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class LoginController {
	private Logger log = Logger.getLogger(this.getClass());

	// private IactivitiService activitiService;

	private ApplicationContext ac = new FileSystemXmlApplicationContext(
			"E:/WbliuWorkSpace/SSM002_activitiTest/src/applicationContext.xml");
	private RepositoryService repositoryService = (RepositoryService) ac.getBean("repositoryService");

	private RuntimeService runtimeService = (RuntimeService) ac.getBean("runtimeService");

	private TaskService taskService = (TaskService) ac.getBean("taskService");

	private HistoryService historyService = (HistoryService) ac.getBean("historyService");
	private ProcessEngine processEngine = (ProcessEngine) ac.getBean("processEngine");

	/* deployMentProcess */
	@RequestMapping("/ac_deployMent")
	public void ac_deployMent() {

		Deployment deployMent = repositoryService.createDeployment()
				.addClasspathResource("com/wbliu/SSM002/activiti/diagrams/leaveProcess.bpmn")
				.addClasspathResource("com/wbliu/SSM002/activiti/diagrams/leaveProcess.png").name("leaveProcess002")
				.deploy();

		System.out.println("deployMentId =" + deployMent.getId());
		System.out.println("deployMentName =" + deployMent.getName());
		System.out.println("deployMentTime =" + deployMent.getDeploymentTime());

	}

	@RequestMapping("/ac_deployMentWithZIP")
	public void deployWithZIP() {
		log.info("deployWithZIP------");

		String processZipPath = "com/wbliu/SSM002_activitiTest/activiti/diagrams/leaveProcess.zip";
		System.out.println("processZipPath = " + processZipPath);
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(processZipPath);

		Deployment deployMent = repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream))
				.name("leaveProcessZIP").deploy();

		System.out.println("id =" + deployMent.getId());
		System.out.println("name " + deployMent.getName());
		System.out.println("DeploymentTime " + deployMent.getDeploymentTime());

	}

	/* startProcess */
	@RequestMapping("/ac_startProcess")
	void startProcess() {
		log.info("startProcess------------");

		ProcessInstance processInstance = runtimeService
				/* .startProcessInstanceByKey("leaveProcess001"); */
				.startProcessInstanceByKey("firstProcess");
		/* .startProcessInstanceById("2501"); */

		System.out.println("getId =" + processInstance.getId());
		System.out.println("getBusinessKey =" + processInstance.getBusinessKey());
		System.out.println("getDescription =" + processInstance.getDescription());
		System.out.println("getDeploymentId =" + processInstance.getDeploymentId());
		System.out.println("getProcessInstanceId =" + processInstance.getProcessInstanceId());
		System.out.println("getActivityId =" + processInstance.getActivityId());
		System.out.println("getProcessDefinitionId =" + processInstance.getProcessDefinitionId());
		System.out.println("getProcessDefinitionKey =" + processInstance.getProcessDefinitionKey());
		System.out.println("getProcessDefinitionName =" + processInstance.getProcessDefinitionName());
		System.out.println("getProcessDefinitionVersion =" + processInstance.getProcessDefinitionVersion());

	}

	/* findTask */
	@RequestMapping("/ac_findTask")
	void findTask() {
		log.info("ac_findTask------------");
		List<Task> taskList = taskService.createTaskQuery().taskAssignee("领导A").list();

		for (Task t : taskList) {

			System.out.println("id = " + t.getId());
			System.out.println("Assignee = " + t.getAssignee());
			System.out.println("Name = " + t.getName());
			System.out.println("TaskDefinitionKey = " + t.getTaskDefinitionKey());
			System.out.println("FormKey = " + t.getFormKey());
			System.out.println("ExecutionId = " + t.getExecutionId());
			System.out.println(" ProcessInstanceId= " + t.getProcessInstanceId());
			System.out.println(" TaskDefinitionKey= " + t.getTaskDefinitionKey());
		}

	}

	/* completeTask */
	@RequestMapping("/ac_completeTask")
	void completeTask() {
		log.info("completeTask------");

		taskService.complete("15008");
		System.out.println("task 15008 is complete!");
	}

	/* findProcessDefinition */
	@RequestMapping("/ac_findProcessDefinition")
	public void findProcessDefinition() {

		log.info("findProcessDefinition.......");

		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()

				/* where */
				/* .orderByDeploymentId() */

				/* order */
				.orderByProcessDefinitionVersion().asc()

				/* result */
				.list();

		if (processDefinitionList != null && processDefinitionList.size() > 0) {

			for (ProcessDefinition pd : processDefinitionList) {

				System.out.println("-------------------------");
				System.out.println("id = " + pd.getId());
				System.out.println("key = " + pd.getKey());
				System.out.println("Name = " + pd.getName());
				System.out.println("Version = " + pd.getVersion());
				System.out.println("DeploymentId = " + pd.getDeploymentId());

			}

		}

	}

	/* findLastProcessDefinition */
	@RequestMapping("/ac_findLastProcessDefinition")
	public void findLastProcessDefinition() {

		log.info("findLastProcessDefinition.......");

		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()

				/* where */
				/* .orderByDeploymentId() */

				/* order */
				.orderByProcessDefinitionVersion().asc()

				/* result */
				.list();

		if (processDefinitionList != null && processDefinitionList.size() > 0) {

			Map<String, ProcessDefinition> processDefinitionMap = new HashMap<>();
			for (ProcessDefinition pd : processDefinitionList) {
				processDefinitionMap.put(pd.getKey(), pd);
			}
			List<ProcessDefinition> processDefinitionList2 = new ArrayList<>(processDefinitionMap.values());

			for (ProcessDefinition pd : processDefinitionList2) {
				System.out.println("-------------------------");
				System.out.println("id = " + pd.getId());
				System.out.println("key = " + pd.getKey());
				System.out.println("Name = " + pd.getName());
				System.out.println("Version = " + pd.getVersion());
				System.out.println("DeploymentId = " + pd.getDeploymentId());
			}

		}

	}

	/* delteProcessDefinition */
	@RequestMapping("/ac_deleteProcessDefinition")
	public void delteProcessDefinition() {
		log.info("deleteProcessDefinition--------");

		String deploymentId = "7501";
		/* common deletion */

		/* repositoryService.deleteDeployment(deploymentId); */

		/*--------------------------------------*/
		/* cascading deletion */

		/* function 1: */
		repositoryService.deleteDeployment(deploymentId, true);

		/* function 2 */
		/* repositoryService.deleteDeploymentCascade(deploymentId); */

		System.out.println("deleteProcessDefinition ok");

	}

	/* deleteProcessDefinitionByKey */

	@RequestMapping("/ac_deleteProcessDefinitionByKey")
	public void deleteProcessDefinitionByKey() {
		log.info("deleteProcessDefinitionByKey-------");

		String processDefinitionKey = "myProcess";
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).list();

		if (processDefinitionList != null && processDefinitionList.size() > 0) {
			for (ProcessDefinition pd : processDefinitionList) {

				System.out.println("processDefinitionId = " + pd.getDeploymentId() + " key  = " + pd.getKey()
						+ " name = " + pd.getName() + " deleted");
				repositoryService.deleteDeployment(pd.getDeploymentId(), true);
			}
		}

		System.out.println("deleteProcessDefinitionByKey ok");
	}

	/* queryProcessImage */

	@RequestMapping("/ac_queryProcessImage")
	public void queryProcessImage() {
		log.info("queryProcessImage--------");

		String processDefinitionId = "10001";

		/* function 1 */
		/*
		 * InputStream inputStream =
		 * repositoryService.getResourceAsStream(processDefinitionId,
		 * "com/wbliu/SSM002/activiti/diagrams/leaveProcess.png");
		 * 
		 * try { FileUtils.copyInputStreamToFile(inputStream, new
		 * File("E:/ActivitiTest/leaveProcess.png"));
		 * 
		 * 
		 * System.out.println("请到  E:/ActivitiTest 查看流程图"); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		/* function 2 */

		List<String> definitionResourceNames = repositoryService.getDeploymentResourceNames(processDefinitionId);

		String resourceName = null;
		if (definitionResourceNames != null && definitionResourceNames.size() > 0) {
			for (String name : definitionResourceNames) {

				if (name.endsWith(".png")) {
					resourceName = name;
				} else {
					continue;
				}

			}
		}

		System.out.println("resourceName  =" + resourceName);

		InputStream inputStream = repositoryService.getResourceAsStream(processDefinitionId, resourceName);

		try {
			FileUtils.copyInputStreamToFile(inputStream, new File("E:/ActivitiTest/leaveProcess002.png"));

			System.out.println("请到  E:/ActivitiTest 查看流程图");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* isProcessEnd */
	@RequestMapping("/ac_isProcessEnd")
	public void isProcessEnd() {
		log.info("isProcessEnd.....");

		String processInstanceId = "15005";
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		if (processInstance == null) {
			System.out.println("processInstance is end");
		} else {
			System.out.println("processInstance is not end!");
		}
	}

	@RequestMapping("/ac_findHistoryTask")
	public void findHistoryTask() {
		log.info("findHistoryTask--------");

		String assigneeName = "领导A";
		List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(assigneeName).list();

		if (historicTaskInstanceList != null && historicTaskInstanceList.size() > 0) {

			for (HistoricTaskInstance hti : historicTaskInstanceList) {
				System.out.println("--------------------------");
				System.out.println("Assignee = " + hti.getAssignee());
				System.out.println("Name = " + hti.getName());
				System.out.println("Id = " + hti.getId());
				System.out.println("ExecutionId = " + hti.getExecutionId());
				System.out.println("ProcessInstanceId = " + hti.getProcessInstanceId());
				System.out.println("StartTime = " + hti.getStartTime());
				System.out.println("EndTime = " + hti.getEndTime());
			}

		}

	}// end function

	/* findHistoryProcessInstance */
	@RequestMapping("ac_findHistoryProcessInstance")
	public void findHistoryProcessInstance() {
		log.info("findHistoryProcessInstance-------");

		String historyProcessInstanceId = "10005";
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(historyProcessInstanceId).singleResult();

		if (historicProcessInstance != null) {

			System.out.println("Id = " + historicProcessInstance.getId());
			System.out.println("Name = " + historicProcessInstance.getName());
			System.out.println("StartTime = " + historicProcessInstance.getStartTime());
			System.out.println("EndTime = " + historicProcessInstance.getEndTime());
			System.out.println("ProcessDefinitionId = " + historicProcessInstance.getProcessDefinitionId());
		} else {

			System.out.println("have no HistoryProcessInstance......");
		}

	}// end function

	/* ExclusiveGateWay Test */

	/* deployment */
	@RequestMapping("ac_ExclusiveGateWayDeployment")
	public void deployment() {
		log.info("ExclusiveGateWayDeployment..........");

		/*
		 * load bpmn file from current package Path InputStream inputStream_bpmn
		 * = this.getClass().getResourceAsStream("exclusiveGateway.bpmn");
		 * 
		 * load bpmn file from class Path InputStream inputStream_bpmn =
		 * this.getClass().getResourceAsStream("/exclusiveGateway.bpmn");
		 * 
		 * or InputStream inputStream_bpmn =
		 * this.getClass().getClassLoader().getResourceAsStream(
		 * "exclusiveGateway.bpmn");
		 */

		InputStream inputStream_bpmn = this.getClass()
				.getResourceAsStream("../activiti/diagrams/exclusiveGateway.bpmn");
		InputStream inputStream_png = this.getClass().getResourceAsStream("../activiti/diagrams/exclusiveGateway.png");

		Deployment deployment = repositoryService.createDeployment()
				.addInputStream("exclusiveGateway.bpmn", inputStream_bpmn)
				.addInputStream("exclusiveGateway.png", inputStream_png).name("ExclusiveGateWayTest001").deploy();

		if (deployment != null) {
			System.out.println("id = " + deployment.getId());
			System.out.println("name = " + deployment.getName());
			System.out.println("DeploymentTime = " + deployment.getDeploymentTime());
		}
	}// end function

	/* startProcessInstance */
	@RequestMapping("ac_ExclusiveGateWayStartProcessInstance")
	public void startProcessInstance() {
		log.info("ExclusiveGateWayStartProcessInstance........");
		String processInstanceKey = "ExclusiveGateWay";
		/*
		 * String processInstanceKey ="ExclusiveGateWayTest001";
		 */
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey);

		if (processInstance != null) {
			System.out.println("id = " + processInstance.getId());
			System.out.println("name = " + processInstance.getName());
			System.out.println("ProcessDefinitionId = " + processInstance.getProcessDefinitionId());
			System.out.println("ProcessDefinitionId = " + processInstance.getProcessDefinitionId());

		}

	}// end function

	/* get personalTaskMessage */

	@RequestMapping("/ac_getPersionalTaskMessage")
	public void getPersonalTaskMessage() {
		System.out.println("getPersionalTaslMessage......");

		/* String assignee = "申请人_大龙"; */
		String assignee = "部门经理_小张";

		List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();

		if (taskList != null && taskList.size() > 0) {

			for (Task t : taskList) {

				System.out.println("Assignee =" + t.getAssignee());
				System.out.println("id =" + t.getId());
				System.out.println("Name =" + t.getName());
				System.out.println("ProcessDefinitionId =" + t.getProcessDefinitionId());
				System.out.println("ProcessInstanceId =" + t.getProcessInstanceId());
			} // end for
		}

	}// end function

	/* complete myPersonalTask */

	@RequestMapping("ac_completeMyPersonalTask")
	public void completeMyPersonalTask() {
		System.out.println("completeMyPersionalTask.....");
		/*
		 * String taskId ="32504"; Map<String,Object> variables = new
		 * HashMap<String,Object>(); variables.put("money", 600);
		 * taskService.complete(taskId,variables);
		 */
		String taskId = "35004";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("money", 600);
		taskService.complete(taskId);

		System.out.println("complete My PersonalTask and set variables for 'money'  ");

	}// end function

	/* parallelGatewayTest */
	/* parallelGatewayDeploymentProcess */
	@RequestMapping("/ac_parallelGatewayDeploymentProcess")
	public void parallelGatewayDeploymentProcess() {
		log.info("parallelGatewayDeploymentProcess.......");

		InputStream inputStream_bpmn = this.getClass().getResourceAsStream("../activiti/diagrams/parallelGateway.bpmn");
		InputStream inputStream_png = this.getClass().getResourceAsStream("../activiti/diagrams/parallelGateway.png");

		Deployment deployment = repositoryService.createDeployment()
				.addInputStream("parallelGateway.bpmn", inputStream_bpmn)
				.addInputStream("parallelGateway.png", inputStream_png).name("parallelGatewayTest").deploy();

		if (deployment != null) {
			System.out.println("id = " + deployment.getId());
			System.out.println("name = " + deployment.getName());
			System.out.println("DeploymentTime = " + deployment.getDeploymentTime());
		}
	}// end function

	/* parallelGateway Start ProcessInstance */
	@RequestMapping("ac_parallelGatewayStartProcessInstance")
	public void parallelGatewayStartProcessInstance() {
		log.info("parallelGatewayStartProcessInstance.....");

		String deploymentKey = "simpleParallelGatewayTest";

		ProcessInstance pi = runtimeService.startProcessInstanceByKey(deploymentKey);

		if (pi != null) {
			System.out.println("getProcessDefinitionKey =" + pi.getProcessDefinitionKey());
			System.out.println("ProcessDefinitionId =" + pi.getProcessDefinitionId());
			System.out.println("Id =" + pi.getId());
			System.out.println("Name =" + pi.getProcessDefinitionName());
			System.out.println("ProcessInstanceId =" + pi.getProcessInstanceId());
		}
	}// end function

	/* parallelGateway findMyPersonalTask */

	@RequestMapping("ac_parallelGatewayFindMyPersonalTask")
	public void parallelGatewayFindMyPersonalTask() {
		log.info("parallelGatewayFindMyPersonalTask.....");

		String processInstanceId = "57505";

		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskCreateTime()
				.asc().list();

		if (taskList != null && taskList.size() > 0) {
			for (Task t : taskList) {
				System.out.println("-------------");
				System.out.println("id =" + t.getId());
				System.out.println("Name =" + t.getName());
				System.out.println("ExecutionId =" + t.getExecutionId());
				System.out.println("ProcessDefinitionId =" + t.getProcessDefinitionId());
				System.out.println("ProcessInstanceId =" + t.getProcessInstanceId());
				System.out.println("CreateTime =" + t.getCreateTime());
				System.out.println("Assignee =" + t.getAssignee());
			}

		}

	}// end function

	/* completeMytask */

	@RequestMapping("ac_parallelGatewayCompleteMyTask")
	public void parallelGatewayCompleteMyTask() {
		log.info("parallelGatewayComplete.....");

		/* String taskId ="45007"; */
		/* String taskId ="45010"; */


		 /*String taskId ="62502";*/ 
		 String taskId = "60002";

		taskService.complete(taskId);

		System.out.println("task [ id  =" + taskId + " ] is over");

	}// end function

	/* showProcess */
	// @RequestMapping("ac_showProcess")
	public void showProcess() {
		log.info("showProcess ......");
		// bpmnView();

	}// end function

	@RequestMapping("ac_showProcess")
	public void bpmnView(HttpServletRequest request, HttpServletResponse response) {
		log.info("showProcess ......");
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {

			// 获取流程实例ID
			/*
			 * String instanceId =
			 * ServletRequestUtils.getStringParameter(request, "instanceId",
			 * "");
			 */
			String instanceId = "57505";

			// 获取流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(instanceId)
					.active()
					.singleResult();

			if (processInstance == null) {
				
				throw new Exception("获取流程异常");
			} else {

				BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

				List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
												.processInstanceId(instanceId)
												.orderByHistoricActivityInstanceStartTime().asc()
												.list();

				List activitiIds = new ArrayList();
				List flowIds = new ArrayList();

				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());

				flowIds = getHighLightedFlows(processDefinition, activityInstances);// 获取流程走过的线
																					// (getHighLightedFlows是下面的方法)

				for (HistoricActivityInstance hai : activityInstances) {
					activitiIds.add(hai.getActivityId());// 获取流程走过的节点
				}

				// 设置图片的字体
				ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) processEngine;
				// ProcessEngines.getDefaultProcessEngine();

				defaultProcessEngine.getProcessEngineConfiguration().setActivityFontName("宋体"); // 有中文的话防止图片中出现乱码，否则会显示类似于“□”这样的字
				defaultProcessEngine.getProcessEngineConfiguration().setLabelFontName("宋体");

				Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

				ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();

				InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, 
																					"png", 
																					activitiIds, // activitiIds就是标亮走过的节点
																					flowIds, // flowIds就是标亮走过的线
																					defaultProcessEngine.getProcessEngineConfiguration().getActivityFontName(),
																					defaultProcessEngine.getProcessEngineConfiguration().getLabelFontName(), 
																					null, 
																					1.0);

				
				  
				
				/*processImageUrl = this.getClass().getClassLoader().getResource("/")+"ProcessImage/"+processInstance.getProcessInstanceId()+"_"+processInstance.getProcessDefinitionKey()+".png";
				
				File processImage = new File(processImageUrl);
			
		        
				
				FileUtils.copyInputStreamToFile(imageStream, processImage);
				
				
				
				
				
				
				
					
				return processImageUrl;
			*/	
				
				response.setContentType("image/png");

				OutputStream os = response.getOutputStream();

				int bytesRead = 0;

				byte[] buffer = new byte[8192];

				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				
				}
                  
				os.close();
				imageStream.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("获取流程图异常!");
		}

	}

	
	
	
	
	
	private List getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List historicActivityInstances) {

		List highFlows = new ArrayList();// 用 以保存高亮的线flowId

		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(((HistoricActivityInstance) historicActivityInstances.get(i)).getActivityId());// 得到节点定义的详细信息

			List sameStartTimeNodes = new ArrayList(); // 用以保存后需开始时间相同的节点

			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(((HistoricActivityInstance) historicActivityInstances.get(i + 1)).getActivityId()); // 将后面第一个节点放在时间相同节点的集合里

			sameStartTimeNodes.add(sameActivityImpl1);

			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {

				HistoricActivityInstance activityImpl1 = (HistoricActivityInstance) historicActivityInstances.get(j); // 后续第一个节点
				HistoricActivityInstance activityImpl2 = (HistoricActivityInstance) historicActivityInstances
						.get(j + 1); // 后续第二个节点

				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) { // 如果第一个节点和第二个节点开始时间相同保存

					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());

					sameStartTimeNodes.add(sameActivityImpl2);

				} else {
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线

			for (PvmTransition pvmTransition : pvmTransitions) { // 对所有的线进行遍历

				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示

				if (sameStartTimeNodes.contains(pvmActivityImpl)) {

					highFlows.add(pvmTransition.getId());

				}

			}

		}
		return highFlows;
	}
	
	
	
	
	
	/*请假审批流程演示*/
	
	
	/*部署流程*/
	@RequestMapping("ac_deploymentProcess")
	@ResponseBody
	public ModelAndView ac_deploymentProcess(){
		log.info("ac_deploymentProcess............");
		ModelAndView mv = new ModelAndView("index");
		Map map = new HashMap();

	  InputStream inputStream_bpmn = this.getClass().getClassLoader().getResourceAsStream("/diagrams/activitiDagram001.bpmn");
	  InputStream inputStream_png = this.getClass().getClassLoader().getResourceAsStream("/diagrams/activitiDagram001.png");
		
		
	  Deployment deployment = repositoryService.createDeployment()
			  .addInputStream("activitiDagram001.bpmn", inputStream_bpmn)
		      .addInputStream("activitiDagram001.png", inputStream_png)
		      .name("activitiDagram001")
		      .deploy();

		
	  if(deployment!=null){
		  
		System.out.println("deploymentId = "+deployment.getId());
		System.out.println("name = "+deployment.getName());
		System.out.println("DeploymentTime = "+deployment.getDeploymentTime());
		map.put("deploymentKey", deployment.getName());
	  }else{
		  map.put("deploymentKey", "123");
	  }
		
		mv.addObject("map", map);
		return mv;
	}//end function
	
	/*启动流程*/
	
	/*ac_startProcess2*/
	@RequestMapping("ac_startProcess2")
	@ResponseBody
	public String ac_startProcess2(String deploymentKey){
		log.info("ac_startProcess.....deploymentKey = "+deploymentKey);
		String processInstanceId ="123";
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(deploymentKey);
		if(processInstance!=null){
			System.out.println("ProcessInstanceId ="+processInstance.getProcessInstanceId());
			System.out.println("ProcessDefinitionKey ="+processInstance.getProcessDefinitionKey());
			System.out.println("ProcessDefinitionVersion ="+processInstance.getProcessDefinitionVersion());
			System.out.println("Name ="+processInstance.getName());
		
		  processInstanceId =processInstance.getProcessInstanceId();
		}
		return processInstanceId;
	}//end function


	/*员工wbliu请假*/
	@RequestMapping("ac_wbliu")
	public String ac_wbliu(String assigneName,String processInstanceId){
		log.info("ac_wbliu....");
		String taskId ="123";
		System.out.println("assigneName = "+assigneName+"  processInstansceId ="+processInstanceId);
		
		
		List<Task> taskList = taskService.createTaskQuery()
				.taskAssignee(assigneName)
				.processInstanceId(processInstanceId)
				.orderByTaskCreateTime().asc()
				.list();

		if(taskList!=null &&taskList.size() > 0){
			for(Task task : taskList){
				
				System.out.println("Assignee = "+task.getAssignee());
				System.out.println("ProcessInstanceId = "+task.getProcessInstanceId());
				System.out.println("CreateTime = "+task.getCreateTime());
				System.out.println("Id = "+task.getId());

				if(task.getAssignee().equals(assigneName)){
					System.out.println("员工wbliu，将要请假...+id ="+task.getId());
					
					/*设置变量的值*/
					Map variableMap = new HashMap<String,Object>();
					variableMap.put("day", 3);
					

					taskService.complete(task.getId(), variableMap);
					
				}else{
					continue;
				}
			}
		}
		
		
		return taskId;
	}//end function

	
	/*事业部主任_小李*/
	@RequestMapping("ac_xiaoLi")
	public String ac_xiaoLi(String assigineeName,String processInstanceId){
		log.info("ac_xiaoLi....");
		String taskId ="123";
		System.out.println("assigineeName = "+assigineeName+"  processInstansceId ="+processInstanceId);
		List<Task> taskList = taskService.createTaskQuery()
				.taskAssignee(assigineeName)
				.processInstanceId(processInstanceId)
				.orderByTaskCreateTime().asc()
				.list();
		
		if(taskList!=null &&taskList.size() > 0){
			for(Task task : taskList){
				
				System.out.println("Assignee = "+task.getAssignee());
				System.out.println("ProcessInstanceId = "+task.getProcessInstanceId());
				System.out.println("CreateTime = "+task.getCreateTime());
				System.out.println("Id = "+task.getId());
				
				if(task.getAssignee().equals(assigineeName)){
					System.out.println("事业部主任_小李，将要审批...+id ="+task.getId());
					
					taskId = task.getId();
					taskService.complete(taskId);
					
				}else{
					continue;
				}
			}
		}
		
		
		return taskId;
	}//end function
	
	
	/*副所长_小刘审批*/
	@RequestMapping("ac_xiaoLiu")
	public Map ac_xiaoLiu(String assigneName,String processInstanceId){
		log.info("ac_xiaoLiu....");
		
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String taskId ="123";
		System.out.println("assigneName = "+assigneName+"  processInstansceId ="+processInstanceId);
		
		
		List<Task> taskList = taskService.createTaskQuery()
				.taskAssignee(assigneName)
				.processInstanceId(processInstanceId)
				.orderByTaskCreateTime().asc()
				.list();
		
		if(taskList!=null &&taskList.size() > 0){
			for(Task task : taskList){
				
				System.out.println("Assignee = "+task.getAssignee());
				System.out.println("ProcessInstanceId = "+task.getProcessInstanceId());
				System.out.println("CreateTime = "+task.getCreateTime());
				System.out.println("Id = "+task.getId());
				
				if(task.getAssignee().equals(assigneName)){
					System.out.println("副所长——小刘将要审批，...+id ="+task.getId());
					taskId = task.getId();
					//int day  = (int) taskService.getVariable(processInstanceId, "day");

					//System.out.println("-----员工wbliu请假"+day+"天，副所长小刘已经审批了-----");
					
					returnMap.put("taskId", taskId);
					returnMap.put("day", 3+"");
				
					/*	设置变量的值*/
					taskService.complete(taskId);
				}else{
					continue;
				}
			}
		}
		
		return returnMap;
	}//end function
	
	/*所长_老赵审批*/
	@RequestMapping("ac_LaoZhao")
	public Map ac_LaoZhao(String assigneName,String processInstanceId){
		log.info("ac_LaoZhao....");
		
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String taskId ="123";
		System.out.println("assigneName = "+assigneName+"  processInstansceId ="+processInstanceId);
		
		
		List<Task> taskList = taskService.createTaskQuery()
				.taskAssignee(assigneName)
				.processInstanceId(processInstanceId)
				.orderByTaskCreateTime().asc()
				.list();
		
		if(taskList!=null &&taskList.size() > 0){
			for(Task task : taskList){
				
				System.out.println("Assignee = "+task.getAssignee());
				System.out.println("ProcessInstanceId = "+task.getProcessInstanceId());
				System.out.println("CreateTime = "+task.getCreateTime());
				System.out.println("Id = "+task.getId());
				
				if(task.getAssignee().equals(assigneName)){
					System.out.println("所长——老赵将要审批，...+id ="+task.getId());
					taskId = task.getId();
					//int day  = (int) taskService.getVariable(processInstanceId, "day");
					
					//System.out.println("-----员工wbliu请假"+day+"天，副所长小刘已经审批了-----");
					
					returnMap.put("taskId", taskId);
					returnMap.put("day", 3+"");
					
					/*	设置变量的值*/
					taskService.complete(taskId);
				}else{
					continue;
				}
			}
		}
		
		return returnMap;
	}//end function
	
	
	
	
	

	
	/*获取审批流程的进度*/
	
	@RequestMapping("ac_showProcess2")
	public void ac_showProcess(HttpServletRequest request, HttpServletResponse response,String processInstanceId) {
	
		log.info("showProcess2 ......");
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			// 获取流程实例ID
			/*
			 * String instanceId =
			 * ServletRequestUtils.getStringParameter(request, "instanceId",
			 * "");
			 */
			String instanceId = "57505";
			if(processInstanceId!=null){
				instanceId = processInstanceId;
			}

			System.out.println("instansceId = "+instanceId);
			// 获取流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(instanceId)
					.active()
					.singleResult();

			
			
			if (processInstance == null) {
				
				throw new Exception("获取流程异常");
			} else {

				BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

				List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
												.processInstanceId(instanceId)
												.orderByHistoricActivityInstanceStartTime().asc()
												.list();

				List activitiIds = new ArrayList();
				List flowIds = new ArrayList();

				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());

				flowIds = getHighLightedFlows(processDefinition, activityInstances);// 获取流程走过的线
																					// (getHighLightedFlows是下面的方法)

				for (HistoricActivityInstance hai : activityInstances) {
					activitiIds.add(hai.getActivityId());// 获取流程走过的节点
				}

				// 设置图片的字体
				ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) processEngine;
				// ProcessEngines.getDefaultProcessEngine();

				defaultProcessEngine.getProcessEngineConfiguration().setActivityFontName("宋体"); // 有中文的话防止图片中出现乱码，否则会显示类似于“□”这样的字
				defaultProcessEngine.getProcessEngineConfiguration().setLabelFontName("宋体");

				Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

				ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();

				InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, 
																					"png", 
																					activitiIds, // activitiIds就是标亮走过的节点
																					flowIds, // flowIds就是标亮走过的线
																					defaultProcessEngine.getProcessEngineConfiguration().getActivityFontName(),
																					defaultProcessEngine.getProcessEngineConfiguration().getLabelFontName(), 
																					null, 
																					1.0);
				
				response.setContentType("image/png");

				OutputStream os = response.getOutputStream();

				int bytesRead = 0;

				byte[] buffer = new byte[8192];

				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				
				}
                  
				os.close();
				imageStream.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("获取流程图异常!");
		}

	}//end function
	
	
	
	
}
