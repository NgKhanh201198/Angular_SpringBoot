package com.nguyenkhanh.backend.services.Impl;

import com.nguyenkhanh.backend.exception.NotFoundException;
import com.nguyenkhanh.backend.services.IStorageService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements IStorageService {
    private final Path storageFolder = Paths.get("uploads");

    public StorageServiceImpl() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg"}).contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            // check file type
            if (!(isImageFile(file))) {
                throw new RuntimeException("You can only upload file image.");
            }

            // check file size
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb.");
            }

            // rename file
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;

            // stored file path
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.storageFolder.resolve(destinationFilePath));
            }
            return generatedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadALl() {
        try {
            return Files.walk(this.storageFolder, 1).filter(path -> !path.equals(this.storageFolder)).map(this.storageFolder::relativize);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load stored files!", exception);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
