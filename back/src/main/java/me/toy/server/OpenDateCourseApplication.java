package me.toy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class OpenDateCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenDateCourseApplication.class, args);
	}
}
