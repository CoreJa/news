package com.corechan.news.common;

public class Status<T> {
    private StatusCode status;
    private String msg;
    private T data;

    public enum StatusCode{
        init,
        success,
        fail,
        unLogin,
        wrongPassword,
        idNotExist,
        idAlreadyExist,
        newsNotExist,
        preferenceNotExist
    }

    public Status() {
        msg=null;
        data=null;
        status=StatusCode.init;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }
}
