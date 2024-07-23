package com.mkc.common.enums;

/**
 * 司法 查询类型  O:企业 P:自然人
 */
public enum SfType {


    /**
     * 企业
     */
    O("O", "企业"),

    /**
     * 自然人
     */
    P("P", "自然人"),

    ;


    SfType(String code, String msg) {
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
