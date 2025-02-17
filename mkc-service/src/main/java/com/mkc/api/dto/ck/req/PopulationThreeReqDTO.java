package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/21 17:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PopulationThreeReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String idcard;

    private String photo;

    private String authorization;

}
