package com.jwt.project.jwtauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// mvn liquibase:generateChangeLog -DoutputChangeLogFile=src/main/resources/db/changelog/db.generated-changelog.xml

@SpringBootApplication
public class JwtauthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtauthenticationApplication.class, args);
	}

}
