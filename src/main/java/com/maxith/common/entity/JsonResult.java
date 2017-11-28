package com.maxith.common.entity;

import java.io.Serializable;

/**
 * 基础结果json对象
 */
public class JsonResult implements Serializable {
    private static final long serialVersionUID = 3138145308653310100L;
    private static String SECCESS_CODE = "000000";
    private static String SECCESS_MSG = "操作成功";
    private String code;
    private String msg;
    private Object data;
    private static final JsonResult result = new JsonResult();

    public static JsonResult getInstance() {
        result.setCode(SECCESS_CODE);
        result.setMsg(SECCESS_MSG);
        return result;
    }

    public JsonResult() {
        this.code = SECCESS_CODE;
        this.msg = SECCESS_MSG;
    }

    public JsonResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(Object data) {
        this.code = SECCESS_CODE;
        this.msg = SECCESS_MSG;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
