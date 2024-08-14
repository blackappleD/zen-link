package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.vo.bg.*;
import com.mkc.bean.SuplierQueryBean;

public interface IBgSupService extends ISupService{


    /**
     * 车辆信息5三要素-简版
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryCarInfo(CarInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 人车核验详版
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryPersonCarInfo(PersonCarDetailReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 不动产信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHouseInfo(HouseInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 不动产结果信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHouseResultInfo(HouseResultInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 不动产结果信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryFinanceInfo(FinanceInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 确信分信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult querySureScoreInfo(SureScoreInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


    /**
     * 行驶证信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryVehicleLicenseInfo(VehicleLicenseReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }



}
