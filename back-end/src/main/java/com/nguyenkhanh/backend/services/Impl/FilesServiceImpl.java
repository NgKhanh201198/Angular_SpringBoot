package com.nguyenkhanh.backend.services.Impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nguyenkhanh.backend.exception.NotFoundException;
import com.nguyenkhanh.backend.services.IFilesService;

@Service
public class FilesServiceImpl implements IFilesService {
	private final Path root = Paths.get("uploads/");

	@Override
	public void save(MultipartFile file) {
		try {

			// copy(**, uploads\filename)
			String fileName = file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4,
					file.getOriginalFilename().length());

			String uuid = UUID.randomUUID().toString() + fileName.toLowerCase();

			Files.copy(file.getInputStream(), this.root.resolve(uuid));
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			// Files.walk(folder, index) tìm kiếm các file ở folder nếu index=1, index > 1
			// sẽ tìm các folder con
			// Trả về các file có trong folder
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root))
					.map(path -> this.root.relativize(path));
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			// Lấy đường dẫn file từ folder root = uploads
			Path file = root.resolve(filename).normalize();

			// Lấy đường dẫn file từ ổ đĩa vd:
			// file:/D:/STUDY/GitRepository/Angular-SpringBoot/back-end/uploads/1.jpg
			Resource resource = new UrlResource(file.toUri());

			// resource.exists() check tồn tại ko || resource.isReadable() check path tồn
			// tại ko
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new NotFoundException("File not found " + filename);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		// Xóa thư mục root = uploads
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public void init() {
		try {
			if (!(Files.isDirectory(root))) {
				Files.createDirectory(root);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

}
