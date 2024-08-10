package com.Scope.Registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Scope.Registration.model.User;
@Repository
public interface OtpRepository extends JpaRepository<User,Integer>{
	public User findByEmail(String email);


}
