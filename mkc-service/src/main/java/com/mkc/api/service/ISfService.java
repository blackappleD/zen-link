package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.ISfSupService;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.dto.sf.DishonestExecutiveReqDTO;
import com.mkc.api.dto.sf.RestrictedConsumerReqDTO;
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
}
