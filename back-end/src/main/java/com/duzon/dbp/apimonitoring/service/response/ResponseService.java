package com.duzon.dbp.apimonitoring.service.response;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.message._Single;

import org.springframework.stereotype.Service;

/**
 * ResponseService
 */
@Service
public class ResponseService {

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {

        SUCCESS(0, "성공");

        int code;
        String message;

        CommonResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
    
    // RESULT : SINGLE 기본 객체
    public <T> _Single<T> getResult_Single(T data) {
        _Single<T> result = new _Single<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }
    
    // RESULT : SINGLE Map를 받을수 있게 (Map<String, String>)
    public <T> _Single<Map<String, Object>> getResult_Single_Map(Map<String, Object> data) {
        _Single<Map<String, Object>> result = new _Single<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }
    
    public _Single<Map<String, List<Map<String, Object>>>> getResult_Single_MapMap(Map<String, List<Map<String, Object>>> data) {
        _Single<Map<String, List<Map<String, Object>>>> result = new _Single<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }
    
    // RESULT : LIST(multi) 기본 객체
    public <T> _List<T> getResult_List(List<T> list) {
        _List<T> result = new _List<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // RESULT : LIST(multi) List Map을 받을수 있게 (List<Map<String, Object>>)
    public <T> _List<Map<String, Object>> getResult_List_Map(List<Map<String, Object>> list) {
        _List<Map<String, Object>> result = new _List<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // RESULT : LIST(multi) List String을 받을수 있게 (List<String>)
    public <T> _List<String> getResult_List_String(List<String> list) {
        _List<String> result = new _List<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // RESULT : COMMON(=SUCCESS)
    public Common getResultSuccess() {
        Common result = new Common();
        setSuccessResult(result);
        return result;
    }

    // RESULT : COMMON(=FAIL)
    public Common getResultFail(int code, String message) {
        Common result = new Common();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // SETTING : API's request Data
    private void setSuccessResult(Common result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
    }
}