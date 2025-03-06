package com.api.bankia.controllers;


import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.api.bankia.models.User;
import com.api.bankia.models.Worker;
import com.api.bankia.payload.request.WorkerRequest;
import com.api.bankia.payload.response.MessageResponse;
import com.api.bankia.repository.WorkerRepository;
import com.api.bankia.security.services.FileStorageService;
import com.api.bankia.security.services.WorkerService;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/bankia")
public class WorkerController {
	@Autowired
    private WorkerService workerService;
	
	@Autowired
	private FileStorageService fileStorageService;
     
    @Autowired
    private WorkerRepository workerRepository;
    
    
    public static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg","image/png","image/gif");
	public WorkerController(WorkerService workerService) {
		super();
		this.workerService = workerService;
		this.workerRepository = null;
	}
	
	/*
	@PostMapping("/worker")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createWorker(@Valid @RequestBody WorkerRequest workerRequest) {
		
		
		
		if (this.workerRepository.existsByEmail(workerRequest.getEmail())) {
			 return  ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Cet email existe déjà !"));
		}
		
		
		Worker worker = new Worker();
		       worker.setName(workerRequest.getName());
		       worker.setFirstname(workerRequest.getFistName());
		       worker.setLastname(workerRequest.getLastName());
		       worker.setSexe(workerRequest.getSexe());
		       worker.setEmail(workerRequest.getEmail());
		       worker.setPhone_number(workerRequest.getPhone_number());
		       worker.setCategory(workerRequest.getCategory());
		       
		
		return  ResponseEntity.ok(workerService.saveWorker(worker));
	}
    */
	@PostMapping("/worker/{id}/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file){
		 if (!this.ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
			  return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Format non prise en charge"));
		  }
		
		try {
			
			return ResponseEntity.ok(workerService.uploadProfilePicture(id, file));
		}catch(IOException e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/{filename}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<byte[]> getProfilePicture(@PathVariable String filename) {
		try {
			byte[] image = workerService.getProfilePicture(filename);
			return ResponseEntity.ok().contentType(MediaType.ALL).body(image);
		}catch(IOException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	  @GetMapping("/list-workers")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<Page<Worker>> getWorkers(@RequestParam(defaultValue = "0") int page,
			                                         @RequestParam(defaultValue = "10") int size,
			                                         @RequestParam(defaultValue = "id") String sortBy,
			                                         @RequestParam(defaultValue = "asc") String direction) {
		  
		  Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC: Sort.Direction.ASC;
		  Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		  
		  return ResponseEntity.ok(workerService.getWorkers(pageable));
	  }
	  
	    @PostMapping("/worker/register")
	    public ResponseEntity<Worker> registerUser(
	    		                                 @RequestParam String name,
	                                             @RequestParam String firstname,
	                                             @RequestParam String lastname,
	                                             @RequestParam String email,
	                                             @RequestParam String phone_number,
	                                             @RequestParam String sexe,
	                                             @RequestParam String category,
	                                             @RequestParam(value = "profileImage", required = false) MultipartFile file) throws IOException {
	       
	    	
	    	Worker worker = new Worker();
	               worker.setName(name);
	               worker.setFirstname(firstname);
	               worker.setLastname(lastname);
	               worker.setEmail(email);
	               worker.setSexe(sexe);
	               worker.setPhone_number(phone_number);
	               worker.setCategory(category);
	               
	       Worker savedWorker = this.workerService.saveWorkerWithProfilePicture(worker, file);
	      //User savedUser = userService.saveUserWithProfilePicture(user, file);
	        return ResponseEntity.ok(savedWorker);
	        
	    	
	    	
	    }
	  
	  @PutMapping("/worker/update/{id}")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<Worker> updateWorkerProfile(
			    @PathVariable Long id,
			    @RequestParam("name") String name,
			    @RequestParam("firstname") String firstname,
		        @RequestParam("lastname") String lastname,
		        @RequestParam("email") String email,
		        @RequestParam("phone_number") String phoneNumber,
		        @RequestParam("sexe") String sexe,
		        @RequestParam("category") String category,
		        @RequestParam(value = "profileImage", required = false) MultipartFile file
			  ) {
		  
		 
		  
		  Worker worker = this.workerRepository.findById(id).get();
		  worker.setName(name);
		  worker.setLastname(lastname);
		  worker.setFirstname(firstname);
		  worker.setEmail(email);
		  worker.setPhone_number(phoneNumber);
		  worker.setCategory(category);
		  worker.setSexe(sexe);
		  
		  if (file  != null && !file.isEmpty()) {
			  String fileName = this.fileStorageService.saveFile(file, id);
			  worker.setProfilPicturePath(fileName);
		  }
		
		
		return ResponseEntity.ok(workerService.saveWorker(worker));
	  }
	  @PutMapping("/worker/{id}")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<Worker> updateWorker(@PathVariable Long id, @RequestBody WorkerRequest workerRequest) {
		  
		  Worker worker = new Worker();
		         worker.setName(workerRequest.getName());
		         worker.setLastname(workerRequest.getLastName());
		         worker.setFirstname(workerRequest.getFistName());
		         worker.setSexe(workerRequest.getSexe());
		         worker.setCategory(workerRequest.getCategory());
		         worker.setPhone_number(workerRequest.getPhone_number());
		         worker.setEmail(workerRequest.getEmail());
		  System.out.print("LASTNAME : " + workerRequest.getLastName());
		  return ResponseEntity.ok(this.workerService.updateWorker(id, worker));
	  }
	  
	  @DeleteMapping("/worker/{id}")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<?> deleteWorker(@PathVariable Long id) {
		  if (!this.workerRepository.existsById(id)) {
			  return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Utitilisateur non trouvé"));
		  }
		  try {
			  this.workerService.deleteWorker(id);
			  return ResponseEntity.noContent().build();
		  }catch( IOException e) {
			  return ResponseEntity.internalServerError().build();
		  }
	  }
	  
	  @GetMapping("/worker/{id}")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<?> getWorkerById(@PathVariable Long id) {
		  
		  
		  if (!this.workerRepository.existsById(id)) {
			  return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Utitilisateur non trouvé"));
		  }
		  
		  Worker worker = new Worker();
		  
		         worker.setId(this.workerRepository.findWorkerById(id).get().getId());
		         worker.setName(this.workerRepository.findWorkerById(id).get().getName());
	             worker.setLastname(this.workerRepository.findWorkerById(id).get().getLastname());
	             worker.setFirstname(this.workerRepository.findWorkerById(id).get().getFirstname());
	             worker.setSexe(this.workerRepository.findWorkerById(id).get().getSexe());
	             worker.setCategory(this.workerRepository.findWorkerById(id).get().getCategory());
	             worker.setPhone_number(this.workerRepository.findWorkerById(id).get().getPhone_number());
	             worker.setEmail(this.workerRepository.findWorkerById(id).get().getEmail());
		         worker.setProfilPicturePath(this.workerRepository.findWorkerById(id).get().getProfilPicturePath());
		  return ResponseEntity.ok(worker);
	  }
	  @PutMapping("/worker/picture/{id}")
	  @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<?>  updateProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
		  if (!this.workerRepository.existsById(id)) {
			  return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Utitilisateur non trouvé"));
		  }
		  
		  
		  if (!this.ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
			  return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Format non prise en charge"));
		  }
		 
			  
		 return ResponseEntity.ok(this.workerService.upadateProfilePicture(id, file));
			 
		  
		  
	  }
	  
	  @GetMapping("/worker/{id}/profile-picture")
	  //@PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<Resource> getProfilePicture(@PathVariable Long id) {
	      try {
	    	  Worker worker = new Worker();
			  System.out.println(id);
		         worker.setId(this.workerRepository.findWorkerById(id).get().getId());
		         worker.setName(this.workerRepository.findWorkerById(id).get().getName());
	             worker.setLastname(this.workerRepository.findWorkerById(id).get().getLastname());
	             worker.setFirstname(this.workerRepository.findWorkerById(id).get().getFirstname());
	             worker.setSexe(this.workerRepository.findWorkerById(id).get().getSexe());
	             worker.setCategory(this.workerRepository.findWorkerById(id).get().getCategory());
	             worker.setPhone_number(this.workerRepository.findWorkerById(id).get().getPhone_number());
	             worker.setEmail(this.workerRepository.findWorkerById(id).get().getEmail());
		         worker.setProfilPicturePath(this.workerRepository.findWorkerById(id).get().getProfilPicturePath());
		         
		         if (worker.getProfilPicturePath() == null) {
		        	 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		         }
		         
		         Path filePath = Paths.get("uploads/").resolve(worker.getProfilPicturePath());
		         
		         Resource resource = new UrlResource(filePath.toUri());
		         
		         if (resource.exists() || resource.isReadable()) {
		             return ResponseEntity.ok()
		                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
		                     .body(resource);
		         } else {
		             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		         }
		         
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	      }
	  }
	
}
