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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="teacher_has_socialNetwork")
public class TeacherHasSocialNetwork implements Serializable{
	//attributes
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="teacher_id")
	@JsonIgnore
	private Teacher teacher;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="socialNetwork_id")
	private SocialNetwork socialNetwork;
	
	@Column(name="nickname")
	private String nickname;
	
	//constructor
	public TeacherHasSocialNetwork() {
		
	}
	//constructor overload

	public TeacherHasSocialNetwork(Teacher teacher, SocialNetwork socialNetwork, String nickname) {
		super();
		this.teacher = teacher;
		this.socialNetwork = socialNetwork;
		this.nickname = nickname;
	}
	//Methods getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(SocialNetwork socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
