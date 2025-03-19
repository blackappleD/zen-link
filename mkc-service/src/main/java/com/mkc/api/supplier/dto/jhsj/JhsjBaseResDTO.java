package com.mkc.api.supplier.dto.jhsj;

import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/19 15:34
 */
@Data
public class JhsjBaseResDTO<T> {
	private int code;
	private String msg;
	private T data;
}
