package com.Scope.Registration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Scope.Registration.model.Country;
import com.Scope.Registration.model.State;

@Repository
public interface StateRepository extends JpaRepository<State,Integer>{
	List<State>findByCountry(Country countryid);
	

}
