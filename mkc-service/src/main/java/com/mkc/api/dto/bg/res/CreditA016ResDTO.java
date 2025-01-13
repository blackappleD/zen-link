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
public class CreditA016ResDTO {

	private Long score;

	@JsonProperty("D177")
	private String D177;

	@JsonProperty("D181")
	private String D181;

	@JsonProperty("D940")
	private String D940;

	@JsonProperty("D941")
	private String D941;

	@JsonProperty("D942")
	private String D942;

	@JsonProperty("D943")
	private String D943;

	@JsonProperty("D944")
	private String D944;

	@JsonProperty("D945")
	private String D945;

}
