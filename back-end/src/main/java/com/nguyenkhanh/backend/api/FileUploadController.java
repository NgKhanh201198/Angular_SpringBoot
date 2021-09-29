package com.nguyenkhanh.backend.api;

import com.nguyenkhanh.backend.entity.Files;
import com.nguyenkhanh.backend.exception.ResponseMessage;
import com.nguyenkhanh.backend.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/upload")
public class FileUploadController {
    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(new Date(), HttpStatus.OK.value(), generatedFileName));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseMessage(new Date(), HttpStatus.NOT_IMPLEMENTED.value(), HttpStatus.NOT_IMPLEMENTED.name(), exception.getMessage()));
        }
    }

    @GetMapping("/file/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("/file")
    public ResponseEntity<?> readAllFile() {
        try {
            List<Files> filesList = storageService.loadALl().map(file -> {
                String filename = file.getFileName().toString();
                String url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "readDetailFile", file.getFileName().toString()).build().toString();
                return new Files(filename, url);
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(filesList);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseMessage(new Date(), HttpStatus.NOT_IMPLEMENTED.value(), HttpStatus.NOT_IMPLEMENTED.name(), exception.getMessage()));
        }

    }
}
