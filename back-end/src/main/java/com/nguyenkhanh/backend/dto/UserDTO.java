package com.nguyenkhanh.backend.dto;

import java.util.Set;

public class UserDTO extends CommonDTO<UserDTO> {
	private Long id;
	private String username;
	private String email;
	private String password;
	private Boolean status;
	private Set<String> role;
	private Set<String> behavior;

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	public Set<String> getBehavior() {
		return behavior;
	}

	public void setBehavior(Set<String> behavior) {
		this.behavior = behavior;
	}

}
