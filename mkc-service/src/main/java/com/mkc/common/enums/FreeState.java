package com.mkc.common.enums;

/**
 * 是否收费状态码 0 不收费 1收费
 * @author tqlei
 * @date 2023/5/18 17:01
 */


public enum   FreeState {

    /**
     * 收费
     */
    YES ("1","收费"),

    /**
     * 不收费
     */
     NO ("0","不收费"),

     ;


    FreeState(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;


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

}
