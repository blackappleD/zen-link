package com.mkc.dto;

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
	@ExcelProperty("响应码")
	private String code;
	@ColumnWidth(12)
	@ExcelProperty("总质量")
	private String crossWeight;
	@ColumnWidth(12)
	@ExcelProperty("颜色")
	private String color;
	@ColumnWidth(12)
	@ExcelProperty("燃料种类")
	private String fuel;
	@ColumnWidth(12)
	@ExcelProperty("轴距")
	private String wheelBase;
	@ColumnWidth(12)
	@ExcelProperty("发动机型号")
	private String engineType;
	@ColumnWidth(12)
	@ExcelProperty("车牌号")
	private String plate;
	@ColumnWidth(12)
	@ExcelProperty("车型")
	private String type;
	@ColumnWidth(12)
	@ExcelProperty("强制报废期止")
	private String retirement;
	@ColumnWidth(12)
	@ExcelProperty("result")
	private String result;
	@ColumnWidth(12)
	@ExcelProperty("发动机号")
	private String engine;
	@ColumnWidth(12)
	@ExcelProperty("variety")
	private String variety;
	@ColumnWidth(12)
	@ExcelProperty("初次登记日期")
	private String record;
	@ColumnWidth(12)
	@ExcelProperty("车架号")
	private String vin;
	@ColumnWidth(12)
	@ExcelProperty("man")
	private String man;
	@ColumnWidth(12)
	@ExcelProperty("车辆状态")
	private String state;
	@ColumnWidth(12)
	@ExcelProperty("品牌名称")
	private String brand;
	@ColumnWidth(12)
	@ExcelProperty("车辆类型")
	private String vehicleType;
	@ColumnWidth(12)
	@ExcelProperty("排量")
	private String cc;
	@ColumnWidth(12)
	@ExcelProperty("最大功率")
	private String maxJourney;
	@ColumnWidth(12)
	@ExcelProperty("核定载质量")
	private String loadWeight;
	@ColumnWidth(12)
	@ExcelProperty("核定载客数")
	private String passengers;
	@ColumnWidth(12)
	@ExcelProperty("后轮距")
	private String rearTread;
	@ColumnWidth(12)
	@ExcelProperty("jianCheTime")
	private String jianCheTime;
	@ColumnWidth(12)
	@ExcelProperty("出厂日期")
	private String pps;
	@ColumnWidth(12)
	@ExcelProperty("前轮距")
	private String frontTread;
	@ColumnWidth(12)
	@ExcelProperty("整备质量")
	private String curbWeight;
	@ColumnWidth(12)
	@ExcelProperty("检验有效期止")
	private String validity;
	@ColumnWidth(12)
	@ExcelProperty("使用性质")
	private String properties;
	@ColumnWidth(12)
	@ExcelProperty("轴数")
	private String shaft;

}
