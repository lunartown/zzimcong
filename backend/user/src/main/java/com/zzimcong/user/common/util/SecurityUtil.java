package com.zzimcong.user.common.util;

import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.UnauthorizedException;
import com.zzimcong.user.infrastructure.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_AUTHENTICATED);
        }
        return Long.parseLong(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
    }
}