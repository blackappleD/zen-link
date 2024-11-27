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
		String merCode = "BhCpTest";
		//		本地
//		String pwd = "e0be01493778d77ecfd2004f54b41a09";
		//线上
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";

		String plaintext = merCode + persons;

		System.out.println(sign(plaintext, pwd));
	}


}
