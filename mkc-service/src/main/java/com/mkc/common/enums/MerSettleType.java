package com.mkc.common.enums;

/**
 * 商户结算类型  Y预付费，H后付费
 */
public enum MerSettleType {


    /**
     * 后付费
     */
    H("h", "后付费"),

    /**
     * 预付费
     */
    Y("y", "预付费"),

    ;


    MerSettleType(String code, String msg) {
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
