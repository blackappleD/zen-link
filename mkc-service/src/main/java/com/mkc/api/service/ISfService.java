package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.bg.res.EnterpriseLitigationResDTO;
import com.mkc.api.dto.bg.res.PersonLitigationResDTO;
import com.mkc.api.dto.bg.res.SsPlusResDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.dto.sf.*;
import com.mkc.api.supplier.ISfSupService;
import com.mkc.bean.SuplierQueryBean;

import java.util.function.BiFunction;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/29 15:01
 */
public interface ISfService {

	public SupResult sfCommonSup(MerReqLogDTO merLog, BaseDTO vo, BiFunction<ISfSupService, SuplierQueryBean, SupResult> function);

	/**
	 * 【司法】限制高消费被执行人接口
	 */
	public Result queryRestrictedConsumerInfo(RestrictedConsumerReqDTO params, MerReqLogDTO merLog);


	/**
	 * 【司法】失信被执行人
	 */
	public Result queryDishonestExecutiveInfo(DishonestExecutiveReqDTO params, MerReqLogDTO merLog);

	/**
	 * 【司法】司法涉诉公开版
	 *
	 * @param params
	 * @param merLog
	 * @return
	 */
	Result<SsPlusResDTO> querySsPlus(SsPlusReqDTO params, MerReqLogDTO merLog);

	Result<PersonLitigationResDTO> personLitigation(PersonLitigationReqDTO params, MerReqLogDTO merLog);

	Result<EnterpriseLitigationResDTO> enterpriseLitigation(EnterpriseLitigationReqDTO params, MerReqLogDTO merLog);
}
