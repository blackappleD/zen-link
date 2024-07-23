package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 *个人手机二要素认证-姓名手机号
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class IdeTwoMnReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String certName;

    /**
     * 手机号
     */
    private String mobile;


}
