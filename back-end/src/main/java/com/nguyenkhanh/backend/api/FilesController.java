package com.nguyenkhanh.backend.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.nguyenkhanh.backend.entity.Files;
import com.nguyenkhanh.backend.exception.BadRequestException;
import com.nguyenkhanh.backend.exception.ResponseMessage;
import com.nguyenkhanh.backend.services.Impl.FilesServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FilesController {

	@Autowired
	FilesServiceImpl storageService;

	// upload one file
	// @PostMapping("/upload")
	// public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
	// 	String message = "";
	// 	try {
	// 		storageService.save(file);

	// 		message = "Uploaded the file successfully: " + file.getOriginalFilename();
	// 		return ResponseEntity.status(HttpStatus.OK)
	// 				.body(new ResponseMessage(new Date(), HttpStatus.OK.value(), message));
	// 	} catch (Exception e) {
	// 		message = "Filename '" + file.getOriginalFilename()
	// 				+ "' already exists. Please rename the file and try again!";
	// 		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(new Date(),
	// 				HttpStatus.EXPECTATION_FAILED.value(), "Expectation Failed", message));
	// 	}
	// }

	// upload multiple file
	@PostMapping("/upload")
	public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			String[] allowedMimeTypes = new String[] { "image/gif", "image/png", "image/jpeg" };
			List<String> fileNames = new ArrayList<>();
			Arrays.asList(file).stream().forEach(fileItem -> {
				if (!ArrayUtils.contains(allowedMimeTypes, fileItem.getContentType().toLowerCase())) {
					throw new BadRequestException("Invalid file, valid files include: jpg, png, gif");
				}
				String fileName = fileItem.getOriginalFilename().substring(fileItem.getOriginalFilename().length() - 4,
						fileItem.getOriginalFilename().length());

				String uuidImage = UUID.randomUUID().toString().replaceAll("-", "") + fileName.toLowerCase();
				storageService.save(fileItem, uuidImage);
				fileNames.add(fileItem.getOriginalFilename());

			});
			String strFileNames = fileNames.stream().map(Object::toString).collect(Collectors.joining(", "));
			message = "Uploaded the file successfully: " + strFileNames;
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage(new Date(), HttpStatus.OK.value(), message));
		} catch (BadRequestException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(new Date(),
					HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
		}
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		System.out.println(file);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/jpeg"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "Content-Disposition: inlines")//show
				// .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")//download
				.body(file);
	}

	@GetMapping("/files")
	public ResponseEntity<List<Files>> getListFiles() {
		List<Files> Files = storageService.loadAll().map(file -> {
			// get filename
			String filename = file.getFileName().toString();

			// create URL format: http://localhost:8080/files + filename
			// fromMethodName(Class<?> controllerType, String methodName, Object... args)
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getFile", file.getFileName().toString()).build().toString();
			return new Files(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(Files);
	}

}
