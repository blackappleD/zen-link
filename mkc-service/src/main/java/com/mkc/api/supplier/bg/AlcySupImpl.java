package com.mkc.api.supplier.bg;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.ck.req.AntiFraudV6ReqDTO;
import com.mkc.api.dto.ck.res.AntiFraudV6ResDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.common.utils.security.Md5Utils;
import com.mkc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service("BG_ALCY")
@Slf4j
public class AlcySupImpl implements IBgSupService {


	@Override
	public SupResult<AntiFraudV6ResDTO> antiFraudV6(AntiFraudV6ReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<AntiFraudV6ResDTO> supResult = null;
		String url = null;
		Map<String, String> params = new HashMap<>();

		try {
			url = bean.getUrl() + "/query/phone/antiFraudScoreV6";
			String appId = bean.getAcc();
			String serverPublicKey = bean.getSignKey();
			String privateKey = bean.getSignPwd();

			params.put("appId", appId);
			params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
			String userName = dto.getUserName();
			String certCode = dto.getCertCode();
			String telNO = dto.getTelNO();
			if (StringUtils.isNotBlank(userName)) {
				if (!Md5Utils.isMD5(userName)) {
					userName = Md5Utils.hash(userName);
				}
				params.put("userName", userName);
			}
			if (StringUtils.isNotBlank(certCode)) {
				if (!Md5Utils.isMD5(certCode)) {
					certCode = Md5Utils.hash(certCode);
				}
				params.put("certCode", certCode);
			}
			if (!Md5Utils.isMD5(telNO)) {
				telNO = Md5Utils.hash(telNO);
			}
			params.put("telNO", telNO);
			String sign = RsaUtil.sign(RsaUtil.generateParamsString(params), RsaUtil.loadPrivateKey(privateKey));

			params.put("sign", sign);

			supResult = new SupResult<>(JsonUtil.toJson(params), LocalDateTime.now());

			result = HttpUtil.doHttpPost(url, params);

			JSONObject responseObj = JSON.parseObject(result);

			String code = responseObj.getString("status");

			supResult.setRespTime(LocalDateTime.now());
			log.info(CharSequenceUtil.format("【反欺诈评分V6】{}", result));
			supResult.setRespJson(result);

			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			if ("0".equals(code)) {
				String checkResult = responseObj.getString("checkResult");
				Double score = responseObj.getDouble("score");
				AntiFraudV6ResDTO res = new AntiFraudV6ResDTO();
				res.setScore(score);
				supResult.setData(res);
				if ("1".equals(checkResult)) {
					supResult.setFree(FreeStatus.YES);
					supResult.setRemark("查询成功");
					supResult.setState(ReqState.SUCCESS);
				} else {
					supResult.setFree(FreeStatus.NO);
					supResult.setRemark("查询失败");
					supResult.setState(ReqState.ERROR);
				}
			} else {
				supResult.setRemark("查询失败");
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【反欺诈评分V6】接口 发生异常 orderNo {} URL {} , 报文: {} , err {}", bean.getOrderNo(), url,
					result, e);
			if (supResult == null) {
				supResult = new SupResult<>(JsonUtil.toJson(params), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}


	}

	private static class RsaUtil {

		private static final String DEFAULT_CHARSET = "UTF-8";
		private static final String RSA_ALGORITHM = "RSA";
		private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
		private static final String RSA_ALGORITHM_PADDING = "RSA/ECB/PKCS1Padding";

		private static final String NO_SUCH_ALGORITHM_EXCEPTION_MSG = "No such algorithm";
		private static final String INVALID_KEY_SPEC_EXCEPTION_MSG = "Invalid key spec";
		private static final String IO_EXCEPTION_MSG = "Reading key data error";
		private static final String NO_SUCH_PADDING_EXCEPTION_MSG = "No such padding";
		private static final String NULL_POINTER_EXCEPTION_MSG = "No key data found";
		private static final String INVALID_KEY_EXCEPTION_MSG = "Invalid key";
		private static final String ILLEGAL_BLOCK_SIZE_EXCEPTION_MSG = "Illegal block size";
		private static final String BAD_PADDING_EXCEPTION_MSG = "Bad padding";
		private static final String SIGNATURE_EXCEPTION_MSG = "Signature exception";
		private static final String UnsupportedEncodingExceptionMsg = "Unsupported encoding";
		private static final String PUBLIC_KEY = "RSAPublicKey";
		private static final String PRIVATE_KEY = "RSAPrivateKey";

		/**
		 * 生成密钥对(公钥和私钥)
		 *
		 * @return
		 * @throws Exception
		 */
		public static Map<String, Object> genKeyPair() throws Exception {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			Map<String, Object> keyMap = new HashMap<String, Object>(2);
			keyMap.put(PUBLIC_KEY, publicKey);
			keyMap.put(PRIVATE_KEY, privateKey);
			return keyMap;
		}

		/**
		 * 获取私钥
		 *
		 * @param keyMap 密钥对
		 * @return
		 * @throws Exception
		 */
		public static String getPrivateKey(Map<String, Object> keyMap)
				throws Exception {
			Key key = (Key) keyMap.get(PRIVATE_KEY);
			return AlcySupImpl.Base64Utils.encode(key.getEncoded());
		}

		/**
		 * 获取公钥
		 *
		 * @param keyMap 密钥对
		 * @return
		 * @throws Exception
		 */
		public static String getPublicKey(Map<String, Object> keyMap)
				throws Exception {
			Key key = (Key) keyMap.get(PUBLIC_KEY);
			return AlcySupImpl.Base64Utils.encode(key.getEncoded());
		}

		/**
		 * 从字符串中加载公钥
		 *
		 * @param publicKeyStr 公钥数据字符串
		 * @throws Exception 加载公钥时产生的异常
		 */
		public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
			RSAPublicKey publicKey;
			try {
				Base64.Decoder base64Decoder = Base64.getDecoder();
				byte[] buffer = base64Decoder.decode(publicKeyStr);
				KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
				publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			} catch (NoSuchAlgorithmException e) {
				throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
			} catch (InvalidKeySpecException e) {
				throw new InvalidKeySpecException(INVALID_KEY_SPEC_EXCEPTION_MSG);
			} catch (NullPointerException e) {
				throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
			}
			return publicKey;
		}

		/**
		 * 加载私钥
		 *
		 * @param privateKeyStr
		 * @return
		 * @throws Exception
		 */
		public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
			RSAPrivateKey privateKey;
			try {
				Base64.Decoder base64Decoder = Base64.getDecoder();
				byte[] buffer = base64Decoder.decode(privateKeyStr);
				PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
				KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
				privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			} catch (NoSuchAlgorithmException e) {
				throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
			} catch (InvalidKeySpecException e) {
				throw new InvalidKeySpecException(INVALID_KEY_SPEC_EXCEPTION_MSG);
			} catch (NullPointerException e) {
				throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
			}
			return privateKey;
		}

		/**
		 * 加密过程
		 *
		 * @param publicKey 公钥
		 * @param content   明文数据
		 * @return
		 * @throws Exception 加密过程中的异常信息
		 */
		public static String encrypt(RSAPublicKey publicKey, String content) throws Exception {
			return encrypt(publicKey, content);
		}

		/**
		 * 加密过程
		 *
		 * @param privateKey 私钥
		 * @param content    明文数据
		 * @return
		 * @throws Exception 加密过程中的异常信息
		 */
		public static String encrypt(RSAPrivateKey privateKey, String content) throws Exception {
			return encrypt(privateKey, content);
		}

		/**
		 * 加密过程
		 *
		 * @param key     秘钥
		 * @param content 明文数据
		 * @return
		 * @throws Exception 加密过程中的异常信息
		 */
		public static String encrypt(Key key, String content) throws Exception {
			byte[] plainTextData = content.getBytes();
			Cipher cipher;
			try {
				cipher = Cipher.getInstance(RSA_ALGORITHM_PADDING);
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] output = cipher.doFinal(plainTextData);
				return ByteFormat.bytesToHexString(output);
			} catch (NoSuchAlgorithmException e) {
				throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
			} catch (NoSuchPaddingException e) {
				throw new NoSuchPaddingException(NO_SUCH_PADDING_EXCEPTION_MSG);
			} catch (InvalidKeyException e) {
				throw new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
			} catch (IllegalBlockSizeException e) {
				throw new IllegalBlockSizeException(ILLEGAL_BLOCK_SIZE_EXCEPTION_MSG);
			} catch (BadPaddingException e) {
				throw new BadPaddingException(BAD_PADDING_EXCEPTION_MSG);
			}
		}

		/**
		 * 解密过程
		 *
		 * @param key     私钥
		 * @param content 密文数据
		 * @return 明文
		 * @throws Exception 解密过程中的异常信息
		 */
		public static String decrypt(Key key, String content) throws Exception {
			byte[] cipherData = ByteFormat.hexToBytes(content);
			Cipher cipher;
			try {
				cipher = Cipher.getInstance(RSA_ALGORITHM_PADDING);
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] output = cipher.doFinal(cipherData);
				return new String(output);
			} catch (NoSuchAlgorithmException e) {
				throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
			} catch (NoSuchPaddingException e) {
				throw new NoSuchPaddingException(NO_SUCH_PADDING_EXCEPTION_MSG);
			} catch (InvalidKeyException e) {
				throw new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
			} catch (IllegalBlockSizeException e) {
				throw new IllegalBlockSizeException(ILLEGAL_BLOCK_SIZE_EXCEPTION_MSG);
			} catch (BadPaddingException e) {
				throw new BadPaddingException(BAD_PADDING_EXCEPTION_MSG);
			}
		}

		/**
		 * 解密过程
		 *
		 * @param privateKey 私钥
		 * @param content    密文数据
		 * @return 明文
		 * @throws Exception 解密过程中的异常信息
		 */
		public static String decrypt(RSAPrivateKey privateKey, String content) throws Exception {
			return decrypt(privateKey, content);
		}

		/**
		 * 解密过程
		 *
		 * @param publicKey 公钥
		 * @param content   密文数据
		 * @return 明文
		 * @throws Exception 解密过程中的异常信息
		 */
		public static String decrypt(RSAPublicKey publicKey, String content) throws Exception {
			return decrypt(publicKey, content);
		}

		/**
		 * RSA签名
		 *
		 * @param content    待签名数据
		 * @param privateKey 私钥
		 * @return 签名值
		 */
		public static String sign(String content, PrivateKey privateKey) throws Exception {
			try {
				Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
				signature.initSign(privateKey);
				signature.update(content.getBytes(DEFAULT_CHARSET));
				byte[] signed = signature.sign();
				return ByteFormat.bytesToHexString(signed);
			} catch (NoSuchAlgorithmException e) {
				throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
			} catch (InvalidKeyException e) {
				throw new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
			} catch (SignatureException e) {
				throw new SignatureException(SIGNATURE_EXCEPTION_MSG);
			} catch (UnsupportedEncodingException e) {
				throw new UnsupportedEncodingException(UnsupportedEncodingExceptionMsg);
			}
		}

		/**
		 * RSA验签
		 *
		 * @param content   待签名数据
		 * @param sign      签名值
		 * @param publicKey 分配给开发商公钥
		 * @return 布尔值
		 */
		public static boolean verifySign(String content, String sign, PublicKey publicKey) throws Exception {
			try {
				Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
				signature.initVerify(publicKey);
				signature.update(content.getBytes(DEFAULT_CHARSET));
				return signature.verify(ByteFormat.hexToBytes(sign));
			} catch (NoSuchAlgorithmException e) {
				throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
			} catch (InvalidKeyException e) {
				throw new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
			} catch (SignatureException e) {
				throw new SignatureException(SIGNATURE_EXCEPTION_MSG);
			} catch (UnsupportedEncodingException e) {
				throw new UnsupportedEncodingException(UnsupportedEncodingExceptionMsg);
			}
		}

		public static String generateParamsString(Map<String, String> map) {
			if (map == null || map.size() == 0) {
				return "";
			}
			TreeMap<String, String> treeMap;
			if (map instanceof TreeMap) {
				treeMap = (TreeMap) map;
			} else {
				treeMap = new TreeMap<>();
				treeMap.putAll(map);
			}
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<String, String> entry : treeMap.entrySet()) {
				builder.append(entry.getValue());
			}
			return builder.toString();
		}
	}

	private static class Base64Utils {

		/**
		 * 文件读取缓冲区大小
		 */
		private static final int CACHE_SIZE = 1024;

		/**
		 * <p>
		 * BASE64字符串解码为二进制数据
		 * </p>
		 *
		 * @param base64
		 * @return
		 * @throws Exception
		 */
		public static byte[] decode(String base64) throws Exception {
			Base64.Decoder base64Decoder = Base64.getDecoder();
			byte[] buffer = base64Decoder.decode(base64);
			return buffer;
		}

		/**
		 * <p>
		 * 二进制数据编码为BASE64字符串
		 * </p>
		 *
		 * @param bytes
		 * @return
		 * @throws Exception
		 */
		public static String encode(byte[] bytes) throws Exception {
			Base64.Encoder base64Encoder = Base64.getEncoder();
			return base64Encoder.encodeToString(bytes);
		}


	}

	private static class ByteFormat {
		private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

		public ByteFormat() {
		}

		public static String bytesToHexString(byte[] bArray) {
			StringBuilder sb = new StringBuilder(bArray.length);

			for (byte b : bArray) {
				String sTemp = Integer.toHexString(255 & b);
				if (sTemp.length() < 2) {
					sb.append(0);
				}

				sb.append(sTemp.toUpperCase());
			}

			return sb.toString();
		}

		public static byte[] hexToBytes(String str) {
			if (str == null) {
				return null;
			} else {
				char[] hex = str.toCharArray();
				int length = hex.length / 2;
				byte[] raw = new byte[length];

				for (int i = 0; i < length; ++i) {
					int high = Character.digit(hex[i * 2], 16);
					int low = Character.digit(hex[i * 2 + 1], 16);
					int value = high << 4 | low;
					if (value > 127) {
						value -= 256;
					}

					raw[i] = (byte) value;
				}

				return raw;
			}
		}
	}


	private static class HttpUtil {

		/**
		 * HTTP POST
		 *
		 * @param url    请求地址
		 * @param params 请求参数
		 * @return 应答参数
		 */
		public static String doHttpPost(String url, Map<String, String> params) {
			String req = "";
			if (params != null && !params.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
				req = sb.toString();
			}
			PrintWriter outPrintWriter = null;
			BufferedReader inBufferedReader = null;
			try {
				HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
				// 设置通用的请求属性
				urlConnection.setRequestProperty("accept", "*/*");
				urlConnection.setRequestProperty("connection", "Keep-Alive");
				urlConnection.setRequestProperty("user-agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);
				urlConnection.setConnectTimeout(30000);
				urlConnection.setReadTimeout(30000);

				urlConnection.connect();

				outPrintWriter = new PrintWriter(urlConnection.getOutputStream());
				System.out.println(req);
				outPrintWriter.print(req);
				outPrintWriter.flush();
				inBufferedReader = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream(), StandardCharsets.UTF_8));
				String line = "";
				String response = "";
				while ((line = inBufferedReader.readLine()) != null) {
					response += line;
				}
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally {
				try {
					if (outPrintWriter != null) {
						outPrintWriter.close();
					}
					if (inBufferedReader != null) {
						inBufferedReader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
