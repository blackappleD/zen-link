package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/7 11:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PeopleEnterpriseReqDTO extends BaseDTO {

	@NotBlank(message = "queryCode不能为空")
	private String queryCode;

	private String encryptMethod = "NONE";

}
