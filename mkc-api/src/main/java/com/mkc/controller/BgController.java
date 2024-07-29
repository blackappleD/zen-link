package com.mkc.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.service.IBgService;
import com.mkc.api.vo.bg.CarInfoReqVo;
import com.mkc.api.vo.bg.HouseInfoReqVo;
import com.mkc.api.vo.bg.HouseResultInfoReqVo;
import com.mkc.api.vo.bg.PersonInfoReqVo;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.CkMerBean;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
     *
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
        List<PersonInfoReqVo> personInfoReqVos = JSONUtil.toList(persons, PersonInfoReqVo.class);
        houseInfoReqVo.setPersons(personInfoReqVos);
        String reqJson = null;
        try {
            reqJson = JSONUtil.toJsonStr(houseInfoReqVo);
            //检查商户参数完整性
            CkMerBean ckMerBean = ckHouseInfoParams(houseInfoReqVo);
            ckMerBean.setProductCode(ProductCodeEum.BG_HOUSE_INFO.getCode());

            //检查商户参数有效性
            MerReqLogVo merLog = ckMer(request, ckMerBean);
            merLog.setReqJson(reqJson);
            Result result = bgService.queryHouseInfo(houseInfoReqVo, merLog);
            return result;
        } catch (ApiServiceException e) {
            return Result.fail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            errMonitorMsg("不动产信息核查 发生异常  reqJson {} ", reqJson,e);
            return Result.fail();
        }


    }

    /**
     *
     * 不动产信息核查
     */
    @PostMapping("/houseResultReqInfo")
    public Result houseResultInfo(HttpServletRequest request, @RequestBody HouseResultInfoReqVo params) {
        String reqJson =null;
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

            return Result.fail(e.getCode(),e.getMessage());

        } catch (Exception e) {
            errMonitorMsg("【不动产结果信息】API 发生异常  reqJson {} ", reqJson,e);
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
            errMonitorMsg("【车5项】API 发生异常  reqJson {} ", reqJson,e);
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
            log.error("缺少参数 reqOrderNo {} , merCode： {}", reqOrderNo,merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }

        String plaintext = merCode + reqOrderNo ;

        return new CkMerBean(merCode, key, plaintext, sign,params.getMerSeq());

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

    private CkMerBean ckHouseInfoParams(HouseInfoReqVo params) {

        String merCode = params.getMerCode();

        String sign = params.getSign();
        String key = params.getKey();

        ckCommonParams(params);

        List<String> types = params.getTypes();
        List<PersonInfoReqVo> persons = params.getPersons();
        if (types == null || types.size() == 0) {
            log.error("缺少参数 types {} , merCode： {}", types,merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (persons == null || persons.size() == 0) {
            log.error("缺少参数 persons {} , merCode： {}", persons,merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (params.getFiles() == null || params.getFiles().size() == 0) {
            log.error("缺少参数 files {}, merCode： {}", params.getFiles(), merCode);
            throw new ApiServiceException(ApiReturnCode.ERR_001);
        }
        if (persons.size() != types.size()) {
            log.error("缺少参数 persons.size() {}, types.size() {}, merCode： {}", persons.size(), types.size(), merCode);
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

        String plaintext = merCode + params.getPersonsStr() ;
        return new CkMerBean(merCode, key, plaintext, sign,params.getMerSeq());

    }










}
