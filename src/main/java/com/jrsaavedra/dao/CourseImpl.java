package com.jrsaavedra.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jrsaavedra.model.Course;

@Repository
@Transactional
public class CourseImpl extends AbstractSession implements CourseDao {

	@Override
	public void saveCourse(Course course) {
		// TODO Auto-generated method stub
		super.getSession().persist(course);
	}

	@Override
	public void deleteCourse(long id) {
		// TODO Auto-generated method stub
		Course course = this.findById(id);
		if(course != null) {
			super.getSession().delete(course);
		}
		
	}

	@Override
	public void updateCourse(Course course) {
		// TODO Auto-generated method stub
		super.getSession().update(course);
	}

	@Override
	public List<Course> findAllCourse() {
		// TODO Auto-generated method stub
		return super.getSession().createQuery("from Course").list();
	}

	@Override
	public Course findById(long id) {
		// TODO Auto-generated method stub
		return (Course) super.getSession().get(Course.class, id);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return (Course) super.getSession().createQuery(
				"from Course where name = :name"
			).setParameter("name", name).uniqueResult();
	}

	@Override
	public List<Course> findByIdTeacher(Long teacher_id) {
		// TODO Auto-generated method stub
		return (List<Course>) super.getSession().createQuery( 
				"from Course c join c.teacher t where t.id = :teacher_id"
			).setParameter("teacher_id", teacher_id).list();
	}

}
