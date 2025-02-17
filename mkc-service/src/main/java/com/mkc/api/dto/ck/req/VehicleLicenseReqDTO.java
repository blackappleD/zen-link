package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/10/12 11:04
 */
@Data
public class VehicleLicenseReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String license;

    private String type;
}
