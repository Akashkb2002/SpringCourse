package com.Scope.Registration.model;

import jakarta.persistence.*;

@Entity
@Table(name="City")
public class City {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cityid")
	private int cityid;
	@Column(name="cityname")
	private String cityname;
	
	@ManyToOne
	@JoinColumn(name="state")
	private State state;

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	

}
