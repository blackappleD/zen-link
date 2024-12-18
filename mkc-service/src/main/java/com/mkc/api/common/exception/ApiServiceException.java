package com.mkc.api.common.exception;

import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.common.enums.ReqState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API 业务异常
 *
 * @author atd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class ApiServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * 错误编码
	 */
	private String code;
	/**
	 * 错误提示
	 */
	private String message;

	/**
	 * 错误明细，内部调试错误
	 */
	private String detailMessage;

	/**
	 * 空构造方法，避免反序列化问题
	 */
	public ApiServiceException() {
	}

	public ApiServiceException(String message) {
		this.code = ReqState.ERROR.getCode();
		this.message = message;
	}

	public ApiServiceException(String code, String message) {
		this.message = message;
		this.code = code;
	}

	public ApiServiceException(ApiReturnCode returnErr) {
		this.message = returnErr.getMsg();
		this.code = returnErr.getCode();
	}


	public ApiServiceException setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
		return this;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public ApiServiceException setMessage(String message) {
		this.message = message;
		return this;
	}
}