package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/17 11:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeVerifyReqDTO extends BaseDTO {

	@NotBlank(message = "idCard不能为空")
	private String idCard;

	@NotBlank(message = "name不能为空")
	private String name;

	@NotBlank(message = "companyName不能为空")
	private String companyName;

	@NotBlank(message = "授权码不能为空")
	private String authCode;

	private String countryCode;

}
