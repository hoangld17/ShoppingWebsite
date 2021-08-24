package com.example.shoesmanagement.dto.response;

public class ShowDataResponse<T> {
    private int errorCode;
    private T data;
    private String message;

    public ShowDataResponse(int errorCode, T data, String Message) {
        super();
        this.errorCode = errorCode;
        this.data = data;
        this.message = Message;
    }

    public ShowDataResponse(T data) {
        this.errorCode = 0;
        this.message = "";
        this.data = data;
    }

    public ShowDataResponse(int errorCode, String Message) {
        super();
        this.errorCode = errorCode;
        this.message = Message;
    }

    public ShowDataResponse() {
        this.errorCode = 0;
        this.message = "";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String Message) {
        this.message = Message;
    }

    @Override
    public String toString() {
        return "{\"errorCode\":" + errorCode + ",\n\"data\":" + data + ",\n\"Message\":\"" + message + "\"}";
    }


}
