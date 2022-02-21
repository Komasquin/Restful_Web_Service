package com.rest.store002.service.imlp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rest.store002.io.entity.UserEntity;
import com.rest.store002.repository.UserRepo;
import com.rest.store002.shared.Utils;
import com.rest.store002.shared.dto.AddressDto;
import com.rest.store002.shared.dto.UserDto;


//might have to add JUnit 5 to build path
//when you are testing actual data in a database this is called an integration test, if you are testing dummy data that is called a unit test
class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepo userRepo;
	
	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	UserEntity userEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userEntity = new UserEntity();
		
		userEntity.setId(1L);
		userEntity.setFirstName("mickey");
		userEntity.setUserID("g44r");
		userEntity.setEncryptedPassword("g44r");
		userEntity.setEmail("moss@moss.com");
		userEntity.setEmailVerificationToken("fhfhfh");
	}

	@Test
	void testGetUser() {
		
		when(userRepo.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDTO = userService.getUser("mickey@gmail.com");
		
		assertNotNull(userDTO);
		assertEquals("mickey", userDTO.getFirstName());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, 
					() -> {
						userService.getUser("moss@moss.com");
					}
				);
	}
	
	@Test//You can test all setter methods here too, not just the few we are
	final void testCreateUser() {
	
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		when(utils.generateID(anyInt())).thenReturn("hgrnghtrir575");
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hgrnghtrir575");
		when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
		
		AddressDto shippingAddressDTO = new AddressDto();
		shippingAddressDTO.setType("shipping");
		
		AddressDto billingAddressDTO = new AddressDto();
		billingAddressDTO.setType("billing");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(shippingAddressDTO);
		addresses.add(billingAddressDTO);
		
		UserDto userDTO = new UserDto();
		userDTO.setAddresses(addresses);
		
		UserDto storedUserDetails = userService.createUser(userDTO);
		
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
	}

}
