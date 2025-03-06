package com.api.bankia.security.services;


import java.io.*;
import java.nio.*;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.bankia.models.Worker;
import com.api.bankia.repository.WorkerRepository;


@Service
public class WorkerService {
    
	private static final String UPLOAD_DIR="uploads/";
	
	@Autowired
	private WorkerRepository workerRepository;
	
	WorkerService() {
		this.workerRepository = null;
	}
	public static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpge","image/png","image/gif");
	
	public Worker saveWorker(Worker worker) {
		return workerRepository.save(worker);
	}
	
	public Worker saveWorkerWithProfilePicture(Worker worker, MultipartFile file) throws IOException {
		
		Path uploadPath = Paths.get(UPLOAD_DIR);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		String fileName = worker.getId() + "_" + file.getOriginalFilename();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
       
        worker.setProfilPicturePath(fileName);

        // Sauvegarder l'utilisateur dans la base de données
        return workerRepository.save(worker);
	}
	
	public Page<Worker> getWorkers(Pageable pageable) {
		return this.workerRepository.findAll(pageable);
	}
	public byte[] getProfilePicture(String fileName) throws IOException{
		Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
		return Files.readAllBytes(filePath);
	}
	
	public void deleteWorker(Long id) throws IOException {
		
		Worker worker = this.workerRepository.findById(id)
				            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
		
		if (worker.getProfilPicturePath() != null) {
			Path filePath = Paths.get(UPLOAD_DIR).resolve(worker.getProfilPicturePath());
			Files.deleteIfExists(filePath);
		}
		
		this.workerRepository.delete(worker);
	}
	

	public Worker updateWorker(Long id, Worker worker) {
		return this.workerRepository.findById(id).map(w -> {
			  w.setName(worker.getName());
			  w.setLastname(worker.getLastname());
			  w.setFirstname(worker.getFirstname());
			  w.setSexe(worker.getSexe());
			  w.setPhone_number(worker.getPhone_number());
			  w.setEmail(worker.getEmail());
			  w.setCategory(worker.getCategory());
			    return this.workerRepository.save(w);
		}).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
	}
	public Worker uploadProfilePicture(Long workerId, MultipartFile file) throws IOException{
		
		Worker worker = workerRepository.findById(workerId)
				                        .orElseThrow(() -> new RuntimeException("User not found!"));
		
		
		Path uploadPath = Paths.get(UPLOAD_DIR);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		String fileName = workerId + "_" + file.getOriginalFilename();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		worker.setProfilPicturePath(fileName);
		
		return workerRepository.save(worker);
	}
	
	public Worker upadateProfilePicture(Long workerId, MultipartFile file) throws IOException {
		
		Worker worker = this.workerRepository.findById(workerId).orElseThrow();
		
		
		
		if (worker.getProfilPicturePath() != null) {
			Path oldFilePath = Paths.get(UPLOAD_DIR).resolve(worker.getProfilPicturePath());
			Files.deleteIfExists(oldFilePath);
		}
		
		String filename = workerId + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
		Files.copy(file.getInputStream(),filePath,StandardCopyOption.REPLACE_EXISTING);
		
		worker.setProfilPicturePath(filename);
		return workerRepository.save(worker);
		
	}
}
