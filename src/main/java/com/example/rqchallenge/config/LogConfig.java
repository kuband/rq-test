package com.example.rqchallenge.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Configuration
public class LogConfig {

    //TODO: add some form of response logging, like AOP of ResponseBodyAdvice
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter() {
            @Override
            public void beforeRequest(HttpServletRequest request, String message) {
                try {
                    String requestId = UUID.randomUUID().toString();
                    MDC.put("traceId", requestId);
                    logger.debug(message);
                } finally {
                    MDC.remove("requestId");
                }
            }

            @Override
            public void afterRequest(HttpServletRequest request, String message) {
                try {
                    logger.debug(message);
                } finally {
                    MDC.remove("requestId");
                }
            }
        };
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        return filter;
    }
}
