package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/9/4 16:34
 */
@Data
public class FinanceInfoV3ReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCard;

    private String name;

    private String mobile;
}
