package com.wbliu.SSM002.activiti;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HelloActiviti {

/*获得各种引擎*/
	
	//ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
	

	/*@Autowired(required =true)
	@Autowired(required = false)
	private RuntimeService runtimeService;
	@Autowired(required = false) 
	private TaskService taskService;
	@Autowired(required = false)
	 private HistoryService historyService;
	@Autowired(required = false) 
	private ManagementService managementService;
	*/
	
	
/*deployment activitiEngine*/


/**
 * 
 */
//@Test
public void deployProcess(){
 
	 //RepositoryService repositoryService = pe.getRepositoryService();
	 
	/* Deployment deployMent = repositoryService.createDeployment()
		 .addClasspathResource("diagrams/helloWorld.bpmn")
		 .addClasspathResource("diagrams/helloWorld.png")
		 .name("helloWorld001")
		 .deploy();
	
System.out.println("deployMentId ="+deployMent.getId());
System.out.println("deployMentName ="+deployMent.getName());
System.out.println("deployMentTime ="+deployMent.getDeploymentTime());*/

 }
  




	
}
