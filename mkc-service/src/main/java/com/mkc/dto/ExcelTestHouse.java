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
    @ExcelProperty("状态码")
    private String code;
    @ColumnWidth(25)
    @ExcelProperty("订单号")
    private String reqOrderNo;
    @ColumnWidth(25)
    @ExcelProperty("不动产权证号")
    private String certNo;
    @ColumnWidth(25)
    @ExcelProperty("不动产单元号")
    private String unitNo;
    @ColumnWidth(25)
    @ExcelProperty("坐落")
    private String location;
    @ColumnWidth(25)
    @ExcelProperty("共有情况")
    private String ownership;
    @ColumnWidth(25)
    @ExcelProperty("房屋面积")
    private String houseArea;
    @ColumnWidth(25)
    @ExcelProperty("权利类型")
    private String rightsType;
    @ColumnWidth(25)
    @ExcelProperty("是否查封")
    private String isSealUp;
    @ColumnWidth(25)
    @ExcelProperty("是否抵押")
    private String isMortgaged;
    @ColumnWidth(25)
    @ExcelProperty("使用期限起始时间")
    private String rightsStartTime;
    @ColumnWidth(25)
    @ExcelProperty("使用期限结束时间")
    private String rightsEndTime;
    @ColumnWidth(25)
    @ExcelProperty("用途")
    private String useTo;
}
