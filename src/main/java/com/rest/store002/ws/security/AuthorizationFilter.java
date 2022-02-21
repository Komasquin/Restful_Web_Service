package com.rest.store002.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {
	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override//This method access the request header to allow for authorization
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//First: Grab the header for the request
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		
		//Second: check that the header is not null, or if there is no token prefix
		if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			//IF: not token or header is null - continue to the next step in the chain
			chain.doFilter(request, response);
			return;
		}
		
		//Third: grab the authentication token
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		
		//Fourth: Set as authentication to the 'SecurityContextHolder'
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//Fifth: continue to the next filter in the filter chain
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		//First: read the token from the aothorization header
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		
		//Second: check if token is not null
		if(token != null) {
			//Third: remove the TOKEN_PREFIX
			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
			
			//Fourth: use web token to parse the token, give it the TOKEN_SECRET for decryption and decrypt it, than get the user details
			String user = Jwts.parser()
//					.setSigningKey(SecurityConstants.TOKEN_SECRET)
					.setSigningKey(SecurityConstants.getTokenSecret())
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			
			//Fifth: check if there is a user with those creditials
			if(user != null) {
				//Last: return an object of 'UsernamePasswordAuthenticationToken' using the users information
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}
