package com.nguyenkhanh.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenkhanh.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Optional<User> findById(long id);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
