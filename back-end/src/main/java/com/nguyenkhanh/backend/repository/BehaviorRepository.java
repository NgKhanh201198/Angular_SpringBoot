package com.nguyenkhanh.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenkhanh.backend.entity.Behavior;

@Repository
public interface BehaviorRepository extends JpaRepository<Behavior, Long> {
	Optional<Behavior> findByName(String name);

	Boolean existsByName(String username);
}
