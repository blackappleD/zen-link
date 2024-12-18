package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/13 16:33
 */
@Data
public class PersonCarDetailReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cid;

    private String plateNo;

    private String name;




}
