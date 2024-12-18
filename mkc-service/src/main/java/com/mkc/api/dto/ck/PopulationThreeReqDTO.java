package com.mkc.api.dto.ck;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/21 17:46
 */
@Data
public class PopulationThreeReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String idcard;

    private String photo;

    private String authorization;

}
