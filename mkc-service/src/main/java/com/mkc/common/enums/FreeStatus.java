package com.mkc.common.enums;

import lombok.Getter;

/**
 * 是否收费状态码 0 不收费 1收费
 *
 * @author tqlei
 * @date 2023/5/18 17:01
 */
@Getter
public enum FreeStatus {

	/**
	 * 收费
	 */
	YES("1", "收费"),

	/**
	 * 不收费
	 */
	NO("0", "不收费"),

	;


	FreeStatus(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private final String code;
	private final String msg;

}
