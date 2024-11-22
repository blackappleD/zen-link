package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.vo.ck.*;
import com.mkc.bean.SuplierQueryBean;

public interface ICkSupService extends ISupService{


    /**
     * 行驶证核验
     */
    default public SupResult ckVehicleLicenseInfo(VehicleLicenseReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 银行卡四要素
     */
    default public SupResult ckBankFour(BankReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }
    /**
     * 银行卡三要素
     */
    default public SupResult ckBankThree(BankReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }
    /**
     * 银行卡二要素
     */
    default public SupResult ckBankTwo(BankReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


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
