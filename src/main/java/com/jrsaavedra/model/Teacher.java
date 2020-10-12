package com.jrsaavedra.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="teachers")
public class Teacher implements Serializable{
	//attributes
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="avatar")
	private String avatar;
	
	//teacher has many courses """cascade=CascadeType.ALL""" -> para eliminar en cascada
	//mappedBy: para decir de que entidad viene la relación el mapeo se hace teniendo en cuenta el campo teacher que existe en la clase Course
	@OneToMany(mappedBy="teacher")
	@JsonIgnore
	private Set<Course> course;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="teacher_id")
	private Set<TeacherHasSocialNetwork> teacherHasSocialNetwork;
	
	//constructor
	public Teacher() {
		super();
	}
	//constructor overload
	public Teacher(String name, String avatar) {
		super();
		this.name = name;
		this.avatar = avatar;
	}
	//methods getters and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Set<Course> getCourse() {
		return course;
	}
	public void setCourse(Set<Course> course) {
		this.course = course;
	}
	public Set<TeacherHasSocialNetwork> getTeacherHasSocialNetwork() {
		return teacherHasSocialNetwork;
	}
	public void setTeacherHasSocialNetwork(Set<TeacherHasSocialNetwork> teacherHasSocialNetwork) {
		this.teacherHasSocialNetwork = teacherHasSocialNetwork;
	}
	
	
	
	
	

}
