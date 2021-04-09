package com.nguyenkhanh.backend.services.Impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.entity.RegisterVerify;
import com.nguyenkhanh.backend.entity.UserEntity;
import com.nguyenkhanh.backend.exception.NotFoundException;
import com.nguyenkhanh.backend.repository.RegisterVerifyRepository;
import com.nguyenkhanh.backend.services.IRegisterVerifyService;
import com.nguyenkhanh.backend.services.SendEmailService;

@Service
public class RegisterVerifyServiceImpl implements IRegisterVerifyService {

	@Autowired
	RegisterVerifyRepository registerVerifyRepository;

	@Autowired
	SendEmailService sendEmailService;

	@Autowired
	UserServiceImpl userServiceImpl;

	@Value("${email.dateExpiedSeconds}")
	private long DATE_EXPIED;

	@Value("${system.baseUrl}")
	private String BASE_URL;

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

	public String updateTokenExpired(String token) {
		RegisterVerify registerVerifyOld = registerVerifyRepository.findByToken(token)
				.orElseThrow(() -> new NotFoundException("Token not found"));

		UserEntity user = registerVerifyOld.getUser();

		RegisterVerify registerVerify = new RegisterVerify();
		registerVerify.setId(registerVerifyOld.getId());
		registerVerify.setUser(user);
		registerVerify.setDateExpied(LocalDateTime.now().plusSeconds(DATE_EXPIED));
		registerVerify.setToken(registerVerifyOld.getToken());
		registerVerify.setStatus(false);

		String link = BASE_URL + "api/auth/confirm?token=" + token;
		sendEmailService.sendEMail(user.getEmail(), userServiceImpl.buildEmail(user.getUsername(), link));

		registerVerifyRepository.save(registerVerify);
		return "<body style=\"margin: 0;\">\r\n"
				+ "    <div style=\"background-color: #212429;width: 100%;height: 663px;\">\r\n"
				+ "        <div style=\"width: 80%;text-align: center;font-size: 35px;line-height: 78px;letter-spacing: 0.2em;padding-top: 100px;margin: 0 auto;color: #c8cee2;font-family: 'Segoe UI',Arial,sans-serif;text-transform: uppercase;text-shadow: 0px 4px 4px rgb(0 0 0 / 25%);\">Register successfully.</div>\r\n"
				+ "        <div style=\"text-align: center;font-size: 24px;line-height: 35px;letter-spacing: 0.05em;color: #cdd4ea;font-family: 'Segoe UI',Arial,sans-serif;text-transform: uppercase;\">Comeback to your email to confirm.</div>\r\n"
				+ "    </div>\r\n" + "</body>";
	}
}
