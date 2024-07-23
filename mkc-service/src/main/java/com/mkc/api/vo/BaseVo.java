package com.mkc.api.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mkc.api.common.constant.enums.ReqParamType;
import lombok.Data;

/**
 * @author tqlei
 * @date 2023/5/29 17:54
 */

@Data
public class BaseVo {

    /**
     * string	是	商户code;
     */
    private String merCode;
    /**
     * string	是	商户流水号;
     */
    private String merSeq;
    /**
     * string	是	商户 KEY;
     */
    @JSONField(serialize = false)
    private String key;

    /**
     * 请求参数类型 明文 ， MD5 ;
     */
    private String paramType= ReqParamType.PLAIN.getCode();

    /**
     * 签名
     */
    private String sign;

}
