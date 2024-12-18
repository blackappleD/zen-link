package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HouseResultInfoReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reqOrderNo;

    private List<String> personCardNumList;


}
