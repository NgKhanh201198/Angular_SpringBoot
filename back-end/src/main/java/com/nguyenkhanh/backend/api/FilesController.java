package com.nguyenkhanh.backend.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import com.nguyenkhanh.backend.entity.File;
import com.nguyenkhanh.backend.exception.ResponseMessage;
import com.nguyenkhanh.backend.services.Impl.FilesService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class FilesController {
//	private static final Logger logger = LoggerFactory.getLogger(FilesController.class);

	@Autowired
	FilesService storageService;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage(new Date(), HttpStatus.OK.value(), message));
		} catch (Exception e) {
			message = "Filename '" + file.getOriginalFilename()
					+ "' already exists. Please rename the file and try again!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(new Date(),
					HttpStatus.EXPECTATION_FAILED.value(), "Expectation Failed", message));
		}
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/jpeg"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "Content-Disposition: inlines")
//				.header(HttpHeaders.CONTENT_DISPOSITION, "Content-Disposition: attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/files")
	public ResponseEntity<List<File>> getListFiles() {
		List<File> Files = storageService.loadAll().map(file -> {
			// get filename
			String filename = file.getFileName().toString();

			// create URL format: http://localhost:8080/files + filename
			// fromMethodName(Class<?> controllerType, String methodName, Object... args)
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getFile", file.getFileName().toString()).build().toString();
			return new File(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(Files);
	}

}
