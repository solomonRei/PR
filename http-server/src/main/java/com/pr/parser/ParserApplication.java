package com.pr.parser;

import com.pr.parser.config.ScrappingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ScrappingProperties.class)
public class ParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParserApplication.class, args);
	}

}
