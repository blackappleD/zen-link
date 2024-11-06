package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/9/11 17:01
 */
@Data
public class BankReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bankCard;

    private String certName;

    private String certNo;

    private String mobile;
}
