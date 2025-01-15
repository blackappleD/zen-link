package com.mkc.api.dto.bg.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/15 9:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HighRiskPeopleReqDTO extends BaseDTO {

	private String certNo;

}
