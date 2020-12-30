package com.nguyenkhanh.backend.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;

public class BehaviorDTO extends CommonDTO<BehaviorDTO> {
	private Long id;

	@Size(min = 3, message = "UserName phải có ít nhất 3 ký tự")
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
