package com.rest.store002.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.store002.SpringApplicationContext;
import com.rest.store002.service.UserService;
import com.rest.store002.shared.dto.UserDto;
import com.rest.store002.ui.model.request.UserLoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
private final AuthenticationManager authenticationManager;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			//get the JSON input sent from the POST request and match it to the class UserLoginRequestModel
			UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);
			//call the UsernamePasswordAuthenticationToken to check if UN and PW match, first authenticated using the AuthManger
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(), creds.getPassword(), new ArrayList<>()
							)
					);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//grab the username from the principal -i.e-user
		String userName = ((User) authResult.getPrincipal()).getUsername();
		//String tokenSecret = new SecurityConstants().getTokenSecret();
		
		//builds a login token for the user to use
		String token = Jwts.builder().setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())//.TOKEN_SECRET)
				.compact();
		
		//creates a bean here of the 'UserServiceImpl' class, can be used to get a user
		UserService userService = (UserService)SpringApplicationContext.getBean("userServiceImpl");
		UserDto user = userService.getUser(userName);
	
		//returns the login token as a header to the user, check the HTTP response header for values
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		//Add the use ID to the header
		response.addHeader("UserID", user.getUserID());
	}
	
}
