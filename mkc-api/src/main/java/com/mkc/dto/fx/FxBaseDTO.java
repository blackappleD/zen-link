package com.mkc.dto.fx;

import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/11 9:33
 */
@Data
public class FxBaseDTO<T> {

	private String code;
	private String message;
	private String requestId;
	private String sign;
	private T data;

}
