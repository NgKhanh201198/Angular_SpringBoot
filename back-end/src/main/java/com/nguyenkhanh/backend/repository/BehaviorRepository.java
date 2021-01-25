package com.nguyenkhanh.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenkhanh.backend.entity.BehaviorEntity;

@Repository
public interface BehaviorRepository extends JpaRepository<BehaviorEntity, Long> {
	Optional<BehaviorEntity> findByName(String name);

	Boolean existsByName(String name);

	Boolean existsById(long id);

	@Transactional
	@Modifying
	@Query("delete from BehaviorEntity b where b.id in ?1")
	void deleteBehaviorWithIds(List<Long> ids);
}
