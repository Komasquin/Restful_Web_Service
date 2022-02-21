package com.rest.store002.ui.model.response;

public enum ErrorMessages {

	//Users will see these messages
	MISSING_REQUIRED_FIELD("missing reuired field. Please check documentation for required fields."),
	RECORD_ALREADY_EXISTS("Record already exists"),
	INTERNAL_SERVER_ERROR("Internal server error"),
	NO_RECORD_FOUND("Record with provided id is not found"),
	AUTHENTICATION_FAILED("Authentication failed"),
	COULD_NOT_UPDATE_RECORD("Could not update record"),
	COULD_NOT_DELETE_RECORD("Could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified"),
	NUL_POINTER("pointer is null dude");
	
	private String errorMessage;
	
	ErrorMessages(String errMessage) {
		this.errorMessage = errMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
