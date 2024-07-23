package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 *个人三要素认证
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class MobThreeReqVo extends BaseVo implements Serializable {

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
