package com.nguyenkhanh.backend.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BehaviorDTO extends CommonDTO<BehaviorDTO> {
	private Long id;

	@NotBlank(message = "{Custom.Name.NotBlank}")
	@Size(min = 3, max = 18, message = "{Custom.Name.Size}")
	@Column(name = "name")
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
