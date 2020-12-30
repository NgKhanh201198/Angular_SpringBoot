package com.nguyenkhanh.backend.services.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.converter.UserConverter;
import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.User;
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
			User oldUser = userRepository.getOne(id);
			oldUser.setStatus(false);
			userRepository.save(oldUser);
		}
	}

	@Override
	public void delete(long id) {
		User oldUser = userRepository.getOne(id);
		oldUser.setStatus(false);
		userRepository.save(oldUser);
	}

	@Override
	public UserDTO findById(long id) {
		Optional<User> user = userRepository.findById(id);
		return userConverter.entityToDTO(user);
	}

}
