package com.mkc.api.dto.ck;

import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/17 16:54
 */
@Data
public class ProQualifyCertResDTO {
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 身份证
	 */
	private String idNum;

	/**
	 * 职业
	 */
	private String profession;

	/**
	 * 职业等级
	 */
	private String level;

	/**
	 * 证书编号
	 */
	private String certNum;

	/**
	 * 核发日期
	 */
	private String issueDate;

}
