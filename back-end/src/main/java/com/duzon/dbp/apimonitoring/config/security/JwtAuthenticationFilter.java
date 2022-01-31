package com.duzon.dbp.apimonitoring.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * JwtAuthenticationFilter 
 * NOTE 
 * ### [CLASS] GenericFilterBean ###
 * 
 * 
 */
public class JwtAuthenticationFilter extends GenericFilterBean {

    // com.rest.api.config.security.JwtTokenProvider
    private JwtTokenProvider jwtTokenProvider;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * NOTE 
     * ### SecurityContextHolder ###
     * - Bean에 등록 된 사용자 정보(token)을 받아옴
     * ### filterChain ###
     * - 정의된 filter를 filterChain으로 관리
     */
    // JwtTokenProvider(jwt token 생성기) 메소드 사용
    // Request로 들어오는 JWT Token에 대한 `유효성`을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        
        if (token != null && jwtTokenProvider.validateToken(token)) {

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        
        }
        
        filterChain.doFilter(request, response);
    }
}