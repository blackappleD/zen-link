package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author linst
 * @date 2024/10/23
 */
@Data
public class ExcelTestDriving {
    @ColumnWidth(12)
    @ExcelProperty("姓名")
    private String name;
    @ColumnWidth(12)
    @ExcelProperty("车牌号")
    private String licensePlateNo;
    @ColumnWidth(12)
    @ExcelProperty("车牌类型")
    private String plateType;
    @ColumnWidth(12)
    @ExcelProperty("code")
    private String code;
    @ColumnWidth(12)
    @ExcelProperty("crossWeight")
    private String crossWeight;
    @ColumnWidth(12)
    @ExcelProperty("color")
    private String color;
    @ColumnWidth(12)
    @ExcelProperty("fuel")
    private String fuel;
    @ColumnWidth(12)
    @ExcelProperty("wheelBase")
    private String wheelBase;
    @ColumnWidth(12)
    @ExcelProperty("engineType")
    private String engineType;
    @ColumnWidth(12)
    @ExcelProperty("plate")
    private String plate;
    @ColumnWidth(12)
    @ExcelProperty("type")
    private String type;
    @ColumnWidth(12)
    @ExcelProperty("retirement")
    private String retirement;
    @ColumnWidth(12)
    @ExcelProperty("result")
    private String result;
    @ColumnWidth(12)
    @ExcelProperty("engine")
    private String engine;
    @ColumnWidth(12)
    @ExcelProperty("variety")
    private String variety;
    @ColumnWidth(12)
    @ExcelProperty("record")
    private String record;
    @ColumnWidth(12)
    @ExcelProperty("vin")
    private String vin;
    @ColumnWidth(12)
    @ExcelProperty("man")
    private String man;
    @ColumnWidth(12)
    @ExcelProperty("state")
    private String state;
    @ColumnWidth(12)
    @ExcelProperty("brand")
    private String brand;
    @ColumnWidth(12)
    @ExcelProperty("vehicleType")
    private String vehicleType;
    @ColumnWidth(12)
    @ExcelProperty("cc")
    private String cc;
    @ColumnWidth(12)
    @ExcelProperty("maxJourney")
    private String maxJourney;
    @ColumnWidth(12)
    @ExcelProperty("loadWeight")
    private String loadWeight;
    @ColumnWidth(12)
    @ExcelProperty("passengers")
    private String passengers;
    @ColumnWidth(12)
    @ExcelProperty("rearTread")
    private String rearTread;
    @ColumnWidth(12)
    @ExcelProperty("jianCheTime")
    private String jianCheTime;
    @ColumnWidth(12)
    @ExcelProperty("pps")
    private String pps;
    @ColumnWidth(12)
    @ExcelProperty("frontTread")
    private String frontTread;
    @ColumnWidth(12)
    @ExcelProperty("curbWeight")
    private String curbWeight;
    @ColumnWidth(12)
    @ExcelProperty("validity")
    private String validity;
    @ColumnWidth(12)
    @ExcelProperty("properties")
    private String properties;
    @ColumnWidth(12)
    @ExcelProperty("shaft")
    private String shaft;

}
