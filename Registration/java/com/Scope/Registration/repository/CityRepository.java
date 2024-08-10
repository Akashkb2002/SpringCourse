package com.Scope.Registration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Scope.Registration.model.City;
import com.Scope.Registration.model.State;
@Repository
public interface CityRepository extends JpaRepository<City,Integer> {
	List<City>findByState(State stateid);

}
