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
public class EnterpriseThreeElementsReqDTO extends BaseDTO {


	private String orgName;

	@NotBlank(message = "orgCertNo不能为空")
	private String orgCertNo;

	@NotBlank(message = "legalPerson不能为空")
	private String legalPerson;

}
