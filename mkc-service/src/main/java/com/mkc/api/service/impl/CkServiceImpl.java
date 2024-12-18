package com.mkc.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.exception.ErrMonitorCode;
import com.mkc.api.handle.ReqLogHandle;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.api.service.ICkService;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.dto.ck.ProQualifyCertReqDTO;
import com.mkc.api.dto.ck.ProQualifyCertResDTO;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.ck.*;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.domain.SupplierRoute;
import com.mkc.process.IMailProcess;
import com.mkc.service.ISupplierProductService;
import com.mkc.service.ISupplierRouteService;
import com.mkc.util.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 自然人身份信息核验相关接口
 *
 * @author tqlei
 * @date 2023/5/29 18:28
 */

@Slf4j
@Service
@DS("business")
public class CkServiceImpl implements ICkService {

	//供应商前缀
	private final static String SUP_PREFIX = "CK_";
	@Autowired
	private Map<String, ICkSupService> ckSupServiceMap;

	@Autowired
	private ISupplierRouteService supplierRouteService;

	@Autowired
	private ISupplierProductService supplierProductService;


	@Autowired
	private ReqLogHandle reqLogHandle;

	@Autowired
	private IMailProcess mailProcess;


	@Override
	public Result ckMobThree(MobThreeReqDTO params, MerReqLogDTO merLog) {

		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckMobThree(params, supQueryBean));
	}

	@Override
	public Result ckPopulationThree(PopulationThreeReqDTO params, MerReqLogDTO merLog) {

		return ckCommon(merLog, params, (ckSupService, supQueryBean) -> {
					try {
						return ckSupService.ckPopulationThree(params, supQueryBean);
					} catch (Exception e) {
						throw e;
					}
				}
		);
	}

	@Override
	public Result ckPersonCar(PersonCarReqDTO params, MerReqLogDTO merLog) {

		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckPersonCar(params, supQueryBean));
	}

	@Override
	public Result ckWorkUnit(WorkUnitReqDTO params, MerReqLogDTO merLog) {

		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckWorkUnit(params, supQueryBean));
	}


	private Result ckCommon(MerReqLogDTO merLog, BaseDTO vo, BiFunction<ICkSupService, SuplierQueryBean, SupResult> function) {

		SupResult supResult = ckCommonSup(merLog, vo, function);

		return getRespResult(merLog, supResult);

	}

	//将查询供应商方法迁移出来
	@Override
	public SupResult ckCommonSup(MerReqLogDTO merLog, BaseDTO vo, BiFunction<ICkSupService, SuplierQueryBean, SupResult> function) {

		String merCode = merLog.getMerCode();
		String productCode = merLog.getProductCode();
		String orderNo = merLog.getOrderNo();

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
				log.info("CK 此产品在供应商和没有可用供应商产品   supCode: {}, productCode: {} , orderNo: {}", supCode, productCode, orderNo);
				continue;
			}
			supQueryBean = newSupQueryBean;
			supQueryBean.setOrderNo(merLog.getOrderNo());
			String supProcessor = newSupQueryBean.getSupProcessor();

			log.info("==== CK 开始查询   supCode: {}, productCode: {} , orderNo: {}", supCode, productCode, orderNo);


			ICkSupService ckSupService = ckSupServiceMap.get(SUP_PREFIX + supProcessor);
			supResult = function.apply(ckSupService, supQueryBean);

			if (supResult == null) {
				supResult = new SupResult<>("", LocalDateTime.now());
				supResult.setRemark(ErrorConstants.SUP_NO_RESPONSE);

				String errMsg = " 【核验类】 查询供应商结果 NULL;   merCode {}, productCode {} ,supCode {} ,orderNo {}";
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
				log.info("【CK核验类】 查询供应商数据失败，走下一个供应商 supCode {} , orderNo {}", supCode, orderNo);
				continue;
			}
			break;

		}

		return supResult;

	}

	@Override
	public Result ckVehicleLicenseInfo(VehicleLicenseReqDTO params, MerReqLogDTO merLog) {

		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckVehicleLicenseInfo(params, supQueryBean));
	}

	@Override
	public Result ckBankFour(BankReqDTO params, MerReqLogDTO merLog) {

		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckBankFour(params, supQueryBean));
	}

	@Override
	public Result ckBankThree(BankReqDTO params, MerReqLogDTO merLog) {
		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckBankThree(params, supQueryBean));
	}

	@Override
	public Result ckBankTwo(BankReqDTO params, MerReqLogDTO merLog) {
		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckBankTwo(params, supQueryBean));
	}

	@Override
	public Result<ProQualifyCertResDTO> ckProQualifyCert(ProQualifyCertReqDTO params, MerReqLogDTO merLog) {
		return ckCommon(merLog, params, (ckSupService, supQueryBean) ->
				ckSupService.ckProQualifyCert(params, supQueryBean));
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
			result = Result.ok(supResult.getData(), orderNo);
			//判断是否 不一致
		} else if (supResult.isNot()) {
			result = Result.not(supResult.getData(), orderNo);
			//查无
		} else if (supResult.isNoGet()) {
			result = Result.no(supResult.getData(), FreeStatus.NO, orderNo);
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
		merLog.setFree(supResult.getFree().getCode());

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
