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
 * @author tqlei
 * @date 2023/4/27 17:52
 */

public interface ICkService {

    public SupResult ckCommonSup(MerReqLogVo merLog, BaseVo vo, BiFunction<ICkSupService, SuplierQueryBean, SupResult> function);



    /**
     * 个人手机三要素认证
     * @param params
     * @return
     */
    public Result ckMobThree(MobThreeReqVo params, MerReqLogVo merLog);

    /**
     * 人车核验
     * @param params
     * @return
     */
    public Result ckPersonCar(PersonCarReqVo params, MerReqLogVo merLog);

}
