package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.ck.req.*;
import com.mkc.api.dto.ck.res.ResumeVerifyResDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.dto.ck.res.ProQualifyCertResDTO;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.bean.SuplierQueryBean;

import java.util.function.BiFunction;

/**
 * 核验类 API 接口
 *
 * @author tqlei
 * @date 2023/4/27 17:52
 */

public interface ICkService {

	public SupResult ckCommonSup(MerReqLogDTO merLog, BaseDTO vo, BiFunction<ICkSupService, SuplierQueryBean, SupResult> function);


	public Result ckVehicleLicenseInfo(VehicleLicenseReqDTO params, MerReqLogDTO merLog);


	/**
	 * 个人手机三要素认证
	 *
	 * @param params
	 * @return
	 */
	public Result ckMobThree(MobThreeReqDTO params, MerReqLogDTO merLog);

	/**
	 * 人车核验
	 *
	 * @param params
	 * @return
	 */
	public Result ckPersonCar(PersonCarReqDTO params, MerReqLogDTO merLog);

	/**
	 * 工作单位核验
	 *
	 * @param params
	 * @return
	 */
	public Result ckWorkUnit(WorkUnitReqDTO params, MerReqLogDTO merLog);

	/**
	 * 全国⼈⼝身份信息三要素核验
	 *
	 * @param params
	 * @return
	 */
	public Result ckPopulationThree(PopulationThreeReqDTO params, MerReqLogDTO merLog);

	/**
	 * 全国⼈⼝身份信息二要素核验
	 *
	 * @param params
	 * @return
	 */
	Result ckPopulationTwo(PopulationTwoReqDTO params, MerReqLogDTO merLog);


	/**
	 * 银行卡四要素核验
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result ckBankFour(BankReqDTO params, MerReqLogDTO merLog);

	/**
	 * 银行卡三要素核验
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result ckBankThree(BankReqDTO params, MerReqLogDTO merLog);


	/**
	 * 银行卡二要素核验
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result ckBankTwo(BankReqDTO params, MerReqLogDTO merLog);

	/**
	 * 全国人社部_技能人员职业资格证书核验数据元件接口
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result<ProQualifyCertResDTO> ckProQualifyCert(ProQualifyCertReqDTO params, MerReqLogDTO merLog);

	/**
	 * 当前履历核验
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result<ResumeVerifyResDTO> ckResumeVerify(ResumeVerifyReqDTO params, MerReqLogDTO merLog);


}
