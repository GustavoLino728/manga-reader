package com.lino.manga_reader_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient mangaDexClient() {
        return WebClient.builder()
            .baseUrl("https://api.mangadex.org")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("User-Agent", "manga-reader-personal/1.0")
            .codecs(config -> config.defaultCodecs()
                .maxInMemorySize(2 * 1024 * 1024)) // 2MB buffer
            .build();
    }
}