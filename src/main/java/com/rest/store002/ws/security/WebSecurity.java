package com.rest.store002.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rest.store002.service.UserService;


@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private final UserService userDetailsService; //this variable is used to implement 'UserDetailsService'
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
		//.antMatchers(HttpMethod.POST, "/users")//this is a hard coded verion of the above line of code
		.permitAll()
		.antMatchers(HttpMethod.GET, SecurityConstants.Verify_Email_URL)
		.permitAll()
		.anyRequest().authenticated().and()
		.addFilter(getAuthenticationFilter())//This line does the same as the below line, but creaates a custom login
//		.addFilter(new AuthenticationFilter(authenticationManager()));//This line creates a new object and has a url of /login
		.addFilter(new AuthorizationFilter(authenticationManager()))//This line add the 'AuthorizationFilter' class to allow the user to access services
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//This tells spring not to make a session, so authentication is always required 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {//This method sets what security service you use and your method of encoding
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	//this method will change the URL for the user login while also providing the filter for the configure() method above
	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
}
