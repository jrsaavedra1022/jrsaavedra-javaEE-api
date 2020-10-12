package com.jrsaavedra.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrsaavedra.dao.TeacherDao;
import com.jrsaavedra.model.Teacher;

@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService {
	@Autowired
	private TeacherDao _teacherDao;
	@Override
	public void saveTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		this._teacherDao.saveTeacher(teacher);
		
	}

	@Override
	public void deleteTeacherById(Long teacherId) {
		// TODO Auto-generated method stub
		this._teacherDao.deleteTeacherById(teacherId);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		this._teacherDao.updateTeacher(teacher);
	}

	@Override
	public List<Teacher> findAllTeachers() {
		// TODO Auto-generated method stub
		return this._teacherDao.findAllTeachers();
	}

	@Override
	public Teacher findById(Long teacherId) {
		// TODO Auto-generated method stub
		return this._teacherDao.findById(teacherId);
	}

	@Override
	public Teacher findByName(String name) {
		// TODO Auto-generated method stub
		return this._teacherDao.findByName(name);
	}

}
