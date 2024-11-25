package com.mkc.tool;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/25 15:07
 */
@Slf4j
public final class Sm4Utils {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final String ENCODING = "UTF-8";
	public static final String ALGORITHM_NAME = "SM4";
	// 加密算法/分组加密模式/分组填充方式
	// PKCS5Padding-以8个字节为一组进行分组加密
	// 定义分组加密模式使用：PKCS5Padding
	public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";


	public static void main(String[] args) {
		try {
			String appSecret = "d616fc6aea7a456eb96f9028f2e47fb8";  //应用秘钥
			String timestamp = "1649396299385";  //时间戳，值为当前时间的毫秒数，也就是从1970年1月1日起至今的时间转换为毫秒，请求有效时间为5分钟
			String paramStr = "{\"flowId\": \"3709098989\",\"productCode\": \"B006\",\"idNumber\": \"370102199009098989\",\"userName\": \"张三\",\"phone\":\"13333333333\"}";
			String key = getSecretKey(appSecret, timestamp);
			String cipher = Sm4Utils.encryptEcb(key, paramStr);
			System.out.println(Sm4Utils.decryptEcb(key, cipher));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 生成ECB暗号
	 *
	 * @param algorithmName 算法名称
	 * @param mode          模式
	 * @param key
	 * @return
	 * @throws Exception
	 * @explain ECB模式（电子密码本模式：Electronic codebook）
	 */
	private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
		Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
		cipher.init(mode, sm4Key);
		return cipher;
	}

	/**
	 * sm4加密
	 *
	 * @param key      公钥
	 * @param paramStr 待加密字符串
	 * @return 返回base64字符串
	 * @explain 加密模式：ECB
	 * 密文长度不固定，会随着被加密字符串长度的变化而变化
	 */
	public static String encryptEcb(String key, String paramStr) {
		try {

			// 16进制字符串-->byte[]
			byte[] keyData = key.getBytes(ENCODING);
			// String-->byte[]
			byte[] srcData = paramStr.getBytes(ENCODING);
			// 加密后的数组
			byte[] cipherArray = encryptEcbPadding(keyData, srcData);
			// byte[]-->hexString
			return Base64.getEncoder().encodeToString(cipherArray);
		} catch (Exception e) {

			log.error("Sm4Utils 加密失敗：{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密模式之Ecb
	 *
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 * @explain
	 */
	public static byte[] encryptEcbPadding(byte[] key, byte[] data) throws Exception {
		Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * sm4解密
	 *
	 * @param key        公钥
	 * @param cipherText 加密字符串（base64）
	 * @return 解密后的字符串
	 * @throws Exception
	 * @explain 解密模式：采用ECB
	 */
	public static String decryptEcb(String key, String cipherText) {
		try {

			// 用于接收解密后的字符串
			byte[] keyData = key.getBytes(ENCODING);
			byte[] cipherData = Base64.getDecoder().decode(cipherText);
			// 解密
			byte[] srcData = decryptEcbPadding(keyData, cipherData);
			// byte[]-->String
			return new String(srcData, ENCODING);
		} catch (Exception e) {
			log.error("Sm4Utils解密失败：{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 *
	 * @param key
	 * @param cipherText
	 * @return
	 * @throws Exception
	 * @explain
	 */
	public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws Exception {
		Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(cipherText);
	}

	/**
	 * 校验加密前后的字符串是否为同一数据
	 *
	 * @param hexKey     16进制密钥（忽略大小写）
	 * @param cipherText 16进制加密后的字符串
	 * @param paramStr   加密前的字符串
	 * @return 是否为同一数据
	 * @throws Exception
	 * @explain
	 */
	public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
		// 用于接收校验结果
		boolean flag = false;
		// hexString-->byte[]
		byte[] keyData = ByteUtils.fromHexString(hexKey);
		// 将16进制字符串转换成数组
		byte[] cipherData = ByteUtils.fromHexString(cipherText);
		// 解密
		byte[] decryptData = decryptEcbPadding(keyData, cipherData);
		// 将原字符串转换成byte[]
		byte[] srcData = paramStr.getBytes(ENCODING);
		// 判断2个数组是否一致
		flag = Arrays.equals(decryptData, srcData);
		return flag;
	}


	public static String getSecretKey(String appSecret, String timestamp) {
		try {

			String stringToSecretKey = timestamp + appSecret;
			String sm3Str = Sm3Utils.encrypt(stringToSecretKey);
			return sm3Str.substring(0, 16);
		} catch (Exception e) {
			log.error("Sm4Utils 获取SecretKey失败：{}", e.getMessage());
			throw new RuntimeException(e);
		}

	}
}
