package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.vo.bg.EducationInfoReqVo;
import com.mkc.api.vo.bg.MaritalRelationshipReqVo;
import com.mkc.api.vo.bg.MaritalStabilityReqVo;
import com.mkc.api.vo.bg.MarriageInfoReqInfo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.sun.javafx.collections.MappingChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XIEWEI
 * @date 2024/9/20 15:15
 */
@Service("BG_ZDZZ")
@Slf4j
public class ZdzzBgSupImpl implements IBgSupService {

    private final static String SUCCESS = "200";

    @Override
    public SupResult<JSONObject> queryMaritalStability(MaritalStabilityReqVo vo, SuplierQueryBean bean) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("xm", vo.getXm());
        queryParams.put("sfzh", vo.getSfzh());
        queryParams.put("yearNum", vo.getYearNum());
        String url = bean.getUrl() + "/api0e148e05297f41cbace8feddaa53d47d";
        bean.setUrl(url);
        String result = null;
        SupResult<JSONObject> supResult = new SupResult<>(JSONUtil.toJsonStr(queryParams), LocalDateTime.now());
        try {
            result = post(queryParams, bean);
            return getSupResult(supResult, result, bean);
        } catch (Throwable e) {
            errMonitorMsg(log, " 【中电郑州】 婚姻稳定状况接口 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            setErrorSupResult(supResult, result, e);
            return supResult;
        }
    }

    @Override
    public SupResult<JSONObject> queryMaritalRelationship(MaritalRelationshipReqVo vo, SuplierQueryBean bean) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("manIdcard", vo.getManIdcard());
        queryParams.put("manName", vo.getManName());
        queryParams.put("womanIdcard", vo.getWomanIdcard());
        queryParams.put("womanName", vo.getWomanName());
        String url = bean.getUrl() + "/api7edb4e91482b452494c57352588af4cd";
        bean.setUrl(url);
        String result = null;
        SupResult<JSONObject> supResult = new SupResult<>(JSONUtil.toJsonStr(queryParams), LocalDateTime.now());
        try {
            result = post(queryParams, bean);
            return getSupResult(supResult, result, bean);
        } catch (Throwable e) {
            errMonitorMsg(log, " 【中电郑州】 婚姻关系验证 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            setErrorSupResult(supResult, result, e);
            return supResult;
        }
    }

    @Override
    public SupResult<JSONObject> queryMaritalStatus(MarriageInfoReqInfo vo, SuplierQueryBean bean) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("xm", vo.getXm());
        queryParams.put("sfzh", vo.getSfzh());
        String url = bean.getUrl() + "/api499e49704c704dd59b0040a4fb023db4";
        bean.setUrl(url);
        String result = null;
        SupResult<JSONObject> supResult = new SupResult<>(JSONUtil.toJsonStr(queryParams), LocalDateTime.now());
        try {
            result = get(queryParams, bean);
            return getSupResult(supResult, result, bean);
        } catch (Throwable e) {
            errMonitorMsg(log, " 【中电郑州】 婚姻状况查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getSupName(), bean.getProductCode(), bean.getOrderNo(), url, result, e);
            setErrorSupResult(supResult, result, e);
            return supResult;
        }
    }

    @Override
    public SupResult<JSONObject> queryEducationInfo(EducationInfoReqVo vo, SuplierQueryBean bean) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("xm", vo.getXm());
        queryParams.put("zjhm", vo.getZjhm());
        String url = bean.getUrl() + "/api172610733b074eb18087f119aeefcb30";
        bean.setUrl(url);
        String result = null;
        SupResult<JSONObject> supResult = new SupResult<>(JSONUtil.toJsonStr(queryParams), LocalDateTime.now());
        try {
            result = get(queryParams, bean);
            return getSupResult(supResult, result, bean);
        } catch (Throwable e) {
            errMonitorMsg(log, " 【中电郑州】 婚姻关系验证 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            setErrorSupResult(supResult, result, e);
            return supResult;
        }
    }


    private String post(Map<String, Object> queryParams, SuplierQueryBean bean) {
        Integer timeOut = bean.getTimeOut();
        String appKey = JSONObject.parseObject(bean.getSignKey()).getString(bean.getSupProductCode());
        String secretKey = JSONObject.parseObject(bean.getSignPwd()).getString(bean.getSupProductCode());

        Map<String, String> headers = new HashMap<>();
        headers.put("app-key", appKey);
        headers.put("secret-key", secretKey);

        return HttpRequest.post(bean.getUrl())
                .body(JSONUtil.toJsonStr(queryParams))
                .addHeaders(headers)
                .setReadTimeout(timeOut)
                .setReadTimeout(timeOut)
                .execute()
                .body();
    }

    private String get(Map<String, Object> queryParams, SuplierQueryBean bean) {

        Integer timeOut = bean.getTimeOut();
        String appKey = JSONObject.parseObject(bean.getSignKey()).getString(bean.getSupProductCode());
        String secretKey = JSONObject.parseObject(bean.getSignPwd()).getString(bean.getSupProductCode());

        Map<String, String> headers = new HashMap<>();
        headers.put("app-key", appKey);
        headers.put("secret-key", secretKey);

        return HttpUtil.createGet(bean.getUrl())
                .addHeaders(headers)
                .form(queryParams)
                .setReadTimeout(timeOut)
                .setReadTimeout(timeOut)
                .execute()
                .body();
    }

    private SupResult<JSONObject> getSupResult(SupResult<JSONObject> supResult, String result, SuplierQueryBean bean) {
        supResult.setRespTime(LocalDateTime.now());
        supResult.setRespJson(result);
        //判断是否有响应结果 无就是请求异常或超时
        if (StringUtils.isBlank(result)) {
            supResult.setRemark("供应商没有响应结果");
            supResult.setState(ReqState.ERROR);
            return supResult;
        }
        JSONObject resultObject = JSON.parseObject(result);
        String code = resultObject.getString("code");
        //                200：成功（收费）
        if (SUCCESS.equals(code)) {
            supResult.setFree(FreeState.YES);
            supResult.setRemark("查询成功");
            supResult.setState(ReqState.SUCCESS);
            JSONObject data = resultObject.getJSONObject("data");
            if (data != null) {
                supResult.setData(data);
                return supResult;
            }
        } else {
            supResult.setFree(FreeState.NO);
            supResult.setRemark("查询失败");
            supResult.setState(ReqState.ERROR);
            errMonitorMsg(log, "【{}】 {} 发生异常 orderNo {} URL {} , 报文: {} "
                    , bean.getSupName(), bean.getProductCode(), bean.getOrderNo(), bean.getUrl(), result);
            return supResult;
        }
        return supResult;
    }



    private void setErrorSupResult(SupResult<JSONObject> supResult, String result, Throwable e) {
        supResult.setState(ReqState.ERROR);
        supResult.setRespTime(LocalDateTime.now());
        supResult.setRespJson(result);
        supResult.setRemark("异常：" + e.getMessage());
    }
}
