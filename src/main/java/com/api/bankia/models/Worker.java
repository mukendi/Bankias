package com.api.bankia.models;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "works", 
uniqueConstraints = { 
      @UniqueConstraint(columnNames = "email")
      /*,
      @UniqueConstraint(columnNames = "phone_number") */
})
public class Worker {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String firstname;
	
	@NotBlank
	private String lastname;
	
	@NotBlank
	private String sexe;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String phone_number;
	
	//private Set<Category> category = new HashSet<>();
	private String category;

	private String profilPicturePath;
	//private String documentIdentityPath;
	
	public Worker() {
		
	}
	public Worker(Long id, @NotBlank String name, @NotBlank String firstname, @NotBlank String lastname,
			@NotBlank String sexe, @NotBlank String email, @NotBlank String phone_number, String category, String profilPicturePath) {
		super();
		this.id = id;
		this.name = name;
		this.firstname = firstname;
		this.lastname = lastname;
		this.sexe = sexe;
		this.email = email;
		this.phone_number = phone_number;
		this.category = category;
		this.profilPicturePath = profilPicturePath;
		//this.documentIdentityPath = documentIdentityPath;
	}
	
	
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
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getSexe() {
		return sexe;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getProfilPicturePath() {
		return profilPicturePath;
	}
	public void setProfilPicturePath(String profilPicturePath) {
		this.profilPicturePath = profilPicturePath;
	}
	/*
	public String getDocumentIdentityPath() {
		return documentIdentityPath;
	}
	public void setDocumentIdentityPath(String documentIdentityPath) {
		this.documentIdentityPath = documentIdentityPath;
	}
	*/
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	/*
	public Set<Category> getCategory() {
		return category;
	}
	public void setCategory(Set<Category> category) {
		this.category = category;
	}
	*/
}
