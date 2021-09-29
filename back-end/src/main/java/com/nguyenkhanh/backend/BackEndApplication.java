package com.nguyenkhanh.backend;

import com.nguyenkhanh.backend.services.Impl.FilesServiceImpl;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {
    @Resource
    private FilesServiceImpl storageService;

    @Override
    public void run(String... args) throws Exception {
//		storageService.deleteAll();
//		storageService.init();

//		<===============TEST===============>


    }

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }
}
