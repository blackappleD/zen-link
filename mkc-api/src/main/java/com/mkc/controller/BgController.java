package com.mkc.controller;

import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.service.IBgService;
import com.mkc.api.vo.bg.CarInfoReqVo;
import com.mkc.api.vo.common.MerReqLogVo;
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
 * 报告信息查询相关类入口
 *
 * @author tqlei
 * @date 2023/5/29 17:45
 */

@Slf4j
@RestController
@RequestMapping("/bg")
public class BgController extends BaseController {

    @Autowired
    private IBgService bgService;


    /**
     * 车五项信息查询
     *
     * @return
     */
    @PostMapping("/carInfo")
    public Result carInfo(HttpServletRequest request, @RequestBody CarInfoReqVo params) {
        String reqJson =null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckCarInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_CAR_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);


            Result result = bgService.queryCarInfo(params, merLog);

            return result;
        } catch (ApiServiceException e) {

            return Result.fail(e.getCode(),e.getMessage());

        } catch (Exception e) {
            errMonitorMsg("【个人手机三要素认证】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }
    }


    private CkMerBean ckCarInfoParams(CarInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String plateNo = params.getPlateNo();
        String plateType = params.getPlateType();
        if (StringUtils.isBlank(plateNo)) {
            log.error("缺少参数 plateNo {} , merCode： {}", plateNo,merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + plateNo ;

        return new CkMerBean(merCode, key, plaintext, sign,params.getMerSeq());

    }










}
