package com.lino.manga_reader_backend;

import com.lino.manga_reader_backend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class MangaReaderBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(MangaReaderBackendApplication.class, args);
	}
}
