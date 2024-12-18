package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/9 11:57
 */
@Data
public class SureScoreInfoReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mobile;

    private String name;

    private String cid;

    private String job;
}
