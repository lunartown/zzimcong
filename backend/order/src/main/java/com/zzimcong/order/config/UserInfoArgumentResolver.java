package com.zzimcong.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.common.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;

@Component
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    public UserInfoArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String userInfoHeader = request.getHeader("X-User-Info");

        if (userInfoHeader != null) {
            String decodedUserInfo = new String(Base64.getDecoder().decode(userInfoHeader));
            return objectMapper.readValue(decodedUserInfo, UserInfo.class);
        }

        return null;
    }
}