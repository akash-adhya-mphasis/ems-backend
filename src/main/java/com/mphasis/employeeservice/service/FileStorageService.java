package com.mphasis.employeeservice.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final String serverPath = "http://localhost:4000/uploads/";

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.fileStorageLocation);
        } catch(Exception ex) {
            throw new RuntimeException("Could not create 'uploads' directory.");
        }
    }


    public String storeFile(MultipartFile file) {
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        try{
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return serverPath+filename;
        } catch (Exception e) {
            throw new RuntimeException("Could not store image");
        }
    }
}
