package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.vo.bg.CarInfoReqVo;
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



}
