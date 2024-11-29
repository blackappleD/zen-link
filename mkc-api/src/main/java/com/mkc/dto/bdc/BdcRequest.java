package com.mkc.dto.bdc;

import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/28 17:24
 */
@Data
public class BdcRequest {

	private BusinessData data;

	@Data
	public static class BusinessData {

		private List<Person> persons;

	}

	@Data
	public static class Person {

		private String cardNum;
		private String name;
	}
}
