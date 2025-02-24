package com.mkc.api.dto.sf;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/24 13:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EnterpriseLitigationReqDTO extends BaseDTO {

	@NotBlank(message = "orgName不能为空")
	private String orgName;

	private String uscc;

	@NotBlank(message = "inquiredAuth不能为空")
	private String inquiredAuth;

	@NotBlank(message = "authorization不能为空")
	private String authorization;

}
