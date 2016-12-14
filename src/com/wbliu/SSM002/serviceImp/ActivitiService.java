package com.wbliu.SSM002.serviceImp;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.wbliu.SSM002.Utils.GetAapplicationContext;
import com.wbliu.SSM002.service.IactivitiService;

@Service
public class ActivitiService implements IactivitiService {

	
	
	 //private String beanXMLPath= this.getClass().getResource("/")+"applicationContext.xml";
	
	 /*private ApplicationContext ac = new FileSystemXmlApplicationContext("E:/WbliuWorkSpace/SM002/src/applicationContext.xml");
	 private RepositoryService repositoryService = (RepositoryService) ac.getBean("repositoryService");	
*/
	
	@Override
	public Deployment deployment() {
		
		// TODO Auto-generated method stub
	   	 Deployment deployMent = null;

		return deployMent;
	}

}
