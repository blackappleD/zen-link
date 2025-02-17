package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiewei
 * @date 2024/08/02 16:06
 */
@Data
public class PersonCarReqDTO extends BaseDTO implements Serializable {

    private String name;

    private String plateNo;

}
