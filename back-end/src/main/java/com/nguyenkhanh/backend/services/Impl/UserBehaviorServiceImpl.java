package com.nguyenkhanh.backend.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.repository.UserBehaviorRepository;
import com.nguyenkhanh.backend.services.IUserBehaviorService;

@Service
public class UserBehaviorServiceImpl implements IUserBehaviorService {

	@Autowired
	UserBehaviorRepository userBehaviorRepository;
//
//	@Override
//	public void deleteBehaviorId(long id) {
//		userBehaviorRepository.deleteByBehaviorid(id);
//	}

}
