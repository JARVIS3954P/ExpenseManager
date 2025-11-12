package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.service.interfaces.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir){
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try{
            Files.createDirectories((this.fileStorageLocation));
        }catch(Exception e){
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }
    @Override
    public String storeFile(MultipartFile file) throws IOException {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch(Exception e) {
            // Handle cases where file has no extension
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

}
