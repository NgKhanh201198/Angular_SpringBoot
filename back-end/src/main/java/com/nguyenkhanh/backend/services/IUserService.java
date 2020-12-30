package com.nguyenkhanh.backend.services;

import com.nguyenkhanh.backend.dto.UserDTO;

public interface IUserService {
	void delete(long[] id);

	void delete(long id);
	
	UserDTO findById(long id);
}
