package com.nguyenkhanh.backend.services.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.entity.RegisterVerify;
import com.nguyenkhanh.backend.repository.RegisterVerifyRepository;
import com.nguyenkhanh.backend.services.IRegisterVerifyService;

@Service
public class RegisterVerifyService implements IRegisterVerifyService {
	@Autowired
	RegisterVerifyRepository registerVerifyRepository;

	@Override
	public Optional<RegisterVerify> getToken(String token) {
		return registerVerifyRepository.findByToken(token);
	}

	@Override
	public boolean getStatus(String token) {
		return registerVerifyRepository.findByStatus(token);
	}

	@Override
	public void updateStatus(String token) {
		registerVerifyRepository.updateStatus(token);
	}

	@Override
	public void save(RegisterVerify registerVerify) {
		registerVerifyRepository.save(registerVerify);
	}
}
