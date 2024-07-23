package com.mkc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.OperatorInfo;
import com.mkc.api.common.constant.enums.YysCode;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.service.IPhoneService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tqlei
 * @date 2023/11/15 17:20
 */
@Service
@DS("business")
@Slf4j
public class PhoneServiceImpl implements IPhoneService {



    @Autowired
    private RedisCache redisCache;

    @Override
    public YysCode queryOperatorByPhone(String phone) {

        if (StringUtils.isBlank(phone)) {
            throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(),"无效的参数");
        }
        if (phone.length() !=11) {
           return YysCode.CM;
        }
      //  String phonePrefix=phone.substring(0,7);

        YysCode yysCode = OperatorInfo.getPhoneOperator(phone);
        return yysCode;

    }





    public static void main(String[] args) {
        System.out.println("1234567890".substring(0,7));
    }
}
