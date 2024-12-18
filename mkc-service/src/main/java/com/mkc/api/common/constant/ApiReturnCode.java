package com.mkc.api.common.constant;

/**
 * @author tqlei
 * @date 2023/5/15 9:45
 */

public enum ApiReturnCode {

	SUCCESS("200", "成功") {},//成功 或 一致
	NOT("403", "认证信息不一致") {},//不一致
	NO("404", "未匹配到相关数据") {},//查无
	FAIL("500", "查询失败") {},//查询失败

	ERR_999("ERR_999", "系统繁忙，请稍候再试") {},//系统错误
	ERR_001("ERR_001", "缺少必输参数") {},
	ERR_002("ERR_002", "商户账户已停用或不存在") {},
	ERR_003("ERR_003", "商户账户余额不足") {},
	ERR_004("ERR_004", "该产品接口未开通或已停用") {},
	ERR_005("ERR_005", "该 ip 没有访问权限") {},
	ERR_006("ERR_006", "签名错误") {},
	ERR_007("ERR_007", "处理超时，请稍后再试") {},
	ERR_008("ERR_008", "令牌错误") {},

	ERR_009("ERR_009", "参数错误或无效") {},

	;


	ApiReturnCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return " ApiReturnCode{" + "code='" + code + '\'' + "msg='" + msg + '\'' + '}';
	}


}
