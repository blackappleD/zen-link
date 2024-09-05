package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.vo.BaseVo;
import com.mkc.api.vo.bg.*;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.SuplierQueryBean;

import java.util.function.BiFunction;

/**
 * 核验类 API 接口
 * @author tqlei
 * @date 2023/4/27 17:52
 */

public interface IBgService {

    public SupResult ckCommonSup(MerReqLogVo merLog, BaseVo vo, BiFunction<IBgSupService, SuplierQueryBean, SupResult> function);



    /**
     * 车五项信息查询
     * @param params
     * @return
     */
    public Result queryCarInfo(CarInfoReqVo params, MerReqLogVo merLog);

    /**
     * 行驶证信息查询
     * @param params
     * @return
     */
    public Result queryVehicleLicenseInfo(VehicleLicenseReqVo params, MerReqLogVo merLog);

    /**
     * 不动产信息查询
     * @param params
     * @return
     */
    public Result queryHouseInfo(HouseInfoReqVo params, MerReqLogVo merLog);

    /**
     * 不动产结果信息查询
     * @param params
     * @return
     */
    public Result queryHouseResultInfo(HouseResultInfoReqVo params, MerReqLogVo merLog);

    /**
     * 经济能力2w信息查询
     * @param params
     * @return
     */
    public Result queryFinanceInfo(FinanceInfoReqVo params, MerReqLogVo merLog);

    /**
     * 经济能力评级V3
     * @param params
     * @return
     */
    public Result queryFinanceInfoV3(FinanceInfoV3ReqVo params, MerReqLogVo merLog);

    /**
     * 确信分信息查询
     * @param params
     * @return
     */
    public Result querySureScoreInfo(SureScoreInfoReqVo params, MerReqLogVo merLog);

    /**
     * 人车核验详版
     * @param params
     * @return
     */
    public Result queryPersonCarInfo(PersonCarDetailReqVo params, MerReqLogVo merLog);

    /**
     * 企业四要素核验
     * @param params
     * @return
     */
    public Result queryFourElementsInfo(EnterpriseFourElementsReqVo params, MerReqLogVo merLog);

    /**
     * 经济能力评级
     * @param params
     * @return
     */
    public Result queryEconomicRateInfo(EconomicRateReqVo params, MerReqLogVo merLog);

    /**
     * 行驶身份核验
     * @param params
     * @return
     */
    public Result queryDrivingLicenseInfo(DrivingLicenseReqVo params, MerReqLogVo merLog);
}
