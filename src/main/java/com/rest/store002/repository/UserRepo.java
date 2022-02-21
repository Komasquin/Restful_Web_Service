package com.rest.store002.repository;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.rest.store002.io.entity.UserEntity;

//This interface is used to write your Entity into a database by using predefined methods from the CrudRepo
//If you choose you can use hibernate to create business logic to do the same thing, requires more code
@Repository//-------------------------------------------------------This annotation lets Spring know this is a Repository to be used in the Bean
public interface UserRepo extends PagingAndSortingRepository<UserEntity, Long> {
								//CrudRepository<UserEntity, Long>
	
	//IMPORTANT: you must name your methods appropriately, 'findBy' is required, than your the column name from the database
	UserEntity findByEmail(String email);
	UserEntity findByUserID(String id);
	UserEntity findUserByEmailVerificationToken(String token);
}
