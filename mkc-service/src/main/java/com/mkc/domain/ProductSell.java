package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品销售对象 t_product_sell
 *
 * @author atd
 * @date 2023-04-24
 */
@TableName(value = "t_product_sell")
@Data
public class ProductSell extends BaseEntity {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 产品分类编码
	 */
	@Excel(name = "产品分类编码")
	private String cgCode;

	/**
	 * 产品code
	 */
	//@Excel(name = "产品code")
	private String productCode;
	/**
	 * 产品名称
	 */

	@Excel(name = "产品名称")
	private String productName;

	/**
	 * 商户编码
	 */
	@Excel(name = "商户编码")
	private String merCode;

	/**
	 * 商户名称
	 */
	@TableField(exist = false)
	@Excel(name = "商户名称")
	private String merName;

	/**
	 * 状态
	 */
	@Excel(name = "状态")
	private String status;

	/**
	 * 状态
	 */
	@Excel(name = "路由切换条件")
	private String routeCon;

	/**
	 * 售价(元)
	 */
	@Excel(name = "售价(元)")
	private BigDecimal sellPrice;

	/**
	 * 路由供应商列表
	 */
	@TableField(exist = false)
	private String supNames;

	private Long reqLimit = 0L;


}
