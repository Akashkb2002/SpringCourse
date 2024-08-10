package com.Scope.Registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Scope.Registration.model.User;
@Repository
public interface Urepository extends JpaRepository<User,Integer>{
	public User findByVerificationcode(String code);

	public User findByEmail(String email);

}
