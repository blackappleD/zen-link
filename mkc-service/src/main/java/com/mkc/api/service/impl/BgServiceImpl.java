package com.mkc.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.common.exception.ErrMonitorCode;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.bg.req.*;
import com.mkc.api.dto.bg.res.*;
import com.mkc.api.dto.ck.req.AntiFraudV6ReqDTO;
import com.mkc.api.dto.ck.req.PersonalVehicleReqDTO;
import com.mkc.api.dto.ck.res.AntiFraudV6ResDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.handle.ReqLogHandle;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.api.service.IBgService;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.domain.SupplierRoute;
import com.mkc.process.IMailProcess;
import com.mkc.service.ISupplierProductService;
import com.mkc.service.ISupplierRouteService;
import com.mkc.util.ErrorConstants;
import com.mkc.util.RateLimitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 信息查询相关接口
 *
 * @author tqlei
 * @date 2023/5/29 18:28
 */

@Slf4j
@Service
@DS("business")
public class BgServiceImpl implements IBgService {

	//供应商前缀
	private final static String SUP_PREFIX = "BG_";
	@Autowired
	private Map<String, IBgSupService> bgSupServiceMap;

	@Autowired
	private ISupplierRouteService supplierRouteService;

	@Autowired
	private ISupplierProductService supplierProductService;

	@Autowired
	private ReqLogHandle reqLogHandle;

	@Autowired
	private IMailProcess mailProcess;

	@Override
	public Result corporateAppointments(CorporateAppointmentsReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.corporateAppointments(params, supQueryBean));
	}

	@Override
	public Result<AntiFraudV6ResDTO> antiFraudV6(AntiFraudV6ReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.antiFraudV6(params, supQueryBean));
	}

	@Override
	public Result personalVehicle(PersonalVehicleReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.personalVehicle(params, supQueryBean));
	}


	@Override
	public Result<HighRiskPeopleResDTO> queryHighRiskPeople(HighRiskPeopleReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryHighRiskPeople(params, supQueryBean));
	}

	@Override
	public Result<FinanceIcsResDTO> financeIcsA(FinanceIcsReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.financeIcsA(params, supQueryBean));
	}

	@Override
	public Result<FinanceIcsResDTO> financeIcsB(FinanceIcsReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.financeIcsB(params, supQueryBean));
	}

	@Override
	public Result<FinanceIcsResDTO> financeIcsE(FinanceIcsReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.financeIcsE(params, supQueryBean));
	}

	@Override
	public Result<FinanceIcsResDTO> financeIcsF(FinanceIcsReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.financeIcsF(params, supQueryBean));
	}


	@Override
	public Result queryCreditA108(CreditA108ReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryCreditA108(params, supQueryBean));
	}

	@Override
	public Result queryCreditA107(CreditA107ReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryCreditA107(params, supQueryBean));
	}

	@Override
	public Result queryCreditA016(CreditA016ReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryCreditA016(params, supQueryBean));
	}

	@Override
	public Result<FinanceI8ResDTO> queryFinanceI8(FinanceI8ReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryFinanceI8(params, supQueryBean));
	}

	@Override
	public Result<FinanceI9ResDTO> queryFinanceI9(FinanceI9ReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryFinanceI9(params, supQueryBean));
	}

	@Override
	public Result<PeopleEnterpriseResDTO> queryPeopleEnterprise(PeopleEnterpriseReqDTO params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryPeopleEnterprise(params, supQueryBean));
	}


	@Override
	public Result queryCarInfo(CarInfoReqVo params, MerReqLogDTO merLog) {

		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryCarInfo(params, supQueryBean));
	}

	@Override
	public Result queryVehicleLicenseInfo(VehicleLicenseReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryVehicleLicenseInfo(params, supQueryBean));
	}

	@Override
	public Result queryHouseInfo(HouseInfoReqVo params, MerReqLogDTO merLog) {

		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryHouseInfo(params, supQueryBean));
	}

	@Override
	public Result queryHouseResultInfo(HouseResultInfoReqVo params, MerReqLogDTO merLog) {

		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryHouseResultInfo(params, supQueryBean));
	}

	@Override
	public Result queryFinanceInfo(FinanceInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryFinanceInfo(params, supQueryBean));
	}

	@Override
	public Result queryFinanceInfoV3(FinanceInfoV3ReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryFinanceInfoV3(params, supQueryBean));
	}

	@Override
	public Result queryFinanceInfoV7(FinanceInfoV3ReqVo params, MerReqLogDTO merLog) {

		return bgCommon(merLog, params, (bgSupService, supQueryBean) ->
				bgSupService.queryFinanceInfoV7(params, supQueryBean));
	}

	@Override
	public Result querySureScoreInfo(SureScoreInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.querySureScoreInfo(params, supQueryBean));
	}

	@Override
	public Result queryPersonCarInfo(PersonCarDetailReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryPersonCarInfo(params, supQueryBean));
	}

	@Override
	public Result queryFourElementsInfo(EnterpriseFourElementsReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryFourElementsInfo(params, supQueryBean));
	}

	@Override
	public Result queryEconomicRateInfo(EconomicRateReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryEconomicRateInfo(params, supQueryBean));
	}

	@Override
	public Result queryDrivingLicenseInfo(DrivingLicenseReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryDrivingLicenseInfo(params, supQueryBean));
	}

	@Override
	public Result queryEducationInfo(EducationInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryEducationInfo(params, supQueryBean));
	}

	@Override
	public Result queryHighSchoolEducationInfo(HighSchoolEducationInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryHighSchoolEducationInfo(params, supQueryBean));
	}

	@Override
	public Result queryHighSchoolEducationResultInfo(HighSchoolEducationResultInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryHighSchoolEducationResultInfo(params, supQueryBean));
	}

	@Override
	public Result queryHighSchoolEducation(HighSchoolEducationInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryHighSchoolEducation(params, supQueryBean));
	}

	@Override
	public Result queryEduAssessment(EducationInfoReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryEduAssessment(params, supQueryBean));
	}

	@Override
	public Result queryMaritalStatus(MarriageInfoReqInfo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryMaritalStatus(params, supQueryBean));
	}

	@Override
	public Result queryMaritalRelationship(MaritalRelationshipReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryMaritalRelationship(params, supQueryBean));
	}

	@Override
	public Result ckMarriageStabilityParams(MaritalStabilityReqVo params, MerReqLogDTO merLog) {
		return bgCommon(merLog, params, (bgSupService, supQueryBean) -> bgSupService.queryMaritalStability(params, supQueryBean));
	}


	private Result bgCommon(MerReqLogDTO merLog, BaseDTO vo, BiFunction<IBgSupService, SuplierQueryBean, SupResult> function) {

		SupResult supResult = bgCommonSup(merLog, vo, function);

		return getRespResult(merLog, supResult);

	}

	//将查询供应商方法迁移出来
	@Override
	public SupResult bgCommonSup(MerReqLogDTO merLog, BaseDTO vo, BiFunction<IBgSupService, SuplierQueryBean, SupResult> function) {

		String merCode = merLog.getMerCode();
		String productCode = merLog.getProductCode();
		String orderNo = merLog.getOrderNo();

		if (RateLimitUtil.rateLimit(productCode, merCode, merLog.getReqLimit())) {
			throw new ApiServiceException(ApiReturnCode.ERR_010);
		}

		List<SupplierRoute> supplierRoutes = supplierRouteService.querySupRouteList(merCode, productCode);
		//判断是否有可用的供应商
		if (CollectionUtil.isEmpty(supplierRoutes)) {
			log.error(ErrMonitorCode.BIZ_ERR + " 该商户的产品 没有配置可用的供应商   merCode: {} , productCode: {} , orderNo: {} ", merLog.getMerCode(), merLog.getProductCode(), orderNo);
			return null;
		}
		SupResult supResult = null;
		SuplierQueryBean supQueryBean = null;

		BigDecimal inPrice = BigDecimal.ZERO;

		for (SupplierRoute supplier : supplierRoutes) {
			String supCode = supplier.getSupCode();
			SuplierQueryBean newSupQueryBean = supplierProductService.selectSupProductBySupCode(supCode, productCode);
			if (newSupQueryBean == null) {
				log.info("BG 此产品在供应商和没有可用供应商产品   supCode: {}, productCode: {} , orderNo: {}", supCode, productCode, orderNo);
				continue;
			}
			supQueryBean = newSupQueryBean;
			supQueryBean.setOrderNo(merLog.getOrderNo());
			String supProcessor = newSupQueryBean.getSupProcessor();

			log.info("==== BG 开始查询   supCode: {}, productCode: {} , orderNo: {}", supCode, productCode, orderNo);


			IBgSupService bgSupService = bgSupServiceMap.get(SUP_PREFIX + supProcessor);
			supResult = function.apply(bgSupService, supQueryBean);

			if (supResult == null) {
				supResult = new SupResult<>("", LocalDateTime.now());
				supResult.setRemark(ErrorConstants.SUP_NO_RESPONSE);

				String errMsg = " 【报告类】 查询供应商结果 NULL;   merCode {}, productCode {} ,supCode {} ,orderNo {}";
				log.error(errMsg, merLog.getMerCode(), merLog.getProductCode(), supCode, orderNo);
				//发送钉钉消息 通知
				DdMonitorMsgUtil.sendDdSysErrMsg(errMsg, merLog.getMerCode(), merLog.getProductCode(), supCode, orderNo);

				supResult.setState(ReqState.ERROR);
				supResult.setRespTime(LocalDateTime.now());
			}
			supResult.setSupCode(supQueryBean.getSupCode());
			supResult.setSupName(supQueryBean.getSupName());

			//记录请求供应商调用记录
			reqLogHandle.supReqLogHandle(supResult, merLog, supQueryBean);

			//判断是否收费 收费的就需要累计 进价
			if (supResult.isFree()) {
				inPrice = inPrice.add(supQueryBean.getInPrice());
			}
			supResult.setInPrice(inPrice);
			if (supResult.isQueryGet()) {
				//调用供应商查询成功 直接跳出循环
				break;
			}
			if (merLog.getRouteCon().contains(supResult.getState().getCode())) {
				log.info("【BG报告类】 查询供应商数据失败，走下一个供应商 supCode {} , orderNo {}", supCode, orderNo);
				continue;
			}
			break;

		}

		return supResult;

	}


	/**
	 * 处理供应商返回结果 公用方法
	 *
	 * @param merLog    订单信息
	 * @param supResult 调用成功，或最后调用的供应商 调用结果
	 * @return
	 */
	private Result getRespResult(MerReqLogDTO merLog, SupResult supResult) {

		if (supResult == null) {
			return getFailResult(merLog);
		}

		BigDecimal inPrice = supResult.getInPrice();
		Result result = null;  //响应时间

		//设置流水号
		String orderNo = merLog.getOrderNo();
		//判断是否 一致
		if (supResult.isSuccess()) {
			// result = Result.ok(supResult.getData(), orderNo, "认证信息一致");
			result = Result.ok(supResult.getData(), supResult.getFree(), orderNo);
			if (Objects.equals(supResult.getFree(), FreeStatus.YES)) {
				result.setBilledTimes(supResult.getBilledTimes());
			}
			//判断是否 不一致
		} else if (supResult.isNot()) {
			result = Result.not(supResult.getData(), orderNo);
			//查无
		} else if (supResult.isNoGet()) {
			result = Result.no(supResult.getData(), supResult.getFree(), orderNo);
			//其它算查询失败活异常
		} else {
			//判断是否是自定义错误消息
			if (supResult.getDefinedFailMsg()) {
				result = Result.fail(supResult.getData(), supResult.getRemark(), orderNo);
			} else {
				result = Result.fail(supResult.getData(), orderNo);
			}
		}

		merLog.setSupCode(supResult.getSupCode());
		merLog.setSupName(supResult.getSupName());
		merLog.setStatus(supResult.getState().getCode());
		merLog.setInPrice(inPrice);
		merLog.setFree(Objects.nonNull(supResult.getBilledTimes()) ? String.valueOf(supResult.getBilledTimes()) : supResult.getFree().getCode());
		merLog.setLevel(supResult.getLevel());
		merLog.setRespTime(LocalDateTime.now());
		merLog.setRespJson(JSON.toJSONString(result));
		//放入队列异步落地
		reqLogHandle.merReqLogHandle(merLog, supResult);

		return result;
	}


	/**
	 * 查询失败
	 *
	 * @param merLog
	 * @return
	 */
	private Result getFailResult(MerReqLogDTO merLog) {

		//设置流水号
		String orderNo = merLog.getOrderNo();
		log.error(ErrMonitorCode.BIZ_ERR + " 该商户的产品 没有配置可用的供应商   merCode: {} , productCode: {} , orderNo: {} ", merLog.getMerCode(), merLog.getProductCode(), orderNo);

		Result result = Result.fail(orderNo);

		merLog.setInPrice(BigDecimal.ZERO);
		merLog.setFree(FreeStatus.NO.getCode());
		merLog.setRemark("没有配置 可查询 的 供应商 ");
		merLog.setStatus(ReqState.ERROR.getCode());

		//响应时间
		merLog.setRespTime(LocalDateTime.now());
		merLog.setRespJson(JSON.toJSONString(result));

		SupResult supResult = new SupResult<>("", LocalDateTime.now());

		//发送邮件预警
		mailProcess.sendProductNotSup(merLog.getMerName(), merLog.getProductName());

		//放入队列异步落地
		reqLogHandle.merReqLogHandle(merLog, supResult);

		return result;
	}


}
