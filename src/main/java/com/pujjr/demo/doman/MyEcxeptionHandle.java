package com.pujjr.demo.doman;


/**
 * 自定义处理异常的异常类
 * 需要继承RuntimeException
 */
public class MyEcxeptionHandle extends RuntimeException {

    //返回结果的状态码，200-成功，404-找不到页面，500-请求参数错误等！！
    private String code;
    //返回结果提示信息
    private String responseMsg;

    public MyEcxeptionHandle(String code, String responseMsg) {
        code = code;
        this.responseMsg = responseMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
