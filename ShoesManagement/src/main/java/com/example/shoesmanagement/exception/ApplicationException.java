package com.example.shoesmanagement.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String message;
  private final HttpStatus httpStatus;

  public ApplicationException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
  public ApplicationException(HttpStatus httpStatus, String message) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

//
//
//  public ApplicationException(String message) {
//	this.message = message;
//	this.httpStatus = HttpStatus.NOT_FOUND;
//}
//


@Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
