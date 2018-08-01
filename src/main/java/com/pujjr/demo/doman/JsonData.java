package com.pujjr.demo.doman;

import java.io.Serializable;

public class JsonData implements Serializable {
    private static final Long SerialVersionUID = 1L;
    //返回结果的状态码，0-成功，1-失败
    private String ResultCode;
    //返回结果提示信息
    private String message;

    public JsonData() {
    }

    public JsonData(String resultCode, String message) {
        ResultCode = resultCode;
        this.message = message;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
