package com.api.bankia.controllers;


import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bankia.models.User;
import com.api.bankia.payload.response.MessageResponse;
import com.api.bankia.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/bankia")
public class UserController {
 
  @Autowired
  UserRepository userRepository;
  
  HashMap<String,Object> responseMessage = null;
  MappingJacksonValue    jsonResponse    = null;
  
  
  public UserController() {
	  this.responseMessage = new HashMap<String,Object>();
	  this.jsonResponse    = new MappingJacksonValue(null);
  }
  
  @DeleteMapping("/admin/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
	  try {
		  Optional<User> user = this.userRepository.findById(id);
		  if (user.isPresent()) {
			  this.userRepository.deleteById(id);
			  this.responseMessage.put("message","User delete with successfull");
			  this.responseMessage.put("status", 200);
			  
			  this.jsonResponse.setValue(this.responseMessage);
			  return new ResponseEntity<Object>(this.jsonResponse,HttpStatus.OK);
		  }else {
			  this.responseMessage.put("message","User not found");
			  this.responseMessage.put("status", 404);
			  this.jsonResponse.setValue(this.responseMessage);
			  return new ResponseEntity<Object>(this.jsonResponse, HttpStatus.NOT_FOUND);
		  }
		  
	  }catch(Exception e) {
		  return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	  }
  }
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public Iterable<User> getUsers(){
	  Iterable<User> listUsers = userRepository.findAll();
	  return listUsers;
  }
  /*
  @GetMapping("/user/{id}")
  @PreAuthorize("#id == principal.id")
  public ResponseEntity<Object> getUserById(@PathVariable("id") long id){
	  Optional<User> user = this.userRepository.findById(id);
	  
	  if (user.isPresent()) {
		  return new ResponseEntity<>(user.get(), HttpStatus.OK);
	  } else {
		  this.responseMessage.put("message","Utilisateur introuvable");
		  this.responseMessage.put("status", 404);
		  
		  this.jsonResponse.setValue(this.responseMessage);
		  return new ResponseEntity<Object>(this.jsonResponse, HttpStatus.NOT_FOUND);
	  }
	  
  }
  @PutMapping("/user/{id}")
  public ResponseEntity<User> updateUser(@Valid @RequestBody User _user,@PathVariable("id") long id){
	Optional<User> user = this.userRepository.findById(id);
	System.out.println(user);
	if (user.isPresent()) {
		user.get().setEmail(_user.getEmail());
		user.get().setUsername(_user.getUsername());
		
		this.responseMessage.put("message","Update with successfully");
		this.jsonResponse.setValue(responseMessage);
		return new ResponseEntity<>(this.userRepository.save(_user), HttpStatus.OK);
		
	}
	
	return null;
	  
  }
  */
}
