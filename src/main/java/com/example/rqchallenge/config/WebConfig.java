package com.example.rqchallenge.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
@Slf4j
public class WebConfig {

    @Value("${url.base}")
    private String baseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(baseUrl).filters(exchangeFilterFunctions -> {
            exchangeFilterFunctions.add(logRequest());
            exchangeFilterFunctions.add(logResponse());
            exchangeFilterFunctions.add(copyMDCMap());
        }).defaultHeader(HttpHeaders.ACCEPT,
                MediaType.APPLICATION_JSON_VALUE).build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                log.debug("Request URL: {}", clientRequest.url().toASCIIString());
                log.debug("Request attributes: {}", clientRequest.attributes().values());
            }
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Log Response status: {}", clientResponse.statusCode());
            if (log.isDebugEnabled()) {
                clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("Response Header: {}={}", name, value)));
            }
            return Mono.just(clientResponse);
        });
    }

    public static ExchangeFilterFunction copyMDCMap() {
        return (request, next) -> {
            Map<String, String> map = MDC.getCopyOfContextMap();
            return next.exchange(request)
                    .doOnNext(value -> {
                        if (map != null) {
                            MDC.setContextMap(map);
                        }
                    });
        };
    }
}
