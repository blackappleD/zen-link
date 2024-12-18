package com.mkc.api.dto.bg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/16 15:02
 */
@Data
@AllArgsConstructor
public class BankFourResDTO {

	// 返回银行卡的借贷标志0：借记卡1：贷记卡,2-未知
	private String dcType;

	// 应答码
	private String detailRespCode;

	// 
	private String detailRespMsg;

}
