package com.safety.anpr_mgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AnprMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnprMgmtApplication.class, args);
	}

}
