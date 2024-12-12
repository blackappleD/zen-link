package com.mkc.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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

	@ExcelProperty(value = "请求时间", converter = LocalDateTimeConverter.class)
	private LocalDateTime reqTime;

	public static class LocalDateTimeConverter implements Converter<LocalDateTime> {
		@Override
		public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
			try {
				return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss");
			} catch (DateTimeParseException e) {
				try {
					return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss.S");
				} catch (DateTimeParseException e1) {
					try {
						return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss.SS");
					} catch (DateTimeParseException e2) {
						try {
							return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss.SSS");
						} catch (DateTimeParseException e3) {
							try {
								return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss.SSSS");
							} catch (DateTimeParseException e4) {
								try {

									return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss.SSSSS");
								}catch (DateTimeParseException e5) {
									return LocalDateTimeUtil.parse(cellData.getStringValue(), "yyyy-MM-dd HH:mm:ss.SSSSSS");
								}
							}
						}
					}
				}
			}
		}


		@Override
		public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDateTime> context) throws Exception {

			return new WriteCellData<>(LocalDateTimeUtil.format(context.getValue(), "yyyy-MM-dd HH:mm:ss"));
		}
	}

}
