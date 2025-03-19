package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
public class PersonalVehicleReqDTO extends BaseDTO {


	@NotBlank(message = "idCard不能为空")
	private String idCard;

	@NotBlank(message = "userType不能为空")
	private String userType;

	private String name;

	private String vehicleType;
}
