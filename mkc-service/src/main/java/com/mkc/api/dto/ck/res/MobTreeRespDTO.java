package com.mkc.api.dto.ck.res;

import lombok.Data;

import java.io.Serializable;

/**
 *个人手机三要素认证详版  响应结果
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class MobTreeRespDTO implements Serializable {

    public static final long serialVersionUID = 1L;
    /**
     * 默认一致
     */
    public final static String SUCCESS="1";
    /**
     * ⼿机号姓名⼀致，证件号不⼀致
     */
    public final static String C20001="20001";
    /**
     * ⼿机号和证件号⼀致，姓名不⼀致
     */
    public final static String C20002="20002";
    /**
     * 其他不⼀致
     */
    public final static String C20004="20004";

    /**
     * 1 一致
     * 20001: ⼿机号姓名⼀致，证件号不⼀致
     * 20002: ⼿机号和证件号⼀致，姓名不⼀致
     * 20003:其他不⼀致
     */
    private String retCode=SUCCESS;

    /**
     * 运营商
     * 移动  CMCC
     * 联通  CU
     * 电信  CT
     * 广电  CBN
     */
    private String isp;




}
