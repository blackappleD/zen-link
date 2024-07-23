package com.mkc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mkc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 商户结算报表
 * 
 * @author mkc
 * @date 2023-06-16
 */
@Data
public class MerSettleReportBean {



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


	/** 产品名称 */
	@Excel(name = "产品",width = 30)
	private String productName;

	/** 单价 */
	@Excel(name = "单价")
	private BigDecimal sellPrice;
	
	/** 收费次数 */
	@Excel(name = "收费次数")
	private Integer feeTimes;

	/** 总价 */
	@Excel(name = "总价")
	private BigDecimal totalPrice;


}
