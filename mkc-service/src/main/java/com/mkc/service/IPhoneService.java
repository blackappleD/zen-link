package com.mkc.service;

import com.mkc.api.common.constant.enums.YysCode;

/**
 * @author tqlei
 * @date 2023/11/15 17:15
 */

public interface IPhoneService {

    /**
     * 根据手机号查询运营商
     * @param phone
     * @return
     */
    public YysCode queryOperatorByPhone(String phone);

}
