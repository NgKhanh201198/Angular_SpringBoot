package com.nguyenkhanh.backend.converter;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.User;

@Component
public class UserConverter {
	public User dtoToEntityDelete(UserDTO dto, User oldUser) {
		oldUser.setStatus(dto.getStatus());
		return oldUser;
	}

	public UserDTO entityToDTO(Optional<User> user) {
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		return userDTO;
	}

	public User dtoToEntity(UserDTO userDTO) {
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(userDTO, User.class);
		return user;
	}
}
