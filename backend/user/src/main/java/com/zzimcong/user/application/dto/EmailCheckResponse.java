package com.zzimcong.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailCheckResponse(
        @Email
        @NotEmpty(message = "이메일을 입력해 주세요")
        String email,

        @NotEmpty(message = "인증 번호를 입력해 주세요")
        String authNum
) {
}