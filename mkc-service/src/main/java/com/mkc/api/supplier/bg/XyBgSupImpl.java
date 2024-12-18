package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.supplier.utils.UrlUtils;
import com.mkc.api.dto.bg.EducationInfoReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author xiewei
 * @date 2024/08/06 15:46
 */
@Service("BG_XYD")
@Slf4j
public class XyBgSupImpl implements IBgSupService {

    private final static String SUCCESS = "0000";


    @Override
    public SupResult queryEduAssessment(EducationInfoReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = bean.getUrl();
        try {

            String appId = bean.getAcc();
            String appKey = bean.getSignKey();
            String api = "2186";

            Integer timeOut = bean.getTimeOut();

            String name = vo.getXm();
            String pid  = vo.getZjhm();
            JSONObject args = new JSONObject();
            args.put("name", name);
            args.put("pid", pid);

            params.put("appid", appId);
            params.put("api", api);
            params.put("args", args.toJSONString());
            params.put("sign", Md5Utils.md5(appKey + "|" + appId + "|" + api + "|" + args));

            url = UrlUtils.getUrl(params, url);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.get(url, timeOut);

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
                JSONObject data = resultObject.getJSONObject("data");
                if (data != null) {
                    supResult.setData(data);
                }
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  学历评估 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        }  catch (Throwable e) {
            errMonitorMsg(log," 【信雅达技术】 学历评估 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
