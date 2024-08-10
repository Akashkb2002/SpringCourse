package com.Scope.Registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Scope.Registration.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer>{

}
