package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/10/18 10:10
 */
@Data
public class MaritalStabilityReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String xm;

    private String sfzh;

    private String yearNum;

}
