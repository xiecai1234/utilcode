package com.fingerbeat.cloud.net.rx;

/**
 * des:服务器请求异常
 * Created by xsf
 * on 2016.09.10:16
 */
public class ApiException extends Exception {
    private int code;
    private String msg;

    public ApiException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
