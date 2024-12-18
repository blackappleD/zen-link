package com.mkc.api.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mkc.api.common.constant.enums.ReqParamType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tqlei
 * @date 2023/5/29 17:54
 */

@Data
public class BaseDTO {

	/**
	 * string	是	商户code;
	 */
	@NotBlank(message = "商户编码不能为空")
	private String merCode;
	/**
	 * string	是	商户流水号;
	 */
	@NotBlank(message = "商户流水号不能为空")
	private String merSeq;
	/**
	 * string	是	商户 KEY;
	 */
	@NotBlank(message = "商户Key不能为空")
	@JSONField(serialize = false)
	private String key;

	/**
	 * 请求参数类型 明文 ， MD5 ;
	 */
	private String paramType = ReqParamType.PLAIN.getCode();

	/**
	 * 签名
	 */
	@NotBlank(message = "商户签名不能为空")
	private String sign;

}
