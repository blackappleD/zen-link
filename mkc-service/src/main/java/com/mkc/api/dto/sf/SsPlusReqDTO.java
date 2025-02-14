package com.mkc.api.dto.sf;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/14 11:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SsPlusReqDTO extends BaseDTO {

	@NotBlank(message = "certName不能为空")
	private String certName;

	@NotBlank(message = "certNo不能为空")
	private String certNo;

	// YYYYMMDD
	@NotBlank(message = "startDate不能为空")
	private String startDate;

	@NotBlank(message = "type不能为空")
	private String type;

}
