package com.jrsaavedra.dao;

import java.util.List;

import com.jrsaavedra.model.Course;

public interface CourseDao {
	//methods to implement
	void saveCourse(Course course);
	void deleteCourse(long id);
	void updateCourse(Course course);
	
	List<Course> findAllCourse();
	Course findById(long id);
	Course findByName(String name);
	List<Course> findByIdTeacher(Long teacher_id);
	
}
