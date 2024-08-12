package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.user.security.jwt.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public TokenService(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    public void storeRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken);
        redisTemplate.expire(username, 7, TimeUnit.DAYS);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(username);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }
}