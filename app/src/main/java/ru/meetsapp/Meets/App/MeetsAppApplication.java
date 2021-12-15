package ru.meetsapp.Meets.App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MeetsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetsAppApplication.class, args);
	}

}
