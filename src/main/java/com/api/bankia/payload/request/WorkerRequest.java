package com.api.bankia.payload.request;

import jakarta.validation.constraints.NotBlank;

public class WorkerRequest {
  @NotBlank
  private String name;
  
  @NotBlank
  private String fistName;
  
  @NotBlank
  private String lastName;
  
  @NotBlank
  private String email;
  
  @NotBlank
  private String phone_number;
  
  @NotBlank
  private String sexe;
  
  @NotBlank
  private String category;

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getFistName() {
	return fistName;
}

public void setFistName(String fistName) {
	this.fistName = fistName;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
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

public String getSexe() {
	return sexe;
}

public void setSexe(String sexe) {
	this.sexe = sexe;
}

public String getCategory() {
	return category;
}

public void setCategory(String category) {
	this.category = category;
}
  
  
  
  
}
