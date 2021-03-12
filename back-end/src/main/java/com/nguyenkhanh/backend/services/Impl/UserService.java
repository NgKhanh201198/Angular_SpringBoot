package com.nguyenkhanh.backend.services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.converter.UserConverter;
import com.nguyenkhanh.backend.dto.UserDTO;
import com.nguyenkhanh.backend.entity.RegisterVerify;
import com.nguyenkhanh.backend.entity.UserEntity;
import com.nguyenkhanh.backend.exception.ResourceNotFoundException;
import com.nguyenkhanh.backend.repository.UserRepository;
import com.nguyenkhanh.backend.services.IUserService;
import com.nguyenkhanh.backend.services.SendEmailService;

@Service
public class UserService implements IUserService {
	@Autowired
	UserConverter userConverter;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SendEmailService sendEmailService;

	@Autowired
	RegisterVerifyService registerVerifyService;

	@Override
	public void delete(long[] ids) {
		for (long id : ids) {
			UserEntity oldUser = userRepository.getOne(id);
			oldUser.setStatus(false);
			userRepository.save(oldUser);
		}
	}

	@Override
	public void deleteAndRestore(long id) {
		UserEntity oldUser = userRepository.getOne(id);
		if (oldUser.getStatus() == true) {
			oldUser.setStatus(false);
		} else {
			oldUser.setStatus(true);
		}
		userRepository.save(oldUser);
	}

	@Override
	public UserEntity userFindById(long id) {
		return userRepository.findById(id);
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

		String token = UUID.randomUUID().toString();

		RegisterVerify registerVerify = new RegisterVerify(user, token, LocalDateTime.now().plusMinutes(10), false);
		registerVerifyService.save(registerVerify);

		String link = "http://localhost:8080/api/auth/confirm?token=" + token;
		sendEmailService.sendEMail(user.getEmail(), buildEmail(user.getUsername(), link));
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

	@Transactional
	public String confirmToken(String token) throws TimeoutException {
		String success = "";
		String login = "";

		RegisterVerify registerVerify = registerVerifyService.getToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("Token not found"));

		LocalDateTime dateExpied = registerVerify.getDateExpied();

		if (dateExpied.isBefore(LocalDateTime.now())) {
			throw new TimeoutException("Token is expired");
		}

		if (registerVerifyService.getStatus(token)) {
			success = "Your account has been confirmed.";
		} else {
			registerVerifyService.updateStatus(token);
			userRepository.updateStatus(registerVerify.getUser().getEmail());
			success = "Your account has already successfully been created.";
			login = "Sign in to continue.";
		}

		return "<body style=\"margin: 0;\">\r\n"
				+ "    <div style=\"background-color: #212429;width: 100%;height: 663px;\">\r\n"
				+ "        <div style=\"width: 80%;text-align: center;font-size: 35px;line-height: 78px;letter-spacing: 0.05em;padding-top: 100px;margin: 0 auto;color: #c8cee2;font-family: 'Segoe UI',Arial,sans-serif;text-transform: uppercase;text-shadow: 0px 4px 4px rgb(0 0 0 / 25%);\">"
				+ success + "</div>\r\n"
				+ "        <div style=\"text-align: center;font-size: 24px;line-height: 35px;letter-spacing: 0.05em;color: #cdd4ea;font-family: 'Segoe UI',Arial,sans-serif;text-transform: uppercase;\">"
				+ login + "</div>\r\n" + "    </div>\r\n" + "</body>";
	}

	private String buildEmail(String name, String link) {
		return "<div style=\"width:60%;padding:20px 60px;margin:auto;background-color:#f1f1f1;\">\r\n"
				+ "        <p style=\"font-family:'Segoe UI',Arial,sans-serif;font-size: 32px;font-weight: 400;margin: 20px 0px -5px 0px;\">Hello "
				+ name + ",</p>\r\n"
				+ "        <p style=\"display:block;font-size: 23px;font-weight: 100;font-family:'Segoe UI',Arial,sans-serif;text-align:left;color:#000000;margin-bottom:30px;\">To continue creating your account. Please click on the below link to activate your account:</p>\r\n"
				+ "        <a href=\"" + link
				+ "\" style=\"display:block;padding:15px 40px;text-decoration:none;font-family:'Segoe UI',Arial,sans-serif;font-weight: 100;background:linear-gradient(90deg,#3a9bed 25%,#235ecf 100%);border-radius:5px;text-transform:uppercase;letter-spacing:5px;color:#ffffff;width:40%;line-height:25px;text-align:center;margin:auto;font-size:18px;\">Verify mail</a>\r\n"
				+ "        <p style=\"font-family: 'Segoe UI',Arial,sans-serif;font-size: 16px;font-weight: 400;padding-top: 20px;\"><b>Note:</b> Link will expire in 10 minutes!</p>\r\n"
				+ "</div>";
	}

}
