package com.mkc.api.supplier.bg;

import cn.com.antcloud.api.common.BaseResponse;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.exception.ApiSupException;
import com.mkc.api.dto.bg.req.FinanceI8ReqDTO;
import com.mkc.api.dto.bg.req.FinanceI9ReqDTO;
import com.mkc.api.dto.bg.res.FinanceI8ResDTO;
import com.mkc.api.dto.bg.res.FinanceI9ResDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.HOURS;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/19 17:07
 */
@Service("BG_ZKZT")
@Slf4j
public class ZKZTBgSupImpl implements IBgSupService {

	private final static boolean SUCCESS = true;
	private final static boolean NO = false;


	@Data
	public static class BusinessBody {
		private String data;

		private String code;

		private String uuid;

		private String retMsg;

	}


	@Override
	public SupResult<FinanceI8ResDTO> queryFinanceI8(FinanceI8ReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<FinanceI8ResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl();
			String account = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String idCard = dto.getIdCard();
			String name = dto.getName();

			Map<String, String> headers = new HashMap<>();
			headers.put("accountId", account);
			headers.put(" prodId", "S0019");
			headers.put("Content-Type", "application/json");

			params.put("idCard", idCard);
			params.put("name", name);
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("data", AesUtil.encode(JsonUtil.toJson(params), signPwd));

			String resJson = JsonUtil.toJson(dataMap);
			supResult = new SupResult<>(resJson, LocalDateTime.now());
			try (HttpResponse response = HttpUtil.createPost(url)
					.headerMap(headers, true)
					.timeout(timeOut)
					.body(resJson)
					.execute()) {

				supResult.setRespTime(LocalDateTime.now());
				if (Objects.isNull(response) || response.getStatus() != 200) {
					throw new ApiSupException(CharSequenceUtil.format("供应商请求报错：{}, {}", response.getStatus(), response.body()));
				}
				result = response.body();
			}

			log.info(CharSequenceUtil.format("【{}】 返回体：{}", bean.getSupProductCode(), result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BusinessBody businessBody = JsonUtil.fromJson(result, BusinessBody.class);
			String code = businessBody.getCode();
			if (!"200".equals(code)) {
				supResult.setRemark("查询失败: " + businessBody.getRetMsg());
				supResult.setFree(FreeStatus.NO);

			} else {
				supResult.setData(JsonUtil.fromJson(AesUtil.decode(businessBody.getData(), signPwd), FinanceI8ResDTO.class));
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【{}】接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getSupProductCode(), bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}

	}


	@Override
	public SupResult<FinanceI9ResDTO> queryFinanceI9(FinanceI9ReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<FinanceI9ResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl();
			String account = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String idCard = dto.getIdCard();
			String name = dto.getName();

			Map<String, String> headers = new HashMap<>();
			headers.put("accountId", account);
			headers.put(" prodId", "S0009");
			headers.put("Content-Type", "application/json");

			params.put("idCard", idCard);
			params.put("name", name);
			params.put("authCode", dto.getAuthCode());


			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("data", AesUtil.encode(JsonUtil.toJson(params), signPwd));

			String resJson = JsonUtil.toJson(dataMap);
			supResult = new SupResult<>(resJson, LocalDateTime.now());
			try (HttpResponse response = HttpUtil.createPost(url)
					.headerMap(headers, true)
					.timeout(timeOut)
					.body(resJson)
					.execute()) {

				supResult.setRespTime(LocalDateTime.now());
				if (Objects.isNull(response) || response.getStatus() != 200) {
					throw new ApiSupException(CharSequenceUtil.format("供应商请求报错：{}, {}", response.getStatus(), response.body()));
				}
				result = response.body();
			}

			log.info(CharSequenceUtil.format("【{}】返回体：{}", bean.getSupProductCode(), result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BusinessBody businessBody = JsonUtil.fromJson(result, BusinessBody.class);
			String code = businessBody.getCode();
			if (!"200".equals(code)) {
				supResult.setRemark("查询失败: " + businessBody.getRetMsg());
				supResult.setFree(FreeStatus.NO);

			} else {
				supResult.setData(JsonUtil.fromJson(AesUtil.decode(businessBody.getData(), signPwd), FinanceI9ResDTO.class));
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【{}】接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getSupProductCode(), bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}

	}

	private static final class AesUtil {

		private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
		private static final String INITIALIZATION_VECTOR = "0000000000000000";
		private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


		private static final SecretKey key;
		private static final IvParameterSpec iv;
		private static final Cipher cipher;

		static {
			try {
				String secretKey = "yourSecretKey"; // Replace with your secret key
				key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
				iv = new IvParameterSpec("0000000000000000".getBytes());
				cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			} catch (Exception e) {
				throw new RuntimeException("Initialization error", e);
			}
		}


		public static String bytesToHexFun2(byte[] bytes) {
			char[] buf = new char[bytes.length * 2];
			int index = 0;
			for (byte b : bytes) {
				buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
				buf[index++] = HEX_CHAR[b & 0xf];
			}
			return new String(buf);
		}

		public static String encode(String content, String key) {
			try {
				if (content.isEmpty()) {
					return "";
				}
				SecretKeySpec keySpec = new SecretKeySpec(Hex.decodeHex(key), "AES");
				Cipher cipher = getCipherInstance(Cipher.ENCRYPT_MODE, keySpec);
				byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
				return Base64.encodeBase64String(encryptedBytes);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		public static String decode(String content, String key) {
			try {
				SecretKeySpec keySpec = new SecretKeySpec(Hex.decodeHex(key), "AES");
				Cipher cipher = getCipherInstance(Cipher.DECRYPT_MODE, keySpec);
				byte[] encryptedBytes = Base64.decodeBase64(content);
				byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
				return new String(decryptedBytes, StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		private static Cipher getCipherInstance(int mode, SecretKeySpec keySpec) {
			try {
				Cipher cipher = Cipher.getInstance(AesUtil.AES_CBC_PKCS5_PADDING);
				cipher.init(mode, keySpec, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes()));
				return cipher;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}


	public static void main(String[] paras) throws Exception {

//    FileOutputStream fos = new FileOutputStream("result-社保.txt");
//    System.setOut(new PrintStream(fos));
		String sss = "220581198902210780\t15867107088\t郑远芳\n";

		String[] ss = sss.split("\n");
		for (String s : ss) {
			String name = s.split("\t")[2];
			String idCard = s.split("\t")[0];
			test(name, idCard, s);
		}
	}

	public static void test(String name, String idCard, String ss)
			throws Exception {

		String ZK_ACCID = "ds20250214vhmjmj";
		String ZK_ENKEY = "c5725d879fa3da6c28e3dc2646a74349";
		String prod = "S0019";

//    String url = "http://122.224.147.153:13188/api/getData"; //zk-prod
		String url = "http://122.224.147.153:13199/api/getData"; //zk-test

		Map<String, Object> thirdInput = new HashMap(2);
		thirdInput.put("idCard", idCard);
		thirdInput.put("name", name);


		String dataValue = AesUtil.encode(JSON.toJSONString(thirdInput), ZK_ENKEY);
		System.out.println("入参data：" + thirdInput);

		OkHttpClient c = new OkHttpClient().newBuilder().readTimeout(200, HOURS).build();
		MediaType media = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(media, "{\n\"data\":\"" + dataValue + "\"\n}");
		Request request = new Request.Builder().url(url).method("POST", body)
				.addHeader("accountId", ZK_ACCID)
				.addHeader("prodId", prod).addHeader("Content-Type", "application/json").build();
		Response resp = c.newCall(request).execute();
		if (resp.code() == 200) {
			BaseResponse firResp = null;
			if (resp.body() != null) {
				firResp = JSON.parseObject(resp.body().string(), BaseResponse.class);
			}
			System.out.println("乙方返回：" + JSON.toJSONString(firResp));

		} else {
			System.out.println("解密后：" + resp);
		}
	}

}
