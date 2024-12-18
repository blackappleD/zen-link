package com.mkc.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author tqlei
 * @date 2023/5/17 13:09
 */

@Data
public class SuplierQueryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * code
	 */
	private String supCode;

	/**
	 * 供应商处理器
	 */
	private String supProcessor;

	/**
	 * 名称
	 */
	private String supName;

	/**
	 * 账号
	 */
	private String acc;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 接口地址
	 */
	private String url;

	/**
	 * 秘钥 加签相关pwd
	 */
	private String signPwd;

	/**
	 * 加签相关key
	 */
	private String signKey;

	/**
	 * 鉴权账号
	 */
	private String authAccount;

	/**
	 * 鉴权密码
	 */
	private String authPwd;

	/**
	 * 排序
	 */
	private Long sort;

	/**
	 * 超时时间 默认5秒
	 */
	private Integer timeOut = 5000;

	/**
	 * 产品
	 */
	private String productCode;

	/**
	 * 供应商产品编码
	 */
	private String supProductCode;

	/**
	 * 成本价
	 */
	private BigDecimal inPrice;
	/**
	 * 当前这条商户请求记录流水号
	 */
	private String orderNo;


}
