package com.jrsaavedra.model;

//imports
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="socialNetworks")
public class SocialNetwork implements Serializable{
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="icon")
	private String icon;
	
	@OneToMany
	@JoinColumn(name="socialNetwork_id")
	@JsonIgnore
	private Set<TeacherHasSocialNetwork> teacherHasSocialNetwork;
	
	//constructor
	public SocialNetwork() {
		super();
	}
	//constructor overload
	public SocialNetwork(String name, String icon) {
		super();
		this.name = name;
		this.icon = icon;
	}
	// Methods getters and setters
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Set<TeacherHasSocialNetwork> getTeacherHasSocialNetwork() {
		return teacherHasSocialNetwork;
	}
	public void setTeacherHasSocialNetwork(Set<TeacherHasSocialNetwork> teacherHasSocialNetwork) {
		this.teacherHasSocialNetwork = teacherHasSocialNetwork;
	}
	
	
	
}
