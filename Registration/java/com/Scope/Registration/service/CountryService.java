package com.Scope.Registration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Scope.Registration.model.Country;
import com.Scope.Registration.repository.CountryRepository;

@Service
public class CountryService {
	@Autowired
	private CountryRepository countryrepository;
	public List<Country>countrylist(){
		return countryrepository.findAll();
		}

}
