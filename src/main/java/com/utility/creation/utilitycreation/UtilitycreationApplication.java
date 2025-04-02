package com.utility.creation.utilitycreation;

import com.utility.creation.utilitycreation.config.AdminUserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = { "com.utility.creation.utilitycreation" })
@EnableCaching
@EnableConfigurationProperties(AdminUserProperties.class)
public class UtilitycreationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilitycreationApplication.class, args);
	}

}
