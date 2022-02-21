package com.rest.store002.ui.model.response;

import java.util.List;

public class UserRest {
	/**
 	this class will be used to return a value in JSON format to acknowledge that the information was successfully saved
	 */
	public String userID;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesRest> addresses;//This variable is used to return a list of addresses to the user 

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AddressesRest> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressesRest> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "UserRest [userID=" + userID + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", addresses=" + addresses + ", getUserID()=" + getUserID() + ", getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + ", getEmail()=" + getEmail() + ", getAddresses()="
				+ getAddresses() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
