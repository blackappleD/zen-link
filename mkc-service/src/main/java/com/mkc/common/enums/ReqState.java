package com.mkc.common.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * 查询状态 1 查得，2 查无，3 失败，4 异常
 * @author tqlei
 * @date 2023/4/27 17:45
 */

@Slf4j
public enum  ReqState {
    /**
     * 查询到数据结果 或 认证一致
     */
     SUCCESS ("1","查询数据成功或一致"),

    /**
     * 查无
     */
    NOGET ("2","查无"),

    /**
     * 不一致
     */
    NOT ("3","不一致"),

    /**
     * 查询异常
     */
    ERROR ("4","查询异常"),


    ;


    public static ReqState findByCode(String code){
        for (ReqState value : ReqState.values()) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        log.warn("ReqState is not get; code {}",code);
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return ReqState.ERROR;
    }

    ReqState(String code, String msg) {
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
