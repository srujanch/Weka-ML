package com.optum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.optum.healthcare.HeartDiseaseClassification;

@SpringBootApplication
public class WekademoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WekademoApplication.class, args);
	}

	@Bean
	public HeartDiseaseClassification heartDiseaseClassification() {
		return new HeartDiseaseClassification();
	}
}
