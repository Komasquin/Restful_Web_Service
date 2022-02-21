package com.rest.store002.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.store002.service.AddressService;
import com.rest.store002.service.imlp.UserServiceImpl;
import com.rest.store002.shared.dto.AddressDto;
import com.rest.store002.shared.dto.UserDto;
import com.rest.store002.ui.model.request.UserDetailsRequestModel;
import com.rest.store002.ui.model.response.AddressesRest;
import com.rest.store002.ui.model.response.ErrorMessages;
import com.rest.store002.ui.model.response.OperationStatusModel;
import com.rest.store002.ui.model.response.RequestOperationName;
import com.rest.store002.ui.model.response.RequestOperationStatus;
import com.rest.store002.ui.model.response.UserRest;
import com.rest.store002.we.exception.UserServiceException;

@RestController
@RequestMapping("users") //http://localhost:8082/users
public class UserController {
	
	@Autowired//-------------------------------------------------This annotation is used to create an Object of the respective class from the Spring Bean
	UserServiceImpl userService;
	
	@Autowired
	AddressService addressService;

	//This annotation is used to input information
	@PostMapping(path="/add", consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		
			System.out.println(userDetails.toString());//------------This line will print to console, used for debugging so you can check data
		
			//Check if the 'UserDetailsRequestModel' parameter variable is empty, if so throw an exception
			if(userDetails.getFirstName().isEmpty()) {
				//throw new NullPointerException("The object is null");
				throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
			}
		
		//ADDING DATA TO THE DATABASE---------------------------------------------------------------------------------------------------------
		
			//First: create objects to return and pass into the service class method
			UserRest returnValue = new UserRest();
			UserDto userDTO = new UserDto();
			ModelMapper modelMapper = new ModelMapper();
		
			//Second: copy the @RequestBody Parameter into the DTO object
			//BeanUtils.copyProperties(userDetails, userDTO);
			userDTO = modelMapper.map(userDetails, UserDto.class);
			System.out.println(userDTO.toString());//------------This line will print to console, used for debugging so you can check data
			
			//Third: pass the DTO object into the service class by calling the respective method and setting it equal to a new DTO object
			UserDto createUser = userService.createUser(userDTO);
		
			//Fourth: copy the newly created DTO object into the Plain Old Java Object 'POJO' to be returned, here it is 'UserRest()'
			BeanUtils.copyProperties(createUser, returnValue);
			returnValue = modelMapper.map(createUser, UserRest.class);
			
			//Last: return the POJO so the user can receive confirmation
			return returnValue;
		
		//ADDING DATA TO THE DATABSE---------------------------------------------------------------------------------------------------------
		
	}//C -create
	
	@GetMapping(path="/find/{id}")	//-----------------------------------------------This annotation is used to return information		
	public UserRest getUser(@PathVariable String id ) {	
			//First: create an UserRest object to be returned
			UserRest returnValue = new UserRest();
		
			//Second: create user DTO object and have the value returned into from the service class
			UserDto userDTO = userService.getUserByUserID(id);
			ModelMapper modelMapper = new ModelMapper();
			
			//Third: copy the values returned from the service class into the 'returnValue' variable
			//BeanUtils.copyProperties(userDTO, returnValue);
			returnValue = modelMapper.map(userDTO, UserRest.class);
			
			//Last: return the UserRest object to let the user see the information
			return returnValue;
			
	}//R -read
	
	@GetMapping(path="/allusers", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "0") int limit) {
		////First: create an UserRest collection to be returned
		List<UserRest> returnValues = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for(UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValues.add(userModel);
		}
		
		return returnValues;
	}//R -read
	
	//------------------------------------------------------------------------------------------------------------------------------ADDRESS_OBJECTS
	
	//http://localhost:8082/store002/users/{ID}/addresses
	@GetMapping(path="/{userId}/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<AddressesRest> getUserAddresses(@PathVariable String userId) throws Exception {
		//First: create a AddressesRest collection to be returned, and a modelMapper object to copy values between objects
		List<AddressesRest> returnValues = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		
		//Second: call the service class for address to locate the requested data
		List<AddressDto> addressesDTO = addressService.getAddresses(userId);
		
		//IF: the service class finds nothing return an error
		if(addressesDTO == null ) {
			throw new Exception("getUserAddresses() method in the controller has a null addressesDTO variable.");
		}
		
		//http://modelmapper.org/user-manual/generics/
		//Third: copy the list into the the returnValues variable
		Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
		returnValues = modelMapper.map(addressesDTO, listType);
		
		//Last: return the object to be returned
		return returnValues;
	}//R -read
	
	//------------------------------------------------------------------------------------------------------------------------------ADDRESS_OBJECTS
	
	//http://localhost:8082/store002/users/{userID}/address/{addressID}
	@GetMapping(path="/{userId}/address/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public AddressesRest getUserAddress(@PathVariable String userId ,@PathVariable String addressId) throws Exception {
		//First: create a AddressesRest collection to be returned, and a modelMapper object to copy values between objects
		AddressesRest returnValue = new AddressesRest();
		ModelMapper modelMapper = new ModelMapper();
		
		//This will create a link to be added to an object of type AddressesRest
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");
		Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
		
		//Second: call the service class for address to locate the requested data
		AddressDto addressesDTO = addressService.getAddress(addressId);
		
		//IF: the service class finds nothing return an error
		if(addressesDTO == null ) {
			throw new Exception("getUserAddress() method in th e controller has a null addressesDTO variable.");
		}
		
		//Third: copy the list into the the returnValues variable
		returnValue = modelMapper.map(addressesDTO, AddressesRest.class);
		
		//Here we add the link to the returned variable, video 133 has a better implementation
		returnValue.add(userLink);
		returnValue.add(userAddressesLink);
		returnValue.add(selfLink);
		
		//Last: return the object to be returned
		return returnValue;
	}//R -read
	
	//------------------------------------------------------------------------------------------------------------------------------ADDRESS_OBJECTS
	
	@PutMapping(path="/update/{id}", consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})	//-----------------------------------------------This annotation is used to update information
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		//UPDATE DATA TO THE DATABSE---------------------------------------------------------------------------------------------------------
		
			//First: create objects to return and pass into the service class method
			UserRest returnValue = new UserRest();
			UserDto userDTO = new UserDto();
			
			//Second: copy the @RequestBody Parameter into the DTO object
			BeanUtils.copyProperties(userDetails, userDTO);
				
			//Third: pass the DTO object into the service class by calling the respective method and setting it equal to a new DTO object
			UserDto updatedUser = userService.updateUser(id, userDTO);
				
			//Fourth: copy the newly created DTO object into the Plain Old Java Object 'POJO' to be returned, here it is 'UserRest()'
			BeanUtils.copyProperties(updatedUser, returnValue);
				
			//Last: return the POJO so the user can receive confirmation
			return returnValue;
				
		//UPDATE DATA TO THE DATABSE---------------------------------------------------------------------------------------------------------
			
	}//U - update
	
	@DeleteMapping(path = "/delete/{id}", consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})//---------------------------------------------This annotation is used to delete information
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		//First: create an object to return
		OperationStatusModel returnValue = new OperationStatusModel();
		
		//Second: set the return objects operation name
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		//Third: remove the data from the database
		userService.deleteUser(id);
		
		//Fourth: determine if the change was reflected in the database
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		//Last: return the result
		return returnValue;
		
	}//D - delete
	
	@GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel emailVerification(@RequestParam(value = "token") String token) {
		OperationStatusModel returnValue = new OperationStatusModel();
		
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		
		boolean isVerified = userService.verifyEmailToken(token);
		
		if(isVerified) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}else {
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		}
		
		return returnValue;
	}
}
