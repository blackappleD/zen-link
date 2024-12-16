package com.mkc.api.supplier.ck;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.supplier.dto.BankFourResDTO;
import com.mkc.api.vo.ck.BankReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 广州亿宏
 *
 * @author xiewei
 * @date 2024/08/02 17:10
 */
@Service("CK_GZYH")
@Slf4j
@SuppressWarnings("rawtypes")
public class YhCkSupImpl implements ICkSupService {

	@AllArgsConstructor
	@Getter
	public enum BusinessCode {
		VERIFICATION_SUCCESS("00000000", "验证通过"),
		VERIFICATION_FAILED("B0000001", "验证不通过"),
		INVALID_CARD_NUMBER("B0000002", "无效卡号或卡状态异常，请换卡重试或联系发卡行"),
		RESTRICTED_CARD("B0000003", "受限制的卡，请换卡重试或联系发卡行"),
		ABNORMAL_CARD_STATUS("B0000004", "卡状态异常，请换卡重试或联系发卡行"),
		TRANSACTION_FAILED("B0000607", "交易失败，详情请咨询您的发卡行"),
		SYSTEM_MAINTENANCE("S0000002", "系统维护期间，请稍后重试"),
		NETWORK_EXCEPTION("S0000003", "网络异常，请稍后重试"),
		TRANSACTION_TIMEOUT("S0000004", "交易超时，请稍后重试"),
		LIMIT_FLOW_OR_FREQUENT("S0000005", "交易限流或过于频繁，请稍后重试"),
		ILLEGAL_REQUEST("S0000011", "非法请求"),
		REQUEST_FORMAT_ERROR("S0000012", "请求报文格式有误"),
		MISSING_REQUIRED_PARAMS("S0000013", "请求报文必填参数缺失"),
		REQUEST_PARAMS_ERROR("S0000014", "请求报文参数有误"),
		PARAM_LENGTH_LIMIT("S0000015", "请求报文有字段长度超限"),
		DECRYPTION_FAILED("S0000016", "报文解密失败"),
		SIGNATURE_FAILED("S0000017", "请求报文签名失败"),
		ENCRYPTION_FAILED("S0000018", "响应报文签名失败"),
		ENCRYPTION_ERROR("S0000019", "响应报文加密失败"),
		TRANSACTION_DUPLICATE("S0000020", "交易流水号重复"),
		TRANSACTION_LIMIT_EXCEEDED("S0000021", "交易次数超限，请隔日重试"),
		BANK_RESTRICTION("S0000101", "交易银行受限，请联系平台"),
		SYSTEM_TIMEOUT_OR_EXCEPTION("0014", "系统超时或异常"),
		SIGNATURE_EXPIRED("0017", "超时签名"),
		MISSING_REQUIRED_FIELDS("0016", "缺少必传字段"),
		IP_RESTRICTION("0018", "IP受限"),
		IP_ADDRESS_MISMATCH("0019", "IP地址和商户号不匹配"),
		ILLEGAL_SIGNATURE("0021", "非法签名"),
		INVALID_APP_ID("0022", "AppId调用接口查无此权限"),
		WRONG_PARAM_SIGNATURE("0023", "签名参数有误"),
		BALANCE_INSUFFICIENT("2020", "调用失败，您的余额不足!"),
		WRONG_SIGNATURE_PARAMS("2001", "签名参数有误"),
		WRONG_SIGNATURE("2006", "签名错误"),
		UNKNOWN("1", "未知异常");
		private final String code;
		private final String message;

		public static BusinessCode getByCode(String code) {
			for (BusinessCode c : BusinessCode.values()) {
				if (c.getCode().equals(code)) {
					return c;
				}
			}
			return UNKNOWN; // 或抛出异常，取决于你的需求
		}

	}


	@Override
	public SupResult ckBankFour(BankReqVo vo, SuplierQueryBean bean) {
		String result = null;
		SupResult supResult = null;
		String url = null;
		Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		LocalDateTime now = LocalDateTime.now();

		try {
			url = bean.getUrl() + "/platform/bankSimple/checkBankSimple";
			params.put("appid", bean.getSignKey());
			params.put("cardNo", vo.getBankCard());
			params.put("realName", vo.getCertName());
			params.put("bizType", "010103");
			params.put("idNo", vo.getCertNo());
			params.put("phone", vo.getMobile());
			params.put("serviceCode", "BHSJBANK4");
			params.put("timestamp", String.valueOf(now.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()));
			params.put("requestId", vo.getMerSeq());
			params.put("sigval", LoyalCryptUtils.cryptMd5(params, bean.getSignPwd()));
			String jsonStr = JSONUtil.toJsonStr(params);
			supResult = new SupResult(jsonStr, now);
			try (HttpResponse response = HttpUtil.createPost(url)
					.header("Content-Type", "application/json")
					.body(jsonStr)
					.timeout(bean.getTimeOut())
					.execute()) {
				result = response.body();
			}
			log.info(CharSequenceUtil.format("【银行四要素核验返回体】{}", result));
			supResult.setRespJson(result);
			if (CharSequenceUtil.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				return supResult;
			}
			JSONObject responseObj = JSONObject.parseObject(result);
			supResult.setRespTime(LocalDateTime.now());
			String code = responseObj.getString("code");
			BusinessCode businessCode = BusinessCode.getByCode(code);
			switch (businessCode) {
				case VERIFICATION_SUCCESS:
				case VERIFICATION_FAILED:
					supResult.setRemark(businessCode.getMessage());
					supResult.setState(ReqState.SUCCESS);
					supResult.setFree(FreeStatus.YES);
					break;
				default:
					supResult.setState(ReqState.ERROR);
					supResult.setFree(FreeStatus.NO);
					supResult.setData(businessCode.getMessage());
			}
			supResult.setRemark(businessCode.getMessage());
			supResult.setData(new BankFourResDTO("2", businessCode.getCode(), businessCode.getMessage()));
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【{}】 银行卡四要素 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getSupName(), bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult(JSONUtil.toJsonStr(params), now);
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}

	private static class LoyalCryptUtils {

		public static String cryptMd5(Map<String, String> source, String appKey) {
			StringBuilder sb = new StringBuilder();
			String[] keyArr = source.keySet().toArray(new String[0]);
			Arrays.sort(keyArr);
			String tmp;
			for (String key : keyArr) {
				tmp = source.get(key);
				if (null != tmp && !tmp.isEmpty()) {
					sb.append(tmp);
				}
			}
			System.out.println("对key排序后value拼接成的值：" + sb);
			System.out.println("签名01：" + cryptMd5(sb.toString(), appKey));
			// System.out.println("签名02：" + Md5Crypt.md5Crypt(sb.toString().getBytes(StandardCharsets.UTF_8), appKey));
			return cryptMd5(sb.toString(), appKey);
		}

		public static String cryptMd5(String source, String key) {
			byte[] k_ipad = new byte[64];
			byte[] k_opad = new byte[64];
			byte[] keyb = key.getBytes(StandardCharsets.UTF_8);
			byte[] value = source.getBytes(StandardCharsets.UTF_8);
			Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
			Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
			for (int i = 0; i < keyb.length; i++) {
				k_ipad[i] = (byte) (keyb[i] ^ 0x36);
				k_opad[i] = (byte) (keyb[i] ^ 0x5c);
			}
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			md.update(k_ipad);
			md.update(value);
			byte[] dg = md.digest();
			md.reset();
			md.update(k_opad);
			md.update(dg, 0, 16);
			dg = md.digest();
			return toHex(dg);
		}

		private static String toHex(byte[] input) {
			if (input == null)
				return null;
			StringBuilder output = new StringBuilder(input.length * 2);
			for (byte b : input) {
				int current = b & 0xff;
				if (current < 16)
					output.append("0");
				output.append(Integer.toString(current, 16));
			}
			return output.toString();
		}

	}
}
