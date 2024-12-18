package com.mkc.api.dto.sf;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/28 15:34
 */
@Data
public class RestrictedConsumerReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String certName;

    private String certNo;

    private String type;
}
