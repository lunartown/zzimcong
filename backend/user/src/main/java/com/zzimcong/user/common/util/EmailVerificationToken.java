package com.zzimcong.user.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EmailVerificationToken {
    private String token;
    private String email;
    private LocalDateTime expiryDate;
}