package com.mkc.controller;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.dto.bg.req.*;
import com.mkc.api.dto.bg.res.*;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.service.IBgService;
import com.mkc.bean.CkMerBean;
import com.mkc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 报告信息查询相关类入口
 *
 * @author tqlei
 * @date 2023/5/29 17:45
 */

@Slf4j
@RestController
@RequestMapping("/bg")
public class BgController extends BaseController {

	@Autowired
	private IBgService bgService;

	/**
	 * 经济能力评级-青龙分
	 *
	 * @return
	 */
	@PostMapping("/finance_ics_a")
	public Result<FinanceIcsResDTO> financeIcsA(HttpServletRequest request,
	                                            @RequestBody @Valid FinanceIcsReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_FINANCE_ICS_A);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.financeIcsA(params, merLog);

	}

	/**
	 * 经济能力评级-白虎分
	 *
	 * @return
	 */
	@PostMapping("/finance_ics_b")
	public Result<FinanceIcsResDTO> financeIcsB(HttpServletRequest request,
	                                            @RequestBody @Valid FinanceIcsReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_FINANCE_ICS_B);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.financeIcsB(params, merLog);

	}

	/**
	 * 经济能力评级-朱雀分
	 *
	 * @return
	 */
	@PostMapping("/finance_ics_e")
	public Result<FinanceIcsResDTO> financeIcsE(HttpServletRequest request,
	                                            @RequestBody @Valid FinanceIcsReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_FINANCE_ICS_E);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.financeIcsE(params, merLog);

	}

	/**
	 * 经济能力评级-玄武分
	 *
	 * @return
	 */
	@PostMapping("/finance_ics_f")
	public Result<FinanceIcsResDTO> financeIcsF(HttpServletRequest request,
	                                            @RequestBody @Valid FinanceIcsReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_FINANCE_ICS_F);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.financeIcsF(params, merLog);

	}

	/**
	 * I8还款能⼒评分
	 *
	 * @return
	 */
	@PostMapping("/finance_i8")
	public Result<FinanceI8ResDTO> financeI8(HttpServletRequest request,
	                                         @RequestBody @Valid FinanceI8ReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_FINANCE_I8);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryFinanceI8(params, merLog);

	}

	/**
	 * I9还款能⼒评分
	 *
	 * @return
	 */
	@PostMapping("/finance_i9")
	public Result<FinanceI9ResDTO> financeI9(HttpServletRequest request,
	                                         @RequestBody @Valid FinanceI9ReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_FINANCE_I9);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryFinanceI9(params, merLog);

	}

	/**
	 * 人企
	 *
	 * @return
	 */
	@PostMapping("/people_enterprise")
	public Result<PeopleEnterpriseResDTO> peopleEnterprise(HttpServletRequest request, @RequestBody @Valid PeopleEnterpriseReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_PEOPLE_ENTERPRISE);
		ckMerBean.setPlaintext(params.getMerCode() + params.getQueryCode() + params.getEncryptMethod());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryPeopleEnterprise(params, merLog);

	}

	/**
	 * 高风险人群核查
	 */
	@PostMapping("/kxdrz")
	public Result<HighRiskPeopleResDTO> highRiskPeople(HttpServletRequest request, @RequestBody HighRiskPeopleReqDTO params) {
		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_HIGH_RISK_PEOPLE)
				.plaintext(params.getMerCode() + params.getCertNo());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryHighRiskPeople(params, merLog);
	}


	@PostMapping("/credit_a108")
	public Result creditA108(HttpServletRequest request, @RequestBody CreditA108ReqDTO params) {
		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_CREDIT_A108)
				.plaintext(params.getMerCode() + params.getCid() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryCreditA108(params, merLog);
	}

	@PostMapping("/credit_a107")
	public Result creditA107(HttpServletRequest request, @RequestBody CreditA107ReqDTO params) {
		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_CREDIT_A107)
				.plaintext(params.getMerCode() + params.getCid() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryCreditA107(params, merLog);
	}

	@PostMapping("/credit_a016")
	public Result creditA016(HttpServletRequest request, @RequestBody CreditA016ReqDTO params) {
		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.BG_CREDIT_A016)
				.plaintext(params.getMerCode() + params.getCid() + params.getName() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return bgService.queryCreditA016(params, merLog);
	}

	/**
	 * 高校学历核查结果查询接口
	 */
	@PostMapping("/highSchoolEduResultInfo")
	public Result highSchoolEducationResultInfo(HttpServletRequest request, @RequestBody HighSchoolEducationResultInfoReqVo params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckHighSchoolEduResultInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryHighSchoolEducationResultInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【高校学历核查接口】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckHighSchoolEduResultInfoParams(HighSchoolEducationResultInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String reqOrderNo = params.getReqOrderNo();

		if (StringUtils.isBlank(reqOrderNo)) {
			log.error("缺少参数 reqOrderNo {} , merCode： {}", reqOrderNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		String plaintext = merCode + reqOrderNo;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	/**
	 * 高校学历核查接口
	 */
	@PostMapping("/highSchoolEduInfo")
	public Result highSchoolEducationInfo(HttpServletRequest request, @RequestBody HighSchoolEducationInfoReqVo params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckHighSchoolEduInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryHighSchoolEducationInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【高校学历核查接口】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckHighSchoolEduInfoParams(HighSchoolEducationInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String xm = params.getXm();
		String zsbh = params.getZsbh();

		if (StringUtils.isBlank(zsbh)) {
			log.error("缺少参数 zsbh {} , merCode： {}", zsbh, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(xm)) {
			log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		String plaintext = merCode + xm + zsbh;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	/**
	 * 高校学历核查实时接口
	 */
	@PostMapping("/highSchoolEdu")
	public Result highSchoolEdu(HttpServletRequest request, @RequestBody HighSchoolEducationInfoReqVo params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckHighSchoolEduInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryHighSchoolEducation(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【高校学历核查实时接口】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 婚姻稳定
	 */
	@PostMapping("/maritalStability")
	public Result maritalStability(HttpServletRequest request, @RequestBody MaritalStabilityReqVo params) {

		String reqJson = null;

		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckMarriageStabilityParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_MARITAL_STABILITY.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.ckMarriageStabilityParams(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【婚姻稳定状况接口】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckMarriageStabilityParams(MaritalStabilityReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String xm = params.getXm();
		String sfzh = params.getSfzh();

		if (StringUtils.isBlank(xm)) {
			log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(sfzh)) {
			log.error("缺少参数 sfzh {} , merCode： {}", sfzh, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		String plaintext = merCode + xm + sfzh;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	/**
	 * 婚姻状况
	 */
	@PostMapping("/maritalStatus")
	public Result maritalStatus(HttpServletRequest request, @RequestBody MarriageInfoReqInfo params) {

		String reqJson = null;

		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckMarriageStatusParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_MARITAL_STATUS.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryMaritalStatus(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【婚姻状况接口】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckMarriageStatusParams(MarriageInfoReqInfo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String sfzh = params.getSfzh();
		String xm = params.getXm();

		if (StringUtils.isBlank(sfzh)) {
			log.error("缺少参数 sfzh {} , merCode： {}", sfzh, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(xm)) {
			log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		String plaintext = merCode + xm + sfzh;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	/**
	 * 婚姻关系核验
	 */
	@PostMapping("/maritalRelationship")
	public Result maritalRelationship(HttpServletRequest request, @RequestBody MaritalRelationshipReqVo params) {

		String reqJson = null;

		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckMarriageRelationshipParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_MARITAL_RELATIONSHIP.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryMaritalRelationship(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【婚姻状况接口】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckMarriageRelationshipParams(MaritalRelationshipReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String manIdcard = params.getManIdcard();
		String manName = params.getManName();
		String womanIdcard = params.getWomanIdcard();
		String womanName = params.getWomanName();

		if (StringUtils.isBlank(manIdcard)) {
			log.error("缺少参数 manIdCard {} , merCode： {}", manIdcard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(manName)) {
			log.error("缺少参数 manName {} , merCode： {}", manName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(womanIdcard)) {
			log.error("缺少参数 womanIdcard {} , merCode： {}", womanIdcard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(womanName)) {
			log.error("缺少参数 womanName {} , merCode： {}", womanName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		String plaintext = merCode + manIdcard + manName + womanIdcard + womanName;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}


	/**
	 * 教育评估
	 */
	@PostMapping("/eduAssessment")
	public Result eduAssessment(HttpServletRequest request, @RequestBody EducationInfoReqVo params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckEducationInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_EDU_ASSESSMENT.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryEduAssessment(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【教育评估】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}


	/**
	 * 全国高等学历信息查询
	 */
	@PostMapping("/educationInfo")
	public Result educationInfo(HttpServletRequest request, @RequestBody EducationInfoReqVo params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckEducationInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_EDUCATION_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryEducationInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【全国高等学历信息查询】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckEducationInfoParams(EducationInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String xm = params.getXm();
		String zjhm = params.getZjhm();
		if (StringUtils.isBlank(zjhm)) {
			log.error("缺少参数 zjhm {} , merCode： {}", zjhm, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(xm)) {
			log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + xm + zjhm;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}


	/**
	 * 行驶身份核验
	 */
	@PostMapping("/drivingLicense")
	public Result drivingLicense(HttpServletRequest request, @RequestBody DrivingLicenseReqVo params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);
			//检查商户参数完整性
			CkMerBean ckMerBean = ckDrivingLicenseParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_DRIVING_LICENSE_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryDrivingLicenseInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【行驶身份核验】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}


	/**
	 * 经济能力评级---社保
	 */
	@PostMapping("/economicRate")
	public Result economicRate(HttpServletRequest request, @RequestBody EconomicRateReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);
			//检查商户参数完整性
			CkMerBean ckMerBean = ckEconomicRateParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_ECONOMIC_RATE_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryEconomicRateInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【经济能力评级】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}


	/**
	 * 企业四要素
	 *
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/enterpriseFourElementInfo")
	public Result enterpriseFourElementInfo(HttpServletRequest request, @RequestBody EnterpriseFourElementsReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckEnterpriseFourElementsParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_ENTERPRISE_FOUR_ELEMENT_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryFourElementsInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【企业四要素】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}


	}


	/**
	 * 人车核验详版
	 */
	@PostMapping("/personCarInfo")
	public Result personCarInfo(HttpServletRequest request, @RequestBody PersonCarDetailReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckPersonCarDetailInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_PERSON_CAR_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryPersonCarInfo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【人车核验详版】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}


	/**
	 * 确信分
	 */
	@PostMapping("/sureScoreInfo")
	public Result sureScoreInfo(HttpServletRequest request, @RequestBody SureScoreInfoReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckSureScoreInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_SURE_SCORE_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.querySureScoreInfo(params, merLog);


		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【确信分】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 经济能力2W
	 */
	@PostMapping("/financeInfo")
	public Result financeInfo(HttpServletRequest request, @RequestBody FinanceInfoReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckfinanceInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_FINANCE_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryFinanceInfo(params, merLog);

		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【经济能力2W】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 经济能力评级V3
	 */
	@PostMapping("/financeInfoV3")
	public Result financeInfoV3(HttpServletRequest request, @RequestBody FinanceInfoV3ReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckfinanceInfoV3Params(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_FINANCE_INFO_V3.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryFinanceInfoV3(params, merLog);

		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【经济能力评级V3】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 经济能力评级V7
	 */
	@PostMapping("/financeInfoV7")
	public Result financeInfoV7(HttpServletRequest request, @RequestBody FinanceInfoV3ReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckfinanceInfoV3Params(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_FINANCE_INFO_V7.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryFinanceInfoV7(params, merLog);

		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【经济能力评级V7】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}


	/**
	 * 不动产信息核查
	 */
	@PostMapping("/houseReqInfo")
	public Result houseInfo(HttpServletRequest request,
	                        @RequestParam("files") List<MultipartFile> files,
	                        @RequestParam("types") List<String> types,
	                        @RequestParam("persons") String persons,
	                        @RequestParam("merCode") String merCode,
	                        @RequestParam("key") String key,
	                        @RequestParam("merSeq") String merSeq,
	                        @RequestParam("sign") String sign) {
		HouseInfoReqVo houseInfoReqVo = new HouseInfoReqVo();
		houseInfoReqVo.setFiles(files);
		houseInfoReqVo.setTypes(types);
		houseInfoReqVo.setMerCode(merCode);
		houseInfoReqVo.setKey(key);
		houseInfoReqVo.setMerSeq(merSeq);
		houseInfoReqVo.setPersonsStr(persons);
		houseInfoReqVo.setSign(sign);
		String reqJson = null;
		try {
			if (!persons.startsWith("[")) {
				persons = "[" + persons + "]";
			}
			List<PersonInfoReqVo> personInfoReqVos = JSONUtil.toList(persons, PersonInfoReqVo.class);
			houseInfoReqVo.setPersons(personInfoReqVos);
			reqJson = JSONUtil.toJsonStr(houseInfoReqVo);
			//检查商户参数完整性
			CkMerBean ckMerBean = ckHouseInfoParams(houseInfoReqVo);
			ckMerBean.setProductCode(ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);
			return bgService.queryHouseInfo(houseInfoReqVo, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("不动产信息核查 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}


	}

	/**
	 * 不动产信息核查
	 */
	@PostMapping("/houseResultReqInfo")
	public Result houseResultInfo(HttpServletRequest request, @RequestBody HouseResultInfoReqVo params) {
		System.err.println(params.toString());
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckHouseResultInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryHouseResultInfo(params, merLog);
		} catch (ApiServiceException e) {

			return Result.fail(e.getCode(), e.getMessage());

		} catch (Exception e) {
			errMonitorMsg("【不动产结果信息】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 车五项信息查询
	 *
	 * @return
	 */
	@PostMapping("/carInfo")
	public Result carInfo(HttpServletRequest request, @RequestBody CarInfoReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckCarInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_CAR_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return bgService.queryCarInfo(params, merLog);
		} catch (ApiServiceException e) {

			return Result.fail(e.getCode(), e.getMessage());

		} catch (Exception e) {
			errMonitorMsg("【车5项】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 行驶证信息查询
	 *
	 * @return
	 */
	@PostMapping("/vehicleLicenseInfo")
	public Result vehicleLicenseInfo(HttpServletRequest request, @RequestBody VehicleLicenseReqVo params) {
		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckVehicleLicenseInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.BG_VEHICLE_LICENSE_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);


			return bgService.queryVehicleLicenseInfo(params, merLog);
		} catch (ApiServiceException e) {

			return Result.fail(e.getCode(), e.getMessage());

		} catch (Exception e) {
			errMonitorMsg("【行驶证信息查询】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckHouseResultInfoParams(HouseResultInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String reqOrderNo = params.getReqOrderNo();
		if (StringUtils.isBlank(reqOrderNo)) {
			log.error("缺少参数 reqOrderNo {} , merCode： {}", reqOrderNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + reqOrderNo;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckDrivingLicenseParams(DrivingLicenseReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String licensePlateNo = params.getLicensePlateNo();
		String name = params.getName();
		String plateType = params.getPlateType();
		if (StringUtils.isBlank(licensePlateNo)) {
			log.error("缺少参数 licensePlateNo {} , merCode： {}", licensePlateNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(plateType)) {
			log.error("缺少参数 plateType {} , merCode： {}", plateType, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + licensePlateNo + name + plateType;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	public static void main(String[] args) {
		String persons = "[{\"name\":\"陈盱\",\"cardNum\":\"310110196912307039\"}]";
		String merCode = "BhCpTest";
		String xm = "闵超";
		String sfzh = "429005198710050913";
//		本地
		String pwd = "e0be01493778d77ecfd2004f54b41a09";
		//线上
//        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
//        String plaintext = merCode + xm + sfzh;
		String bankCard = "6214835901683332";
		String certName = "郑远芳";
		String plaintext = merCode + bankCard + certName;
		String signText = plaintext + pwd;
		String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
		System.err.println(signMd5);

	}

	private CkMerBean ckEconomicRateParams(EconomicRateReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String idCard = params.getIdCard();
		String name = params.getName();
		String mobile = params.getMobile();
		if (StringUtils.isBlank(idCard)) {
			log.error("缺少参数 cid {} , merCode： {}", idCard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + idCard + name + mobile;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}


	private CkMerBean ckEnterpriseFourElementsParams(EnterpriseFourElementsReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String companyName = params.getCompanyName();
		String creditCode = params.getCreditCode();
		String legalPerson = params.getLegalPerson();
		String certNo = params.getCertNo();

//        if (StringUtils.isBlank(companyName)) {
//            log.error("缺少参数 companyName {} , merCode： {}", companyName, merCode);
//            throw new ApiServiceException(ApiReturnCode.ERR_001);
//        }

		if (StringUtils.isBlank(creditCode)) {
			log.error("缺少参数 creditCode {} , merCode： {}", creditCode, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(legalPerson)) {
			log.error("缺少参数 creditCode {} , merCode： {}", legalPerson, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(certNo)) {
			log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (!IdcardUtil.isValidCard(certNo)) {
			log.error("缺少不正确 personId {} , merCode： {}", certNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_009);
		}


		String plaintext = merCode + companyName + creditCode + legalPerson + certNo;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckPersonCarDetailInfoParams(PersonCarDetailReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String cid = params.getCid();
		String name = params.getName();
		String plateNo = params.getPlateNo();

		if (StringUtils.isBlank(cid)) {
			log.error("缺少参数 idCard {} , merCode： {}", cid, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(plateNo)) {
			log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + cid + plateNo + name;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckSureScoreInfoParams(SureScoreInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String cid = params.getCid();
		String name = params.getName();
		String mobile = params.getMobile();
		String job = params.getJob();

		if (StringUtils.isBlank(cid)) {
			log.error("缺少参数 idCard {} , merCode： {}", cid, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (!IdcardUtil.isValidCard(cid)) {
			log.error("无效参数 idCard {} , merCode： {}", cid, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_009);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(job)) {
			log.error("缺少参数 job {} , merCode： {}", job, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + cid + mobile + name + job;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckfinanceInfoV3Params(FinanceInfoV3ReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String idCard = params.getIdCard();
		String name = params.getName();
		String mobile = params.getMobile();

		if (StringUtils.isBlank(idCard)) {
			log.error("缺少参数 idCard {} , merCode： {}", idCard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + idCard + name + mobile;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckfinanceInfoParams(FinanceInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String idCard = params.getIdCard();
		String name = params.getName();
		String mobile = params.getMobile();

		if (StringUtils.isBlank(idCard)) {
			log.error("缺少参数 idCard {} , merCode： {}", idCard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + idCard + name + mobile;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckCarInfoParams(CarInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String plateNo = params.getPlateNo();
		String plateType = params.getPlateType();
		if (StringUtils.isBlank(plateNo)) {
			log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + plateNo;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckVehicleLicenseInfoParams(VehicleLicenseReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String plateNo = params.getPlateNo();
		String plateType = params.getPlateType();
		String name = params.getName();
		if (StringUtils.isBlank(plateNo)) {
			log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(plateType)) {
			log.error("缺少参数 plateType {} , merCode： {}", plateType, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + name + plateNo + plateType;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private CkMerBean ckHouseInfoParams(HouseInfoReqVo params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		List<String> types = params.getTypes();
		List<PersonInfoReqVo> persons = params.getPersons();
		if (types == null || types.isEmpty()) {
			log.error("缺少参数 types {} , merCode： {}", types, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (persons == null || persons.isEmpty()) {
			log.error("缺少参数 persons {} , merCode： {}", persons, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (params.getFiles() == null || params.getFiles().isEmpty()) {
			log.error("缺少参数 files {}, merCode： {}", params.getFiles(), merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (params.getFiles().size() != types.size()) {
			log.error("缺少参数 files().size() {}, types.size() {}, merCode： {}", persons.size(), types.size(), merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		for (PersonInfoReqVo personInfoReqVo : persons) {
			if (StringUtils.isBlank(personInfoReqVo.getName())) {
				log.error("缺少参数 personInfoReqVo.getName {} , merCode： {}", personInfoReqVo.getName(), merCode);
				throw new ApiServiceException(ApiReturnCode.ERR_001);
			}
			if (StringUtils.isBlank(personInfoReqVo.getCardNum())) {
				log.error("缺少参数 personInfoReqVo.getCardNum {} , merCode： {}", personInfoReqVo.getCardNum(), merCode);
				throw new ApiServiceException(ApiReturnCode.ERR_001);
			}
		}

		String plaintext = merCode + params.getPersonsStr();
		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}


}
