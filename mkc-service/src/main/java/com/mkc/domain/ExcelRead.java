package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author linst
 * @date 2024/10/23
 */
@Data
public class ExcelRead {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("身份证")
    private String idCard;
    @ExcelProperty("手机号")
    private String mobile = "18888888888";
    @ExcelProperty("code")
    private String code;
    @ExcelProperty("range")
    private String range;
    @ExcelProperty("history")
    private String history;
    @ExcelProperty("stability")
    private String stability;


}
