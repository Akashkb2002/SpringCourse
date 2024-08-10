package com.Scope.Registration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Scope.Registration.model.Country;
import com.Scope.Registration.model.State;
import com.Scope.Registration.repository.StateRepository;

@Service
public class StateService {
	@Autowired
	private StateRepository staterepository;
	public List<State>getState(){
		return staterepository.findAll();	
	}
	public List<State>getStateBy(Country id){
		return staterepository.findByCountry(id);
		
	}
	

}
