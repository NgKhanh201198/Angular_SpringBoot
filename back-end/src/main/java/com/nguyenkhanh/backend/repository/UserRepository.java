package com.nguyenkhanh.backend.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nguyenkhanh.backend.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);

	UserEntity findById(long id);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Transactional
	@Modifying
	@Query("UPDATE UserEntity u " + "SET u.status = true " + "WHERE u.email = ?1")
	int updateStatus(String email);

	@Query("SELECT u FROM UserEntity u WHERE CONCAT(u.username, u.email, u.create_by) LIKE %?1%")
	public List<UserEntity> search(String keyword);
}
