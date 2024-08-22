package com.zzimcong.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressCreateRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 64, message = "이름은 64자를 초과할 수 없습니다.")
        String name,

        @NotBlank(message = "주소는 필수입니다.")
        @Size(max = 255, message = "주소는 255자를 초과할 수 없습니다.")
        String streetAddress,

        @NotBlank(message = "상세 주소는 필수입니다.")
        @Size(max = 255, message = "상세 주소는 255자를 초과할 수 없습니다.")
        String addressDetail,

        @NotBlank(message = "우편번호는 필수입니다.")
        @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
        String zipcode,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Size(max = 20, message = "전화번호는 20자를 초과할 수 없습니다.")
        String phone,

        @Size(max = 255, message = "메시지는 255자를 초과할 수 없습니다.")
        String message,

        boolean isDefault
) {
}