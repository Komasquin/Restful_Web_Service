package com.rest.store002.service.imlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rest.store002.io.entity.UserEntity;
import com.rest.store002.repository.UserRepo;
import com.rest.store002.service.UserService;
import com.rest.store002.shared.Utils;
import com.rest.store002.shared.dto.AddressDto;
import com.rest.store002.shared.dto.UserDto;
import com.rest.store002.ui.model.response.ErrorMessages;
//import com.rest.store002.ui.model.response.UserRest;
import com.rest.store002.we.exception.UserServiceException;

@Service // --------------------------------------------------This annotation adds this
			// class to the Bean so it can be Autowired
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		//This object is used to make a deep copy of an object
		ModelMapper modelMapper = new ModelMapper();
		
		// This is a check for duplicate email's
		if (userRepo.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Email already in use.");
		}
		
		//FOR: every address passed set the ID
		for(int i = 0; i < user.getAddresses().size(); i++) {
			//First: create a DTO object
			AddressDto addressDTO = user.getAddresses().get(i);
			//Second: set 'userDetails' to the 'user' being created
			addressDTO.setUserDetails(user);
			//Third: set the ID of the address using the random generator we made
			addressDTO.setAddressID(utils.generateID(30));
			//Fourth: pass new data into the 'user' objects addresses collection 
			user.getAddresses().set(i, addressDTO);
		}

		// First: create objects to save into the database and return
		UserEntity userEntity = new UserEntity();
		UserDto userDTO = new UserDto();

		// Second: copy the Param into the Entity object
		BeanUtils.copyProperties(user, userEntity);
		userEntity = modelMapper.map(user, UserEntity.class);
		
		String publicUserID = utils.generateID(30);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserID(publicUserID);
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserID));
		//userEntity.setUserID(utils.generateID(30));

		// Third: save the Entity object into the database using the CrudRepository
		// interface
		UserEntity storedUserDetails = userRepo.save(userEntity);

		// Fourth: copy the stored values into a Entity object to be returned
		//BeanUtils.copyProperties(storedUserDetails, userDTO);
		userDTO = modelMapper.map(storedUserDetails, UserDto.class);
		
		// Last: return the DTO object for the Controller method to use
		return userDTO;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// First: search for the user by email
		UserEntity userEntity = userRepo.findByEmail(email);

		// IF: there is no email throw exception
		if (userEntity == null) {
			throw new UsernameNotFoundException("username not found");
		}
		// Return a General User provided by Spring, CTRL + click to find what the
		// arguments are
		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
		
		//The below comment is used here
//		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity., boolean accountNonExpired,
//		boolean credentialsNonExpired, boolean accountNonLocked,
//		Collection<? extends GrantedAuthority> authorities);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		// First: search for the user by email
		UserEntity userEntity = userRepo.findByEmail(email);

		// IF: there is no email throw exception
		if (userEntity == null) {
			throw new UsernameNotFoundException("username not found in UserServiceImpl->getUser()");
		}

		// Second: create an object to return
		UserDto returnValue = new UserDto();

		// Third: copy the attribute from the database into a DTO
		BeanUtils.copyProperties(userEntity, returnValue);

		// Fourth: return the DTO
		return returnValue;
	}

	@Override
	public UserDto getUserByUserID(String id) {
		// First: create an object to return
		UserDto returnValue = new UserDto();

		//This object is used to make a deep copy of an object
		ModelMapper modelMapper = new ModelMapper();
		
		// Second: query the database for the user, and store it into an Entity object
		UserEntity userEntity = userRepo.findByUserID(id);

		// IF: object is null return an error
		if (userEntity == null) {
			throw new UsernameNotFoundException("user with ID " + id + " not found");
		}

		// Third: copy the Entity object into the DTO object
		//BeanUtils.copyProperties(userEntity, returnValue);
		returnValue = modelMapper.map(userEntity, UserDto.class);
		
		// Return a DTO object for the Controller class to use
		return returnValue;
	}

	@Override
	public UserDto updateUser(String id, UserDto user) {
		// First: create an object to return
		UserDto returnValue = new UserDto();

		// Second: query the database for the user, and store it into an Entity object
		UserEntity userEntity = userRepo.findByUserID(id);

		// IF: object is null return an error
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		// Third: update the fields that you want to change, NOTE: remember to check if
		// the values are empty
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		// Fourth: commit the changes to the database using our repository, and set the
		// values to a new object of type 'UserEntity
		UserEntity updatedUserDetails = userRepo.save(userEntity);

		// Fifth: copy the returned values from the database into our returnValue
		// variable
		BeanUtils.copyProperties(updatedUserDetails, returnValue);

		// Last: return the DTO object
		return returnValue;
	}

	@Override // change the return type to boolean and implement a check to return true or
				// false if the delete worked
	public void deleteUser(String id) {
		// First: query the database for the user, and store it into an Entity object
		UserEntity userEntity = userRepo.findByUserID(id);

		// IF: object is null return an error
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		// Last: remove the data from the database
		userRepo.delete(userEntity);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		// First: create an collection to return
		List<UserDto> returnValues = new ArrayList<>();
		
		//IF: pages start at 0, this changes that to page start with 1
		if(page > 0) {
			page = page - 1;
		}
		
		//Second: create a pageable object to pass into the repository 
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		//Third: call the repository and store it in a Page collection
		Page<UserEntity> userPage = userRepo.findAll(pageableRequest);
		
		//Fourth: insert the Pages into a collection
		List<UserEntity> users = userPage.getContent();
		
		//Fifth: iterate the list, copy into a DTO object and store that object into the return valuable 
		for(UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValues.add(userDto);
		}
		
		//Last: return the DTO object
		return returnValues;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		 boolean returnValue = false;

	        // Find user by token in the database
	        UserEntity userEntity = userRepo.findUserByEmailVerificationToken(token);

	        //IF: user is found in database
	        if (userEntity != null) {
	        	//Check if token is expired
	            boolean hastokenExpired = Utils.hasTokenExpired(token);
	            //IF: NOT EXPIRED
	            if (!hastokenExpired) {
	            	//Set Entity object email verification information
	                userEntity.setEmailVerificationToken(null);
	                userEntity.setEmailVerificationStatus(Boolean.TRUE);
	                //Save changes to database
	                userRepo.save(userEntity);
	                //Set returnValue to true
	                returnValue = true;
	            }
	        }

	        //Last: return the returnValue
	        return returnValue;
	}

}
