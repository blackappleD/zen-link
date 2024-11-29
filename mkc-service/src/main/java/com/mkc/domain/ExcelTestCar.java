package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 车五项信息查询
 *
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class ExcelTestCar {

	@ColumnWidth(12)
	@ExcelProperty("车牌号码")
	private String plateNo;
	@ExcelProperty("颜色")
	private String color;
	@ColumnWidth(12)
	@ExcelProperty("code")
	private String code;
	@ColumnWidth(12)
	@ExcelProperty("msg")
	private String msg;
	@ColumnWidth(12)
	@ExcelProperty("engineNo")
	private String engineNo;
	@ColumnWidth(20)
	@ExcelProperty("brandName")
	private String brandName;
	@ColumnWidth(20)
	@ExcelProperty("vin")
	private String vin;
	@ColumnWidth(36)
	@ExcelProperty("initialRegistrationDate")
	private String initialRegistrationDate;
	@ColumnWidth(12)
	@ExcelProperty("modelNo")
	private String modelNo;

	@ExcelProperty("是否缺项")
	private String missParam;
}

