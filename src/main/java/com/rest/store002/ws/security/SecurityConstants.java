package com.rest.store002.ws.security;

import com.rest.store002.SpringApplicationContext;

public class SecurityConstants {
	//These variable are to be passed into the 'WebSecurity' class overridden method 'Configure(HTTP http)' method
	public static final long EXPIRATION_TIME = 864000000; //this is for ten days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";//Included in the header to let you know that the information with this is an Authorization token
	public static final String SIGN_UP_URL = "/users/add";//Used to set what end points do not need authentication
	//public static final String TOKEN_SECRET = "jf9i4jgu83nfl0";//Used to encrypt access token
	public static final String Verify_Email_URL = "/users/email-verification";
	
	public static String getTokenSecret() {
		//Using the 'SpringApplicationContext' class we access the bean for 'AppPropeerties' 
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		//then we return the TOKEN_SECRET using the 'AppProperties' class method 'getTokenSecret()'
		return appProperties.getTokenSecret();
	}
}
