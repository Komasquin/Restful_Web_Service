package com.rest.store002.service.imlp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.store002.io.entity.AddressEntity;
import com.rest.store002.io.entity.UserEntity;
import com.rest.store002.repository.AddressRepo;
import com.rest.store002.repository.UserRepo;
import com.rest.store002.service.AddressService;
import com.rest.store002.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	AddressRepo addressRepo;

	@Override
	public List<AddressDto> getAddresses(String userID) {
		List<AddressDto> returnValues = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepo.findByUserID(userID);
		//userEntity.getAddresses();//------------You can use this method to return a list of addresses, but we will do it another way
		
		if(userEntity == null) {
			return returnValues;
		}
		
		Iterable<AddressEntity> addresses = addressRepo.findAllByUserDetails(userEntity);
		
		for(AddressEntity addressEntity : addresses) {
			returnValues.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValues;
	}

	@Override
	public AddressDto getAddress(String addressID) {
		AddressDto returnValue = new AddressDto();
		
		AddressEntity addressEntity = addressRepo.findByAddressID(addressID);
		
		if(addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}

}
