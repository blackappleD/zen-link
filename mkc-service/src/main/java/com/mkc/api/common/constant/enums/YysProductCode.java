package com.mkc.api.common.constant.enums;

/**
 * 运营商对应系统产品 枚举类
 */
public enum YysProductCode {


	//手机 三要素  产品Code  移动，联通，电信
//	THREE_CM(YysCode.CM.getCode(), ProductCodeEum.CK_MOB_THREE.getCode(), ProductCodeEum.CK_MOB_THREE_10086.getCode()),
//	THREE_CU(YysCode.CU.getCode(), ProductCodeEum.CK_MOB_THREE.getCode(), ProductCodeEum.CK_MOB_THREE_10010.getCode()),
//	THREE_CT(YysCode.CT.getCode(), ProductCodeEum.CK_MOB_THREE.getCode(), ProductCodeEum.CK_MOB_THREE_10000.getCode()),

//    //手机 二要素（手机号，证件号） 产品Code  移动，联通，电信
//    THREE_MC_CM(YysCode.CM.getCode(), ProductCodeEum.CK_MOB_TWO_MC.getCode(),ProductCodeEum.CK_MOB_TWO_MC_10086.getCode()),
//    THREE_MC_CU(YysCode.CU.getCode(), ProductCodeEum.CK_MOB_TWO_MC.getCode(),ProductCodeEum.CK_MOB_TWO_MC_10010.getCode()),
//    THREE_MC_CT(YysCode.CT.getCode(), ProductCodeEum.CK_MOB_TWO_MC.getCode(),ProductCodeEum.CK_MOB_TWO_MC_10000.getCode()),


	//运营商 手机在网状态  产品Code  移动，联通，电信
//    MOB_STATE_CM(YysCode.CM.getCode(), ProductCodeEum.YYS_STATE.getCode(),ProductCodeEum.YYS_STATE_10086.getCode()),
//    MOB_STATE_CU(YysCode.CU.getCode(), ProductCodeEum.YYS_STATE.getCode(),ProductCodeEum.YYS_STATE_10010.getCode()),
//    MOB_STATE_CT(YysCode.CT.getCode(), ProductCodeEum.YYS_STATE.getCode(),ProductCodeEum.YYS_STATE_10000.getCode()),
//
//
//    MOB_ONLINE_CM(YysCode.CM.getCode(), ProductCodeEum.YYS_ONLINE.getCode(),ProductCodeEum.YYS_ONLINE_10086.getCode()),
//    MOB_ONLINE_CU(YysCode.CU.getCode(), ProductCodeEum.YYS_ONLINE.getCode(),ProductCodeEum.YYS_ONLINE_10010.getCode()),
//    MOB_ONLINE_CT(YysCode.CT.getCode(), ProductCodeEum.YYS_ONLINE.getCode(),ProductCodeEum.YYS_ONLINE_10000.getCode()),

	;


	public static YysProductCode findByCode(String yysCode, String mobReqProductCode) {
		for (YysProductCode value : YysProductCode.values()) {
			if (value.getYysCode().equals(yysCode) && value.getMobReqProductCode().equals(mobReqProductCode)) {
				return value;
			}
		}
		//根据自身的业务 查不到可以返回null，或者抛出异常。
		return null;
	}


	/**
	 * 运营商code  CM-移动  CU-联通 CT-电信
	 **/
	private String yysCode;

	/**
	 * 对应手机三要素要素 总产品code
	 */
	private String mobReqProductCode;

	/**
	 * 对应手机三要素要素 运营商产品code
	 */
	private String mobThreeYysProductCode;


	YysProductCode(String yysCode, String mobReqProductCode, String mobThreeYysProductCode) {
		this.yysCode = yysCode;
		this.mobReqProductCode = mobReqProductCode;
		this.mobThreeYysProductCode = mobThreeYysProductCode;
	}

	public String getYysCode() {
		return yysCode;
	}

	public void setYysCode(String yysCode) {
		this.yysCode = yysCode;
	}

	public String getMobReqProductCode() {
		return mobReqProductCode;
	}

	public void setMobReqProductCode(String mobReqProductCode) {
		this.mobReqProductCode = mobReqProductCode;
	}

	public String getMobThreeYysProductCode() {
		return mobThreeYysProductCode;
	}

	public void setMobThreeYysProductCode(String mobThreeYysProductCode) {
		this.mobThreeYysProductCode = mobThreeYysProductCode;
	}
}
