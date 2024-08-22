package com.zzimcong.apigateway.filter;

import com.zzimcong.apigateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j(topic = "JwtAuthFilter")
@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // JWT 토큰이 없거나 "Bearer " 접두사가 없는 경우
            if (token == null || !token.startsWith("Bearer ")) {
                return onError(exchange, "인증 토큰이 없거나 잘못되었습니다.", HttpStatus.UNAUTHORIZED);
            }

            // "Bearer " 접두사 제거
            token = token.substring(7);

            // 토큰 유효성 검사
            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }

            // userId 추출 및 요청 속성에 저장
            String userId = jwtUtil.extractUserId(token);
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-Auth-User-ID", userId)
                            .build())
                    .build();

            return chain.filter(modifiedExchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String responseBody = String.format("{\"error\": \"%s\"}", err);
        log.error("Authentication error: {}", err);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
    }

    public static class Config {
        // 필요한 경우 설정 속성을 추가할 수 있습니다.
    }
}