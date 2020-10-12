package com.jrsaavedra.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.jrsaavedra.model.SocialNetwork;
import com.jrsaavedra.model.Teacher;
import com.jrsaavedra.model.TeacherHasSocialNetwork;
import com.jrsaavedra.service.SocialNetworkService;
import com.jrsaavedra.service.TeacherService;
import com.jrsaavedra.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {
	@Autowired
	private TeacherService _teacherService;
	@Autowired
	private SocialNetworkService _socialNetworkService;
	
	//GET
	@RequestMapping(value="/teachers", method=RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name){
		List<Teacher> teachers = new ArrayList<>();
		if(name == null) {
			teachers = this._teacherService.findAllTeachers();
			if(teachers == null) {
				return new ResponseEntity<List<Teacher>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}else {
			Teacher teacher = this._teacherService.findByName(name);
			if(teacher==null) {
				return new ResponseEntity<List<Teacher>>(HttpStatus.NOT_FOUND);
			}
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}
	}
	
	//GET BY ID
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long teacher_id){
		if(teacher_id == null || teacher_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The field teacher_id is required."), HttpStatus.CONFLICT);
		}
		Teacher teacher = this._teacherService.findById(teacher_id);
		if(teacher == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}
	
	// CREATE TEACHER
	@RequestMapping(value="/teachers", method=RequestMethod.POST, headers="Accept=application/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder){
		if(teacher.getName().equals(null) || teacher.getName().isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("The teacher's name is required."), HttpStatus.CONFLICT);
		}
		if(this._teacherService.findByName(teacher.getName()) != null) {
			return new ResponseEntity<>(new CustomErrorType("This teacher already exists."), HttpStatus.CONFLICT);
		}
		// create new teacher
		this._teacherService.saveTeacher(teacher);
		Teacher teacherNew = this._teacherService.findByName(teacher.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation( 
				uriComponentsBuilder
				.path("/v1/teachers/{id}")
				.buildAndExpand(teacherNew.getId())
				.toUri()
		);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
	
	// UPDATE TEACHER
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Long teacher_id, @RequestBody Teacher teacher){
		Teacher currentTeacher = this._teacherService.findById(teacher_id);
		if(currentTeacher == null) {
			return new ResponseEntity(new CustomErrorType("The teacher to update don't exists."), HttpStatus.CONFLICT);
		}
		currentTeacher.setName(teacher.getName());
		currentTeacher.setAvatar(teacher.getAvatar());
		this._teacherService.updateTeacher(currentTeacher);
		return new ResponseEntity<Teacher>(currentTeacher, HttpStatus.OK);
	}
	
	// DELETE TEACHER
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteTeacher(@PathVariable("id") Long teacher_id){
		if(teacher_id == null || teacher_id <= 0) {
			return new ResponseEntity<>(new CustomErrorType("The field teacher_id is required."), HttpStatus.CONFLICT);
		}
		Teacher currentTeacher = this._teacherService.findById(teacher_id);
		if(currentTeacher == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		this._teacherService.deleteTeacherById(teacher_id);
		return new ResponseEntity<Teacher>(HttpStatus.OK);
	}
	
	public static final String TEACHER_UPLOADED_FOLDER = "images/teachers/";
	//CREATE TEACHER IMAGE
	@RequestMapping(value="/teachers/images", method=RequestMethod.POST, headers=("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id") Long teacher_id, @RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder){
		if(teacher_id == null || teacher_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The field teacher_id is required."), HttpStatus.NO_CONTENT);
		}
		
		if(multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload."), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = this._teacherService.findById(teacher_id);
		if(teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with teacher_id: " + teacher_id +" not found."), HttpStatus.NOT_FOUND);
		}
		
		if(!teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if(f.exists()) {
				f.delete();
			}
		}
		
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName = dateFormat.format(date);
			
			String fileName = String.valueOf(teacher_id) + "-pictureTeacher-" + dateName + "." + multipartFile.getContentType().split("/")[1];
			teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			this._teacherService.updateTeacher(teacher);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload: " + multipartFile.getOriginalFilename()), HttpStatus.CONFLICT);
		}
	}
	
	// GET IMAGE
	@RequestMapping(value="/teachers/{id}/images", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id") Long teacher_id){
		if(teacher_id == null || teacher_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The field teacher_id is required."), HttpStatus.NO_CONTENT);
		}
		Teacher teacher = this._teacherService.findById(teacher_id);
		if(teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with teacher_id: " + teacher_id + " not found."), HttpStatus.NOT_FOUND);
		}
		try {
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if(!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Teacher with teacher_id: " + teacher_id + " has'nt image."), HttpStatus.NOT_FOUND);
			}
			byte[] image = Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error to show image."), HttpStatus.CONFLICT);
		}
	}
	
	//DELETE IMAGE
	@RequestMapping(value="/teachers/{id}/images", method=RequestMethod.DELETE, headers="Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable("id") Long teacher_id){
		if(teacher_id == null || teacher_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The field teacher_id is required."), HttpStatus.NO_CONTENT);
		}
		Teacher teacher = this._teacherService.findById(teacher_id);
		if(teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with teacher_id: " + teacher_id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		if(teacher.getAvatar().isEmpty() || teacher.getAvatar() == null) {
			return new ResponseEntity(new CustomErrorType("This Teacher does'nt have a image to assigned."), HttpStatus.NOT_FOUND);
		}
		String fileName = teacher.getAvatar();
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if(file.exists()) {
			file.delete();
		}
		teacher.setAvatar("");
		this._teacherService.updateTeacher(teacher);
		
		return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/teachers/socialNetworks", method=RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<?> assingTeacherSocialNetwork(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder){
		if(teacher.getId() == null) {
			return new ResponseEntity(new CustomErrorType("We need at least teacher_id, socialNetwork_id and nickname."), HttpStatus.CONFLICT);
		}
		Teacher teacherSaved = this._teacherService.findById(teacher.getId());
		if(teacherSaved == null) {
			return new ResponseEntity(new CustomErrorType("The teacher_id: "+ teacher.getId()+" not found."), HttpStatus.NOT_FOUND);
		}
		
		if(teacher.getTeacherHasSocialNetwork().size() == 0) {
			return new ResponseEntity(new CustomErrorType("We need at least teacher_id, socialNetwork_id and nickname."), HttpStatus.CONFLICT);
		}else {
			Iterator<TeacherHasSocialNetwork> i = teacher.getTeacherHasSocialNetwork().iterator();
			while(i.hasNext()) {
				TeacherHasSocialNetwork teacherHasSocialNetwork = i.next();
				if(teacherHasSocialNetwork.getSocialNetwork().getId() == null || teacherHasSocialNetwork.getNickname() == null) {
					return new ResponseEntity(new CustomErrorType("We need at least teacher_id, socialNetwork_id and nickname."), HttpStatus.CONFLICT);
				}else {
					TeacherHasSocialNetwork tsnAux = this._socialNetworkService.findSocialNetworkByIdAndName(teacherHasSocialNetwork.getSocialNetwork().getId(), teacherHasSocialNetwork.getNickname()); 
					if(tsnAux != null) {
						return new ResponseEntity(new CustomErrorType("The Social Network " + teacherHasSocialNetwork.getSocialNetwork().getId() + " with nickname: " + teacherHasSocialNetwork.getNickname() + " already exists."), HttpStatus.CONFLICT);
					}
					SocialNetwork socialNetwork = this._socialNetworkService.findById(teacherHasSocialNetwork.getSocialNetwork().getId());
					if(socialNetwork == null) {
						return new ResponseEntity(new CustomErrorType("The social network " + teacherHasSocialNetwork.getSocialNetwork().getId() + " not found."), HttpStatus.NOT_FOUND);
					}
					teacherHasSocialNetwork.setSocialNetwork(socialNetwork);
					teacherHasSocialNetwork.setTeacher(teacherSaved);
					
					if(tsnAux == null) {
						teacherSaved.getTeacherHasSocialNetwork().add(teacherHasSocialNetwork);
					}else {
						LinkedList<TeacherHasSocialNetwork> teacherHasSocialNetworks = new LinkedList<>();
						teacherHasSocialNetworks.addAll(teacherSaved.getTeacherHasSocialNetwork());
						
						for(int j = 0; j < teacherHasSocialNetworks.size(); j++) {
							TeacherHasSocialNetwork teacherHasSocialNetworkComp = teacherHasSocialNetworks.get(j);
							if(teacherHasSocialNetwork.getTeacher().getId() == teacherHasSocialNetworkComp.getTeacher().getId()
							 && teacherHasSocialNetwork.getSocialNetwork().getId() == teacherHasSocialNetworkComp.getSocialNetwork().getId()
							) {
								teacherHasSocialNetworkComp.setNickname(teacherHasSocialNetwork.getNickname());
								teacherHasSocialNetworks.set(j, teacherHasSocialNetworkComp);
							}else {
								teacherHasSocialNetworks.set(j, teacherHasSocialNetworkComp);
							}
						}
						
						teacherSaved.getTeacherHasSocialNetwork().clear();
						teacherSaved.getTeacherHasSocialNetwork().addAll(teacherHasSocialNetworks);
						
					}
				}
				
			}
		}
		
		this._teacherService.updateTeacher(teacherSaved);
		return new ResponseEntity<Teacher>(teacherSaved, HttpStatus.OK);
	}
	
}
