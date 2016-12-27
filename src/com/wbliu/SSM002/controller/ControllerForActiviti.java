package com.wbliu.SSM002.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



/*设计思路
 * 
 * 1、 1.1、页面提交任务 ；1.2、部署流程(部署一次)；1.3、启动实例（可以启动多次实例）1.4、结束第一个任务；1.5、查询下一个任务）；
 * 2、2.1、页面显示 “xx 审批”；2.2、点击提交，后台；2.3、结束当前任务；2.4、查询下一个任务；循环，直到所有流程执行完毕

 * */


/*测试请假审批流程的controller*/

@Controller
@RequestMapping("/")
public class ControllerForActiviti {
	private Logger log = Logger.getLogger(this.getClass());

	private ApplicationContext ac = new FileSystemXmlApplicationContext(
			"E:/WbliuWorkSpace/SSM002_activitiTest/src/applicationContext.xml");
	private RepositoryService repositoryService = (RepositoryService) ac.getBean("repositoryService");

	private RuntimeService runtimeService = (RuntimeService) ac.getBean("runtimeService");

	private TaskService taskService = (TaskService) ac.getBean("taskService");

	private HistoryService historyService = (HistoryService) ac.getBean("historyService");
	private ProcessEngine processEngine = (ProcessEngine) ac.getBean("processEngine");

	
	
	
	/*部署流程*/
	@RequestMapping("deployMent")
	public ModelAndView deployMent(){
		log.info("deployMent.......");
		String key ="hello";
	   
		key = deploymentProcess();

		System.out.println("key  ="+key);
		ModelAndView model = new ModelAndView();
		
		model.addObject("key",key);
		
		return model;
	}//end function
	
	
	/*启动流程，并结束第一个任务*/
	@RequestMapping("startProcessAndEndFirstTask")
	public ModelAndView startProcessAndEndFirstTask(String key,String name,String day){
		ModelAndView model = new ModelAndView();
		log.info("startProcessAndEndFirstTask-------");
		System.out.println("key  ="+key+" name ="+name+" day="+day);

		
		/* 启动实例 */
		String processInstanceId = startProcess(key);
		
		/*查询当前任务*/
		//Task task = queryTask(processInstanceId);
		
		/* 结束当前任务 */
		//copleteTask(task);
       
		/*model.addObject("processInstanceId", "1233");*/
		model.addObject("processInstanceId", processInstanceId);
		return model;
	}//end function
	
	
	
	@RequestMapping("showHandlePeople")
	public ModelAndView showHandlePeople(String processInstanceId){
		ModelAndView model = new ModelAndView();
		log.info("showHandlePeople--------");
		System.out.println("processInstanceId = "+processInstanceId);
		
		/*查询当前任务*/
	    Task task = queryTask(processInstanceId);
		
		
	    if(task != null){
	    	System.out.println("[task is exist!]");
	    	
	    	model.addObject("taskId", task.getId());
	    	 
	    	if(task.getAssignee()!=null){
	    		
	    		
	    		 
	    		
	    		 model.addObject("assiginee", task.getAssignee());
	    		
	    		 
	    		 
	    	 }else{
	    		 model.addObject("assiginee", "系统");
	    		 
	    	 }
	    	
	    }else{
	    	System.out.println("[task is not exist!]");
	    
	    	model.addObject("taskId", "taskId is not exist");
	    	model.addObject("assiginee", "null");
	    }
		
		return model;
	}//end function

	
	
	/*结束当前任务*/
	@RequestMapping("completeTask")
	public ModelAndView completeTask(String taskId,String processInstanceId,String radio,String day){
		ModelAndView model = new ModelAndView();
		log.info("completeTask------");
		System.out.println("taskId = "+taskId+" processInstanceId ="+processInstanceId+"day = "+day);

		/*转换参数的类型*/
		int radio1 = 0;
		int day1 = 1;
		if(radio !=null){
			radio1 = Integer.parseInt(radio);
		}
		
		if(day!=null){
			day1 = Integer.parseInt(day);
		}

		
		/*查询当前任务*/
		Task task = queryTask(processInstanceId);
		
		/* 结束当前任务 */
		copleteTask(task,radio1,day1);
		
		
		model.addObject("taskId", taskId);
		return model;
	}//end function
	
	
	
	
	/*部署和启动流程实例*/
	@RequestMapping("deploymentAndStartProcess")
	public ModelAndView deploymentAndStartProcess(@RequestParam(value="name") String name,@RequestParam(value="day")String day){
		ModelAndView model = new ModelAndView("index");
		log.info("deploymentAndStartProcess.........");
		System.out.println("name ="+name +" day  = "+day);
		
		/* 部署流程 */
		String key = deploymentProcess();
		Task task = null;
		
		/* 启动实例 */
		String processInstanceId = startProcess(key);

		//判断当前任务是否结束
		if(queryAndCompleteTask(processInstanceId)){
			
			//查询下一个任务
			task = queryTask(processInstanceId);
			
		}else{
			System.out.println("当前任务没有结束.....");
			
		}
	    model.addObject("processInstanceId", processInstanceId);
		model.addObject("assigineName", task.getAssignee());			
		
		
		/*model.addObject("processInstanceId", "123");
		model.addObject("assigineName", "wbliu");
		*/
		
		
		return model;
	}//end function

	
	/*结束当前任务并查找下一个获得的任务*/
	private boolean queryAndCompleteTask(String processInstanceId) {
		boolean flag = false;
		/* 查询任务 */
		Task task = queryTask(processInstanceId);

		/* 结束当前任务 */
		flag = copleteTask(task);

		return flag;
	}//end function
	
	

	
/*审批任务*/
@RequestMapping("handleProcess")
public ModelAndView	handleProcess(String processInstanceId,String assigineName){
	log.info("handleProcess.............");
	
	ModelAndView model = new ModelAndView();
	log.info("processInstanceId = "+processInstanceId+" assigineName = "+assigineName);
	Task task = null;
	
	if(processInstanceId != null){
		
		//判断当前任务是否结束
		if(queryAndCompleteTask(processInstanceId)){
			//查询下一个任务
			task = queryTask(processInstanceId);
			
		}else{
			System.out.println("当前任务没有结束.....");
			
		}
	
		
		
		
		/*model.addObject("processInstanceId", processInstanceId);
		model.addObject("assigineName", assigineName+"123");	*/		
	
		model.addObject("processInstanceId", processInstanceId);
		model.addObject("assigineName", task.getAssignee());			
	
		}
	
		return model;
	}//end function
	
	
	
	
/*获取审批流程的进度*/
@RequestMapping("showProcess2")
public String ac_showProcess2(String processInstanceId) {
	log.info("showProcess2 ......");

	System.out.println("processInstanceId = "+processInstanceId);

	return processInstanceId.toString();
}
	



	/* 获取审批流程的进度 */
	@RequestMapping("showProcess")
public void ac_showProcess(HttpServletRequest request, HttpServletResponse response, String processInstanceId) {

		log.info("showProcess ......");
		System.out.println("processInstanceId = " + processInstanceId);

		String message = "null";
		OutputStream os = null;

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

			if (processInstanceId != null) {
				instanceId = processInstanceId;

				System.out.println("instansceId = " + instanceId);
				// 获取流程实例
				ProcessInstance processInstance = null;
				processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).active()
						.singleResult();

				if (processInstance == null) {

					/*throw new Exception("获取流程异常");*/
					System.out.println("processInstance is null!");
				} else {

					message ="ProcessInstance is exist!";
					BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

					List<HistoricActivityInstance> activityInstances = historyService
							.createHistoricActivityInstanceQuery().processInstanceId(instanceId)
							.orderByHistoricActivityInstanceStartTime().asc().list();

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

					InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", activitiIds, // activitiIds就是标亮走过的节点
							flowIds, // flowIds就是标亮走过的线
							defaultProcessEngine.getProcessEngineConfiguration().getActivityFontName(),
							defaultProcessEngine.getProcessEngineConfiguration().getLabelFontName(), null, 1.0);

					response.setContentType("image/png");

					 os = response.getOutputStream();

					int bytesRead = 0;

					byte[] buffer = new byte[8192];

					while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
						os.write(buffer, 0, bytesRead);

					}

					os.close();
					imageStream.close();
				}

			} else {
				System.out.println("processInstance is not exist!");

			}

		} catch (Exception e) {
			// TODO: handle exception
			//throw new RuntimeException("获取流程图异常!");
		
			System.out.println("获取流程图异常!");
		}

	}// end function

	
	
	
	
	
	
	
	
	/*完成当前任务*/
	private boolean copleteTask(Task task) {
		boolean flag = false;
		
		Map<String ,Object> varMap = new HashMap<String,Object>();

		if(task!=null){
			
			String assigineeName = task.getAssignee();
			
			switch (assigineeName) {

			case "员工wbliu": {
				varMap.put("day", 3);
				taskService.complete(task.getId(), varMap);

				System.out.println("员工任务 结束");
				break;
			}
			case "事业部主任_小李": {

				varMap.put("handle0", 0);
				taskService.complete(task.getId(), varMap);
				System.out.println("事业部主任任务   结束");
				break;
			}
			case "副所长_小刘": {

				varMap.put("handle1", 0);
				taskService.complete(task.getId(), varMap);
				System.out.println("副所长任务   结束");
				break;
			}
			case "所长_老赵": {
				varMap.put("handle2", 0);
				taskService.complete(task.getId(), varMap);
				System.out.println("所长任务   结束");
				break;
			}
			case "系统": {
				taskService.complete(task.getId());
				System.out.println("系统任务   结束");
				break;
			}

			default: {
				System.out.println("没有需要结束的任务....");
			}

			}
			
			//判断任务是否结束
			flag = taskIsActive(task.getId());	

			
		}else{
			
			System.out.println("没有任务要结束");
		}
		
		return flag;
	}

	/*完成当前任务*/
	private boolean copleteTask(Task task,int radio, int day) {
		boolean flag = false;

		Map<String, Object> varMap = new HashMap<String, Object>();

		if (task != null) {

			String assigineeName = task.getAssignee();

			switch (assigineeName) {
			case "员工wbliu": {
				varMap.put("day", day);
				taskService.complete(task.getId(), varMap);

				System.out.println("员工任务 结束");
				break;
			}
			case "事业部主任_小李": {

				varMap.put("handle0", radio);
				taskService.complete(task.getId(), varMap);
				System.out.println("事业部主任任务   结束");
				break;
			}
			case "副所长_小刘": {

				varMap.put("handle1", radio);
				taskService.complete(task.getId(), varMap);
				System.out.println("副所长任务   结束");
				break;
			}
			case "所长_老赵": {
				varMap.put("handle2", radio);
				taskService.complete(task.getId(), varMap);
				System.out.println("所长任务   结束");
				break;
			}
			case "系统": {
				taskService.complete(task.getId());
				System.out.println("系统任务   结束");
				break;
			}

			default: {
				System.out.println("没有需要结束的任务....");
			}

			}

			// 判断任务是否结束
			flag = taskIsActive(task.getId());

		} else {

			System.out.println("没有任务要结束");
		}
		
		return flag;
	}


	
/*判断任务是否结束*/
private boolean taskIsActive(String taskId) {
		boolean flag;
		TaskQuery tq =taskService.createTaskQuery().taskId(taskId).active();
		
		if(tq.list().size() >0){
			flag =false;
		}else{
			flag =true;
		}
		
		return flag;
	}

	

/*查询当前任务*/
private Task queryTask(String processInstanceId) {
		// TODO Auto-generated method stub
	Task task1 = null;
	
	List<Task> taskList = taskService.createTaskQuery()
			.processInstanceId(processInstanceId)
			.orderByTaskCreateTime().asc()
			.list();

	if(taskList!=null &&taskList.size() > 0){
		for(Task task : taskList){
			System.out.println("....查询任务.....");
			System.out.println("Assignee ="+task.getAssignee());
			System.out.println("Id ="+task.getId());
			System.out.println("ProcessInstanceId ="+task.getProcessInstanceId());
			
			task1 = task;
		}
	}

	return task1;
	}





/*启动流程*/
private String startProcess(String key) {
	
	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
	if(processInstance!=null){
		
		System.out.println("..............流程启动.............");
		System.out.println("ProcessInstanceId ="+processInstance.getProcessInstanceId());
		System.out.println("ProcessDefinitionKey ="+processInstance.getProcessDefinitionKey());
		System.out.println("ProcessDefinitionVersion ="+processInstance.getProcessDefinitionVersion());
		System.out.println("Name ="+processInstance.getName());
	
	  return processInstance.getProcessInstanceId();	
	}
	
	 return null;
}





/*部署流程*/
private String deploymentProcess() {
		// TODO Auto-generated method stub
	InputStream inputStream_bpmn = this.getClass().getClassLoader().getResourceAsStream("/diagrams/handleLeaveProcess.bpmn");
	InputStream inputStream_png = this.getClass().getClassLoader().getResourceAsStream("/diagrams/handleLeaveProcess.png");
		
		
	  Deployment deployment = repositoryService.createDeployment()
			  .addInputStream("handleLeaveProcess.bpmn", inputStream_bpmn)
		      .addInputStream("handleLeaveProcess.png", inputStream_png)
		      .name("handleLeaveProcess")
		      .deploy();

		
	  if(deployment!=null){

		  System.out.println("........部署完成......");
		
		  System.out.println("deploymentId = "+deployment.getId());
		  System.out.println("name = "+deployment.getName());
		  System.out.println("DeploymentTime = " + deployment.getDeploymentTime());
	  
		
		return deployment.getName();
	  }
	
	 return null;
   }



/*获得高亮的显示部分*/
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


	
		
}


