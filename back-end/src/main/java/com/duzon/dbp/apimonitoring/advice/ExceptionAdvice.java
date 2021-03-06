package com.duzon.dbp.apimonitoring.advice;

import javax.servlet.http.HttpServletRequest;

import com.duzon.dbp.apimonitoring.advice.exception.ApiAuthorityError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiCategoryNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiDelayNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiErrorNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithURLandMethodError;
import com.duzon.dbp.apimonitoring.advice.exception.CAuthenticationEntryPointException;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeIdSigninFailedError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeNotFoundError;
import com.duzon.dbp.apimonitoring.advice.exception.EmailSendEmployeeError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeSameIdError;
import com.duzon.dbp.apimonitoring.advice.exception.ErrAndDelayCheckError;
import com.duzon.dbp.apimonitoring.advice.exception.GroupNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.IdAndTokenNotMatchError;
import com.duzon.dbp.apimonitoring.advice.exception.NotificationNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.SameNameError;
import com.duzon.dbp.apimonitoring.advice.exception.SameURLError;
import com.duzon.dbp.apimonitoring.advice.exception.ServiceCategoryNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ServiceNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.SqlIndexError;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

/**
 * ExceptionAdvice
 */

@RestControllerAdvice
public class ExceptionAdvice {

    @Autowired
    ResponseService responseService;

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(EmployeeNotFoundError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Common EmployeeNotFoundErrException(HttpServletRequest request, EmployeeNotFoundError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("EmployeeNotFoundErrException.code")), getMessage("EmployeeNotFoundErrException.msg"));
    }

    // ~/exception/
    @ExceptionHandler(EmployeeIdSigninFailedError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Common EmployeeIdSigninFailedErrException(HttpServletRequest request, EmployeeIdSigninFailedError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("EmployeeIdSigninFailedErrException.code")), getMessage("EmployeeIdSigninFailedErrException.msg"));
    }

    // ~/exception/
  @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Common authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("CAuthenticationEntryPointException.code")), getMessage("CAuthenticationEntryPointException.msg"));
    }
    /**
     * AccessDeniedException : ?????? ?????? ??????
     */
    // security.access.AccessDeniedException
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Common accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    @ExceptionHandler(SqlIndexError.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Common SqlIndexErrorException(HttpServletRequest request, SqlIndexError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("SqlIndexErrorException.code")), getMessage("SqlIndexErrorException.msg"));
    }
    
    // Valid??????
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common MethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("MethodArgumentNotValidException.code")), getMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
    
    // Id??? ????????? Id??? ????????? ??????
    @ExceptionHandler(IdAndTokenNotMatchError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common IdAndTokenNotMatchErrorException(HttpServletRequest request, IdAndTokenNotMatchError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("IdAndTokenNotMatchErrorException.code")), getMessage("IdAndTokenNotMatchErrorException.msg"));
    }
    
    // ????????? ???????????? ?????????
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Common MalformedJwtException(HttpServletRequest request, MalformedJwtException e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("MalformedJwtException.code")), getMessage("MalformedJwtException.msg"));
    }

    // ????????? ???????????? ?????????
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Common SignatureException(HttpServletRequest request, SignatureException e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("SignatureException.code")), getMessage("SignatureException.msg"));
    }

    // ????????? ??????????????? ????????????
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Common ExpiredJwtException(HttpServletRequest request, ExpiredJwtException e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ExpiredJwtException.code")), getMessage("ExpiredJwtException.msg"));
    }

    // Name ?????? ??????
    @ExceptionHandler(SameNameError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common SameNameErrorException(HttpServletRequest request, SameNameError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("SameNameErrorException.code")), getMessage("SameNameErrorException.msg"));
    }

    // Name ?????? ??????
    @ExceptionHandler(SameURLError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common SameURLErrorException(HttpServletRequest request, SameURLError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("SameURLErrorException.code")), getMessage("SameURLErrorException.msg"));
    }

    // Employee Id ?????? ??????
    @ExceptionHandler(EmployeeSameIdError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common EmployeeSameIdErrorException(HttpServletRequest request, EmployeeSameIdError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("EmployeeSameIdErrorException.code")), getMessage("EmployeeSameIdErrorException.msg"));
    }

    // Email Send Employee ??????
    @ExceptionHandler(EmailSendEmployeeError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common EmailSendEmployeeErrorException(HttpServletRequest request, EmailSendEmployeeError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("EmailSendEmployeeErrorException.code")), getMessage("EmailSendEmployeeErrorException.msg"));
    }

    // Api_url, Method??? ????????? Api??? ????????????.
    @ExceptionHandler(ApiNotFoundWithURLandMethodError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiNotFoundWithURLandMethodErrorException(HttpServletRequest request, ApiNotFoundWithURLandMethodError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiNotFoundWithURLandMethodErrorException.code")), getMessage("ApiNotFoundWithURLandMethodErrorException.msg"));
    }

    // Service_Category_no??? Service_Category??? ????????????.
    @ExceptionHandler(ServiceCategoryNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ServiceCategoryNotFoundWithNumberErrorException(HttpServletRequest request, ServiceCategoryNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ServiceCategoryNotFoundWithNumberErrorException.code")), getMessage("ServiceCategoryNotFoundWithNumberErrorException.msg"));
    }

    // Service_no??? Service??? ????????????.
    @ExceptionHandler(ServiceNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ServiceNotFoundWithNumberErrorException(HttpServletRequest request, ServiceNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ServiceNotFoundWithNumberErrorException.code")), getMessage("ServiceNotFoundWithNumberErrorException.msg"));
    }

    // Api_Category_no??? Api_Category??? ????????????.
    @ExceptionHandler(ApiCategoryNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiCategoryNotFoundWithNumberErrorException(HttpServletRequest request, ApiCategoryNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiCategoryNotFoundWithNumberErrorException.code")), getMessage("ApiCategoryNotFoundWithNumberErrorException.msg"));
    }

    // Api_no??? Api??? ????????????.
    @ExceptionHandler(ApiNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiNotFoundWithNumberErrorException(HttpServletRequest request, ApiNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiNotFoundWithNumberErrorException.code")), getMessage("ApiNotFoundWithNumberErrorException.msg"));
    }
    
    // Api_Err_no??? Err??? ????????????.
    @ExceptionHandler(ApiErrorNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiErrorNotFoundWithNumberErrorException(HttpServletRequest request, ApiErrorNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiErrorNotFoundWithNumberErrorException.code")), getMessage("ApiErrorNotFoundWithNumberErrorException.msg"));
    }

    // Api_Delay_no??? Delay??? ????????????.
    @ExceptionHandler(ApiDelayNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiDelayNotFoundWithNumberErrorException(HttpServletRequest request, ApiDelayNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiDelayNotFoundWithNumberErrorException.code")), getMessage("ApiDelayNotFoundWithNumberErrorException.msg"));
    }

    // Notify_no??? Notification??? ????????????.
    @ExceptionHandler(GroupNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common GroupNotFoundWithNumberErrorException(HttpServletRequest request, GroupNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("GroupNotFoundWithNumberErrorException.code")), getMessage("GroupNotFoundWithNumberErrorException.msg"));
    }

    // Notify_no??? Notification??? ????????????.
    @ExceptionHandler(NotificationNotFoundWithNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common NotificationNotFoundWithNumberErrorException(HttpServletRequest request, NotificationNotFoundWithNumberError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("NotificationNotFoundWithNumberErrorException.code")), getMessage("NotificationNotFoundWithNumberErrorException.msg"));
    }

    // Api??? Err ??? Delay??? ????????? ???????????????.
    @ExceptionHandler(ErrAndDelayCheckError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ErrAndDelayCheckErrorException(HttpServletRequest request, ErrAndDelayCheckError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ErrAndDelayCheckErrorException.code")), getMessage("ErrAndDelayCheckErrorException.msg"));
    }

    // Api_no??? Api_url, Method??? ???????????? Api??? ????????????.
    @ExceptionHandler(ApiNotFoundError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiNotFoundErrorException(HttpServletRequest request, ApiNotFoundError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiNotFoundErrorException.code")), getMessage("ApiNotFoundErrorException.msg"));
    }

    // ?????? API??? ???????????? ?????? ??????
    @ExceptionHandler(ApiAuthorityError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Common ApiAuthorityErrorException(HttpServletRequest request, ApiAuthorityError e) {
        return responseService.getResultFail(Integer.valueOf(getMessage("ApiAuthorityErrorException.code")), getMessage("ApiAuthorityErrorException.msg"));
    }
    
    // ?????? : ?????? ????????? ?????? ?????????
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code??????, ?????? argument??? ?????? locale??? ?????? ???????????? ???????????????.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}