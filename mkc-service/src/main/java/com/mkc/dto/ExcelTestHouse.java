package com.mkc.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author linst
 */
@Data
public class ExcelTestHouse {
    @ColumnWidth(25)
    @ExcelProperty("姓名")
    private String xm;
    @ColumnWidth(25)
    @ExcelProperty("身份证号")
    private String personCardNum;
    @ColumnWidth(25)
    @ExcelProperty("code")
    private String code;
    @ColumnWidth(25)
    @ExcelProperty("reqOrderNo")
    private String reqOrderNo;
    @ColumnWidth(25)
    @ExcelProperty("certNo")
    private String certNo;
    @ColumnWidth(25)
    @ExcelProperty("unitNo")
    private String unitNo;
    @ColumnWidth(25)
    @ExcelProperty("location")
    private String location;
    @ColumnWidth(25)
    @ExcelProperty("ownership")
    private String ownership;
    @ColumnWidth(25)
    @ExcelProperty("houseArea")
    private String houseArea;
    @ColumnWidth(25)
    @ExcelProperty("rightsType")
    private String rightsType;
    @ColumnWidth(25)
    @ExcelProperty("isSealUp")
    private String isSealUp;
    @ColumnWidth(25)
    @ExcelProperty("isMortgaged")
    private String isMortgaged;
    @ColumnWidth(25)
    @ExcelProperty("rightsStartTime")
    private String rightsStartTime;
    @ColumnWidth(25)
    @ExcelProperty("rightsEndTime")
    private String rightsEndTime;
    @ColumnWidth(25)
    @ExcelProperty("useTo")
    private String useTo;
}
