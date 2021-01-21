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
import com.nguyenkhanh.backend.exception.MessageResponse;

@Component
public final class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
//		try {
		logger.error("Unauthorized error: {}", authException.getMessage());

		MessageResponse error = new MessageResponse();
		error.setTimestamp(new Date());
		error.setstatusCode(HttpServletResponse.SC_UNAUTHORIZED);
		error.setError("Unauthorized");
		error.setMessage(authException.getMessage());
		error.setPath(request.getRequestURI());
		Gson gson = new Gson();
		String errorMessage = gson.toJson(error);

		response.resetBuffer();
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(errorMessage);
		response.flushBuffer();
//		} catch (Forbidden e) {
//			MessageResponse error = new MessageResponse();
//			error.setTimestamp(new Date());
//			error.setstatusCode(HttpServletResponse.SC_FORBIDDEN);
//			error.setError("Forbidden");
//			error.setMessage("Invalid Authorization token");
//			error.setPath(request.getRequestURI());
//			Gson gson = new Gson();
//			String errorMessage = gson.toJson(error);
//
//			response.resetBuffer();
//			response.setContentType("application/json");
//			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//			response.getWriter().write(errorMessage);
//			response.flushBuffer();
//		}
	}

}
