//package com.example.shoesmanagement.exception;
//
//import java.io.IOException;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import com.example.shoesmanagement.dto.response.ShowDataResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.web.error.ErrorAttributeOptions;
//import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//
//
//@RestControllerAdvice
//public class GlobalExceptionHandlerController {
//	Logger logger =  LoggerFactory.getLogger(GlobalExceptionHandlerController.class);
//	@Bean
//	public ErrorAttributes errorAttributes() {
//		// Hide exception field in the return object
//		return new DefaultErrorAttributes() {
//			@Override
//			public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
//				Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
//				errorAttributes.remove("exception");
//				return errorAttributes;
//			}
//		};
//	}
//
//	@ExceptionHandler(ApplicationException.class)
//	public ResponseEntity<?> handleCustomException(HttpServletResponse res, ApplicationException ex) throws IOException {
//		logger.error(ex.getMessage());
//
//		return new ResponseEntity<>(new ShowDataResponse<String>(ex.getHttpStatus().value(), ex.getMessage()),
//				ex.getHttpStatus());
//	}
//
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<?> handleAccessDeniedException(HttpServletResponse res) throws IOException {
//		// res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
//		return new ResponseEntity<>(new ShowDataResponse<>(HttpStatus.FORBIDDEN.value(), "Access denied"),
//				HttpStatus.FORBIDDEN);
//	}
//
//	@ExceptionHandler(NotFoundException.class)
//	public ResponseEntity<?> handleNotFoundException(HttpServletResponse res) throws IOException {
//		// res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
//		return new ResponseEntity<>(new ShowDataResponse<String>(HttpStatus.NOT_FOUND.value(), "Incorrect path"),
//				HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> handleException(HttpServletResponse res) throws IOException {
//		// res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
//		return new ResponseEntity<>(new ShowDataResponse<String>(HttpStatus.BAD_REQUEST.value(), "Something went wrong"),
//				HttpStatus.BAD_REQUEST);
//	}
//
//}
