package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/12 10:07
 */
@Data
public class VehicleLicenseReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String plateNo;

    private String name;

    private String plateType;

}
