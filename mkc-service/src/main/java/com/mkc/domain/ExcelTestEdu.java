package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author linst
 */
@Data
public class ExcelTestEdu {
    @ExcelProperty("姓名")
    private String xm;
    @ExcelProperty("证书编号")
    private String zsbh;
    @ExcelProperty("code")
    private String code;
    @ExcelProperty("resultCode")
    private String resultCode;
    @ExcelProperty("reqOrderNo")
    private String reqOrderNo;
    @ExcelProperty("status")
    private String status;
    @ExcelProperty("isExists")
    private Boolean isExists;
    @ExcelProperty("zjbh")
    private String zjbh;
    @ExcelProperty("yxmc")
    private String yxmc;
    @ExcelProperty("zymc")
    private String zymc;
    @ExcelProperty("cc")
    private String cc;
    @ExcelProperty("rxrq")
    private String rxrq;
    @ExcelProperty("byrq")
    private String byrq;
    @ExcelProperty("xxxs")
    private String xxxs;


}
