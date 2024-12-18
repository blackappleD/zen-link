package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/9/4 16:34
 */
@Data
public class FinanceInfoV3ReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCard;

    private String name;

    private String mobile;
}
