package com.nguyenkhanh.backend.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nguyenkhanh.backend.converter.BehaviorConverter;
import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.entity.BehaviorEntity;
import com.nguyenkhanh.backend.repository.BehaviorRepository;
import com.nguyenkhanh.backend.repository.UserBehaviorRepository;
import com.nguyenkhanh.backend.services.IBehaviorService;

@Service
public class BehaviorServiceImpl implements IBehaviorService {
	@Autowired
	private BehaviorRepository behaviorRepository;

	@Autowired
	private UserBehaviorRepository userBehaviorRepository;

	@Autowired
	private BehaviorConverter behaviorConverter;

	// Add data
	@Override
	public BehaviorDTO save(BehaviorDTO behaviorDTO) {
		BehaviorEntity behavior = new BehaviorEntity();
		if (behaviorDTO.getId() != null) {
			BehaviorEntity oldBehavior = behaviorRepository.getOne(behaviorDTO.getId());
			behavior = behaviorConverter.dtoToEntity(behaviorDTO, oldBehavior);
		} else {
			behavior = behaviorConverter.dtoToEntity(behaviorDTO);
		}
		behavior = behaviorRepository.save(behavior);
		return behaviorConverter.entityToDTO(behavior);
	}

	@Override
	public void deleteBehaviorByIds(List<Long> ids) {
		behaviorRepository.deleteBehaviorWithIds(ids);
	}

	@Override
	public void delete(long id) {
		userBehaviorRepository.deleteByBehaviorid(id);
		behaviorRepository.deleteById(id);
	}

	@Override
	public Optional<BehaviorEntity> findByName(String name) {
		return behaviorRepository.findByName(name);
	}

	@Override
	public boolean isBehaviorExistsByName(String name) {
		return behaviorRepository.existsByName(name);
	}

	@Override
	public boolean isBehaviorExistsById(long id) {
		return behaviorRepository.existsById(id);
	}

	@Override
	public List<BehaviorEntity> behaviorFindAll() {
		return behaviorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

}
