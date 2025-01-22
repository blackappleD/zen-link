package com.mkc.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author linst
 */
@Data
public class ExcelTestEduZjhm {
    @ExcelProperty("姓名")
    private String xm;
    @ExcelProperty("身份证号")
    private String zjhm;
    @ExcelProperty("code")
    private String code;
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
    @ExcelProperty("zsbh")
    private String zsbh;


}
