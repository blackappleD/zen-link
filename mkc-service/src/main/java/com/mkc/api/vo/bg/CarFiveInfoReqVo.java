package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/10/9 15:36
 */
@Data
public class CarFiveInfoReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String carNo;

}
