package com.mkc.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/6 14:43
 */
public class RequestDemo {

	/**
	 * 婚姻状况查询
	 */
	public void maritalStatusRequest() {
		String url = "http://localhost:8080/maritalstatus";
		// sign加签方式参考接口文档
		MaritalReqDTO dto = new MaritalReqDTO("xxx", "xxx", "xxx", "xxx", "xxx");

		try (HttpResponse execute = HttpRequest.post(url)
				.body(JSONUtil.toJsonStr(dto))
				.execute()) {
			String result = execute.body();
		}
	}

	@Data
	@AllArgsConstructor
	public static class MaritalReqDTO {
		private String merCode;
		private String merSeq;
		private String sign;
		private String xm;
		private String sfzh;

	}

	/**
	 * 不动产发起
	 */
	public void houseRequest() {
		String url = "http://localhost:8083/bg/houseReqInfo";
		File file1 = new File("D:\\跑数\\不动产\\11.27\\授权书_吉.pdf");
		File file2 = new File("D:\\跑数\\不动产\\11.27\\授权书_倪.pdf");
		String persons = JSONUtil.toJsonStr(new PersonInfo("张三", "xxxxxxxxxxx"));
		String merCode = "Test";
		String pwd = "xxxxx";
		Map<String, Object> params = new HashMap<>();
		params.put("types", "3");
		params.put("persons", persons);
		params.put("merCode", merCode);
		params.put("merSeq", RandomUtil.randomString(16));
		params.put("sign", sign(merCode + persons, pwd));
		try (HttpResponse execute = HttpRequest.post(url)
				.form(params)
				.form("files", file1)
				.form("files", file2)
				.execute()) {
			String result = execute.body();
		}
	}

	/**
	 * 不动产结果请求
	 */
	public void houseResultRequest() {
		String url = "http://localhost:8083/bg/houseResultReqInfo";
		HouseResultReqDTO dto = new HouseResultReqDTO("xxx", "xxx", ListUtil.of(""), "xxx", "xxx");

		try (HttpResponse execute = HttpRequest.post(url)
				.body(JSONUtil.toJsonStr(dto))
				.execute()) {
			String result = execute.body();
		}

	}

	@Data
	@AllArgsConstructor
	public static class HouseResultReqDTO {
		private String merCode;
		private String merSeq;
		private List<String> personCardNumList;
		private String reqOrderNo;
		private String sign;

	}

	@Data
	public static class PersonInfo {
		private String name;
		private String cardNum;

		public PersonInfo(String name, String cardNum) {
			this.name = name;
			this.cardNum = cardNum;
		}
	}

	@Data
	public static class Response {
		private String code;
		private String seqNo;
		private String free;
		private String msg;
		private String data;
	}

	/**
	 * 加签
	 *
	 * @param plaintext 拼接方式参考接口文档
	 * @param pwd
	 * @return
	 */
	public static String sign(String plaintext, String pwd) {
		String signText = plaintext + pwd;
		return DigestUtils.md5DigestAsHex(signText.getBytes());
	}
}
