package com.mkc.api.service;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.vo.BaseVo;
import com.mkc.api.vo.ck.*;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.SuplierQueryBean;

import java.util.function.BiFunction;

/**
 * 核验类 API 接口
 *
 * @author tqlei
 * @date 2023/4/27 17:52
 */

public interface ICkService {

    public SupResult ckCommonSup(MerReqLogVo merLog, BaseVo vo, BiFunction<ICkSupService, SuplierQueryBean, SupResult> function);


    public Result ckVehicleLicenseInfo(VehicleLicenseReqVo params, MerReqLogVo merLog);


    /**
     * 个人手机三要素认证
     *
     * @param params
     * @return
     */
    public Result ckMobThree(MobThreeReqVo params, MerReqLogVo merLog);

    /**
     * 人车核验
     *
     * @param params
     * @return
     */
    public Result ckPersonCar(PersonCarReqVo params, MerReqLogVo merLog);

    /**
     * 工作单位核验
     *
     * @param params
     * @return
     */
    public Result ckWorkUnit(WorkUnitReqVo params, MerReqLogVo merLog);

    /**
     * 全国⼈⼝身份信息三要素核验
     *
     * @param params
     * @return
     */
    public Result ckPopulationThree(PopulationThreeReqVo params, MerReqLogVo merLog);

    /**
     * 银行卡四要素核验
     *
     * @param params
     * @param merLog
     * @return
     */
    Result ckBankFour(BankReqVo params, MerReqLogVo merLog);

    /**
     * 银行卡三要素核验
     *
     * @param params
     * @param merLog
     * @return
     */
    Result ckBankThree(BankReqVo params, MerReqLogVo merLog);


    /**
     * 银行卡二要素核验
     *
     * @param params
     * @param merLog
     * @return
     */
    Result ckBankTwo(BankReqVo params, MerReqLogVo merLog);
}
