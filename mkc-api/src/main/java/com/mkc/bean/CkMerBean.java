package com.mkc.bean;

import lombok.Data;

/**
 * @author tqlei
 * @date 2023/5/17 14:48
 */
@Data
public class CkMerBean {


    private String merCode;
    private String merSeq;
    private String key;
    private String plaintext;
    private String merSign;
    private String productCode;


    public CkMerBean(){}

    public CkMerBean(String merCode,String key , String plaintext, String merSign,String merSeq, String productCode) {
        this.merCode = merCode;
        this.plaintext = plaintext;
        this.merSign = merSign;
        this.productCode = productCode;
        this.merSeq = merSeq;
        this.key = key;
    }
    public CkMerBean(String merCode, String key ,String plaintext, String merSign,String merSeq) {
        this.merCode = merCode;
        this.plaintext = plaintext;
        this.merSign = merSign;
        this.merSeq = merSeq;
        this.key = key;
    }
}
