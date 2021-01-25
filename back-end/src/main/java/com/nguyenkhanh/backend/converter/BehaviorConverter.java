package com.nguyenkhanh.backend.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.entity.BehaviorEntity;

@Component
public class BehaviorConverter {
	public BehaviorDTO entityToDTO(BehaviorEntity behavior) {
		ModelMapper mapper = new ModelMapper();
		BehaviorDTO behaviorDTO = mapper.map(behavior, BehaviorDTO.class);
		return behaviorDTO;
	}

	public BehaviorEntity dtoToEntity(BehaviorDTO behaviorDTO) {
		ModelMapper mapper = new ModelMapper();
		BehaviorEntity behavior = mapper.map(behaviorDTO, BehaviorEntity.class);
		return behavior;
	}

	public BehaviorEntity dtoToEntity(BehaviorDTO behaviorDTO, BehaviorEntity oldBehavior) {
		oldBehavior.setName(behaviorDTO.getName());
		return oldBehavior;
	}

}
