package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.vo.bg.EducationInfoReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  XIEWEI
 * @date 2024/9/20 15:15
 */
@Service("BG_ZDZZ")
@Slf4j
public class ZdzzBgSupImpl implements IBgSupService {

    private final static String SUCCESS = "200";

    @Override
    public SupResult queryEducationInfo(EducationInfoReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        String appToken = null;
        try {
            url = bean.getUrl() + "/dc-sso/componentToken/generateAppToken";
            Integer timeOut = bean.getTimeOut();

            params.put("appkey", bean.getSignKey());
            params.put("password", bean.getSignPwd());
            params.put("input", bean.getAcc());

            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.post(url, params.toJSONString(), timeOut);

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
            //判断token是否获取成功
            if (SUCCESS.equals(code)) {
                appToken = resultObject.getString("data");
                Map<String, Object> queryParams = new HashMap<>();
                queryParams.put("xm", vo.getXm());
                queryParams.put("zjhm", vo.getZjhm());
                supResult = new SupResult(JSONUtil.toJsonStr(queryParams), LocalDateTime.now());
                Map<String, String> headers = new HashMap<>();
                headers.put("app-token", appToken);
                result = HttpUtil.createGet(bean.getUrl() + "/dc-dbapi/data-product/172621339414163")
                        .addHeaders(headers)
                        .form(queryParams)
                        .execute()
                        .body();
                supResult.setRespTime(LocalDateTime.now());
                supResult.setRespJson(result);
                //判断是否有响应结果 无就是请求异常或超时
                if (StringUtils.isBlank(result)) {
                    supResult.setRemark("供应商没有响应结果");
                    supResult.setState(ReqState.ERROR);
                    return supResult;
                }
                resultObject = JSON.parseObject(result);
                code = resultObject.getString("code");
                //                200：成功（收费）
                if (SUCCESS.equals(code)) {
                    supResult.setFree(FreeState.YES);
                    supResult.setRemark("查询成功");
                    supResult.setState(ReqState.SUCCESS);
                    JSONArray data = resultObject.getJSONArray("data");
                    if (data != null) {
                        supResult.setData(data);
                        return supResult;
                    }
                } else {
                    supResult.setFree(FreeState.NO);
                    supResult.setRespTime(LocalDateTime.now());
                    supResult.setRemark("查询失败");
                    supResult.setState(ReqState.ERROR);
                    errMonitorMsg(log,"  全国高等学历信息查询接口 发生异常 orderNo {} URL {} , 报文: {} "
                            , bean.getOrderNo(),url, result);
                    return supResult;
                }
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRespTime(LocalDateTime.now());
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  全国高等学历信息查询 token获取接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        }  catch (Throwable e) {
            errMonitorMsg(log," 【中电郑州】 全国高等学历信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(),url, result, e);
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
}
