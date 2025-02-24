package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.bg.res.EnterpriseLitigationResDTO;
import com.mkc.api.dto.bg.res.PersonLitigationResDTO;
import com.mkc.api.dto.bg.res.SsPlusResDTO;
import com.mkc.api.dto.sf.*;
import com.mkc.bean.SuplierQueryBean;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/28 15:38
 */
public interface ISfSupService extends ISupService {

	/**
	 * 限制高消费被执行人接口
	 *
	 * @param vo
	 * @param bean
	 * @return
	 */
	default public SupResult queryRestrictedConsumerInfo(RestrictedConsumerReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 【司法】失信被执行人
	 *
	 * @param vo
	 * @param bean
	 * @return
	 */
	default public SupResult queryDishonestExecutiveInfo(DishonestExecutiveReqDTO vo, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 【司法】司法涉诉公开版
	 *
	 * @param dto
	 * @param bean
	 * @return
	 */
	default SupResult<SsPlusResDTO> querySsPlus(SsPlusReqDTO dto, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 【司法】全国自然人司法模型服务查询
	 *
	 * @param dto
	 * @param bean
	 * @return
	 */
	default SupResult<PersonLitigationResDTO> personLitigation(PersonLitigationReqDTO dto, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

	/**
	 * 【司法】全国企业司法模型服务查询
	 *
	 * @param dto
	 * @param bean
	 * @return
	 */
	default SupResult<EnterpriseLitigationResDTO> enterpriseLitigation(EnterpriseLitigationReqDTO dto, SuplierQueryBean bean) {
		return SupResult.supNotSupport();
	}

}
