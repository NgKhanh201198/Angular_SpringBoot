package com.nguyenkhanh.backend.exception;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	// Default
	@ExceptionHandler({ Exception.class })
	public final ResponseEntity<?> handleAllException(Exception exception, WebRequest request) {
		ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	public final ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	// Not found entity (getById not found)
	@ExceptionHandler({ EntityNotFoundException.class })
	public ResponseEntity<?> handlExceptionNotFound(EntityNotFoundException exception, WebRequest request) {
		ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), exception.getMessage(),
				"Did not find the ID you requested", request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ DataAccessException.class })
	public ResponseEntity<?> handlDataAccessException(DataAccessException exception, WebRequest request) {
		ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found",
				"Not found ID");
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ TimeoutException.class })
	public ResponseEntity<?> handlTimeoutException(TimeoutException exception, WebRequest request) {
		ResponseMessage message = new ResponseMessage(new Date(), HttpStatus.REQUEST_TIMEOUT.value(), "Request timeout",
				exception.getMessage());
		return new ResponseEntity<>(message, HttpStatus.REQUEST_TIMEOUT);
	}

	// Validate data
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ResponseMessage message = new ResponseMessage(new Date(), status.value(), "Validation Error",
				exception.getBindingResult().getFieldError().getDefaultMessage(), request.getDescription(false));
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	// upload file
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc,
			WebRequest request) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseMessage(new Date(), HttpStatus.EXPECTATION_FAILED.value(), "Expectation Failed",
						"File too large!", request.getDescription(false)));
	}
}
