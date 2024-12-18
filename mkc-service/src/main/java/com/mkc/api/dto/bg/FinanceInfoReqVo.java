package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiewei
 * @date 2024/08/05 17:01
 */
@Data
public class    FinanceInfoReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCard;

    private String name;

    private String mobile;

}
