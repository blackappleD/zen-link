package com.mkc.dto.sddw;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/25 15:16
 */
@Data
public class AuthInfoReqDTO {

	// 否	第三方业务流水号，每一笔业务的唯一标识
	private String flowId;

	// 否	使用场景标识，由第三方线下填写纸质申请单并审核通过后获取该标识（参考附4）
	private String productCode;

	// 否	证件类型（见字典项说明6.1）（使用方）
	private String useIdType;

	//否	证件号码（使用方）
	private String useIdNumber;

	//否	证件类型 (见字典项说明6.1)（资产拥有方）
	private String idType;

	//否	证件号码（资产拥有方）
	private String idNumber;

}
