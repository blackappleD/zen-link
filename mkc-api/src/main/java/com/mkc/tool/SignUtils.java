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
		String merCode = "LXZL";
		//		本地
//		String pwd = "e0be01493778d77ecfd2004f54b41a09";
		//线上
		String pwd = "23d0418220cb3ddbf108b440bcab3560";

		//(merCode+idCard+
		// name+mobile+pwd

//		String plaintext = merCode + "330381199910181122" + "杨鑫" + "16604322562";


		System.out.println(sign("HZZNKJ" + "500102199308073693" + "甘立川", "742a2f3219da7177e677f19764eca797"));
	}


}
