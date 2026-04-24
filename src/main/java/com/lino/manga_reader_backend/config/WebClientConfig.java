package com.lino.manga_reader_backend.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    private HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000) // 30 segundos
                .responseTimeout(Duration.ofSeconds(30));
    }

    @Bean
    public WebClient mangaDexClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .baseUrl("https://api.mangadex.org")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("User-Agent", "manga-reader-personal/1.0")
                .codecs(config -> config.defaultCodecs()
                        .maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    @Bean
    public WebClient imageClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader("User-Agent", "manga-reader-personal/1.0")
                .codecs(config -> config.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)) // 10MB para imagens
                .build();
    }
}