package com.Scope.Registration.model;

import jakarta.persistence.*;

@Entity
@Table(name="course")

public class Course {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="courseid")
	private int courseid;
	@Column(name="coursename")
	private String coursename;
	@Column(name="courseduration")
	private String courseduration;
	@Column(name="coursefees")
	private String coursefees;
	public int getCourseid() {
		return courseid;
	}
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getCourseduration() {
		return courseduration;
	}
	public void setCourseduration(String courseduration) {
		this.courseduration = courseduration;
	}
	public String getCoursefees() {
		return coursefees;
	}
	public void setCoursefees(String coursefees) {
		this.coursefees = coursefees;
	}

}