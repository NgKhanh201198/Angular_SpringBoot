package com.nguyenkhanh.backend.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.nguyenkhanh.backend.exception.ResponseMessage;

@Component
public final class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, 
						 HttpServletResponse response,
						 AuthenticationException authException) throws IOException, ServletException {

		logger.error("Unauthorized error: {}", authException.getMessage());
//		logger.error("Responding with unauthorized error. Message - {}", authException.getMessage());
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

		ResponseMessage error = new ResponseMessage();
		error.setTimestamp(new Date());
		error.setstatusCode(HttpServletResponse.SC_UNAUTHORIZED);
		error.setError("Unauthorized");
		error.setMessage("Sorry, you need full authentication to access this resource");
//		error.setMessage(authException.getMessage());
		error.setPath(request.getRequestURI());
		Gson gson = new Gson();
		String errorMessage = gson.toJson(error);

		response.resetBuffer();
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(errorMessage);
		response.flushBuffer();
	}

}
