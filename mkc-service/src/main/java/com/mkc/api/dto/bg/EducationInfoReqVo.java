package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/9/20 11:36
 */
@Data
public class EducationInfoReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String xm;

    private String zjhm;

}
