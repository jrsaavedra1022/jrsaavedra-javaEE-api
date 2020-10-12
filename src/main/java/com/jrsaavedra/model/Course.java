package com.jrsaavedra.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="courses")
public class Course implements Serializable{
	//Attributes
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="temary")
	private String temary;
	
	@Column(name="project")
	private String project;
	
	// relationShip course has one Teacher. """optional=true""" -> no sea necesario crear un teacher
	// JoinColumn -> campo de llave foranea
	// fetch=FetchType.EAGER -> para que cuando seleccione un curso también traiga teacher
	@ManyToOne(optional=true, fetch=FetchType.EAGER)
	@JoinColumn(name="teacher_id")
	private Teacher teacher;
	
	//constructor
	public Course() {
		super();
	}
	//constructor overload

	public Course(String name, String temary, String project) {
		super();
		this.name = name;
		this.temary = temary;
		this.project = project;
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

	public String getTemary() {
		return temary;
	}

	public void setTemary(String temary) {
		this.temary = temary;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	

}
