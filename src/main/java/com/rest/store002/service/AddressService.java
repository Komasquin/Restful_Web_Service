package com.rest.store002.service;

import java.util.List;

import com.rest.store002.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAddresses(String userID);
	AddressDto getAddress(String addressID);
}
