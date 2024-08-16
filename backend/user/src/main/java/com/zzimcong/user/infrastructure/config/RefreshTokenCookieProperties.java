package com.zzimcong.user.infrastructure.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "auth.cookie.refresh-token")
@Validated
@Getter
@Setter
public class RefreshTokenCookieProperties {
    @NotBlank
    private String name;

    @Min(0)
    private int maxAge;

    private boolean httpOnly;
    private boolean secure;

    @NotBlank
    private String path;
}