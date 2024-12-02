package com.mkc.tool;


import org.springframework.util.DigestUtils;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/27 16:48
 */
public class SignUtils {
	public static String sign(String plaintext, String pwd) {

		String signText = plaintext + pwd;
		return DigestUtils.md5DigestAsHex(signText.getBytes());

	}

	public static void main(String[] args) {

		String persons = "[{\"name\":\"倪春云\",\"cardNum\":\"321323198606074120\"}]";
		String merCode = "XZSZKJSON1";
		//		本地
//		String pwd = "e0be01493778d77ecfd2004f54b41a09";
		//线上
		String pwd = "dc86ec7091b13263d44a7f23ab868385";

		String plaintext = merCode + "魏佳宁"+ "RNg8m3kIRezwtqVfujGixYdbn9l2Q5AyFckJMkZbVSc=";

		System.out.println(sign(plaintext, pwd));
	}


}
