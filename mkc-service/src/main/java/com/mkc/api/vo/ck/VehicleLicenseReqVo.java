package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/10/12 11:04
 */
@Data
public class VehicleLicenseReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String license;

    private String type;
}
