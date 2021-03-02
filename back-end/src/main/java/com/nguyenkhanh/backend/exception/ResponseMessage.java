package com.nguyenkhanh.backend.exception;

import java.util.Date;

public class ResponseMessage {
	private Date timestamp;
	private int statusCode;
	private String error;
	private String message;
	private String path;

	public ResponseMessage(Date timestamp, int statusCode, String error, String message, String path) {
		this.timestamp = timestamp;
		this.statusCode = statusCode;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public ResponseMessage(Date timestamp, int statusCode, String error, String message) {
		this.timestamp = timestamp;
		this.statusCode = statusCode;
		this.error = error;
		this.message = message;
	}

	public ResponseMessage(Date timestamp, String error, String message) {
		this.timestamp = timestamp;
		this.error = error;
		this.message = message;
	}

	public ResponseMessage(Date timestamp, int statusCode, String message) {
		this.timestamp = timestamp;
		this.statusCode = statusCode;
		this.message = message;
	}

	public ResponseMessage(String message) {
		this.message = message;
	}
	public ResponseMessage() {
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getstatusCode() {
		return statusCode;
	}

	public void setstatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
