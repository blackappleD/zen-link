package com.mkc.api.supplier.sf;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.ISfSupService;
import com.mkc.api.vo.sf.DishonestExecutiveReqVo;
import com.mkc.api.vo.sf.RestrictedConsumerReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/28 15:51
 */

@Service("BG_JZKJ")
@Slf4j
public class JzKjSfSupImpl implements ISfSupService {

    private final static  String SUCCESS="100";
    private final static String NO="404";

    @Override
    public SupResult queryDishonestExecutiveInfo(DishonestExecutiveReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/sf/sxr";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String certName = vo.getCertName();
            String certNo = vo.getCertNo();
            String type = vo.getType();

            params.put("appkey", appkey);
            params.put("certName", certName);
            params.put("certNo", certNo);
            params.put("type", type);
            params.put("merSeq", vo.getMerSeq());
            String sign= Md5Utils.md5(appkey + certName + certNo + appsecret);
            params.put("sign", sign);

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


//                0：成功（收费）
//                405：查无（不收费）TODO

            if (SUCCESS.equals(code)) {

                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject data = resultObject.getJSONObject("data");
                if (data != null) {
                    supResult.setData(data);
                    return supResult;
                }
            } else if (NO.equals(code)) {

                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOGET);
                return supResult;
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  【司法】失信被执行人 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }

            return supResult;

        } catch (Throwable e) {

            errMonitorMsg(log," 【司法】失信被执行人 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(),url, result, e);

            if (supResult == null) {
                supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            }
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常:"+e.getMessage());
            return supResult;
        }
    }

    @Override
    public SupResult queryRestrictedConsumerInfo(RestrictedConsumerReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/sf/xgr";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String certName = vo.getCertName();
            String certNo = vo.getCertNo();
            String type = vo.getType();

            params.put("appkey", appkey);
            params.put("certName", certName);
            params.put("certNo", certNo);
            params.put("type", type);
            params.put("merSeq", vo.getMerSeq());
            String sign= Md5Utils.md5(appkey + certName + certNo + appsecret);
            params.put("sign", sign);

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


//                0：成功（收费）
//                405：查无（不收费）TODO

            if (SUCCESS.equals(code)) {

                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject data = resultObject.getJSONObject("data");
                if (data != null) {
                    supResult.setData(data);
                    return supResult;
                }
            } else if (NO.equals(code)) {

                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOGET);
                return supResult;
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  【司法】限制高消费被执行人接口 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }

            return supResult;

        } catch (Throwable e) {

            errMonitorMsg(log," 【司法】限制高消费被执行人接口 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(),url, result, e);

            if (supResult == null) {
                supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            }
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常:"+e.getMessage());
            return supResult;
        }
    }
}
