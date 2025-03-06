package com.api.bankia.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.bankia.models.User;
import com.api.bankia.models.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>{
	//Optional<User> findByUsername(String username);
	Optional<User> findById(long id);

	//Boolean existsByUsername(String username);
	 
	 Boolean existsByEmail(String email);
	 @Autowired
	 @Query(value = "SELECT * FROM works WHERE phone_number = :phone_number", nativeQuery=true)
	 Optional<Worker> findByPhoneNumber(@Param("phone_number") String phone_number);
	 
	 
	 @Autowired
	 @Query(value = "SELECT * FROM works WHERE id = :id", nativeQuery=true)
	 Optional<Worker> findWorkerById(@Param("id") Long id);
	 
	 Page<Worker> findAll(Pageable pageable);
	 /*
	 @Autowired
	 @Query(value = "SELECT * FROM works WHERE phone_number = :phone_number", nativeQuery=true)
	 Optional<Worker> findByPhoneNumber(@Param("phone_number") String phone_number);
	
	 @Autowired
	 @Query(value = "SELECT * FROM works WHERE email = :email", nativeQuery=true)
	 Boolean existEmail(@Param("email") String email);
	 
	
	 @Autowired
	 @Query(value = "SELECT * FROM users WHERE phone_number = :phone_number", nativeQuery=true)
	 Boolean existPhoneNumber(@Param("phone_number") String phone_number);
	 */
}
