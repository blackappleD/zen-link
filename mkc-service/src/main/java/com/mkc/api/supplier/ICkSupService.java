package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.vo.ck.*;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.SuplierQueryBean;

public interface ICkSupService extends ISupService{


    /**
     * 个人手机三要素-简版
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult ckMobThree(MobThreeReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 人车核验
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult ckPersonCar(PersonCarReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 工作单位核验
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult ckWorkUnit(WorkUnitReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     *全国⼈⼝身份信息三要素核验
     */
    default public SupResult ckPopulationThree(PopulationThreeReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


}
