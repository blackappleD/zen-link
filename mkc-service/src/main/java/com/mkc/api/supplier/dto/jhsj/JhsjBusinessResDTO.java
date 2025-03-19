package com.mkc.api.supplier.dto.jhsj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/14 16:07
 */
@Data
public class JhsjBusinessResDTO {

	private String requestId;

	private String resultCode;

	@JsonProperty("messge")
	private String message;

}
