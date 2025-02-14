package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.bg.res.SsPlusResDTO;
import com.mkc.api.dto.sf.DishonestExecutiveReqDTO;
import com.mkc.api.dto.sf.RestrictedConsumerReqDTO;
import com.mkc.api.dto.sf.SsPlusReqDTO;
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

}
