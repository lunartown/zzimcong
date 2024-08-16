package com.zzimcong.user.infrastructure.security.jwt;

import com.zzimcong.user.api.response.ErrorResponse;
import com.zzimcong.user.common.exception.*;
import com.zzimcong.user.domain.entity.User;
import com.zzimcong.user.domain.repository.UserRepository;
import com.zzimcong.user.infrastructure.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT_REQUEST_FILTER")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtRequestFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {

            final String authorizationHeader = request.getHeader("Authorization");

            Long id = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    id = jwtUtil.extractId(jwt);
                } catch (ExpiredJwtException e) {
                    log.warn("JWT 토큰이 만료되었습니다", e);
                    throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
                } catch (Exception e) {
                    log.error("JWT 토큰에서 id를 추출하는 중 오류가 발생했습니다", e);
                    throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
                }
            }

            if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

                if (jwtUtil.validateToken(jwt, user.getId())) {
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("JWT 토큰 검증에 실패했습니다");
                    throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 오류가 발생했습니다", e);
            ErrorResponse errorResponse = createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
            response.getWriter().write(errorResponse.toString());
        }
    }

    // Create ErrorResponse
    private ErrorResponse createErrorResponse(Exception ex, HttpStatus status, String path) {
        logException(ex, status, path);
        BaseException baseException = ex instanceof BaseException ?
                (BaseException) ex : new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
        return ErrorResponse.of(baseException, status, path);
    }

    // Log Exception
    private void logException(Exception ex, HttpStatus status, String path) {
        log.error(
                "\n\nException occurred:: Error Code: {}, HTTP Status: {}, Message: {}, Path: {}\n",
                ex instanceof BaseException ? ((BaseException) ex).getErrorCode() : "UNKNOWN",
                status,
                ex.getMessage(),
                path,
                ex
        );
    }
}