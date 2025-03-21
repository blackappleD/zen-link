package com.mkc.api.supplier.ck;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.exception.ApiSupException;
import com.mkc.api.dto.ck.req.CurrentWorkReqDTO;
import com.mkc.api.dto.ck.req.ResumeVerifyReqDTO;
import com.mkc.api.dto.ck.res.CurrentWorkResDTO;
import com.mkc.api.dto.ck.res.ResumeVerifyResDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.supplier.bg.ZKZTBgSupImpl;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 中科智通
 */
@Service("CK_ZKZT")
@Slf4j
public class ZKZTCkSupImpl implements ICkSupService {


	@Data
	public static class BusinessBody {
		private String data;

		private String code;

		private String uuid;

		private String retMsg;

	}

	@Override
	public SupResult<CurrentWorkResDTO> currentWork(CurrentWorkReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<CurrentWorkResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl();
			String account = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();

			Map<String, String> headers = new HashMap<>();
			headers.put("accountId", account);
			headers.put("prodId", "CUR-WOK-001");
			headers.put("Content-Type", "application/json");

			params.put("idCard", dto.getIdCard());
			params.put("name", dto.getName());
			params.put("companyName", dto.getCompanyName());
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

			ZKZTBgSupImpl.BusinessBody businessBody = JsonUtil.fromJson(result, ZKZTBgSupImpl.BusinessBody.class);
			String code = businessBody.getCode();
			if (!"200".equals(code)) {
				supResult.setRemark("查询失败: " + businessBody.getRetMsg());
				supResult.setFree(FreeStatus.NO);

			} else {
				supResult.setData(JsonUtil.fromJson(AesUtil.decode(businessBody.getData(), signPwd), CurrentWorkResDTO.class));
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
	public SupResult<ResumeVerifyResDTO> ckResumeVerify(ResumeVerifyReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<ResumeVerifyResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl();
			String account = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();

			Map<String, String> headers = new HashMap<>();
			headers.put("accountId", account);
			headers.put("prodId", "S0021");
			headers.put("Content-Type", "application/json");

			params.put("idCard", dto.getIdCard());
			params.put("name", dto.getName());
			params.put("companyName", dto.getCompanyName());
			params.put("authCode", dto.getAuthCode());
			if (dto.getAuthCode() != null) {
				params.put("countryCode", dto.getCountryCode());
			}
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

			ZKZTBgSupImpl.BusinessBody businessBody = JsonUtil.fromJson(result, ZKZTBgSupImpl.BusinessBody.class);
			String code = businessBody.getCode();
			if (!"200".equals(code)) {
				supResult.setRemark("查询失败: " + businessBody.getRetMsg());
				supResult.setFree(FreeStatus.NO);

			} else {
				supResult.setData(JsonUtil.fromJson(AesUtil.decode(businessBody.getData(), signPwd), ResumeVerifyResDTO.class));
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


}
