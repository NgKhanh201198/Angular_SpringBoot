package com.nguyenkhanh.backend.services;

import java.util.List;

import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.UserEntity;

public interface IUserService {
	public void delete(long[] id);

	public void delete(long id);

	public void restore(long id);

	public void save(UserEntity user);

	public UserEntity userFindById(long id);

	public UserDTO userGetOne(long id);

	public List<UserEntity> userFindAll();

	public boolean isUserExitsById(long id);

	public boolean isUserExitsByUsername(String username);

	public boolean isUserExitsByEmail(String email);
}
