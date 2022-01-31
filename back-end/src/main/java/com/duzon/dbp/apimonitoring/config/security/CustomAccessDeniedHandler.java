package com.duzon.dbp.apimonitoring.config.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * CustomAccessDeniedHandler
 * 
 * - 인증(로그인)은 되었으나 해당 요청에 대한 권한이 없을 경우에는 AccessDeniedHandler 부분에서
 * AccessDeniedException 이 발생된다.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/exception/accessdenied");
        requestDispatcher.forward(request, response);

    }
}