package com.mkc.api.dto.bg.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 10:30
 */
@Data
public class CreditA107ResDTO {

	private Long score;

	@JsonProperty("D156")
	private String D156;

	@JsonProperty("D157")
	private String D157;

	@JsonProperty("D834")
	private String D834;

	@JsonProperty("D857")
	private String D857;

	@JsonProperty("D859")
	private String D859;

	@JsonProperty("D863")
	private String D863;

	@JsonProperty("D868")
	private String D868;

	@JsonProperty("D869")
	private String D869;

	@JsonProperty("D875")
	private String D875;
}
