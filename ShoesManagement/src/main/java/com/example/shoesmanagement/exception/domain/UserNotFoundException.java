package com.example.shoesmanagement.exception.domain;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}

}
