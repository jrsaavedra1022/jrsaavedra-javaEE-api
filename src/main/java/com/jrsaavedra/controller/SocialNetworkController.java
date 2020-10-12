package com.jrsaavedra.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.jrsaavedra.service.SocialNetworkService;
import com.jrsaavedra.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class SocialNetworkController {
	@Autowired
	private SocialNetworkService _socialNetworkService;
	//GET
	@RequestMapping(value="/socialNetworks", method=RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<List<SocialNetwork>> getSocialNetworks(@RequestParam(value="name", required=false) String name){
		List<SocialNetwork> socialNetworks = new ArrayList<>();
		if(name == null) {
			socialNetworks = this._socialNetworkService.findAllSocialNetwork();
			if(socialNetworks.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<SocialNetwork>>(socialNetworks, HttpStatus.OK);
		}else {
			SocialNetwork socialNetwork = this._socialNetworkService.findByName(name);
			if(socialNetwork == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			socialNetworks.add(socialNetwork);
			return new ResponseEntity<List<SocialNetwork>>(socialNetworks, HttpStatus.OK);
		}
		
	}
	
	//GET by Id
	@RequestMapping(value="/socialNetworks/{id}", method=RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<SocialNetwork> getSocialMediaById(@PathVariable("id") Long socialNetwork_id){
		if(socialNetwork_id == null || socialNetwork_id <= 0) {
			return new ResponseEntity(new CustomErrorType("SocialNetwork_id is required !!"), HttpStatus.CONFLICT);
		}
		SocialNetwork socialNetwork = this._socialNetworkService.findById(socialNetwork_id);
		if(socialNetwork == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<SocialNetwork>(socialNetwork, HttpStatus.OK);
				
	}
	//POST
	@RequestMapping(value="/socialNetworks", method=RequestMethod.POST, headers="Accept=application/json")
	public ResponseEntity<?> createSocialNetwork(@RequestBody SocialNetwork socialNetwork, UriComponentsBuilder uriComponentsBuilder){
		if(socialNetwork.getName().equals(null) || socialNetwork.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("SocialNetwork name is required !!"), HttpStatus.CONFLICT);
		}
		if(this._socialNetworkService.findByName(socialNetwork.getName())!= null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		this._socialNetworkService.saveSocialNetwork(socialNetwork);
		SocialNetwork socialNetworkNew = this._socialNetworkService.findByName(socialNetwork.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder
				.path("v1/socialNetworks/{id}")
				.buildAndExpand(socialNetworkNew.getId())
				.toUri()
		);
		
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//UPDATE
	@RequestMapping(value="/socialNetworks/{id}", method=RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<SocialNetwork> updateSocialNetwork(@PathVariable("id") long socialNetwork_id, @RequestBody SocialNetwork socialNetwork){
		SocialNetwork currentSocialNetwork = this._socialNetworkService.findById(socialNetwork_id);
		if(currentSocialNetwork == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		currentSocialNetwork.setName(socialNetwork.getName());
		currentSocialNetwork.setIcon(socialNetwork.getIcon());
		this._socialNetworkService.updateSocialNetwork(currentSocialNetwork);
		return new ResponseEntity<SocialNetwork>(currentSocialNetwork, HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/socialNetworks/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteSocialNetwork(@PathVariable("id") Long socialNetwork_id){
		if(socialNetwork_id == null || socialNetwork_id <= 0) {
			return new ResponseEntity(new CustomErrorType("SocialNetwork_id is required !!"), HttpStatus.CONFLICT);
		}
		SocialNetwork currentSocialNetwork = this._socialNetworkService.findById(socialNetwork_id);
		if(currentSocialNetwork == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		this._socialNetworkService.deleteSocialNetwork(socialNetwork_id);
		return new ResponseEntity<SocialNetwork>(HttpStatus.OK);
	}
	
	public static final String SOCIALNETWORK_UPLOADED_FOLDER = "images/socialNetworks/";
	//CREATE SOCIALNETWORK ICON
	@RequestMapping(value="/socialNetworks/images", method=RequestMethod.POST, headers="content-type=multipart/form-data")
	public ResponseEntity<byte[]> uploadSocialNetworkImage(@RequestParam("id") Long socialNetwork_id, @RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder){
		if(socialNetwork_id == null || socialNetwork_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The socialNetwork_id is required."), HttpStatus.CONFLICT);
		}
		if(multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload."), HttpStatus.CONFLICT);
		}
		
		SocialNetwork socialNetwork = this._socialNetworkService.findById(socialNetwork_id);
		if(socialNetwork == null) {
			return new ResponseEntity(new CustomErrorType("The socialNetwork's id: " + socialNetwork_id + " doesn't exists"), HttpStatus.NOT_FOUND);
		
		}
		
		if(!socialNetwork.getIcon().isEmpty() || socialNetwork.getIcon() != null) {
			String fileName = socialNetwork.getIcon();
			Path path = Paths.get(fileName);
			File file =  path.toFile();
			if(file.exists()) {
				file.delete();
			}
		}
		
		try {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName = simpleDateFormat.format(date);
			
			String fileName = String.valueOf(socialNetwork_id) + "-socialNetwork_icon-" + dateName + "." + multipartFile.getContentType().split("/")[1]; 
			socialNetwork.setIcon(SOCIALNETWORK_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(SOCIALNETWORK_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			this._socialNetworkService.updateSocialNetwork(socialNetwork);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("The socialNetwork's icon could not be uploaded."), HttpStatus.CONFLICT);
		}
	}
	
	//GET IMAGE
	@RequestMapping(value="/socialNetworks/{id}/images", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getSocialNetworkImageById(@PathVariable("id") Long socialNetwork_id){
		if(socialNetwork_id == null || socialNetwork_id <= 0) {
			return new ResponseEntity(new CustomErrorType("The socialNetwork_id is required."), HttpStatus.CONFLICT);
		}
		
		SocialNetwork socialNetwork = this._socialNetworkService.findById(socialNetwork_id);
		if(socialNetwork == null) {
			return new ResponseEntity(new CustomErrorType("The socialNetwork's id: " + socialNetwork_id + " doesn't exists"), HttpStatus.NOT_FOUND);
		
		}
		try {
			String fileName = socialNetwork.getIcon();
			Path path = Paths.get(fileName);
			File file = path.toFile();
			if(!file.exists()) {
				return new ResponseEntity(new CustomErrorType("The socialNetwork's icon with id: "+ socialNetwork_id + " doesn't exists."), HttpStatus.CONFLICT);
			}
			byte[] image = Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("The socialNetwork's icon with id: "+ socialNetwork_id + " doesn't exists."), HttpStatus.CONFLICT);
		}
	}
	// DELETE IMAGE
	@RequestMapping(value="/socialNetworks/{id}/images", method=RequestMethod.DELETE, headers="Accept=application/json")
	public ResponseEntity<?> deleteSocialNetworkImage(@PathVariable("id") Long socialNetwork_id){
		if(socialNetwork_id == null || socialNetwork_id <= 0) {
			return new ResponseEntity<>(new CustomErrorType("The socialNetwork_id is required."), HttpStatus.CONFLICT);
		}
		
		SocialNetwork socialNetwork = this._socialNetworkService.findById(socialNetwork_id);
		if(socialNetwork == null) {
			return new ResponseEntity<>(new CustomErrorType("The socialNetwork's id: " + socialNetwork_id + " doesn't exists"), HttpStatus.NOT_FOUND);
		
		}
		if(socialNetwork.getIcon().isEmpty() || socialNetwork.getIcon() == null) {
			return new ResponseEntity<>(new CustomErrorType("This socialNetwork's id "+socialNetwork_id+" doesn't have a icon assigned."), HttpStatus.CONFLICT);
		}
		String fileName = socialNetwork.getIcon();
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if(file.exists()) {
			file.delete();
		}
		
		socialNetwork.setIcon("");
		this._socialNetworkService.updateSocialNetwork(socialNetwork);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
