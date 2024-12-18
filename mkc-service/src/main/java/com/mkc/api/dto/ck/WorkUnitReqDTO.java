package com.mkc.api.dto.ck;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiewei
 * @date 2024/08/05 9:52
 */
@Data
public class WorkUnitReqDTO extends BaseDTO implements Serializable {

    private String name;

    private String cid;

    private String workplace;



}
