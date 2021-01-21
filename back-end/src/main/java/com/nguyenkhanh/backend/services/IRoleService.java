package com.nguyenkhanh.backend.services;

import java.util.Optional;

import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.entity.RoleEntity;

public interface IRoleService {
	public Optional<RoleEntity> roleFindByName(ERole name);
}
