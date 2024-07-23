package com.mkc.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商户结算报表
 * 
 * @author mkc
 * @date 2023-06-16
 */
@Data
public class MerProfitReportBean {



	/** 调用日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "调用日期", width = 30, dateFormat = "yyyy-MM-dd")
	private LocalDate reqDate;

	/** 商户编码 */
	//@Excel(name = "商户编码")
	//private String merCode;

	/** 商户名称 */
	@Excel(name = "商户",width = 30)
	private String merName;


	/** 供应商名称 */
	@Excel(name = "供应商")
	private String supName;

	/** 产品名称 */
	@Excel(name = "产品",width = 30)
	private String productName;

	/** 单销售价 */
	@Excel(name = "单价")
	private BigDecimal sellPrice;
	
	/** 收费次数 */
	@Excel(name = "收费次数")
	private Integer feeTimes;

	/** 总次数 */
	@Excel(name = "总次数")
	private BigDecimal reqTimes;

	/** 总成本价 */
	@Excel(name = "总成本价")
	private BigDecimal inPrice;

	/** 总价 */
	@Excel(name = "总价")
	private BigDecimal totalPrice;

	/** 查得次数 */
	private Integer statusOkFit;

	/** 查得次数 */
	private Integer statusOkUnfit;

	/** 查无次数 */
	private Integer statusNo;

	/** 异常次数 */
	private Integer statusErr;


}
