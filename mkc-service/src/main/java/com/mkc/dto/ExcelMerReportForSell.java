package com.mkc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.common.annotation.Excel;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/22 10:46
 */
@Data
public class ExcelMerReportForSell {

	/** 调用日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "调用日期", width = 30, dateFormat = "yyyy-MM-dd")
	private LocalDate reqDate;

	/** 商户名称 */
	@Excel(name = "商户名称")
	private String merName;

	/** 产品名称 */
	@Excel(name = "产品名称")
	private String productName;

	@Excel(name = "总次数")
	private transient Integer totalTimes;

	/** 收费次数 */
	@Excel(name = "计费次数")
	private Integer feeTimes;

	/** 查得次数 */
	@Excel(name = "查得匹配次数")
	private Integer statusOkFit;

	/** 查得次数 */
	@Excel(name = "查得不匹配次数")
	private Integer statusOkUnfit;

	/** 查无次数 */
	@Excel(name = "查无次数")
	private Integer statusNo;

	/** 异常次数 */
	@Excel(name = "异常次数")
	private Integer statusErr;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

}
