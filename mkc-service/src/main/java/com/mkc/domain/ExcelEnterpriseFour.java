package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author linst
 * @date 2024/10/23
 */
@Data
public class ExcelEnterpriseFour {

    @ColumnWidth(20)
    @ExcelProperty("企业名称")
    private String companyName;

    @ColumnWidth(20)
    @ExcelProperty("统一社会信用代码")
    private String creditCode;

    @ColumnWidth(20)
    @ExcelProperty("注册号")
    private String registrationNumber;

    @ColumnWidth(20)
    @ExcelProperty("法人姓名")
    private String legalPerson;

    @ColumnWidth(20)
    @ExcelProperty("法人身份证号")
    private String certNo;

    @ColumnWidth(15)
    @ExcelProperty("code")
    private String code;

    @ColumnWidth(20)
    @ExcelProperty("returnCode")
    private String returnCode;

    @ColumnWidth(20)
    @ExcelProperty("certNoMatch")
    private String certNoMatch;

    @ColumnWidth(20)
    @ExcelProperty("creditCodeMatch")
    private String creditCodeMatch;

    @ColumnWidth(20)
    @ExcelProperty("companyNameMatch")
    private String companyNameMatch;

    @ColumnWidth(20)
    @ExcelProperty("legalPersonMatch")
    private String legalPersonMatch;



}
