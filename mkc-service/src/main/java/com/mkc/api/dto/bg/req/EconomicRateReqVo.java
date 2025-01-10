package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/22 16:12
 */
@Data
public class EconomicRateReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCard;

    private String name;

    private String mobile;

}
