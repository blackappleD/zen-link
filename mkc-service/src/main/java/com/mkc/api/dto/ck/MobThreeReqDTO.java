package com.mkc.api.dto.ck;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 *个人三要素认证
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class MobThreeReqDTO extends BaseDTO implements Serializable {

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
     * 手机号
     */
    private String mobile;


}
