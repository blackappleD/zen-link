package com.mkc.tool;

import com.mkc.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/25 15:06
 */
@Slf4j
public class Sm3Utils {
	private static final String ENCODING = "UTF-8";

	public static void main(String[] args) throws Exception {
		String appId = "04721f31d19e45ff95896092a138eec8";  //应用Id
		String appSecret = "d616fc6aea7a456eb96f9028f2e47fb8";  //应用秘钥
		String nonce = UUID.randomUUID().toString().replaceAll("-", "");//第三方生成的随机数，32位字符串。字符串数字，字母均可
		String timestamp = String.valueOf(Instant.now().toEpochMilli());
		System.out.println(getSign(appId, appSecret, timestamp, nonce));
		System.out.println(getSign2(appId, appSecret, timestamp, nonce));
	}


	/**
	 * SM3加密方式之：不提供密钥的方式 SM3加密，返回加密后长度为64位的16进制字符串
	 *
	 * @param src 明文
	 * @return
	 */
	public static String encrypt(String src) throws Exception {
		return ByteUtils.toHexString(getEncryptBySrcByte(src.getBytes(ENCODING)));
	}

	/**
	 * 返回长度为32位的加密后的byte数组
	 *
	 * @param srcByte
	 * @return
	 */
	public static byte[] getEncryptBySrcByte(byte[] srcByte) {
		SM3Digest sm3 = new SM3Digest();
		sm3.update(srcByte, 0, srcByte.length);
		byte[] encryptByte = new byte[sm3.getDigestSize()];
		sm3.doFinal(encryptByte, 0);
		return encryptByte;
	}


	public static String getSign(String appId, String appSecret, String timestamp, String nonce) {
		try {

			// 此处生成签名  
			String stringToSign = appId + timestamp + nonce + appSecret;
			String md5f = Sm3Utils.encrypt(stringToSign);
			String s = md5f.substring(2, 8);
			md5f = s + md5f;
			String md5s = Sm3Utils.encrypt(md5f);
			//进行BASE64编码
			return Base64.getEncoder().encodeToString(md5s.getBytes(ENCODING));
		} catch (Exception e) {
			log.error("SmUtils: 生成签名失败, {}", e.getMessage());
			throw new RuntimeException();
		}
	}

	private static String getSign2(String appId, String appSecret, String timestamp, String nonce) {
		// 此处生成签名  
		String stringToSign = appId + timestamp + nonce + appSecret;
		String md5f = DigestUtils.md5DigestAsHex(stringToSign.getBytes());
		String s = md5f.substring(2, 8);
		md5f = s + md5f;
		String md5s = DigestUtils.md5DigestAsHex(md5f.getBytes());

		//进行BASE64编码
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(md5s.getBytes(StandardCharsets.UTF_8));
	}

}