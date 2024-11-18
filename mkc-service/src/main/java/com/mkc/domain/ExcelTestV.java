package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author linst
 * @date 2024/10/23
 */
@Data
public class ExcelTestV {
    @ColumnWidth(20)
    @ExcelProperty("姓名")
    private String name = "张三";
    @ColumnWidth(20)
    @ExcelProperty("身份证号")
    private String idCard;
    @ColumnWidth(20)
    @ExcelProperty("手机号")
    private String mobile = "18888888888";
    @ExcelProperty("code")
    private String code;
    @ColumnWidth(15)
    @ExcelProperty("level")
    private String level;


}
