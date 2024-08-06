package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.vo.bg.CarInfoReqVo;
import com.mkc.api.vo.bg.FinanceInfoReqVo;
import com.mkc.api.vo.bg.HouseInfoReqVo;
import com.mkc.api.vo.bg.HouseResultInfoReqVo;
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



}
