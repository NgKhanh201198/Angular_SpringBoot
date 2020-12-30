package com.nguyenkhanh.backend.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.converter.BehaviorConverter;
import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.entity.Behavior;
import com.nguyenkhanh.backend.repository.BehaviorRepository;
import com.nguyenkhanh.backend.services.IBehaviorService;

@Service
public class BehaviorService implements IBehaviorService {
	@Autowired
	private BehaviorRepository behaviorRepository;

	@Autowired
	private BehaviorConverter behaviorConverter;

	// Add data
	@Override
	public BehaviorDTO save(BehaviorDTO behaviorDTO) {
		Behavior behavior = new Behavior();
		if (behaviorDTO.getId() != null) {
			Behavior oldBehavior = behaviorRepository.getOne(behaviorDTO.getId());
			behavior = behaviorConverter.dtoToEntity(behaviorDTO, oldBehavior);
		} else {
			behavior = behaviorConverter.dtoToEntity(behaviorDTO);
		}
		behavior = behaviorRepository.save(behavior);
		return behaviorConverter.entityToDTO(behavior);
	}

	@Override
	public void delete(long[] ids) {
		for (long id : ids) {
			behaviorRepository.deleteById(id);
		}

	}

}
