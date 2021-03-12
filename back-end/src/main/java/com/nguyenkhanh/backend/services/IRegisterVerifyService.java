package com.nguyenkhanh.backend.services;

import java.util.Optional;

import com.nguyenkhanh.backend.entity.RegisterVerify;

public interface IRegisterVerifyService {
	public Optional<RegisterVerify> getToken(String token);

	public boolean getStatus(String token);

	public void updateStatus(String token);

	public void save(RegisterVerify registerVerify);

}
