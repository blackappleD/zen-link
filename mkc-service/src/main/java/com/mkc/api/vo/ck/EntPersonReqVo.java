package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 *人企信息核验认证请求
 * @author tqlei
 * @date 2023/10/12 10:33
 */

@Data
public class EntPersonReqVo extends BaseVo implements Serializable {

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
