package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/9/25 11:46
 */
@Data
public class HighSchoolEducationInfoReqVo extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String xm;

    private String zsbh;
}
