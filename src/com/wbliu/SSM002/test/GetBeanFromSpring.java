package com.wbliu.SSM002.test;

import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/*测试获得Bean的方法*/
public class GetBeanFromSpring {

	
	
	/* 通过“ApplicationContext”对象，加载配置文件来获得配置的bean 
	 * 说明：这种方式适用于采用Spring框架的独立应用程序，需要程序通过配置文件手工初始化Spring。
	 * */
	//@Test
	public void getBeanFromBeanXML() {
		String beanXMLPath = this.getClass().getResource("/") + "applicationContext.xml";
		/*
		 * ApplicationContext ac = new FileSystemXmlApplicationContext(
		 * "E:/WbliuWorkSpace/SM002/src/applicationContext.xml");
		 */
		ApplicationContext ac = new FileSystemXmlApplicationContext(beanXMLPath);
		RepositoryService repositoryService = (RepositoryService) ac.getBean("repositoryService");

		
		printResulut(repositoryService);
	}	
	
	
	public void printResulut(RepositoryService repositoryService){
		System.out.println("result is  "+repositoryService != null ? "ok" : "erro");
	}
	
	
	
	
	

	   @Test
	public  void testClassLoader(){
		   
		   System.out.println(this.getClass().getClassLoader().getResourceAsStream(""));
	   }
	   	
	
	
}
