package com.nguyenkhanh.backend.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFilesService {
	public void init();

	public void save(MultipartFile file);

	public Stream<Path> loadAll();

	public Resource load(String filename);

	public void deleteAll();
}
