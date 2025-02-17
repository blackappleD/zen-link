package com.mkc.api.dto.ck.res;

import lombok.Data;

import java.io.Serializable;

/**
 *手机 运营商 响应结果
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class MobIspRespDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * 运营商
     * 移动  CM
     * 联通  CU
     * 电信  CT
     * 广电  CBN
     */
    private String isp;


    public MobIspRespDTO() {

    }
    public MobIspRespDTO(String isp) {
        this.isp = isp;
    }
}
