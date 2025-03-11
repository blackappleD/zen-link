package com.mkc.api.supplier.bg;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.bg.req.PeopleEnterpriseReqDTO;
import com.mkc.api.dto.bg.res.PeopleEnterpriseResDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/7 11:38
 */
@Slf4j
@Service("BG_ZDBIGDATA")
public class ZdBigDataSupImpl implements IBgSupService {

	private static final String SUCCESS = "30100000";
	private static final String YQ_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVM/rx1c2LiLcGHO+XOtrd1hoo0KIK+FXZ1Tl4Zm3m2vaAtxV+4KaUC7mRcmlBKyX+C10w4N/V/hzkrQK0DW/TUPAIhbx0LIVLmu6JSyYYsX+z3nQvRD+bPt/jiAqx6TKf7OADHtQKaFJzP1y0Frn69iIuOB7w+TvETWGy4CpcUQIDAQAB";


	@Data
	private static class BaseResponse {
		private String code;

		private String msg;

		private DataBody data;

		/**
		 * 是否成功：true=成功，false=失败
		 */
		private Boolean success;
	}

	@Data
	private static class DataBody {

		private String secretKey;

		private String content;
	}

	@Data
	@AllArgsConstructor
	private static class BaseRequest {

		/**
		 * 使用RSA公钥加密过的AES加密密钥
		 */
		private String secretKey;

		/**
		 * 使用AES密钥加密过的请求内容。
		 */
		private String content;
	}

	@Data
	private static class ProductQueryRequest {

		/**
		 * 产品所对应的code
		 */
		private String productCode;

		/**
		 * 查询原因ID
		 */
		private Integer queryReasonId = 1;

		/**
		 * 授权数据使用方标识
		 */
		private String dataAuthOrg;

		/**
		 * 授权数据使用方内容
		 */
		private String dataAuthContent = null;

		/**
		 * 查询条件
		 */
		private Map<String, Object> conditions;

		public ProductQueryRequest(String productCode) {
			this.productCode = productCode;
		}
	}


	@Override
	public SupResult<PeopleEnterpriseResDTO> queryPeopleEnterprise(PeopleEnterpriseReqDTO dto, SuplierQueryBean bean) {

		String supProductCode = bean.getSupProductCode();
		SupResult<PeopleEnterpriseResDTO> supResult;
		String resJson = "";
		String url = bean.getUrl();
		String appId = bean.getAcc();
		String orgPublicKey = bean.getSignKey();
		String orgPrivateKey = bean.getSignPwd();
		String userName = bean.getAuthAccount();
		String password = bean.getAuthPwd();
		Integer timeOut = bean.getTimeOut();


		ProductQueryRequest request = new ProductQueryRequest("12583");

		Map<String, Object> conditions = new HashMap<>();
		conditions.put("queryCode", dto.getQueryCode());
		conditions.put("encryptMethod", dto.getEncryptMethod());

		request.setConditions(conditions);
		String reqJson = JSON.toJSONString(request);
		log.info(reqJson);
		try {
			String authorization = genAuthorization(appId, userName, password);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Content-Type", "application/json");
			httpHeaders.add("Authorization", authorization);
			String encryptRequest = OpenApiEncryptUtil.encryptRequest(reqJson, YQ_PUBLIC_KEY);

			log.info(" {} 请求头 : {} ", supProductCode, httpHeaders);
			log.info(" {} 请求体 : {}", supProductCode, encryptRequest);
			try (HttpResponse response = HttpUtil.createPost(url)
					.header(httpHeaders)
					.body(encryptRequest)
					.timeout(timeOut)
					.execute()) {
				supResult = new SupResult<>(reqJson, LocalDateTime.now());

				log.info(" {} 返回体 ：{}", supProductCode, response.body());
				BaseResponse responseBody = JsonUtil.fromJson(response.body(), BaseResponse.class);
				String code = responseBody.getCode();
				supResult.setRespTime(LocalDateTime.now());
				if (!SUCCESS.equals(code)) {
					supResult.setFree(FreeStatus.NO);
					supResult.setRemark("供应商：" + responseBody.getMsg());
					supResult.setRespJson(response.body());
					return supResult;
				} else {
					String decryptBody = OpenApiEncryptUtil.decryptResponse(responseBody.getData().getContent(), responseBody.getData().getSecretKey(), orgPrivateKey);
					DataBody dataBody = JsonUtil.fromJson(decryptBody, DataBody.class);
					responseBody.getData().setContent(decryptBody);
					resJson = JsonUtil.toJson(responseBody);
					supResult.setRespJson(resJson);
					PeopleEnterpriseResDTO peopleEnterpriseResDTO = JsonUtil.fromJson(dataBody.getContent(), PeopleEnterpriseResDTO.class);
					supResult.setData(peopleEnterpriseResDTO);
					supResult.setFree(FreeStatus.YES);
					supResult.setRemark("查询成功");
					supResult.setState(ReqState.SUCCESS);
					supResult.setData(peopleEnterpriseResDTO);
				}
				return supResult;
			}
		} catch (Exception e) {
			errMonitorMsg(log, " 【中鼎大数据供应商】 人企信息 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, reqJson, e);
			supResult = new SupResult<>(reqJson, LocalDateTime.now());
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(resJson);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}

	public String genAuthorization(String appId, String username, String password) {


		return String.format("Basic %s", Base64.encodeBase64String(String.format("%s:%s:%s:%s", System.currentTimeMillis(), appId, username, password).getBytes(StandardCharsets.UTF_8)));
	}


	private static class OpenApiEncryptUtil {
		private static final String AES = "AES/CFB/NoPadding";

		public static String encryptRequest(String jsonStr, String publicKey) throws Exception {
			String aesRandomKey = getAESRandomKey();
			// 加密查询条件
			String queryContent = encryptByAES(jsonStr, aesRandomKey);
			// 加密AES密钥
			String encryptedAesKey = encryptByRSA(aesRandomKey, publicKey);
			BaseRequest baseRequest = new BaseRequest(encryptedAesKey, queryContent);
			return JSON.toJSONString(baseRequest);
		}

		public static String decryptResponse(String content, String secretKey, String privateKey) throws Exception {
			return decryptByAES(content, decryptByRSA(secretKey, privateKey));
		}

		/**
		 * 生成16位AES随机密钥
		 *
		 * @return
		 */
		public static String getAESRandomKey() {
			return RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 16);
		}

		public static String encryptByAES(String inputStr, String password) throws Exception {
			byte[] byteData = inputStr.getBytes();
			byte[] bytePassword = password.getBytes();
			return Base64.encodeBase64String(encryptByAES(byteData, bytePassword, bytePassword));
		}

		public static byte[] encryptByAES(byte[] data, byte[] pwd, byte[] iv) throws Exception {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec keySpec = new SecretKeySpec(pwd, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
			return cipher.doFinal(data);
		}

		public static String decryptByAES(String inputStr, String password) throws Exception {
			byte[] byteData = Base64.decodeBase64(inputStr);
			byte[] bytePassword = password.getBytes();
			return new String(decryptByAES(byteData, bytePassword, bytePassword));
		}

		public static byte[] decryptByAES(byte[] data, byte[] pwd, byte[] iv) throws Exception {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec keySpec = new SecretKeySpec(pwd, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			return cipher.doFinal(data);
		}

		public static String encryptByRSA(String inputStr, String publicKey) throws Exception {
			PublicKey key = getPublicKeyFromString(publicKey);
			return Base64.encodeBase64String(encryptByRSA(inputStr.getBytes(), key));
		}

		public static byte[] encryptByRSA(byte[] input, Key key) throws Exception {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(input);
		}

		public static String decryptByRSA(String inputStr, String privateKey) throws Exception {
			PrivateKey key = getPrivateKeyFromString(privateKey);
			return new String(decryptByRSA(Base64.decodeBase64(inputStr), key));
		}

		public static byte[] decryptByRSA(byte[] input, Key key) throws Exception {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(input);
		}

		public static PublicKey getPublicKeyFromString(String base64String)
				throws NoSuchAlgorithmException, InvalidKeySpecException {
			byte[] bt = Base64.decodeBase64(base64String);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bt);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(publicKeySpec);
		}

		public static PrivateKey getPrivateKeyFromString(String base64String)
				throws InvalidKeySpecException, NoSuchAlgorithmException {
			byte[] bt = Base64.decodeBase64(base64String);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bt);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(privateKeySpec);
		}
	}

}