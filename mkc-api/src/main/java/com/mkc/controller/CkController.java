package com.mkc.controller;

import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.constant.enums.ReqParamType;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.dto.ck.req.*;
import com.mkc.api.dto.ck.res.MobileThreePlusResDTO;
import com.mkc.api.dto.ck.res.MobileThreeResDTO;
import com.mkc.api.dto.ck.res.ProQualifyCertResDTO;
import com.mkc.api.dto.ck.res.ResumeVerifyResDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.service.ICkService;
import com.mkc.bean.CkMerBean;
import com.mkc.tool.RegTool;
import com.mkc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 信息核验相关类入口
 *
 * @author tqlei
 * @date 2023/5/29 17:45
 */

@Slf4j
@RestController
@RequestMapping("/ck")
public class CkController extends BaseController {

	@Autowired
	private ICkService ckService;


	/**
	 * 当前履历核验
	 *
	 * @return
	 */
	@PostMapping("/resume_verify")
	public Result<ResumeVerifyResDTO> ckResumeVerify(HttpServletRequest request,
	                                                 @RequestBody @Valid ResumeVerifyReqDTO params) {

		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.CK_RESUME_VERIFY);
		ckMerBean.setPlaintext(params.getMerCode() + params.getIdCard() + params.getName() + params.getCompanyName() + params.getAuthCode());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return ckService.ckResumeVerify(params, merLog);

	}


	/**
	 * 技能人员职业资格证书核验数据元件接口
	 *
	 * @param request
	 * @param req
	 * @return
	 */
	@PostMapping("/pro_qualify_cert")
	public Result<ProQualifyCertResDTO> ckProQualifyCert(HttpServletRequest request,
	                                                     @Valid @RequestBody ProQualifyCertReqDTO req) {
		CkMerBean ckMerBean = CkMerBean.build(req, ProductCodeEum.CK_PRO_QUALIFY_CERT_001);
		ckMerBean.setPlaintext(req.getMerCode() + req.getName() + req.getIdNum());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(req));
		return ckService.ckProQualifyCert(req, merLog);
	}

	/**
	 * 银行卡四要素
	 */
	@PostMapping("/bankFour")
	public Result ckBankFour(HttpServletRequest request, @RequestBody BankReqDTO params) throws InterruptedException {

		String reqJson = null;
		long start = System.currentTimeMillis();
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckBankFourParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_BANK_FOUR.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			Result result = ckService.ckBankFour(params, merLog);

			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return result;
		} catch (ApiServiceException e) {
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			errMonitorMsg("【银行卡四要素】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 银行卡三要素
	 */
	@PostMapping("/bankThree")
	public Result ckBankThree(HttpServletRequest request, @RequestBody BankReqDTO params) throws InterruptedException {

		String reqJson = null;
		long start = System.currentTimeMillis();
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckBankThreeParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_BANK_THREE.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			Result result = ckService.ckBankThree(params, merLog);
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return result;
		} catch (ApiServiceException e) {
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【银行卡三要素】API 发生异常  reqJson {} ", reqJson, e);
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return Result.fail();
		}
	}

	/**
	 * 银行卡二要素
	 */
	@PostMapping("/bankTwo")
	public Result ckBankTwo(HttpServletRequest request, @RequestBody BankReqDTO params) throws InterruptedException {

		String reqJson = null;
		long start = System.currentTimeMillis();
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckBankTwoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_BANK_TWO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			Result result = ckService.ckBankTwo(params, merLog);
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return result;
		} catch (ApiServiceException e) {
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			long end = System.currentTimeMillis();
			// 要求接口响应时间超过700ms
			if (end - start <= 700) {
				Thread.sleep(700 - (end - start));
			}
			errMonitorMsg("【银行卡二要素】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 行驶证核验
	 */
	@PostMapping("/ckVehicleLicense")
	public Result ckVehicleLicense(HttpServletRequest request, @RequestBody VehicleLicenseReqDTO params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckVehicleLicenseParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_VEHICLE_LICENSE_INFO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			Result result = ckService.ckVehicleLicenseInfo(params, merLog);

			return result;
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【行驶证核验】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckVehicleLicenseParams(VehicleLicenseReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String license = params.getLicense();
		String type = params.getType();
		String name = params.getName();

		if (StringUtils.isBlank(license)) {
			log.error("缺少参数 license {} , merCode： {}", license, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(type)) {
			log.error("缺少参数 name {} , merCode： {}", type, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + name + license + type;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	private CkMerBean ckBankFourParams(BankReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String bankCard = params.getBankCard();
		String certName = params.getCertName();
		String certNo = params.getCertNo();
		String mobile = params.getMobile();

		if (StringUtils.isBlank(bankCard)) {
			log.error("缺少参数 bankCard {} , merCode： {}", bankCard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(certName)) {
			log.error("缺少参数 certName {} , merCode： {}", certName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(certNo)) {
			log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		String plaintext = merCode + bankCard + certName + certNo + mobile;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	private CkMerBean ckBankThreeParams(BankReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String bankCard = params.getBankCard();
		String certName = params.getCertName();
		String certNo = params.getCertNo();

		if (StringUtils.isBlank(bankCard)) {
			log.error("缺少参数 bankCard {} , merCode： {}", bankCard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(certName)) {
			log.error("缺少参数 certName {} , merCode： {}", certName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(certNo)) {
			log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + bankCard + certName + certNo;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	private CkMerBean ckBankTwoParams(BankReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String bankCard = params.getBankCard();
		String certName = params.getCertName();

		if (StringUtils.isBlank(bankCard)) {
			log.error("缺少参数 bankCard {} , merCode： {}", bankCard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(certName)) {
			log.error("缺少参数 certName {} , merCode： {}", certName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + bankCard + certName;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}


	/**
	 * 全国⼈⼝身份信息三要素核验
	 *
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/populationThree")
	public Result populationThree(HttpServletRequest request, @RequestBody PopulationThreeReqDTO params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckPopulationThreeParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_POPULATION_THREE.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return ckService.ckPopulationThree(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【全国⼈⼝身份信息三要素核验】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	/**
	 * 全国⼈⼝身份信息二要素核验
	 *
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/populationTwo")
	public Result populationTwo(HttpServletRequest request, @RequestBody PopulationTwoReqDTO params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckPopulationTwoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_POPULATION_TWO.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			return ckService.ckPopulationTwo(params, merLog);
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【全国⼈⼝身份信息二要素核验】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	@PostMapping("/workUnitVerify")
	public Result workUnitVerify(HttpServletRequest request, @RequestBody WorkUnitReqDTO params) {

		String reqJson = null;
		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckWorkUnitInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_WORK_UNIT.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			Result result = ckService.ckWorkUnit(params, merLog);

			return result;
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【工作单位】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}
	}

	private CkMerBean ckPopulationThreeParams(PopulationThreeReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String name = params.getName();
		String photo = params.getPhoto();
		String idcard = params.getIdcard();
		String authorization = params.getAuthorization();

		if (StringUtils.isBlank(idcard)) {
			log.error("缺少参数 cid {} , merCode： {}", idcard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(photo)) {
			log.error("缺少参数 name {} , merCode： {}", photo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(authorization)) {
			log.error("缺少参数 name {} , merCode： {}", authorization, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + name + idcard + photo + authorization;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	private CkMerBean ckPopulationTwoParams(PopulationTwoReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String name = params.getName();
		String idcard = params.getIdcard();
		String authorization = params.getAuthorization();

		if (StringUtils.isBlank(idcard)) {
			log.error("缺少参数 cid {} , merCode： {}", idcard, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(authorization)) {
			log.error("缺少参数 name {} , merCode： {}", authorization, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + name + idcard + authorization;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	private CkMerBean ckWorkUnitInfoParams(WorkUnitReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String name = params.getName();
		String cid = params.getCid();
		String workplace = params.getWorkplace();

		if (StringUtils.isBlank(cid)) {
			log.error("缺少参数 cid {} , merCode： {}", cid, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 name {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(workplace)) {
			log.error("缺少参数 workplace {} , merCode： {}", workplace, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + name + cid + workplace;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}

	@PostMapping("/personCarVerify")
	public Result personCarVerify(HttpServletRequest request, @RequestBody PersonCarReqDTO params) {

		String reqJson = null;

		try {
			reqJson = JSON.toJSONString(params);

			//检查商户参数完整性
			CkMerBean ckMerBean = ckPersonCarInfoParams(params);
			ckMerBean.setProductCode(ProductCodeEum.CK_PERSON_CAR.getCode());

			//检查商户参数有效性
			MerReqLogDTO merLog = ckMer(request, ckMerBean);
			merLog.setReqJson(reqJson);

			Result result = ckService.ckPersonCar(params, merLog);

			return result;
		} catch (ApiServiceException e) {
			return Result.fail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			errMonitorMsg("【人车核验】API 发生异常  reqJson {} ", reqJson, e);
			return Result.fail();
		}

	}

	private CkMerBean ckPersonCarInfoParams(PersonCarReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String name = params.getName();
		String plateNo = params.getPlateNo();

		if (StringUtils.isBlank(plateNo)) {
			log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (StringUtils.isBlank(name)) {
			log.error("缺少参数 plateNo {} , merCode： {}", name, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		String plaintext = merCode + name + plateNo;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
	}


	/**
	 * 个人手机三要素认证-简版
	 *
	 * @return
	 */
	@PostMapping("/mobileThree")
	public Result<MobileThreeResDTO> mobileThree(HttpServletRequest request, @RequestBody MobThreeReqDTO params) {
		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.CK_MOB_THREE);
		ckMerBean.setPlaintext(params.getMerCode() + params.getCertName() + params.getCertNo() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return ckService.ckMobThree(params, merLog);
	}

	/**
	 * 个人手机三要素认证-详版
	 *
	 * @return
	 */
	@PostMapping("/mobileThreePlus")
	public Result<MobileThreePlusResDTO> mobileThreePlus(HttpServletRequest request, @RequestBody MobThreeReqDTO params) {
		CkMerBean ckMerBean = CkMerBean.build(params, ProductCodeEum.CK_MOB_THREE_PLUS);
		ckMerBean.setPlaintext(params.getMerCode() + params.getCertName() + params.getCertNo() + params.getMobile());
		//检查商户参数有效性
		MerReqLogDTO merLog = ckMer(request, ckMerBean);
		merLog.setReqJson(JsonUtil.toJson(params));
		return ckService.ckMobThreePlus(params, merLog);
	}


	private CkMerBean ckThreeParams(MobThreeReqDTO params) {

		String merCode = params.getMerCode();

		String sign = params.getSign();
		String key = params.getKey();

		ckCommonParams(params);

		String certName = params.getCertName();
		String certNo = params.getCertNo();
		String mobile = params.getMobile();

		ckCommonThree(merCode, certName, certNo, mobile, params.getParamType());

		String plaintext = merCode + certName + certNo + mobile;

		return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

	}

	private void ckCommonThree(String merCode, String certName, String certNo, String mobile, String paramType) {

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {}, merCode:  {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		//判断类型为MD5
		if (ReqParamType.isMd5Type(paramType)) {
			if (!RegTool.checkMd5(certName) && !ckCertName(merCode, certName)) {
				log.error("无效的参数 MD5 certName {} , merCode:  {}", certName, merCode);
				throw new ApiServiceException(ApiReturnCode.ERR_009);
			}
			if (!RegTool.checkMd5(certNo) && !ckCertNo(merCode, certNo)) {
				log.error("无效的参数 MD5 certNo {} , merCode:  {}", certNo, merCode);
				throw new ApiServiceException(ApiReturnCode.ERR_009);
			}
			if (!RegTool.checkMd5(mobile) && !ckMobile(merCode, mobile)) {
				log.error("无效的参数 MD5 mobile {}, merCode:  {}", mobile, merCode);
				throw new ApiServiceException(ApiReturnCode.ERR_009);
			}
			return;
		}
		ckCertNo(merCode, certNo);
		ckCertName(merCode, certName);
		ckMobile(merCode, mobile);

	}


}
