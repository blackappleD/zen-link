package com.mkc.api.supplier.ck;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.res.BankElementCheckResDTO;
import com.mkc.api.dto.ck.req.BankReqDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.supplier.dto.jhsj.JhsjBankFourResDTO;
import com.mkc.api.supplier.enums.BankFourCode;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.JsonUtil;
import com.mkc.util.sm4.SM4;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 钜盒数据
 */
@Service("CK_JHSJ")
@Slf4j
public class JhsjCkSupImpl implements ICkSupService {

	@Data
	public static class BaseResDTO<T> {

		private int code;
		private String msg;
		private T data;

	}

	@AllArgsConstructor
	@Getter
	public enum Code {
		SUCCESS(200, "成功"),
		MISSING_HEADER(1000, "缺少必要参数（Header）"),
		SIGNATURE_TIMEOUT(1001, "签名时间超过规定时间"),
		ACCOUNT_EXCEPTION(1002, "账户异常"),
		SIGNATURE_VERIFICATION_FAILED(1003, "验签失败"),
		IP_NOT_WHITELISTED(1004, "请求 IP 不在白名单内"),
		PRODUCT_UNAVAILABLE(1005, "未购买此产品或者购买的额度已经用完"),
		TOTAL_CALLS_EXCEEDED(1006, "总调用次数超限"),
		INSUFFICIENT_BALANCE(1007, "账户余额不足"),
		DUPLICATE_SERIAL_NUMBER(1008, "流水号重复"),
		PARAMETER_PARSE_ERROR(1009, "参数异常-解析失败"),
		ORDER_PRODUCT_CONFIG_ERROR(1010, "订单产品配置异常"),
		SYSTEM_ERROR(1011, "系统错误"),
		NO_DATA_FOUND(1012, "查询无数据"),
		CARD_VERIFICATION_LIMIT_EXCEEDED(1014, "同一张卡 24 小时内验证次数不能超过 5 次");;

		private final int code;
		private final String message;

		public static Code getByCode(int code) {
			for (Code c : Code.values()) {
				if (c.getCode() == code) {
					return c;
				}
			}
			return SYSTEM_ERROR;
		}
	}

	@AllArgsConstructor
	@Getter
	public enum BusinessCode {
		VERIFICATION_SUCCESS("12", BankFourCode.VERIFICATION_SUCCESS, "验证通过"),
		VERIFICATION_FAILED("14", BankFourCode.VERIFICATION_FAILED, "验证不通过"),
		INVALID_CARD_NUMBER("13", BankFourCode.INVALID_CARD, "无效卡号或卡状态异常，请换卡重试或联系发卡行"),
		RESTRICTED_CARD("18", BankFourCode.CARD_STATUS_ABNORMAL, "受限制的卡，请换卡重试或联系发卡行"),
		ABNORMAL_CARD_STATUS("17", BankFourCode.CARD_STATUS_ABNORMAL, "卡状态异常，请换卡重试或联系发卡行"),
		TRANSACTION_FAILED("19", BankFourCode.TRANSACTION_FAILED, "交易失败，详情请咨询您的发卡行"),
		LIMIT_FLOW_OR_FREQUENT("16", BankFourCode.TRANSACTION_TOO_FREQUENT, "交易限流或过于频繁，请稍后重试"),
		TRANSACTION_LIMIT_EXCEEDED("15", BankFourCode.TRANSACTION_TOO_FREQUENT, "交易次数超限，请隔日重试"),
		UNKNOWN("99", BankFourCode.ILLEGAL_REQUEST, "未知异常");
		private final String code;
		private final BankFourCode bankFourCode;
		private final String desc;

		public static BusinessCode getByCode(String code) {
			for (BusinessCode c : BusinessCode.values()) {
				if (c.getCode().equals(code)) {
					return c;
				}
			}
			return UNKNOWN; // 或抛出异常，取决于你的需求
		}

		public String getBankFourCode() {
			return bankFourCode == null ? code : bankFourCode.getCode();
		}

		public String getBankFourDesc() {
			return bankFourCode == null ? code : bankFourCode.getMessage();
		}
	}

	@Override
	public SupResult<BankElementCheckResDTO> ckBankFour(BankReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<BankElementCheckResDTO> supResult = null;
		String url = null;
		long timestamp = System.currentTimeMillis();

		Map<String, String> headers = new HashMap<>();
		Map<String, String> params = new HashMap<>();
		try {
			url = bean.getUrl() + "/bankCard/info/hverifyV2";
			String appId = bean.getSignKey();
			String appSecret = bean.getSignPwd();
			String sm4Key = appSecret.substring(0, 16);
			Integer timeOut = bean.getTimeOut();
			String nonceStr = RandomUtil.randomString(16);
			String sign = Md5Utils.md5(CharSequenceUtil.format("appId={}&nonceStr={}&timestamp={}{}", appId, nonceStr, timestamp, appSecret)).toUpperCase();

			headers.put("appId", appId);
			headers.put("timestamp", String.valueOf(timestamp));
			headers.put("nonceStr", nonceStr);
			headers.put("sign", sign);
			headers.put("Content-Type", "application/json");

			SM4 sm4 = new SM4(sm4Key);
			params.put("requestId", vo.getMerSeq());
			params.put("fullName", sm4.encryptDataToString_ECB(vo.getCertName()));
			params.put("idCardNo", sm4.encryptDataToString_ECB(vo.getCertNo()));
			params.put("mobile", sm4.encryptDataToString_ECB(vo.getMobile()));
			params.put("bankCardNo", sm4.encryptDataToString_ECB(vo.getBankCard()));

			String paramJson = JSONUtil.toJsonStr(params);
			supResult = new SupResult<>(paramJson, LocalDateTime.now());
			try (HttpResponse response = HttpUtil.createPost(url)
					.headerMap(headers, false)
					.body(paramJson)
					.timeout(timeOut)
					.execute()) {
				result = response.body();
				if (response.getStatus() != 200) {
					throw new RuntimeException(result);
				}
			}
			BaseResDTO<JhsjBankFourResDTO> res = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<JhsjBankFourResDTO>>() {
			});
			Code code = Code.getByCode(res.getCode());
			if (code != Code.SUCCESS) {
				throw new RuntimeException("供应商异常：" + code.getMessage());
			}

			log.info(CharSequenceUtil.format("【银行四要素核验返回体】{}", result));
			supResult.setRespJson(result);
			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			supResult.setRespTime(LocalDateTime.now());
			String resultCode = res.getData().getResultCode();
			supResult.setSupCode(resultCode);
			BusinessCode businessCode = BusinessCode.getByCode(resultCode);
			switch (businessCode) {
				case LIMIT_FLOW_OR_FREQUENT:
				case TRANSACTION_LIMIT_EXCEEDED:
				case UNKNOWN:
					supResult.setFree(FreeStatus.NO);
					supResult.setState(ReqState.SUCCESS);
					break;
				default:
					supResult.setFree(FreeStatus.YES);
					supResult.setState(ReqState.SUCCESS);
			}
			supResult.setData(new BankElementCheckResDTO("2", businessCode.getBankFourCode(), businessCode.getBankFourDesc()));
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【钜盒数据】 银行卡四要素 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult<>(JSONUtil.toJsonStr(params), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}
}
