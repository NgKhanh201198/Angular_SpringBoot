package com.nguyenkhanh.backend.exception;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.google.gson.Gson;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.error("Access error: {}", accessDeniedException.getMessage());

		ResponseMessage error = new ResponseMessage();
		error.setTimestamp(new Date());
		error.setstatusCode(HttpServletResponse.SC_FORBIDDEN);
		error.setError("Forbidden");
		error.setMessage("Sorry, You're not authorized to access this resource.");
		error.setPath(request.getRequestURI());
		Gson gson = new Gson();
		String errorMessage = gson.toJson(error);

		response.resetBuffer();
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(errorMessage);
		response.flushBuffer();

	}
}
