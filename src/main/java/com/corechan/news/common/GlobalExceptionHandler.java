package com.corechan.news.common;


import com.corechan.news.common.exceptions.UnLoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW="error";

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Status defaultError(Exception e){
        Status errorStatus=new Status();
        errorStatus.setStatus(Status.StatusCode.fail);
        errorStatus.setMsg(e.getMessage());
        e.printStackTrace();
        return errorStatus;
    }

    @ResponseBody
    @ExceptionHandler(value = UnLoginException.class)
    public Status unLoginError(UnLoginException e){
        Status errorStatus=new Status();
        errorStatus.setStatus(Status.StatusCode.unLogin);
        errorStatus.setMsg(e.getMessage());
        return errorStatus;
    }

}
