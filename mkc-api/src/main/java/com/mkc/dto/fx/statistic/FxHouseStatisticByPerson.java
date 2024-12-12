package com.mkc.dto.fx.statistic;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mkc.dto.SupLogLine;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/11 15:34
 */
@Data
public class FxHouseStatisticByPerson {

	@ColumnWidth(20)
	@ExcelProperty("req_order_no")
	private String reqOrderNo;

	@ColumnWidth(20)
	@ExcelProperty(value = "create_time", converter = SupLogLine.LocalDateTimeConverter.class)
	private LocalDateTime createTime;

	@ColumnWidth(20)
	@ExcelProperty(value = "update_time", converter = SupLogLine.LocalDateTimeConverter.class)
	private LocalDateTime updateTime;

	@ColumnWidth(20)
	@ExcelProperty("mer_request_data")
	private String merRequestData;

	@ColumnWidth(20)
	@ExcelProperty("mer_result_data")
	private String merResultData;

	@ColumnWidth(20)
	@ExcelProperty("被核查人")
	private String userName;

	@ColumnWidth(20)
	@ExcelProperty("是否查得")
	private String get = "否";

	@ColumnWidth(20)
	@ExcelProperty("是否计费")
	private String pay = "否";


}
