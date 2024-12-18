package com.mkc.controller;

import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.service.ISfService;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.api.dto.sf.DishonestExecutiveReqDTO;
import com.mkc.api.dto.sf.RestrictedConsumerReqDTO;
import com.mkc.bean.CkMerBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/28 15:25
 */
@Slf4j
@RestController
@RequestMapping("/sf")
public class SfController extends BaseController {

    @Autowired
    private ISfService sfService;

    /**
     * 【司法】失信被执行人
     */
    @PostMapping("/dishonestExecutive")
    public Result dishonestExecutive(HttpServletRequest request, @RequestBody DishonestExecutiveReqDTO params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);
            //检查商户参数完整性
            CkMerBean ckMerBean = sfDishonestExecutiveParams(params);
            ckMerBean.setProductCode(ProductCodeEum.SF_DISHONEST_EXECUTIVE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogDTO merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = sfService.queryDishonestExecutiveInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【失信被执行人】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }
    }

    private CkMerBean sfDishonestExecutiveParams(DishonestExecutiveReqDTO params) {
        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        sfCommonParams(params);

        String certNo = params.getCertNo();
        String certName = params.getCertName();
        String type = params.getType();

        if (StringUtils.isBlank(certNo)) {
            log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(certName)) {
            log.error("缺少参数 certName {} , merCode： {}", certName, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(type)) {
            log.error("缺少参数 type {} , merCode： {}", type, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + certName + certNo ;
        return new CkMerBean(merCode, key, plaintext, sign,params.getMerSeq());
    }

    /**
     * 【【司法】限制高消费被执行人接口
     */
    @PostMapping("/restrictedConsumer")
    public Result restrictedConsumer(HttpServletRequest request, @RequestBody RestrictedConsumerReqDTO params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);
            //检查商户参数完整性
            CkMerBean ckMerBean = sfRestrictedConsumerParams(params);
            ckMerBean.setProductCode(ProductCodeEum.SF_RESTRICTED_CONSUMER_INFO.getCode());

            //检查商户参数有效性
            MerReqLogDTO merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = sfService.queryRestrictedConsumerInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【失信被执行人】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }
    }

    private CkMerBean sfRestrictedConsumerParams(RestrictedConsumerReqDTO params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        sfCommonParams(params);

        String certNo = params.getCertNo();
        String certName = params.getCertName();
        String type = params.getType();

        if (StringUtils.isBlank(certNo)) {
            log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(certName)) {
            log.error("缺少参数 certName {} , merCode： {}", certName, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(type)) {
            log.error("缺少参数 type {} , merCode： {}", type, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + certName + certNo ;
        return new CkMerBean(merCode, key, plaintext, sign,params.getMerSeq());

    }

    public void sfCommonParams(BaseDTO params) {
        String merCode = params.getMerCode();
        String sign = params.getSign();
        String key = params.getKey();
        String merSeq = params.getMerSeq();

        if (StringUtils.isBlank(merCode)) {
            log.error("缺少参数 merCode {}",merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(key)) {
            log.error("缺少参数 merCode {}, key {}", merCode, key);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(sign)){
            log.error("缺少参数 merCode {} , sign {}", merCode, sign);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if(StringUtils.isBlank(merSeq)){
            log.error("缺少参数 merCode {} , merSeq {}", merCode, merSeq);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if(merSeq.length()>32){
            log.error("无效的参数 merCode {} , merSeq {}", merCode, merSeq);
            throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(), "无效的商户请求流水号");
        }
    }


}
