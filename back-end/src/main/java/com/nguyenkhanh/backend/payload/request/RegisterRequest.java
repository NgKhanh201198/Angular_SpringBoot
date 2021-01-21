package com.nguyenkhanh.backend.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.nguyenkhanh.backend.entity.Common;
import com.nguyenkhanh.backend.validation.CustomEmailValidate;

public class RegisterRequest extends Common {
	@NotBlank(message = "{Custom.UserName.NotBlank}")
	@Size(min = 3, max = 18, message = "{Custom.Name.Size}")
	private String username;

	// ----------------------------------------------------------------------------------------------
	@NotNull(message = "{Custom.Email.NotBlank}")
	@CustomEmailValidate(message = "{Custom.Email.Validate}")
	@Email(message = "{Custom.Email.Validate}")
	private String email;

	// ----------------------------------------------------------------------------------------------
//	@NotBlank(message = "{Custom.Password.NotBlank}")
	@Size(min = 6, max = 18, message = "{Custom.Password.Size}")
	private String password;

	// ----------------------------------------------------------------------------------------------
//	@NotNull(message = "{Custom.Role.NotNull}")
	private Set<String> roles;
	private Set<String> behaviors;

	// ----------------------------------------------------------------------------------------------

	private boolean status;

	public Set<String> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(Set<String> behaviors) {
		this.behaviors = behaviors;
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
