package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.bg.req.*;
import com.mkc.api.dto.bg.res.*;
import com.mkc.api.dto.ck.req.AntiFraudV6ReqDTO;
import com.mkc.api.dto.ck.req.PersonalVehicleReqDTO;
import com.mkc.api.dto.ck.res.AntiFraudV6ResDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;

import java.util.function.BiFunction;

/**
 * 核验类 API 接口
 *
 * @author tqlei
 * @date 2023/4/27 17:52
 */

public interface IBgService {

	public SupResult bgCommonSup(MerReqLogDTO merLog, BaseDTO vo, BiFunction<IBgSupService, SuplierQueryBean, SupResult> function);

	Result<AntiFraudV6ResDTO> antiFraudV6(AntiFraudV6ReqDTO params, MerReqLogDTO merReqLog);

	Result personalVehicle(PersonalVehicleReqDTO params, MerReqLogDTO merReqLog);

	Result corporateAppointments(CorporateAppointmentsReqDTO params, MerReqLogDTO merReqLog);

	Result<HighRiskPeopleResDTO> queryHighRiskPeople(HighRiskPeopleReqDTO params, MerReqLogDTO merReqLog);

	Result<FinanceIcsResDTO> financeIcsA(FinanceIcsReqDTO params, MerReqLogDTO merReqLog);

	Result<FinanceIcsResDTO> financeIcsB(FinanceIcsReqDTO params, MerReqLogDTO merReqLog);

	Result<FinanceIcsResDTO> financeIcsE(FinanceIcsReqDTO params, MerReqLogDTO merReqLog);

	Result<FinanceIcsResDTO> financeIcsF(FinanceIcsReqDTO params, MerReqLogDTO merReqLog);

	/**
	 * 网贷（授信多头）
	 *
	 * @param params
	 * @param merReqLog
	 * @return
	 */
	Result queryCreditA108(CreditA108ReqDTO params, MerReqLogDTO merReqLog);


	/**
	 * 网贷（申请多头）
	 *
	 * @param params
	 * @param merReqLog
	 * @return
	 */
	Result queryCreditA107(CreditA107ReqDTO params, MerReqLogDTO merReqLog);


	/**
	 * 网贷（逾期多头）
	 *
	 * @param params
	 * @param merReqLog
	 * @return
	 */
	Result queryCreditA016(CreditA016ReqDTO params, MerReqLogDTO merReqLog);

	/**
	 * I8还款能⼒评分
	 *
	 * @param params
	 * @param merReqLog
	 * @return
	 */
	Result<FinanceI8ResDTO> queryFinanceI8(FinanceI8ReqDTO params, MerReqLogDTO merReqLog);


	/**
	 * I8还款能⼒评分
	 *
	 * @param params
	 * @param merReqLog
	 * @return
	 */
	Result<FinanceI9ResDTO> queryFinanceI9(FinanceI9ReqDTO params, MerReqLogDTO merReqLog);


	/**
	 * 人企
	 *
	 * @param params
	 * @param merReqLog
	 * @return
	 */
	Result<PeopleEnterpriseResDTO> queryPeopleEnterprise(PeopleEnterpriseReqDTO params, MerReqLogDTO merReqLog);

	/**
	 * 车五项信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result queryCarInfo(CarInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 行驶证信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result queryVehicleLicenseInfo(VehicleLicenseReqVo params, MerReqLogDTO merLog);

	/**
	 * 不动产信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result queryHouseInfo(HouseInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 不动产结果信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result queryHouseResultInfo(HouseResultInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 经济能力2w信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result queryFinanceInfo(FinanceInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 经济能力评级V3
	 *
	 * @param params
	 * @return
	 */
	public Result queryFinanceInfoV3(FinanceInfoV3ReqVo params, MerReqLogDTO merLog);


	/**
	 * 经济能力评级V7
	 *
	 * @param params
	 * @return
	 */
	Result queryFinanceInfoV7(FinanceInfoV3ReqVo params, MerReqLogDTO merLog);

	/**
	 * 确信分信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result querySureScoreInfo(SureScoreInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 人车核验详版
	 *
	 * @param params
	 * @return
	 */
	public Result queryPersonCarInfo(PersonCarDetailReqVo params, MerReqLogDTO merLog);

	/**
	 * 企业四要素核验
	 *
	 * @param params
	 * @return
	 */
	public Result queryFourElementsInfo(EnterpriseFourElementsReqVo params, MerReqLogDTO merLog);

	/**
	 * 经济能力评级
	 *
	 * @param params
	 * @return
	 */
	public Result queryEconomicRateInfo(EconomicRateReqVo params, MerReqLogDTO merLog);

	/**
	 * 行驶身份核验
	 *
	 * @param params
	 * @return
	 */
	public Result queryDrivingLicenseInfo(DrivingLicenseReqVo params, MerReqLogDTO merLog);

	/**
	 * 学历信息查询
	 *
	 * @param params
	 * @return
	 */
	public Result queryEducationInfo(EducationInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 高校学历核查接口
	 *
	 * @param params
	 * @return
	 */
	Result queryHighSchoolEducationInfo(HighSchoolEducationInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 高校学历证书结果核查接口
	 *
	 * @param params
	 * @return
	 */
	Result queryHighSchoolEducationResultInfo(HighSchoolEducationResultInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 婚姻状况
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result queryMaritalStatus(MarriageInfoReqInfo params, MerReqLogDTO merLog);

	/**
	 * 婚姻关系验证
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result queryMaritalRelationship(MaritalRelationshipReqVo params, MerReqLogDTO merLog);

	/**
	 * 婚姻稳定状况
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result ckMarriageStabilityParams(MaritalStabilityReqVo params, MerReqLogDTO merLog);

	/**
	 * 高校学历核查实时接口
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result queryHighSchoolEducation(HighSchoolEducationInfoReqVo params, MerReqLogDTO merLog);

	/**
	 * 教育评估
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result queryEduAssessment(EducationInfoReqVo params, MerReqLogDTO merLog);
}
