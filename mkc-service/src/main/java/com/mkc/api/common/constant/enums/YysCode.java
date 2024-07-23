package com.mkc.api.common.constant.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * 运营商枚举类
 */
@Slf4j
public enum YysCode {

    CM("CM","移动"),
    CU("CU","联通"),
    CT("CT","电信"),
    CB("CB","广电"),


    ;


    public static  YysCode findByCode(String code){
        for (YysCode value : YysCode.values()) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        log.error("CODE 没有匹配到,返回默认 CM YYSCODE {}",code);
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return YysCode.CM;
    }

    /**
     * 根据名字匹配枚举
     * @param name
     * @return 无的时候 默认返回移动
     */
    public static YysCode findByName(String name){
        for (YysCode value : YysCode.values()) {
            if(value.getName().equals(name)){
                return value;
            }
        }
        log.error("NAME 没有匹配到,返回默认 CM YYSCODE {}",name);
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return YysCode.CM;
    }
    /**
     * 根据名字匹配枚举
     * @param name
     * @return 无的时候 默认返回移动
     */
    public static YysCode findByName2(String name){
        for (YysCode value : YysCode.values()) {
            if(value.getName().equals(name)){
                return value;
            }
        }
        return null;
    }




    /** 运营商code  CM-移动  CU-联通 CT-电信**/
    private String code;

    /** 运营商名称  CM-移动  CU-联通 CT-电信**/
    private String name;



    YysCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }



    public String getName() {
        return name;
    }


}
