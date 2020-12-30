package com.nguyenkhanh.backend.services;

import com.nguyenkhanh.backend.dto.BehaviorDTO;

public interface IBehaviorService {
	BehaviorDTO save(BehaviorDTO behaviorDTO);

	void delete(long[] ids);
}
