package com.api.bankia.security.services;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/";

    public FileStorageService() {
        // Crée le dossier d'upload s'il n'existe pas
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }

    public String saveFile(MultipartFile file, Long workerId) {
        try {
            // Génère un nom unique pour éviter les conflits
            String fileName = workerId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // Sauvegarde le fichier
            Files.write(filePath, file.getBytes());

            return fileName; // Retourne le nom du fichier stocké
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }
}