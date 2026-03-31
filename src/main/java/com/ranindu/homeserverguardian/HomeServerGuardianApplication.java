package com.ranindu.homeserverguardian;

import com.ranindu.homeserverguardian.config.AIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class HomeServerGuardianApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeServerGuardianApplication.class, args);
	}


}
