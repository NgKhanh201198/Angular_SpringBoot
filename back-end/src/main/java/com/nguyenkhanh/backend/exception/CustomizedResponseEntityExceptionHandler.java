package com.nguyenkhanh.backend.exception;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	// Default
	@ExceptionHandler({ Exception.class })
	public final ResponseEntity<Object> handleAllException(Exception exception, WebRequest request) {
		MessageResponse message = new MessageResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		MessageResponse message = new MessageResponse(new Date(), exception.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

//	// Authentication account
//	@ExceptionHandler(AuthenticationException.class)
//	public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception,
//			HttpServletResponse response, WebRequest request) {
//		MessageResponse message = new MessageResponse(new Date(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
//				"Username or password is incorrect.", request.getDescription(false));
//		return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
//	}

//	// Not found entity (getById not found)
//	@ExceptionHandler({ EntityNotFoundException.class })
//	public ResponseEntity<?> handlExceptionNotFound(EntityNotFoundException exception, WebRequest request) {
//		MessageResponse message = new MessageResponse(new Date(), HttpStatus.NOT_FOUND.value(), exception.getMessage(),
//				"Did not find the ID you requested", request.getDescription(false));
//		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
//	}

	@ExceptionHandler({ DataAccessException.class })
	public ResponseEntity<?> handlDataAccessException(DataAccessException exception, WebRequest request) {
		MessageResponse message = new MessageResponse(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
				"Not found ID");
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}
	
	// Validate data
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		MessageResponse message = new MessageResponse(new Date(), status.value(), "Validation Error",
				exception.getBindingResult().getFieldError().getDefaultMessage(), request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
}
