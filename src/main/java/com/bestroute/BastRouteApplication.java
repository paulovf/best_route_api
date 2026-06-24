package com.bestroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class BastRouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BastRouteApplication.class, args);
	}

}
