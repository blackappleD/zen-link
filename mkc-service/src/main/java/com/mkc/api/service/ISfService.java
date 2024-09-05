package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.ISfSupService;
import com.mkc.api.vo.BaseVo;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.api.vo.sf.DishonestExecutiveReqVo;
import com.mkc.api.vo.sf.RestrictedConsumerReqVo;
import com.mkc.bean.SuplierQueryBean;

import java.util.function.BiFunction;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/29 15:01
 */
public interface ISfService {

    public SupResult sfCommonSup(MerReqLogVo merLog, BaseVo vo, BiFunction<ISfSupService, SuplierQueryBean, SupResult> function);

    /**
     * 【司法】限制高消费被执行人接口
     */
    public Result queryRestrictedConsumerInfo(RestrictedConsumerReqVo params, MerReqLogVo merLog);


    /**
     * 【司法】失信被执行人
     */
    public Result queryDishonestExecutiveInfo(DishonestExecutiveReqVo params, MerReqLogVo merLog);
}
