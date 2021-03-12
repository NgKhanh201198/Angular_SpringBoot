package com.nguyenkhanh.backend.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nguyenkhanh.backend.entity.RegisterVerify;

public interface RegisterVerifyRepository extends JpaRepository<RegisterVerify, Long> {
	public Optional<RegisterVerify> findByToken(String token);

	@Transactional
	@Query("SELECT r.status FROM RegisterVerify r " + "WHERE r.token = ?1")
	public boolean findByStatus(String token);

	@Transactional
	@Modifying
	@Query("UPDATE RegisterVerify r " + "SET r.status = true " + "WHERE r.token = ?1")
	public int updateStatus(String token);
}
