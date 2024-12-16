package com.mkc.api.supplier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/16 15:34
 */
@Getter
@AllArgsConstructor
public enum BankFourCode {

	VERIFICATION_SUCCESS("0000", "验证通过"),
	RESPONSE_TIMEOUT("1302", "发卡行响应超时，请稍后重试"),
	SYSTEM_EXCEPTION("1399", "发卡行系统异常，请稍后重试"),
	CHANNEL_SYSTEM_EXCEPTION("2208", "渠道方系统异常，请稍后重试"),
	NO_SUCH_CARD("2314", "发卡行无此卡号"),
	TRANSACTION_FAILED("2315", "交易失败，建议持卡人与发卡行联系"),
	CARD_STATUS_ABNORMAL("2316", "发卡行返回该卡状态不正常，建议持卡人与发卡行联系"),
	VERIFICATION_FAILED("2319", "户名、证件信息或手机号等验证失败"),
	PASSWORD_ATTEMPT_EXCEEDED("2320", "发卡行返回该卡密码错次数超限，建议持卡人与发卡行联系"),
	CARD_NOT_VERIFIED("2325", "发卡行返回该卡不支持验证，建议持卡人与发卡行联系"),
	SYSTEM_EXCEPTION_RETRY("2329", "系统异常，请稍后重试"),
	VERIFICATION_ATTEMPT_EXCEEDED("2334", "发卡行返回该卡验证次数已超限，请明日再试"),
	NO_RESERVED_PHONE("2344", "发卡行返回该卡未预留手机号，建议持卡人与发卡行联系"),
	INVALID_CARD("4001", "无效卡"),
	INVALID_IDENTITY_TYPE("4002", "无效证件类型"),
	INVALID_IDENTITY_NUMBER("4003", "无效证件号"),
	INVALID_PHONE("4004", "无效手机号"),
	INVALID_NAME("4005", "无效姓名"),
	MULTIPLE_FORMAT_ERRORS("4006", "多种要素格式错误"),
	RISK_ELEMENT_FORMAT_ERROR("4007", "风险要素信息格式错误"),
	TRANSACTION_TOO_FREQUENT("5101", "该卡交易过于频繁，请稍后重试"),
	IDENTITY_TOO_FREQUENT("5102", "该证件号交易过于频繁，请稍后重试"),
	CARD_VERIFICATION_FAILED("5103", "该卡今日验证失败次数过多，请明日重试"),
	IDENTITY_VERIFICATION_FAILED("5104", "该证件号今日验证失败次数过多，请明日重试"),
	CARD_DUPLICATED_TRANSACTION("5105", "短期内有同卡重复交易，请稍后重试"),
	CARD_VERIFICATION_TOO_MANY("5106", "该卡今日验证次数过多，请明日重试"),
	DAILY_VERIFICATION_LIMIT("5108", "当日验证次数已达最大值，请明日再试"),
	SERVICE_NOT_FOR_UNDER_14("5111", "认证服务不对小于14周岁的人提供服务");

	private final String code;
	private final String message;


}
