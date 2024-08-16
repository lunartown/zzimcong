package com.zzimcong.auth.common;

import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String role;
    // 필요한 다른 필드들...
}