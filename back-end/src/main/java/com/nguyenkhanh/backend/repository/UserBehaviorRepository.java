package com.nguyenkhanh.backend.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nguyenkhanh.backend.entity.UserBehaviorEntity;

@Repository
public interface UserBehaviorRepository extends JpaRepository<UserBehaviorEntity, Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM UserBehaviorEntity ub WHERE ub.behaviorid = :behaviorid")
	void deleteByBehaviorid(@Param("behaviorid") long id);

	@Transactional
	@Modifying
	@Query("DELETE FROM UserBehaviorEntity ub WHERE ub.userid = :userid")
	void deleteByUserid(@Param("userid") long id);
}
