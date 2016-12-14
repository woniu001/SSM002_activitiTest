package com.wbliu.SSM002.Utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class GetAapplicationContext {
	
	  private static String str;
	  private static ApplicationContext ac = null;

	  
	  private GetAapplicationContext(){
		  String beanXMLPath= this.getClass().getResource("/")+"applicationContext.xml"; 
		  
		  str = beanXMLPath;
	  }

	  
	  public static ApplicationContext getApplicationContextInstance(){
		  if(ac == null)
		   ac =new FileSystemXmlApplicationContext(str); 

		  return ac ;
	  } 

}
