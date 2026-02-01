package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SportEaseApplication {
	public static void main(String[] args) {
		SpringApplication.run(SportEaseApplication.class, args);
	}

}
