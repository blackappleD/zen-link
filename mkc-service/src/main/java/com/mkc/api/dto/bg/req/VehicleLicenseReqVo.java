package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/12 10:07
 */
@Data
public class VehicleLicenseReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String plateNo;

    private String name;

    private String plateType;

}
