package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HouseResultInfoReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reqOrderNo;

    private List<String> personCardNumList;


}
