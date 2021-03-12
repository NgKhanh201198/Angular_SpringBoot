package com.nguyenkhanh.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "register_verify")
public class RegisterVerify {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(name = "token")
	private String token;

	@Column(name = "date_expied")
	private LocalDateTime dateExpied;

	@Column(name = "status")
	private Boolean status;

	public RegisterVerify() {
		super();
	}

	public RegisterVerify(UserEntity user, String token, LocalDateTime dateExpied, Boolean status) {
		super();
		this.user = user;
		this.token = token;
		this.dateExpied = dateExpied;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getDateExpied() {
		return dateExpied;
	}

	public void setDateExpied(LocalDateTime dateExpied) {
		this.dateExpied = dateExpied;
	}
}
