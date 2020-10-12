package com.jrsaavedra.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrsaavedra.dao.CourseDao;
import com.jrsaavedra.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService{
	//@Autowired : llamar al objeto mas generico compatible
	@Autowired
	private CourseDao _courseDao;
	
	@Override
	public void saveCourse(Course course) {
		// TODO Auto-generated method stub
		this._courseDao.saveCourse(course);
	}

	@Override
	public void deleteCourse(long id) {
		// TODO Auto-generated method stub
		this._courseDao.deleteCourse(id);
	}

	@Override
	public void updateCourse(Course course) {
		// TODO Auto-generated method stub
		this._courseDao.updateCourse(course);
	}

	@Override
	public List<Course> findAllCourse() {
		// TODO Auto-generated method stub
		return this._courseDao.findAllCourse();
	}

	@Override
	public Course findById(long id) {
		// TODO Auto-generated method stub
		return this._courseDao.findById(id);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return this._courseDao.findByName(name);
	}

	@Override
	public List<Course> findByIdTeacher(Long teacher_id) {
		// TODO Auto-generated method stub
		return this._courseDao.findByIdTeacher(teacher_id);
	}

}
