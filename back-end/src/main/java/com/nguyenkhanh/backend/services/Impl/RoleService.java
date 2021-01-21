package com.nguyenkhanh.backend.services.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.entity.RoleEntity;
import com.nguyenkhanh.backend.repository.RoleRepository;
import com.nguyenkhanh.backend.services.IRoleService;


@Service
public class RoleService implements IRoleService {
	@Autowired
	RoleRepository roleRepository;

	@Override
	public Optional<RoleEntity> roleFindByName(ERole name) {
		return roleRepository.findByName(name);

	}

}
