package com.mkc.api.supplier.ck;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.vo.ck.PersonCarReqVo;
import com.mkc.api.vo.ck.VehicleLicenseReqVo;
import com.mkc.api.vo.ck.WorkUnitReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 银融致信 供应商 核验类 产品服务
 *
 * @author xiewei
 * @date 2024/08/02 17:10
 */
@Service("CK_YRZX")
@Slf4j
public class YrzxCkSupImpl implements ICkSupService {

    private final static String SUCCESS = "001";

    private final static String EMPTY = "999";

    @Override
    public SupResult ckVehicleLicenseInfo(VehicleLicenseReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;

        try {
            url = bean.getUrl() + "/yrzx/car/vehicle/license";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String name = vo.getName();
            String license = vo.getLicense();
            String type = vo.getType();

            params.put("name", name);
            params.put("license", license);
            params.put("type", type);
            params.put("account", appkey);
            if (StringUtils.isNotBlank(vo.getMerSeq()) && vo.getMerSeq().length() > 20) {
                params.put("reqid", StrUtil.sub(vo.getMerSeq(), vo.getMerSeq().length() - 20, vo.getMerSeq().length()));
            } else if (StringUtils.isNotBlank(vo.getMerSeq())) {
                params.put("reqid", vo.getMerSeq());
            }

            StringBuilder verify = new StringBuilder();
            verify.append(params.getString("account")).append(params.getString("license")).append(params.getString("type")).append(params.getString("name"))
                    .append(params.getString("reqid")).append(appsecret);
            params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());
            String reqUrl = getUrl(params, url);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.get(reqUrl, timeOut);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            JSONObject resultObject = JSON.parseObject(result);
            String code = resultObject.getString("code");
            if (SUCCESS.equals(code)) {
                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    supResult.setData(resultJson);
                    return supResult;
                } else {
                    supResult.setFree(FreeStatus.NO);
                    supResult.setRemark("查询失败");
                    supResult.setState(ReqState.ERROR);
                    errMonitorMsg(log, "  行驶证核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                            , bean.getOrderNo(), url, result);
                    return supResult;
                }
            }
            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log, " 【北京银融致信科技供应商】 行驶证核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            if (supResult == null) {
                supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            }
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常：" + e.getMessage());
            return supResult;
        }
    }

    @Override
    public SupResult ckWorkUnit(WorkUnitReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;

        try {
            url = bean.getUrl() + "/yrzx/tel/work/unit/check";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String name = vo.getName();
            String cid = vo.getCid();
            String workplace = vo.getWorkplace();

            params.put("account", appkey);
            params.put("name", name);
            params.put("cid", cid);
            params.put("workplace", workplace);
            params.put("reqid", vo.getMerSeq());
            StringBuilder verify = new StringBuilder();
            verify.append(params.getString("account")).append(params.getString("name"))
                    .append(params.getString("cid")).append(params.getString("workplace"))
                    .append(params.getString("reqid")).append(appsecret);
            params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());
            String reqUrl = getUrl(params, url);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.get(reqUrl, timeOut);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            JSONObject resultObject = JSON.parseObject(result);
            String code = resultObject.getString("code");
            if (SUCCESS.equals(code)) {
                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    supResult.setData(resultJson);
                    return supResult;
                } else {
                    supResult.setFree(FreeStatus.NO);
                    supResult.setRemark("查询失败");
                    supResult.setState(ReqState.ERROR);
                    errMonitorMsg(log, "  工作单位核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                            , bean.getOrderNo(), url, result);
                    return supResult;
                }
            }
            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log, " 【北京银融致信科技供应商】 工作单位核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            if (supResult == null) {
                supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            }
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常：" + e.getMessage());
            return supResult;
        }
    }


    @Override
    public SupResult ckPersonCar(PersonCarReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;

        try {
            url = bean.getUrl() + "/yrzx/car/personalCarCheck";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String plateNo = vo.getPlateNo();
            String name = vo.getName();

            params.put("name", name);
            params.put("plateNo", plateNo);
            params.put("account", appkey);
            params.put("reqid", String.valueOf(System.currentTimeMillis()));
            StringBuilder verify = new StringBuilder();
            verify.append(params.getString("account")).append(params.getString("name")).append(params.getString("plateNo")).append(params.getString("reqid")).append(appsecret);
            params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());
            String reqUrl = getUrl(params, url);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.get(reqUrl, timeOut);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }

            JSONObject resultObject = JSON.parseObject(result);
            String code = resultObject.getString("code");

            if (SUCCESS.equals(code)) {
                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    supResult.setData(resultJson);
                    return supResult;
                }

            } else if (EMPTY.equals(code)) {
                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOT_GET);
                supResult.setData(resultObject.getJSONObject("msg"));
                return supResult;
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log, "  人车核验信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(), url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log, " 【北京银融致信科技供应商】 人车核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            if (supResult == null) {
                supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            }
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常：" + e.getMessage());
            return supResult;
        }
    }

    public static String getUrl(JSONObject params, String url) {
        StringBuilder urlParam = new StringBuilder();

        for (String key : params.keySet()) {
            urlParam.append(key).append("=").append(params.getString(key)).append("&");
        }
        String urlP = urlParam.toString().substring(0, urlParam.toString().length() - 1);
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?").append(urlP);
        return sb.toString();
    }


}
