package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.user.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

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