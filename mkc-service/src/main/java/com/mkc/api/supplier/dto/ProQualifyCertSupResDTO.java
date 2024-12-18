package com.mkc.api.supplier.dto;

import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/17 17:31
 */
@Data
public class ProQualifyCertSupResDTO {

	//AAC002	是	String	居民身份证号码
	//ACA111	是	String	职业
	//AAC015	是	String	等级：1为1级或高级技师；2为2级或技师；3为3级或高级工；4为4级或中级工；5为5级或初级工
	//AHC011	是	String	证书编号
	//AAC187	是	String	核发日期
	//AAC003	是	String	姓名
	private String AAC002;
	private String ACA111;
	private String AAC015;
	private String AHC011;
	private String AAC187;
	private String AAC003;


}
