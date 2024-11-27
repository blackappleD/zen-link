package com.mkc.domain;

import com.mkc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商户调用日志统计对象 t_mer_report
 *
 * @author mkc
 * @date 2023-06-16
 */
@Data
public class MerReportExcel {
	/** 商户编码 */
	@Excel(name = "商户编码")
	private String merCode;

	/** 商户名称 */
	@Excel(name = "商户名称")
	private String merName;

	/** 产品名称 */
	@Excel(name = "产品名称")
	private String productName;

//	/** 总成本价 */
//	@Excel(name = "总成本价")
//	private BigDecimal inPrice = BigDecimal.valueOf(0);

//	/** 单价 */
//	@Excel(name = "单价")
//	private BigDecimal sellPrice;

	/** 收费次数 */
	@Excel(name = "计费次数")
	private Integer feeTimes;

	/** 收费次数 */
	@Excel(name = "一类")
	private Integer level1;

	/** 收费次数 */
	@Excel(name = "二类")
	private Integer level2;

	/** 收费次数 */
	@Excel(name = "三类")
	private Integer level3;

//	/** 总价 */
//	@Excel(name = "总价")
//	private BigDecimal totalPrice;

	/** 调用日期 */
	@Excel(name = "调用日期", width = 30)
	private String reqDate;

}
