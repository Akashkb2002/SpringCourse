package com.Scope.Registration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Scope.Registration.model.City;
import com.Scope.Registration.model.State;
import com.Scope.Registration.repository.CityRepository;

@Service
public class CityService {
	@Autowired
	private CityRepository cityrepository;
	public List<City>getCity(){
		return cityrepository.findAll();	
	}
	public List<City>getCityBy(State stateid){
		return cityrepository.findByState(stateid);
		
	}

}
