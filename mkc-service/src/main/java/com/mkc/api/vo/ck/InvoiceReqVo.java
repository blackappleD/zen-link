package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tqlei
 * @date 2024/7/3 14:07
 */
@Data
public class InvoiceReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开票⽇期  格式为 yyyyMMdd 例如：20220812
     */
    private String billingDate = "";

    /**
     * 发票代码
     */
    private String invoiceCode = "";
    /**
     *发票号码
     */
    private String invoiceNumber = "";
    /**
     *⾦额
     */
    private String amount = "";
    /**
     *校验码
     */
    private String checkCode = "";



}
