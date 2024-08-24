package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/16 17:01
 */
@Data
public class EnterpriseFourElementsReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orgName;

    private String orgCertNo;

    private String personName;

    private String personId;

}
