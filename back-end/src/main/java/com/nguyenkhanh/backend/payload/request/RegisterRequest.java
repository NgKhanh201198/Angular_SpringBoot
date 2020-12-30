package com.nguyenkhanh.backend.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.nguyenkhanh.backend.entity.Common;

public class RegisterRequest extends Common {
	@NotBlank
	@Size(min = 3, max = 18, message = "Use 3 to 18 characters or more for your username")
	private String username;

	// ----------------------------------------------------------------------------------------------
	@NotBlank
	@Size(max = 50)
	@Email(message = "Invalid email")
	private String email;

	// ----------------------------------------------------------------------------------------------
	@NotBlank
	@Size(min = 6, max = 18, message = "Use 6 to 18 characters or more for your password")
	private String password;

	// ----------------------------------------------------------------------------------------------
	private Set<String> roles;
	private Set<String> behavior;

	// ----------------------------------------------------------------------------------------------

	public Set<String> getBehavior() {
		return behavior;
	}

	public void setBehavior(Set<String> behavior) {
		this.behavior = behavior;
	}

	private boolean status;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
