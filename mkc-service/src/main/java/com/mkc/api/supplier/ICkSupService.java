package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.bg.res.BankElementCheckResDTO;
import com.mkc.api.dto.ck.req.*;
import com.mkc.api.dto.ck.res.CurrentWorkResDTO;
import com.mkc.api.dto.ck.res.EnterpriseThreeElementsResDTO;
import com.mkc.api.dto.ck.res.ProQualifyCertResDTO;
import com.mkc.api.dto.ck.res.ResumeVerifyResDTO;
import com.mkc.bean.SuplierQueryBean;

import java.util.List;

public interface ICkSupService extends ISupService {

	default SupResult<CurrentWorkResDTO> currentWork(CurrentWorkReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	default SupResult<EnterpriseThreeElementsResDTO> enterpriseThreeElements(EnterpriseThreeElementsReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 行驶证核验
	 */
	default SupResult ckVehicleLicenseInfo(VehicleLicenseReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 银行卡四要素
	 */
	default SupResult<BankElementCheckResDTO> ckBankFour(BankReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 银行卡三要素
	 */
	default SupResult ckBankThree(BankReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 银行卡二要素
	 */
	default SupResult ckBankTwo(BankReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}


	/**
	 * 个人手机三要素-简版
	 *
	 * @param vo
	 * @param bean
	 * @return
	 */
	default SupResult ckMobThree(MobThreeReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 个人手机三要素-详版
	 *
	 * @param vo
	 * @param bean
	 * @return
	 */
	default SupResult ckMobThreePlus(MobThreeReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 人车核验
	 *
	 * @param vo
	 * @param bean
	 * @return
	 */
	default SupResult ckPersonCar(PersonCarReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 工作单位核验
	 *
	 * @param vo
	 * @param bean
	 * @return
	 */
	default SupResult ckWorkUnit(WorkUnitReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 全国⼈⼝身份信息三要素核验
	 */
	default SupResult ckPopulationThree(PopulationThreeReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 全国⼈⼝身份信息二要素核验
	 */
	default SupResult ckPopulationTwo(PopulationTwoReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}


	/**
	 * 全国人社部_技能人员职业资格证书核验数据元件接口
	 *
	 * @param dto
	 * @param bean
	 * @return
	 */
	default SupResult<List<ProQualifyCertResDTO>> ckProQualifyCert(ProQualifyCertReqDTO dto, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 当前履历核验
	 *
	 * @param dto
	 * @param bean
	 * @return
	 */
	default SupResult<ResumeVerifyResDTO> ckResumeVerify(ResumeVerifyReqDTO dto, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

}
