package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 *人企信息核验认证请求
 * @author tqlei
 * @date 2023/10/12 10:33
 */

@Data
public class EntPersonReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String certName;

    /**
     * 身份证
     */
    private String certNo;


    /**
     * 统一信用代码
     */
    private String entNo="";
    /**
     * 企业全称
     */
    private String entName="";


}
