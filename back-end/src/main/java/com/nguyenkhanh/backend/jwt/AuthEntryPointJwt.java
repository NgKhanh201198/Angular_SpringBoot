package com.nguyenkhanh.backend.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@SuppressWarnings("unused")
class ErrorUnauthorized {
	private Date timestamp = new Date((new Date()).getTime());
	private Integer status = 401;
	private String error = "Unauthorized error";
	private String message = "Username or password is incorrect.";
}

@Component
public final class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());

		ErrorUnauthorized error = new ErrorUnauthorized();
		Gson gson = new Gson();
		String errorMessage = gson.toJson(error);

		response.resetBuffer();
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(errorMessage);
		response.flushBuffer();
	}

}
