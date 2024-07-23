package com.mkc.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mkc.common.annotation.Excel;

import lombok.Data;

/**
 * 商户调用日志统计对象 t_mer_report
 * 
 * @author mkc
 * @date 2023-06-16
 */
@Data
@TableName(value = "t_mer_report")
public class MerReport {
	
	/** 编号 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 供应商编码 */
	//@Excel(name = "供应商编码")
	private String supCode;

	/** 供应商名称 */
	@Excel(name = "供应商名称")
	private String supName;

	/** 商户编码 */
	//@Excel(name = "商户编码")
	private String merCode;

	/** 商户名称 */
	@Excel(name = "商户名称")
	private String merName;

	/** 产品编码 */
	//@Excel(name = "产品编码")
	private String productCode;

	/** 产品名称 */
	@Excel(name = "产品名称")
	private String productName;

	/** 产品分类编码 */
	@Excel(name = "产品分类编码")
	private String cgCode;

	/** 总成本价 */
	@Excel(name = "总成本价")
	private BigDecimal inPrice;

	/** 单价 */
	@Excel(name = "单价")
	private BigDecimal sellPrice;
	
	/** 收费次数 */
	@Excel(name = "收费次数")
	private Integer feeTimes;

	/** 总价 */
	@Excel(name = "总价")
	private BigDecimal totalPrice;

	/** 调用日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "调用日期", width = 30, dateFormat = "yyyy-MM-dd")
	private LocalDate reqDate;

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

	/** 平均响应时间ms */
	@Excel(name = "平均响应时间ms")
	private BigDecimal avgTime;

	/** 响应时间1秒内请求次数 不包含1秒 */
	@Excel(name = "响应时间1秒内请求次数 不包含1秒")
	private Integer times1;

	/** 响应时间1-3秒内请求次数 不包含3秒 */
	@Excel(name = "响应时间1-3秒内请求次数 不包含3秒")
	private Integer times3;

	/** 响应时间3-10秒内请求次数 不包含10秒 */
	@Excel(name = "响应时间3-10秒内请求次数 不包含10秒")
	private Integer times10;

	/** 响应时间大于等于10秒请求次数 */
	@Excel(name = "响应时间大于等于10秒请求次数")
	private Integer timesGe10;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@TableField(exist = false)
	@JsonIgnore
	private String statClm;
	@TableField(exist = false)
	@JsonIgnore
	private Integer reportType;
	
	@TableField(exist = false)
	@JsonIgnore
	private LocalDate startTime;
	@TableField(exist = false)
	@JsonIgnore
	private LocalDate endTime;

}
