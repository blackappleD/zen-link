package com.mkc.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/28 17:10
 */
@Data
public class SupLogLine {

	//流水号	产品分类	商户编码	供应商名称	请求参数Json	响应参数Json	供应产品	供应商订单号	产品	查询状态	请求时间	总耗时(毫秒)
	@ExcelProperty("流水号")
	private String orderNum;

	@ExcelProperty("产品分类")
	private String productType;

	@ExcelProperty("商户编码")
	private String merCode;

	@ExcelProperty("供应商名称")
	private String supName;

	@ExcelProperty("请求参数Json")
	private String reqJson;

	@ExcelProperty("响应参数Json")
	private String resJson;

	@ExcelProperty("产品")
	private String productCode;

	@ExcelProperty("查询状态")
	private String status;
}
