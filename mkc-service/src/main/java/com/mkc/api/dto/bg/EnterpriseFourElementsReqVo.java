package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/16 17:01
 */
@Data
public class EnterpriseFourElementsReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;

    private String creditCode;

    private String legalPerson;

    private String certNo;

}
