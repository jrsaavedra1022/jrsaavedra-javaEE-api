package com.jrsaavedra.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.jrsaavedra.model.Course;
import com.jrsaavedra.model.Teacher;
import com.jrsaavedra.service.CourseService;
import com.jrsaavedra.service.TeacherService;
import com.jrsaavedra.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {
	@Autowired
	private CourseService _courseService;
	@Autowired
	private TeacherService _teacherService;
	
	//GET
	@RequestMapping(value="/courses", method=RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name, @RequestParam(value="teacher_id", required=false) Long teacher_id){
		List<Course> courses = new ArrayList<>();
		// check teacher id
		if(teacher_id != null) {
			courses = this._courseService.findByIdTeacher(teacher_id);
			if(courses.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		}
		// check name course
		if(name != null) {
			Course course = this._courseService.findByName(name);
			if(course == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			courses.add(course);
		}
		
		// get all courses
		if(name == null && teacher_id == null) {
			courses = this._courseService.findAllCourse();
			if(courses.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		}
				
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		
	}
	// GET by ID
	@RequestMapping(value="/courses/{id}", method=RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long course_id){
		if(course_id == null || course_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The field course_id is required"), HttpStatus.CONFLICT);
		}
		Course course = this._courseService.findById(course_id);
		if(course == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	//POST
	@RequestMapping(value="/courses", method=RequestMethod.POST, headers="Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder){
		if(course.getName().equals(null) || course.getName().isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("The field name is required."), HttpStatus.CONFLICT);
		}
		if(this._courseService.findByName(course.getName()) != null) {
			return new ResponseEntity<>(new CustomErrorType("This course already exists."), HttpStatus.CONFLICT);
		}
		//create a new course
		this._courseService.saveCourse(course);
		Course courseNew = this._courseService.findByName(course.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder
				.path("/v1/courses/{id}")
				.buildAndExpand(courseNew.getId())
				.toUri()
		);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
	
	//UPDATE
	@RequestMapping(value="/courses/{id}", method=RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<Course> updateCourse(@PathVariable("id") Long course_id, @RequestBody Course course){
		Course currentCourse = this._courseService.findById(course_id);
		if(currentCourse == null) {
			return new ResponseEntity(new CustomErrorType("This course to update don't exits in the system."), HttpStatus.CONFLICT);
		}
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setTemary(course.getTemary());
		this._courseService.updateCourse(currentCourse);
		return new ResponseEntity<Course>(currentCourse, HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/courses/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteCourse(@PathVariable("id") Long course_id){
		if(course_id == null || course_id <= 0) {
			return new ResponseEntity<>(new CustomErrorType("The field course_id is required."), HttpStatus.CONFLICT);
		}
		Course currentCourse =  this._courseService.findById(course_id);
		if(currentCourse == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		this._courseService.deleteCourse(course_id);
		return new ResponseEntity<Course>(HttpStatus.OK);
	}
	
	//ASSIGN TEACHER TO COURSE
	@RequestMapping(value="/courses/teacher", method=RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<?> assingTeacherToCourse(@RequestBody Course course){
		if(course.getId() == null) {
			return new ResponseEntity<>(new CustomErrorType("The course's id is required."), HttpStatus.CONFLICT);
		}
		Course courseSaved = this._courseService.findById(course.getId());
		if(courseSaved == null) {
			return new ResponseEntity<>(new CustomErrorType("The course's id: " + course.getId() + " doesn't exists."), HttpStatus.NOT_FOUND);
		}
		
		if(course.getTeacher() == null) {
			return new ResponseEntity<>(new CustomErrorType("A teacher_id is required to continue."), HttpStatus.CONFLICT);
		}else {
			if(course.getTeacher().getId() == null || course.getTeacher().getId() <= 0) {
				return new ResponseEntity<>(new CustomErrorType("A teacher_id is required to continue."), HttpStatus.CONFLICT);
			}
			Teacher teacher = this._teacherService.findById(course.getTeacher().getId());
			if(teacher == null) {
				return new ResponseEntity<>(new CustomErrorType("The teacher's id: "+course.getTeacher().getId()+" doesn't exists."), HttpStatus.NOT_FOUND);
			}
			
			courseSaved.setTeacher(teacher);
			this._courseService.updateCourse(courseSaved);
			return new ResponseEntity<Course>(courseSaved, HttpStatus.OK);
		}
	}

}
