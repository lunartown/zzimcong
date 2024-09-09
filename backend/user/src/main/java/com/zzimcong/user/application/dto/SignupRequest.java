package com.zzimcong.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String name,

        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        String phone,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        String password
) {
}
