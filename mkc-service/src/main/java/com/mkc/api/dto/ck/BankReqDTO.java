package com.mkc.api.dto.ck;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/9/11 17:01
 */
@Data
public class BankReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 银行卡号
    private String bankCard;

    // 姓名
    private String certName;

    // 证件号号
    private String certNo;

    // 电话号码
    private String mobile;
}
