package com.nguyenkhanh.backend;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nguyenkhanh.backend.services.Impl.FilesServiceImpl;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {
	@Resource
	private FilesServiceImpl storageService;

	@Override
	public void run(String... args) throws Exception {
//		filesService.deleteAll();
		storageService.init();

//		<===============TEST===============>

	}

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}
}
