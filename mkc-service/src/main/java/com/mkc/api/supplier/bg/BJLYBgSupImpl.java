package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.req.CarInfoReqVo;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author XIEWEI
 * @date 2024/10/10 10:38
 */
@Service("BG_BJLY")
@Slf4j
public class BJLYBgSupImpl implements IBgSupService {

    private final static String SUCCESS = "0000";
    private final static String NOGET = "1910020001";

    public SupResult queryCarInfo(CarInfoReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        try {
            url = bean.getUrl() + "/data_product/traffic/car/cwxByCar";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String plateNo = vo.getPlateNo();
            JSONObject sendData = new JSONObject();
            sendData.put("carNo", plateNo);
            params.put("data", sendData);
            params.put("appkey", appkey);
            String timestamp = System.currentTimeMillis() + "";
            params.put("timestamp", timestamp);
            String sign = Md5Utils.md5(appkey + appsecret + timestamp);
            params.put("sign", sign);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.post(url, params.toJSONString(), timeOut);

            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            JSONObject resultObject = JSON.parseObject(result);
            String code = resultObject.getString("code");

            //0000：成功（收费）
            if (SUCCESS.equals(code)) {
                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject data = resultObject.getJSONObject("result");
                if (data != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("engineNo", data.getString("engine"));
                    jsonObject.put("brandName", data.getString("carName"));
                    jsonObject.put("vin", data.getString("vin"));
                    jsonObject.put("initialRegistrationDate", data.getString("recordDate"));
                    jsonObject.put("modelNo", data.getString("vehicleModel"));
                    supResult.setData(jsonObject);
                    return supResult;
                }
            } else if (NOGET.equals(code)) {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOT_GET);
            } else {
                supResult.setFree(FreeStatus.NO);
                String message = resultObject.getString("message");
                supResult.setRemark(StringUtils.isNotBlank(message) ? message : "查询失败");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log, " 【北京菱云科技有限公司】 车五项 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);

            if (supResult == null) {
                supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            }
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常:" + e.getMessage());
            return supResult;
        }
    }
}
