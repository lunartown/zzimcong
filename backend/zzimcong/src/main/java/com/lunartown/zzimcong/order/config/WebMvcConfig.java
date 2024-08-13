package com.lunartown.zzimcong.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserInfoArgumentResolver userInfoArgumentResolver;

    public WebMvcConfig(UserInfoArgumentResolver userInfoArgumentResolver) {
        this.userInfoArgumentResolver = userInfoArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoArgumentResolver);
    }
}