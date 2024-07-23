package com.mkc.api.common.constant.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 请求参数敏感字段 的 类型
 * 0 明文，  1 加密
 * @author tqlei
 * @date 2023/6/19 9:32
 */

public enum ReqParamType {


    PLAIN("0","明文"),
    MD5("1","MD5"),
    SHA256("2","SHA256"),


    ;



    /** 0 明文，  1 md5加密  2 sha256 加密**/
    private String code;

    private String name;



    ReqParamType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否是明文 入参
     * @param pamamType
     * @return
     */
    public static boolean isPanType(String pamamType){
        return PLAIN.getCode().equals(pamamType) || StringUtils.isBlank(pamamType);
    }
    /**
     * 是否是 MD5加密 入参
     * @param pamamType
     * @return
     */
    public static boolean isMd5Type(String pamamType){
        return MD5.getCode().equals(pamamType);
    }
    /**
     * 是否是sha256 加密入参 入参
     * @param pamamType
     * @return
     */
    public static boolean isSha256Type(String pamamType){
        return SHA256.getCode().equals(pamamType);
    }

    public String getCode() {
        return code;
    }



    public String getName() {
        return name;
    }



}
