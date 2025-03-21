package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
public class AntiFraudV6ReqDTO extends BaseDTO {

	private String userName;

	private String certCode;

	@NotBlank(message = "telNO不能为空")
	private String telNO;

}
