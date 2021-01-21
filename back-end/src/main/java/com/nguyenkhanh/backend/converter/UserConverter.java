package com.nguyenkhanh.backend.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.UserEntity;

@Component
public class UserConverter {
	//delete
	public UserEntity dtoToEntityDelete(UserDTO dto, UserEntity oldUser) {
		oldUser.setStatus(dto.getStatus());
		return oldUser;
	}

	//all
	public UserDTO entityToDTO(UserEntity user) {
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		return userDTO;
	}

	public UserEntity dtoToEntity(UserDTO userDTO) {
		ModelMapper mapper = new ModelMapper();
		UserEntity user = mapper.map(userDTO, UserEntity.class);
		return user;
	}
}
