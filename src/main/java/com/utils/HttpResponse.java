package com.utils;

public class HttpResponse {
    public Integer code;
    public Object data;
    public String localizedMsg;
    public String message;

    public HttpResponse(Integer code, Object data, String localizedMsg, String message) {
        this.code = code;
        this.data = data;
        this.localizedMsg = localizedMsg;
        this.message = message;
    }

    public HttpResponse(Integer code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public HttpResponse(Integer code) {
        this.code = code;
    }

    public HttpResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpResponse() {
        this.code = 200;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getLocalizedMsg() {
        return localizedMsg;
    }

    public void setLocalizedMsg(String localizedMsg) {
        this.localizedMsg = localizedMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
