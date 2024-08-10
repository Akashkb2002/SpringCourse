package com.Scope.Registration.model;

import jakarta.persistence.*;

@Entity
@Table(name="state")
public class State {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="stateid")
	private int stateid;
	@Column(name="statename")
	private String statename;
	
	@ManyToOne
	@JoinColumn(name="country")
	private Country country;

	public int getStateid() {
		return stateid;
	}

	public void setStateid(int stateid) {
		this.stateid = stateid;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
