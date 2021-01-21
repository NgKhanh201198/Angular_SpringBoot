package com.nguyenkhanh.backend.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.converter.UserConverter;
import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.UserEntity;
import com.nguyenkhanh.backend.repository.UserRepository;
import com.nguyenkhanh.backend.services.IUserService;

@Service
public class UserService implements IUserService {
	@Autowired
	UserConverter userConverter;

	@Autowired
	UserRepository userRepository;

	@Override
	public void delete(long[] ids) {
		for (long id : ids) {
			UserEntity oldUser = userRepository.getOne(id);
			oldUser.setStatus(false);
			userRepository.save(oldUser);
		}
	}

	@Override
	public void delete(long id) {
		UserEntity oldUser = userRepository.getOne(id);
		oldUser.setStatus(false);
		userRepository.save(oldUser);
	}

	@Override
	public UserEntity userFindById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public void restore(long id) {
		UserEntity oldUser = userRepository.getOne(id);
		oldUser.setStatus(true);
		userRepository.save(oldUser);
	}

	@Override
	public boolean isUserExitsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean isUserExitsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public void save(UserEntity user) {
		userRepository.save(user);
	}

	@Override
	public List<UserEntity> userFindAll() {
		return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	@Override
	public UserDTO userGetOne(long id) {
		UserEntity user = userRepository.getOne(id);
		return userConverter.entityToDTO(user);
	}

	@Override
	public boolean isUserExitsById(long id) {
		return userRepository.existsById(id);
	}

}
