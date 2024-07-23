package com.mkc.common.enums;

import com.mkc.api.common.constant.enums.YysCode;

/**
 * 商户账户金额 变更类型 a 充值  d,手动扣减，c 系统扣减
 */
public enum MerAmountType {


    /**
     * 充值
     */
    ADD("A", "充值"),

    /**
     * 手动扣减
     */
    DISCOUNT ("D", "手动扣减"),
    /**
     * 系统扣减
     */
    SYS_DISCOUNT ("C", "系统扣减"),

    ;


    MerAmountType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static MerAmountType findByCode(String code){
        for (MerAmountType value : MerAmountType.values()) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return null;
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
