package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 10:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreditA016ReqDTO extends BaseDTO {

	@NotBlank(message = "身份证号不能为空")
	private String cid;

	@NotBlank(message = "姓名不能为空")
	private String name;

	private String mobile;
}
