package com.mkc.controller;

import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.constant.enums.ReqParamType;
import com.mkc.api.common.constant.enums.YysProductCode;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.service.ICkService;
import com.mkc.api.vo.ck.*;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.CkMerBean;
import com.mkc.tool.RegTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 信息核验相关类入口
 *
 * @author tqlei
 * @date 2023/5/29 17:45
 */

@Slf4j
@RestController
@RequestMapping("/ck")
public class CkController extends BaseController {

    @Autowired
    private ICkService ckService;

    /**
     *全国⼈⼝身份信息三要素核验
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/populationThree")
    public Result populationThree(HttpServletRequest request, @RequestBody PopulationThreeReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckPopulationThreeParams(params);
            ckMerBean.setProductCode(ProductCodeEum.CK_POPULATION_THREE.getCode());


            return null;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【全国⼈⼝身份信息三要素核验】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }
    }

    @PostMapping("/workUnitVerify")
    public Result workUnitVerify(HttpServletRequest request, @RequestBody WorkUnitReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckWorkUnitInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.CK_WORK_UNIT.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = ckService.ckWorkUnit(params, merLog);

            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【工作单位】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }
    }

    private CkMerBean ckPopulationThreeParams(PopulationThreeReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String name = params.getName();
        String authorization = params.getAuthorization();
        String photo = params.getPhoto();
        String idcard = params.getIdcard();

        if (StringUtils.isBlank(idcard)) {
            log.error("缺少参数 cid {} , merCode： {}", idcard, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(authorization)) {
            log.error("缺少参数 workplace {} , merCode： {}", authorization, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + name + photo + authorization +idcard;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }

    private CkMerBean ckWorkUnitInfoParams(WorkUnitReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String name = params.getName();
        String cid = params.getCid();
        String workplace = params.getWorkplace();

        if (StringUtils.isBlank(cid)) {
            log.error("缺少参数 cid {} , merCode： {}", cid, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(workplace)) {
            log.error("缺少参数 workplace {} , merCode： {}", workplace, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + name + cid + workplace;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }

    @PostMapping("/personCarVerify")
    public Result personCarVerify(HttpServletRequest request, @RequestBody PersonCarReqVo params) {

        String reqJson = null;

        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckPersonCarInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.CK_PERSON_CAR.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = ckService.ckPersonCar(params, merLog);

            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【人车核验】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }

    }

    private CkMerBean ckPersonCarInfoParams(PersonCarReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String name = params.getName();
        String plateNo = params.getPlateNo();

        if (StringUtils.isBlank(plateNo)) {
            log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 plateNo {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + name + plateNo;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }




    /**
     * 个人手机三要素认证
     *
     * @return
     */
    @PostMapping("/mobileThree")
    public Result mobileThree(HttpServletRequest request, @RequestBody MobThreeReqVo params) {
        String reqJson =null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckThreeParams(params);
            YysProductCode yysProduct=getYysProductCode(params.getMobile(),ProductCodeEum.CK_MOB_THREE);
            ckMerBean.setProductCode(yysProduct.getMobThreeYysProductCode());
           // ckMerBean.setProductCode(ProductCodeEum.CK_MOB_THREE.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);
            merLog.setReqProductCode(yysProduct.getMobReqProductCode());
            merLog.setYysProductCode(yysProduct);

            Result result = ckService.ckMobThree(params, merLog);

            return result;
        } catch (ApiServiceException e) {

            return Result.fail(e.getCode(),e.getMessage());

        } catch (Exception e) {
            errMonitorMsg("【个人手机三要素认证】API 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }
    }


    private CkMerBean ckThreeParams(MobThreeReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String certName = params.getCertName();
        String certNo = params.getCertNo();
        String mobile = params.getMobile();

        ckCommonThree(merCode,certName, certNo,mobile,params.getParamType());

        String plaintext = merCode + certName + certNo + mobile;

        return new CkMerBean(merCode, key, plaintext, sign,params.getMerSeq());

    }

    private void ckCommonThree(String merCode,String certName, String certNo,String mobile,String paramType) {

        if (StringUtils.isBlank(mobile)) {
            log.error("缺少参数 mobile {}, merCode:  {}", mobile,merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        //判断类型为MD5
        if(ReqParamType.isMd5Type(paramType)){
            if (!RegTool.checkMd5(certName) && !ckCertName(merCode,certName)){
                log.error("无效的参数 MD5 certName {} , merCode:  {}", certName,merCode);
                throw new ApiServiceException(ApiReturnCode.ERR_009);
            }
            if (!RegTool.checkMd5(certNo) && !ckCertNo(merCode,certNo)){
                log.error("无效的参数 MD5 certNo {} , merCode:  {}", certNo,merCode);
                throw new ApiServiceException(ApiReturnCode.ERR_009);
            }
            if(!RegTool.checkMd5(mobile) && !ckMobile(merCode,mobile)){
                log.error("无效的参数 MD5 mobile {}, merCode:  {}", mobile,merCode);
                throw new ApiServiceException(ApiReturnCode.ERR_009);
            }
            return;
        }
        ckCertNo(merCode,certNo);
        ckCertName(merCode,certName);
        ckMobile(merCode,mobile);

    }










}
