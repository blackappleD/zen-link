package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author linst
 * @date 2024/10/23
 */
@Data
public class    ExcelTest2W {
    @ColumnWidth(20)
    @ExcelProperty("姓名")
    private String name = "张三";
    @ColumnWidth(20)
    @ExcelProperty("身份证号")
    private String idCard;
    @ColumnWidth(20)
    @ExcelProperty("手机号")
    private String mobile = "18888888888";
    @ColumnWidth(20)
    @ExcelProperty("日期")
    private String date;
    @ExcelProperty("code")
    private String code;
    @ColumnWidth(15)
    @ExcelProperty("range")
    private String range;
    @ColumnWidth(15)
    @ExcelProperty("history")
    private String history;
    @ColumnWidth(15)
    @ExcelProperty("stability")
    private String stability;


}
