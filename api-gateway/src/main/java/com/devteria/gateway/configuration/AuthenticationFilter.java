package com.devteria.gateway.configuration;

import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;

    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = {"/identity/auth/.*", "/identity/users" +
            "/registration"};

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter... ");

        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        // Get token from header
        val authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token {}, aa {}", token, identityService);

        // verify token
        return identityService.introspect(token).flatMap(res -> {
            if (res.getResult().isValid()) {
                log.info("aaa");
                return chain.filter(exchange);
            } else {
                log.info("bbb");
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));

    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse res) {
        String body = null;

        log.info("BBB vo day");

        ApiResponse<?> apiResponse = ApiResponse.builder().code(1401).message(
                "Unauthenticated").build();

        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        res.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return res.writeWith(Mono.just(res.bufferFactory().wrap(body.getBytes())));
    }
}
