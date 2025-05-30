package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/26 14:57
 */
@Data
public class DrivingLicenseReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String licensePlateNo;

    private String plateType;

    private String name;
}
