package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 *车五项信息查询
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class ExcelTestCar {

    @ColumnWidth(12)
    @ExcelProperty("车牌号")
    private String plateNo;
    @ColumnWidth(12)
    @ExcelProperty("code")
    private String code;
    @ColumnWidth(12)
    @ExcelProperty("msg")
    private String msg;
    @ColumnWidth(12)
    @ExcelProperty("engineNo")
    private String engineNo;
    @ColumnWidth(12)
    @ExcelProperty("brandName")
    private String brandName;
    @ColumnWidth(12)
    @ExcelProperty("vin")
    private String vin;
    @ColumnWidth(12)
    @ExcelProperty("initialRegistrationDate")
    private String initialRegistrationDate;
    @ColumnWidth(12)
    @ExcelProperty("modelNo")
    private String modelNo;
}

