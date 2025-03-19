package com.mkc.api.supplier.bg;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.ck.req.PersonalVehicleReqDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.supplier.dto.PersonalVehicleResDTO;
import com.mkc.api.supplier.dto.jhsj.JhsjBaseResDTO;
import com.mkc.api.supplier.dto.jhsj.JhsjPersonalVehicleResDTO;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.common.utils.bean.BeanUtils;
import com.mkc.util.JsonUtil;
import com.mkc.util.sm4.SM4;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 钜盒数据
 */
@Service("BG_JHSJ")
@Slf4j
public class JhsjBgSupImpl implements IBgSupService {

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

	@Override
	public SupResult<PersonalVehicleResDTO> personalVehicle(PersonalVehicleReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<PersonalVehicleResDTO> supResult = null;
		String url = null;
		long timestamp = System.currentTimeMillis();

		Map<String, String> headers = new HashMap<>();
		Map<String, String> params = new HashMap<>();
		try {
			url = bean.getUrl() + "/vehicle/info/personalVehicle";
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
			params.put("idCardNo", sm4.encryptDataToString_ECB(vo.getIdCard()));
			if (CharSequenceUtil.isNotBlank(vo.getName())) {
				params.put("fullName", sm4.encryptDataToString_ECB(vo.getName()));
			}
			params.put("userType", vo.getUserType());
			if (CharSequenceUtil.isNotBlank(vo.getVehicleType())) {
				params.put("vehicleType", sm4.encryptDataToString_ECB(vo.getVehicleType()));
			}

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
			JhsjBaseResDTO<JhsjPersonalVehicleResDTO> res = JsonUtil.fromJson(result, new TypeReference<JhsjBaseResDTO<JhsjPersonalVehicleResDTO>>() {
			});
			Code code = Code.getByCode(res.getCode());
			if (code != Code.SUCCESS) {
				throw new RuntimeException("供应商异常：" + code.getMessage());
			}

			log.info(CharSequenceUtil.format("【个人名下车辆】{}", result));
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
			switch (resultCode) {
				case "0":
					supResult.setFree(FreeStatus.YES);
					supResult.setState(ReqState.NOT_GET);
					supResult.setRemark("查询成功无结果");
					break;
				case "1":
					supResult.setFree(FreeStatus.YES);
					supResult.setState(ReqState.SUCCESS);
					res.getData().getList().forEach(v -> v.setPlateNum(sm4.decryptDataToString_ECB(v.getPlateNum())));
					PersonalVehicleResDTO resDTO = new PersonalVehicleResDTO();
					BeanUtils.copyProperties(res.getData(), resDTO);
					supResult.setData(resDTO);
					break;
				default:
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【钜盒数据】 个人名下车辆 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
