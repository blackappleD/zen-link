package com.mkc.api.vo.sf;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/28 15:28
 */
@Data
public class DishonestExecutiveReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String certName;

    private String certNo;

    private String type;
}
