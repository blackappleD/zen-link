package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/14 15:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FinanceI8ReqDTO extends BaseDTO {

	@NotBlank(message = "idCard不能为空")
	private String idCard;

	@NotBlank(message = "name不能为空")
	private String name;


}
