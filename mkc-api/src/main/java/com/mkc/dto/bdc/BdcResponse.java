package com.mkc.dto.bdc;

import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/28 17:21
 */
@Data
public class BdcResponse {

	private String code;
	private String message;
	private String requestId;
	private String sign;
	private BusinessData data;

	@Data
	public static class BusinessData {
		private String resultCode;
		private String reqOrderNo;
	}
}
