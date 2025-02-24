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
public class PersonLitigationReqDTO extends BaseDTO {

	@NotBlank(message = "name不能为空")
	private String name;

	@NotBlank(message = "idCard不能为空")
	private String idCard;

	private String familyName;

	@NotBlank(message = "inquiredAuth不能为空")
	private String inquiredAuth;

	@NotBlank(message = "authorization不能为空")
	private String authorization;

}
