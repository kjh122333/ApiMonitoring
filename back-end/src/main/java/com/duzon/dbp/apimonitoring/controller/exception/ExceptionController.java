package com.duzon.dbp.apimonitoring.controller.exception;

import com.duzon.dbp.apimonitoring.advice.exception.CAuthenticationEntryPointException;
import com.duzon.dbp.apimonitoring.dto.message.Common;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @RequestMapping(value = "/entrypoint")
    public Common entrypointException() {
        log.info("======================== 토큰 에러 ========================");
        throw new CAuthenticationEntryPointException();
    }

    @RequestMapping(value = "/accessdenied")
    public Common accessdeniedException() {
        log.info("======================== 권한 에러 ========================");
        throw new AccessDeniedException("");
    }
}