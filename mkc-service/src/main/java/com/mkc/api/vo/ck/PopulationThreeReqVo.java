package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/21 17:46
 */
@Data
public class PopulationThreeReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String idcard;

    private String photo;

    private String authorization;

}
