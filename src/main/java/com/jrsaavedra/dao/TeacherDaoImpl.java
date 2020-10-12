package com.jrsaavedra.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jrsaavedra.model.Teacher;
import com.jrsaavedra.model.TeacherHasSocialNetwork;

@Repository
@Transactional
public class TeacherDaoImpl extends AbstractSession implements TeacherDao  {

	
	@Override
	public void saveTeacher(Teacher teacher) {
		// guardar teacher
		//persist == save method
		super.getSession().persist(teacher);
		
	}

	@Override
	public void deleteTeacherById(Long teacherId) {
		// TODO Auto-generated method stub
		Teacher teacher = this.findById((long) teacherId);
		if(teacher != null) {
			//eliminar las redes sociales
			Iterator<TeacherHasSocialNetwork> i = teacher.getTeacherHasSocialNetwork().iterator();
			while(i.hasNext()) {
				TeacherHasSocialNetwork teacherHasSocialNetwork = i.next();
				i.remove();
				super.getSession().delete(teacherHasSocialNetwork);
			}
			//limpiar antes de eliminar teacher
			teacher.getTeacherHasSocialNetwork().clear();
			super.getSession().delete(teacher);
		}
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		super.getSession().update(teacher);
	}

	@Override
	public List<Teacher> findAllTeachers() {
		// obtener todos los teachers: obtenemos a partir de la clase Teacher
		return super.getSession().createQuery("from Teacher").list();
	}

	@Override
	public Teacher findById(Long id) {
		// TODO Auto-generated method stub
		return (Teacher) super.getSession().get(Teacher.class, id);
	}

	@Override
	public Teacher findByName(String name) {
		// TODO Auto-generated method stub
		// datos de como esta en la clase
		return (Teacher) super.getSession().createQuery(
				"from Teacher where name = :name"
			).setParameter("name", name).uniqueResult();
	}

}
