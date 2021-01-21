package com.nguyenkhanh.backend.services;

import java.util.List;
import java.util.Optional;

import com.nguyenkhanh.backend.dto.BehaviorDTO;
import com.nguyenkhanh.backend.entity.BehaviorEntity;

public interface IBehaviorService {
	public BehaviorDTO save(BehaviorDTO behaviorDTO);

	public Optional<BehaviorEntity> findByName(String name);

	public List<BehaviorEntity> behaviorFindAll();

	public void deleteBehaviorByIds(List<Long> ids);

	public void delete(long id);

	public boolean isBehaviorExistsByName(String name);

	public boolean isBehaviorExistsById(long id);
}
