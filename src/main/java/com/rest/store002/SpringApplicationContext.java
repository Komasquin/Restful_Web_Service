package com.rest.store002;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

//This class helps you create beans to be used before the Application, this class has to implement 'ApplicationContextAware'
//SecurityConstants uses it to call a bean declared in the main method
public class SpringApplicationContext implements ApplicationContextAware {

	public static ApplicationContext CONTEXT;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext; 
		
	}
	
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

}
