package com.nguyenkhanh.backend;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nguyenkhanh.backend.services.Impl.FilesService;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {
	@Resource
	FilesService filesService;

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		filesService.deleteAll();
		filesService.init();
	}

}
