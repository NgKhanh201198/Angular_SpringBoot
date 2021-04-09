package com.nguyenkhanh.backend.payload.response;

import java.util.List;

public class JwtResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private boolean status;
	private String token;
	private String type = "Bearer";

	public JwtResponse(Long id, String username, String email, List<String> roles, boolean status, String token) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.status = status;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
