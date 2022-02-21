package com.rest.store002.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.rest.store002.shared.dto.UserDto;
//'UserDetailsService' class being extended here allows you to use your service class to override required methods
//this helps in the 'WebSecuirty' class
public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);//C
	UserDto getUser(String email);//R
	List<UserDto> getUsers(int page, int limit);//R
	UserDto updateUser(String id, UserDto user);//U
	void deleteUser(String id);//D
	
	UserDto getUserByUserID(String id);
	boolean verifyEmailToken(String token);
}
