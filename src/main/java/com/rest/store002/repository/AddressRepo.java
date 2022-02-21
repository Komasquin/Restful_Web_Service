package com.rest.store002.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.rest.store002.io.entity.AddressEntity;
import com.rest.store002.io.entity.UserEntity;

@Repository
public interface AddressRepo extends PagingAndSortingRepository<AddressEntity, Long> {

	//IMPORTANT: you must name your methods appropriately, 'findBy' is required, than your the column name from the database
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressID(String addressID);
}
