package com.zzimcong.auth.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyRequestDto {
    private String email;
    private String username;
    private String phone;
    private String password;
}
