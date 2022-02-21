package com.rest.store002.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

	//This variable will access the Application.Properties file
	@Autowired
	private Environment env;
	
	//This method gets the value from the Application.Properties
	public String getTokenSecret() {
		return env.getProperty("tokenSecret");
	}
}
