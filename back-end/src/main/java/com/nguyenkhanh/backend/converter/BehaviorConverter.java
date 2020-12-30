package com.nguyenkhanh.backend.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.entity.Behavior;

@Component
public class BehaviorConverter {
	public BehaviorDTO entityToDTO(Behavior behavior) {
		ModelMapper mapper = new ModelMapper();
		BehaviorDTO behaviorDTO = mapper.map(behavior, BehaviorDTO.class);
		return behaviorDTO;
	}

	public Behavior dtoToEntity(BehaviorDTO behaviorDTO) {
		ModelMapper mapper = new ModelMapper();
		Behavior behavior = mapper.map(behaviorDTO, Behavior.class);
		return behavior;
	}

	public Behavior dtoToEntity(BehaviorDTO behaviorDTO, Behavior oldBehavior) {
		oldBehavior.setName(behaviorDTO.getName());
		return oldBehavior;
	}

}
