package com.zzimcong.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotEmpty(message = "이메일을 입력해 주세요")
        String email
) {
}