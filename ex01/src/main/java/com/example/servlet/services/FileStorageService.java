package com.example.servlet.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.servlet.models.UploadedFile;
import com.example.servlet.models.User;
import com.example.servlet.repositories.UploadedFileRepository;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path storageLocation;
    private final UploadedFileRepository uploadedFileRepository;

    @Autowired
    public FileStorageService(
            @Value("${storage.path}") String storagePath,
            UploadedFileRepository uploadedFileRepository) {

        this.uploadedFileRepository = uploadedFileRepository;
        this.storageLocation = Paths.get(storagePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.storageLocation);
            logger.info("Storage directory created at: {}", this.storageLocation);
        } catch (IOException e) {
            logger.error("Could not create storage directory: {}", e.getMessage());
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    public UploadedFile storeFile(InputStream fileContent, String fileName, String contentType,
            long fileSize, User user) {
        // Generate unique filename to prevent collisions
        String fileExtension = getFileExtension(fileName);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            // Copy file to storage location
            Path targetLocation = this.storageLocation.resolve(uniqueFilename);
            Files.copy(fileContent, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create and save file record in database
            UploadedFile uploadedFile = new UploadedFile(
                    user.getId(),
                    fileName,
                    uniqueFilename,
                    contentType,
                    fileSize
            );

            return uploadedFileRepository.save(uploadedFile);

        } catch (IOException e) {
            logger.error("Failed to store file: {}", e.getMessage());
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public List<UploadedFile> getUserFiles(Long userId) {
        return uploadedFileRepository.findByUserId(userId);
    }

    public Optional<Path> getFilePath(String storedFilename) {
        Path filePath = this.storageLocation.resolve(storedFilename);
        if (Files.exists(filePath)) {
            return Optional.of(filePath);
        }
        return Optional.empty();
    }

    public Optional<UploadedFile> getFileInfo(String storedFilename) {
        return uploadedFileRepository.findByStoredFilename(storedFilename);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
