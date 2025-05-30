package com.mkc.api.common.constant.enums;

/**
 * 标准产品 code 枚举类
 *
 * @author tqlei
 * @date 2023/5/25 15:55
 */
public enum ProductCodeEum {

	CK_CRE_TWO("CK_002", "个人身份二要素认证", "/ck/certIDverify") {},

	CK_MOB_THREE_CT("CK_MOB_THREE_CT", "个人手机三要素认证-简版-电信", "/ck/mobileThree") {},
	CK_MOB_THREE_CM("CK_MOB_THREE_CM", "个人手机三要素认证-简版-移动", "/ck/mobileThree") {},
	CK_MOB_THREE_CU("CK_MOB_THREE_CU", "个人手机三要素认证-简版-联通", "/bg/mobileThree") {},
	CK_MOB_THREE_CB("CK_MOB_THREE_CB", "个人手机三要素认证-简版-广电", "/bg/mobileThree") {},

	CK_MOB_THREE_PLUS_CT("CK_MOB_THREE_PLUS_CT", "个人手机三要素认证-详版-电信", "/ck/mobileThreePlus") {},
	CK_MOB_THREE_PLUS_CM("CK_MOB_THREE_PLUS_CM", "个人手机三要素认证-详版-移动", "/ck/mobileThreePlus") {},
	CK_MOB_THREE_PLUS_CU("CK_MOB_THREE_PLUS_CU", "个人手机三要素认证-详版-联通", "/bg/mobileThreePlus") {},
	CK_MOB_THREE_PLUS_CB("CK_MOB_THREE_PLUS_CB", "个人手机三要素认证-详版-广电", "/bg/mobileThreePlus") {},

	CK_PERSON_CAR("CK_PERSONCAR_001", "人车核验", "/ck/personCarVerify") {},
	CK_WORK_UNIT("CK_WORKUNIT_001", "工作单位核验", "/ck/workUnitVerify") {},
	CK_POPULATION_THREE("CK_POPULATION_THREE", "全国⼈⼝身份信息三要素核验", "/ck/populationThree") {},
	CK_POPULATION_TWO("CK_POPULATION_TWO", "全国⼈⼝身份信息二要素核验", "/ck/populationTwo") {},
	CK_VEHICLE_LICENSE_INFO("CK_VEHICLE_LICENSE_001", "行驶证核验", "/ck/ckVehicleLicense") {},
	CK_BANK_FOUR("CK_BANK_FOUR_INFO_001", "银行卡四要素核验", "/ck/bankFour") {},
	CK_BANK_THREE("CK_BANK_THREE_INFO_001", "银行卡三要素核验", "/ck/bankThree") {},
	CK_BANK_TWO("CK_BANK_TWO_INFO_001", "银行卡二要素核验", "/ck/bankTwo") {},
	CK_PRO_QUALIFY_CERT_001("CK_PRO_QUALIFY_CERT_001", "技能人员职业资格证书核验", "/ck/pro_qualify_cert") {},
	CK_RESUME_VERIFY("CK_RESUME_VERIFY", "当前履历核验", "/ck/resume_verify") {},
	CK_ENTERPRISE_THREE_ELEMENTS("CK_ENTERPRISE_THREE_ELEMENTS", "企业三要素核验", "/ck/enterprise_three_elements"),
	CK_CURRENT_WORK("CK_CURRENT_WORK", "当前工作履历核验", "/ck/current_work"),

	BG_ANTI_FRAUD_V6("BG_ANTI_FRAUD_V6", "反欺诈评分V6", "/bg/anti_fraud_v6"),

	BG_PERSONAL_VEHICLE("BG_PERSONAL_VEHICLE", "个人名下车辆", "/bg/personal_vehicle"),

	BG_CORPORATE_APPOINTMENTS("BG_CORPORATE_APPOINTMENTS", "企业任职关联信息查询", "/bg/corporate_appointments"),

	BG_FINANCE_ICS_A("BG_FINANCE_ICS_A", "经济能力评级-青龙分", "/bg/finance_ics_a") {
	},

	BG_FINANCE_ICS_B("BG_FINANCE_ICS_B", "经济能力评级-白虎分", "/bg/finance_ics_b") {
	},

	BG_FINANCE_ICS_E("BG_FINANCE_ICS_E", "经济能力评级-朱雀分", "/bg/finance_ics_e") {
	},

	BG_FINANCE_ICS_F("BG_FINANCE_ICS_F", "经济能力评级-玄武分", "/bg/finance_ics_f") {
	},

	BG_FINANCE_I8("BG_FINANCE_I8", "I8还款能⼒评分", "/bg/finance_i8") {
	},

	BG_FINANCE_I9("BG_FINANCE_I9", "I9还款能⼒评分", "/bg/finance_i9") {
	},

	BG_PEOPLE_ENTERPRISE("BG_PEOPLE_ENTERPRISE", "人企", "/bg/people_enterprise") {
	},

	BG_CREDIT_A016("BG_CREDIT_A016", "网贷（逾期多头）", "/bg/credit_a016") {
	},

	BG_CREDIT_A107("BG_CREDIT_A107", "网贷（申请多头）", "/bg/credit_a107") {
	},

	BG_CREDIT_A108("BG_CREDIT_A108", "网贷（授信多头）", "/bg/credit_a108") {
	},

	BG_HIGH_RISK_PEOPLE("BG_HIGH_RISK_PEOPLE", "高风险人群核查", "/bg/kxdrz") {
	},

	BG_CAR_INFO("BG_CAR_001", "车五项信息查询", "/bg/carInfo") {
	},

	BG_CAR_FIVE_INFO("BG_CAR_002", "车五项信息查询", "/bg/carFiveInfo") {
	},

	BG_VEHICLE_LICENSE_INFO("BG_VEHICLE_LICENSE_001", "行驶证信息查询", "/bg/vehicleLicenseInfo") {
	},

	BG_ECONOMIC_RATE_INFO("BG_ECONOMIC_RATE_001", "经济能力评级", "/bg/economicRate") {
	},

	BG_HOUSE_INFO("BG_HOUSE_001", "不动产信息查询", "/bg/houseInfo") {
	},

	BG_HOUSE_RESULT_INFO("BG_HOUSE_002", "不动产结果信息查询", "/bg/houseResultInfo") {
	},

	BG_FINANCE_INFO("BG_FINANCE_001", "经济能力2W查询", "/bg/financeInfo") {
	},

	BG_FINANCE_INFO_V3("BG_FINANCE_002", "经济能力评级V3", "/bg/financeInfoV3") {
	},

	BG_FINANCE_INFO_V7("BG_FINANCE_003", "经济能力评级V7", "/bg/financeInfoV3") {
	},

	BG_SURE_SCORE_INFO("BG_SURE_SCORE_001", "确信分", "/bg/sureScoreInfo") {
	},

	BG_PERSON_CAR_INFO("BG_PERSON_CAR_001", "人车核验详版", "/bg/personCarInfo") {
	},

	BG_ENTERPRISE_FOUR_ELEMENT_INFO("BG_ENTERPRISE_FOUR_ELEMENT_001", "企业四要素", "/bg/enterpriseFourElementInfo") {
	},

	BG_EDUCATION_INFO("BG_EDUCATION_001", "全国高等学历信息查询", "/bg/educationInfo") {
	},

	BG_HIGH_SCHOOL_EDUCATION_INFO("BG_HIGH_SCHOOL_EDUCATION_001", "高校学历核查接口", "/bg/highSchoolEduInfo") {
	},

	BG_HIGH_SCHOOL_EDUCATION("BG_HIGH_SCHOOL_EDUCATION_002", "高校学历核查实时接口", "/bg/highSchoolEdu") {
	},

	BG_EDU_ASSESSMENT("BG_EDUCATION_002", "学历评估", "/bg/eduAssessment") {
	},

	BG_DRIVING_LICENSE_INFO("BG_DRIVING_LICENSE_001", "行驶身份核验", "/bg/drivingLicense") {
	},

	SF_SS_PLUS_INFO("SF_SS_PLUS_INFO", "司法涉诉公开版", "/sf/ssPlus") {
	},

	PERSON_LITIGATION("PERSON_LITIGATION", "全国自然人司法模型服务查询", "/sf/person_litigation") {
	},

	ENTERPRISE_LITIGATION("ENTERPRISE_LITIGATION", "全国企业司法模型服务查询", "/sf/enterprise_litigation") {
	},

	SF_DISHONEST_EXECUTIVE_INFO("SF_DISHONEST_EXECUTIVE_001", "失信被执行人", "/sf/dishonestExecutive") {
	},

	SF_RESTRICTED_CONSUMER_INFO("SF_RESTRICTED_CONSUMER_001", "限制高消费被执行人接口", "/sf/restrictedConsumer") {
	},

	BG_MARITAL_RELATIONSHIP("BG_MARRIAGE_001", "婚姻关系验证", "/bg/maritalRelationship") {
	},

	BG_MARITAL_STATUS("BG_MARRIAGE_002", "婚姻状况查询", "/bg/maritalStatus") {
	},

	BG_MARITAL_STABILITY("BG_MARRIAGE_003", "婚姻稳定状况查询", "/bg/maritalStability") {
	},

	UN_KNOW("UN_KNOWN", "未知", "");


	ProductCodeEum(String code, String name, String url) {
		this.code = code;
		this.name = name;
		this.url = url;
	}

	private String code;
	private String name;

	private String url;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return " ApiReturnCode{" + "code='" + code + '\'' + "name" + name + '\'' + '}';
	}

	/**
	 * 判断当前商品是否需要获取 实际运营商价格
	 *
	 * @param productCode
	 * @return
	 */
	public static boolean isGetYysInfo(String productCode) {

//		switch (ProductCodeEum.findByProductCode(productCode)) {
//			case CK_MOB_THREE:
//			case CK_MOB_THREE_PLUS:
//				return true;
//			default:
		return false;
//		}

	}

	/**
	 * 根据名字匹配枚举
	 *
	 * @param productCode
	 * @return 无的时候 默认返回移动
	 */
	public static ProductCodeEum findByProductCode(String productCode) {
		for (ProductCodeEum value : ProductCodeEum.values()) {
			if (value.getCode().equals(productCode)) {
				return value;
			}
		}
		//根据自身的业务 查不到可以返回null，或者抛出异常。
		return UN_KNOW;
	}


}
