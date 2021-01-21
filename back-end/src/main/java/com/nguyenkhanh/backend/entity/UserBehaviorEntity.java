package com.nguyenkhanh.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_behavior")
public class UserBehaviorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userid;

	@Column(name = "behavior_id")
	private Long behaviorid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getBehaviorid() {
		return behaviorid;
	}

	public void setBehaviorid(Long behaviorid) {
		this.behaviorid = behaviorid;
	}

}
