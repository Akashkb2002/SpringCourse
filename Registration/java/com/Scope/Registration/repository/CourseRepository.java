package com.Scope.Registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Scope.Registration.model.Course;

public interface CourseRepository extends JpaRepository<Course,Integer>{
	public Course findById(int id);

}
