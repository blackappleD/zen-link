package com.mkc.controller;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.service.IBgService;
import com.mkc.api.vo.bg.*;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.CkMerBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 车五项
     */
    @PostMapping("/carFiveInfo")
    public Result carInfo(HttpServletRequest request, @RequestBody CarFiveInfoReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);
            //检查商户参数完整性
            CkMerBean ckMerBean = ckCarFiveInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_CAR_FIVE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = null;
            //bgService.queryHighSchoolEducationResultInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【车五项接口】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    private CkMerBean ckCarFiveInfoParams(CarFiveInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String carNo = params.getCarNo();

        if (StringUtils.isBlank(carNo)) {
            log.error("缺少参数 carNo {} , merCode： {}", carNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        String plaintext = merCode + carNo;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }


    /**
     * 高校学历核查结果查询接口
     */
    @PostMapping("/highSchoolEduResultInfo")
    public Result highSchoolEducationResultInfo(HttpServletRequest request, @RequestBody HighSchoolEducationResultInfoReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckHighSchoolEduResultInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryHighSchoolEducationResultInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【高校学历核查接口】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    private CkMerBean ckHighSchoolEduResultInfoParams(HighSchoolEducationResultInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String reqOrderNo = params.getReqOrderNo();

        if (StringUtils.isBlank(reqOrderNo)) {
            log.error("缺少参数 reqOrderNo {} , merCode： {}", reqOrderNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        String plaintext = merCode + reqOrderNo;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }

    /**
     * 婚姻状况
     */
    @PostMapping("/marriageResultInfo")
    public Result marriageResultInfo(HttpServletRequest request, @RequestBody MarriageInfoReqInfo params) {

        String reqJson = null;

        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckMarriageResultInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_MARRIAGE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryMarriageResultInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【婚姻状况接口】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    private CkMerBean ckMarriageResultInfoParams(MarriageInfoReqInfo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String sfzh = params.getSfzh();
        String xm = params.getXm();

        if (StringUtils.isBlank(sfzh)) {
            log.error("缺少参数 sfzh {} , merCode： {}", sfzh, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(xm)) {
            log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        String plaintext = merCode + xm + sfzh;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }

    /**
     * 高校学历核查接口
     */
    @PostMapping("/highSchoolEduInfo")
    public Result highSchoolEducationInfo(HttpServletRequest request, @RequestBody HighSchoolEducationInfoReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckHighSchoolEduInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryHighSchoolEducationInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【高校学历核查接口】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    private CkMerBean ckHighSchoolEduInfoParams(HighSchoolEducationInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String xm = params.getXm();
        String zsbh = params.getZsbh();

        if (StringUtils.isBlank(zsbh)) {
            log.error("缺少参数 zsbh {} , merCode： {}", zsbh, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (StringUtils.isBlank(xm)) {
            log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        String plaintext = merCode + xm + zsbh;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }

    /**
     * 全国高等学历信息查询
     */
    @PostMapping("/educationInfo")
    public Result educationInfo(HttpServletRequest request, @RequestBody EducationInfoReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckEducationInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_EDUCATION_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryEducationInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【全国高等学历信息查询】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    private CkMerBean ckEducationInfoParams(EducationInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String xm = params.getXm();
        String zjhm = params.getZjhm();
        if (StringUtils.isBlank(zjhm)) {
            log.error("缺少参数 zjhm {} , merCode： {}", zjhm, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (StringUtils.isBlank(xm)) {
            log.error("缺少参数 xm {} , merCode： {}", xm, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + xm + zjhm;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }


    /**
     * 行驶身份核验
     */
    @PostMapping("/drivingLicense")
    public Result drivingLicense(HttpServletRequest request, @RequestBody DrivingLicenseReqVo params) {

        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);
            //检查商户参数完整性
            CkMerBean ckMerBean = ckDrivingLicenseParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_DRIVING_LICENSE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryDrivingLicenseInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【行驶身份核验】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }


    /**
     * 经济能力评级---社保
     */
    @PostMapping("/economicRate")
    public Result economicRate(HttpServletRequest request, @RequestBody EconomicRateReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);
            //检查商户参数完整性
            CkMerBean ckMerBean = ckEconomicRateParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_ECONOMIC_RATE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryEconomicRateInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【经济能力评级】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }


    /**
     * 企业四要素
     *
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/enterpriseFourElementInfo")
    public Result enterpriseFourElementInfo(HttpServletRequest request, @RequestBody EnterpriseFourElementsReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckEnterpriseFourElementsParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_ENTERPRISE_FOUR_ELEMENT_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryFourElementsInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【企业四要素】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }


    }


    /**
     * 人车核验详版
     */
    @PostMapping("/personCarInfo")
    public Result personCarInfo(HttpServletRequest request, @RequestBody PersonCarDetailReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckPersonCarDetailInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_PERSON_CAR_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryPersonCarInfo(params, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【人车核验详版】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }


    /**
     * 确信分
     */
    @PostMapping("/sureScoreInfo")
    public Result sureScoreInfo(HttpServletRequest request, @RequestBody SureScoreInfoReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckSureScoreInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_SURE_SCORE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.querySureScoreInfo(params, merLog);
            return result;


        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【确信分】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    /**
     * 经济能力2W
     */
    @PostMapping("/financeInfo")
    public Result financeInfo(HttpServletRequest request, @RequestBody FinanceInfoReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckfinanceInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_FINANCE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryFinanceInfo(params, merLog);
            return result;

        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【经济能力2W】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    /**
     * 经济能力评级V3
     */
    @PostMapping("/financeInfoV3")
    public Result financeInfoV3(HttpServletRequest request, @RequestBody FinanceInfoV3ReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckfinanceInfoV3Params(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_FINANCE_INFO_V3.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);

            Result result = bgService.queryFinanceInfoV3(params, merLog);
            return result;

        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("【经济能力评级V3】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }


    /**
     * 不动产信息核查
     */
    @PostMapping("/houseReqInfo")
    public Result houseInfo(HttpServletRequest request, @RequestParam("files") List<MultipartFile> files,
                            @RequestParam("types") List<String> types, @RequestParam("persons") String persons,
                            @RequestParam("merCode") String merCode, @RequestParam("key") String key,
                            @RequestParam("merSeq") String merSeq, @RequestParam("sign") String sign) {
        HouseInfoReqVo houseInfoReqVo = new HouseInfoReqVo();
        houseInfoReqVo.setFiles(files);
        houseInfoReqVo.setTypes(types);
        houseInfoReqVo.setMerCode(merCode);
        houseInfoReqVo.setKey(key);
        houseInfoReqVo.setMerSeq(merSeq);
        houseInfoReqVo.setPersonsStr(persons);
        houseInfoReqVo.setSign(sign);
        String reqJson = null;
        try {
            List<PersonInfoReqVo> personInfoReqVos = JSONUtil.toList(persons, PersonInfoReqVo.class);
            houseInfoReqVo.setPersons(personInfoReqVos);
            reqJson = JSONUtil.toJsonStr(houseInfoReqVo);
            //检查商户参数完整性
            CkMerBean ckMerBean = ckHouseInfoParams(houseInfoReqVo);
            ckMerBean.setProductCode(ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);
            Result result = bgService.queryHouseInfo(houseInfoReqVo, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("不动产信息核查 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }


    }


    /**
     * 不动产信息核查
     */
    @PostMapping("/houseResultReqInfo")
    public Result houseResultInfo(HttpServletRequest request, @RequestBody HouseResultInfoReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckHouseResultInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);


            Result result = bgService.queryHouseResultInfo(params, merLog);

            return result;
        } catch (ApiServiceException e) {

            return Result.fail(e.getCode(), e.getMessage());

        } catch (Exception e) {
            errMonitorMsg("【不动产结果信息】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    /**
     * 车五项信息查询
     *
     * @return
     */
    @PostMapping("/carInfo")
    public Result carInfo(HttpServletRequest request, @RequestBody CarInfoReqVo params) {
        String reqJson = null;
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

            return Result.fail(e.getCode(), e.getMessage());

        } catch (Exception e) {
            errMonitorMsg("【车5项】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    /**
     * 行驶证信息查询
     *
     * @return
     */
    @PostMapping("/vehicleLicenseInfo")
    public Result vehicleLicenseInfo(HttpServletRequest request, @RequestBody VehicleLicenseReqVo params) {
        String reqJson = null;
        try {
            reqJson = JSON.toJSONString(params);

            //检查商户参数完整性
            CkMerBean ckMerBean = ckVehicleLicenseInfoParams(params);
            ckMerBean.setProductCode(ProductCodeEum.BG_VEHICLE_LICENSE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);


            Result result = bgService.queryVehicleLicenseInfo(params, merLog);

            return result;
        } catch (ApiServiceException e) {

            return Result.fail(e.getCode(), e.getMessage());

        } catch (Exception e) {
            errMonitorMsg("【行驶证信息查询】API 发生异常  reqJson {} ", reqJson, e);
            return Result.fail();
        }
    }

    private CkMerBean ckHouseResultInfoParams(HouseResultInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String reqOrderNo = params.getReqOrderNo();
        if (StringUtils.isBlank(reqOrderNo)) {
            log.error("缺少参数 reqOrderNo {} , merCode： {}", reqOrderNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + reqOrderNo;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckDrivingLicenseParams(DrivingLicenseReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String licensePlateNo = params.getLicensePlateNo();
        String name = params.getName();
        String plateType = params.getPlateType();
        if (StringUtils.isBlank(licensePlateNo)) {
            log.error("缺少参数 licensePlateNo {} , merCode： {}", licensePlateNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(plateType)) {
            log.error("缺少参数 plateType {} , merCode： {}", plateType, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + licensePlateNo + name + plateType;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }

    public static void main(String[] args) {

        String xm = "林舒婷";
        String sfzh = "330381199910181122";

        String merCode = "BhCpTest";
        String pwd = "e0be01493778d77ecfd2004f54b41a09";
//        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String plaintext = merCode + xm + sfzh;
        String signText = plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        System.err.println(signMd5);
    }

    private CkMerBean ckEconomicRateParams(EconomicRateReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String idCard = params.getIdCard();
        String name = params.getName();
        String mobile = params.getMobile();
        if (StringUtils.isBlank(idCard)) {
            log.error("缺少参数 cid {} , merCode： {}", idCard, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(mobile)) {
            log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + idCard + name + mobile;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());
    }


    private CkMerBean ckEnterpriseFourElementsParams(EnterpriseFourElementsReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String companyName = params.getCompanyName();
        String creditCode = params.getCreditCode();
        String legalPerson = params.getLegalPerson();
        String certNo = params.getCertNo();

        if (StringUtils.isBlank(companyName)) {
            log.error("缺少参数 companyName {} , merCode： {}", companyName, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(creditCode)) {
            log.error("缺少参数 creditCode {} , merCode： {}", creditCode, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(legalPerson)) {
            log.error("缺少参数 creditCode {} , merCode： {}", legalPerson, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(certNo)) {
            log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (!IdcardUtil.isValidCard(certNo)) {
            log.error("缺少不正确 personId {} , merCode： {}", certNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_009);
        }


        String plaintext = merCode + companyName + creditCode + legalPerson + certNo;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckPersonCarDetailInfoParams(PersonCarDetailReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String cid = params.getCid();
        String name = params.getName();
        String plateNo = params.getPlateNo();

        if (StringUtils.isBlank(cid)) {
            log.error("缺少参数 idCard {} , merCode： {}", cid, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(plateNo)) {
            log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + cid + plateNo + name;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckSureScoreInfoParams(SureScoreInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String cid = params.getCid();
        String name = params.getName();
        String mobile = params.getMobile();
        String job = params.getJob();

        if (StringUtils.isBlank(cid)) {
            log.error("缺少参数 idCard {} , merCode： {}", cid, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (!IdcardUtil.isValidCard(cid)) {
            log.error("无效参数 idCard {} , merCode： {}", cid, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_009);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(mobile)) {
            log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(job)) {
            log.error("缺少参数 job {} , merCode： {}", job, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + cid + mobile + name + job;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckfinanceInfoV3Params(FinanceInfoV3ReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String idCard = params.getIdCard();
        String name = params.getName();
        String mobile = params.getMobile();

        if (StringUtils.isBlank(idCard)) {
            log.error("缺少参数 idCard {} , merCode： {}", idCard, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(mobile)) {
            log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + idCard + name + mobile;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckfinanceInfoParams(FinanceInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String idCard = params.getIdCard();
        String name = params.getName();
        String mobile = params.getMobile();

        if (StringUtils.isBlank(idCard)) {
            log.error("缺少参数 idCard {} , merCode： {}", idCard, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        if (StringUtils.isBlank(mobile)) {
            log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + idCard + name + mobile;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckCarInfoParams(CarInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String plateNo = params.getPlateNo();
        String plateType = params.getPlateType();
        if (StringUtils.isBlank(plateNo)) {
            log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + plateNo;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckVehicleLicenseInfoParams(VehicleLicenseReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        String plateNo = params.getPlateNo();
        String plateType = params.getPlateType();
        String name = params.getName();
        if (StringUtils.isBlank(plateNo)) {
            log.error("缺少参数 plateNo {} , merCode： {}", plateNo, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (StringUtils.isBlank(plateType)) {
            log.error("缺少参数 plateType {} , merCode： {}", plateType, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (StringUtils.isBlank(name)) {
            log.error("缺少参数 name {} , merCode： {}", name, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }


        String plaintext = merCode + name + plateNo + plateType;

        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }

    private CkMerBean ckHouseInfoParams(HouseInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        List<String> types = params.getTypes();
        List<PersonInfoReqVo> persons = params.getPersons();
        if (types == null || types.size() == 0) {
            log.error("缺少参数 types {} , merCode： {}", types, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (persons == null || persons.size() == 0) {
            log.error("缺少参数 persons {} , merCode： {}", persons, merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (params.getFiles() == null || params.getFiles().size() == 0) {
            log.error("缺少参数 files {}, merCode： {}", params.getFiles(), merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (params.getFiles().size() != types.size()) {
            log.error("缺少参数 files().size() {}, types.size() {}, merCode： {}", persons.size(), types.size(), merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        for (PersonInfoReqVo personInfoReqVo : persons) {
            if (StringUtils.isBlank(personInfoReqVo.getName())) {
                log.error("缺少参数 personInfoReqVo.getName {} , merCode： {}", personInfoReqVo.getName(), merCode);
                throw new ApiServiceException(ApiReturnCode.ERR_001);
            }
            if (StringUtils.isBlank(personInfoReqVo.getCardNum())) {
                log.error("缺少参数 personInfoReqVo.getCardNum {} , merCode： {}", personInfoReqVo.getCardNum(), merCode);
                throw new ApiServiceException(ApiReturnCode.ERR_001);
            }
        }

        String plaintext = merCode + params.getPersonsStr();
        return new CkMerBean(merCode, key, plaintext, sign, params.getMerSeq());

    }


}
