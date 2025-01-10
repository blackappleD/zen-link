package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/10/9 15:36
 */
@Data
public class CarFiveInfoReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String carNo;

}
