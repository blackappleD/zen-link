package com.mkc.api.handle;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.bean.SuplierQueryBean;

/**
 * @author tqlei
 * @date 2023/5/24 11:51
 */

public interface  ReqLogHandle {


    public void  supReqLogHandle(SupResult supResult, MerReqLogDTO merReqLog, SuplierQueryBean supQueryBean);


    public void  merReqLogHandle(MerReqLogDTO merLog, SupResult supResult);

}