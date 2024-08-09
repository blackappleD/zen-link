package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.vo.bg.FinanceInfoReqVo;
import com.mkc.api.vo.bg.SureScoreInfoReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author xiewei
 * @date 2024/08/06 15:46
 */
@Service("BG_YRZX")
@Slf4j
public class YrzxBgSupImpl implements IBgSupService {

    private final static String SUCCESS = "001";

    private final static String EMPTY = "999";

    @Override
    public SupResult queryFinanceInfo(FinanceInfoReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/yrzx/finan/net/2w";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String name = vo.getName();
            String mobile = vo.getMobile();
            String idCard = vo.getIdCard();

            params.put("account", appkey);
            params.put("idCard", idCard);
            params.put("name", name);
            params.put("mobile", mobile);
            params.put("reqid", vo.getMerSeq());
            StringBuilder verify = new StringBuilder();
            verify.append(params.getString("account")).append(params.getString("idCard"))
                    .append(params.getString("name")).append(params.getString("mobile"))
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
                supResult.setFree(FreeState.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    supResult.setData(resultJson);
                    return supResult;
                }

            } else if (EMPTY.equals(code)) {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOGET);
                supResult.setData(resultObject.getJSONObject("msg"));
                return  supResult;
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  经济能力2W查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        }  catch (Throwable e) {
            errMonitorMsg(log," 【北京银融致信科技供应商】 经济能力2W 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

    @Override
    public SupResult querySureScoreInfo(SureScoreInfoReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/yrzx/score/A11";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String name = vo.getName();
            String mobile = vo.getMobile();
            String cid = vo.getCid();
            String job = vo.getJob();

            params.put("account", appkey);
            params.put("cid", cid);
            params.put("job", job);
            params.put("name", name);
            params.put("mobile", mobile);
            params.put("reqid", vo.getMerSeq());
            StringBuilder verify = new StringBuilder();
            verify.append(params.getString("account")).append(params.getString("cid"))
                    .append(params.getString("mobile")).append(params.getString("name")).append(params.getString("job"))
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
                supResult.setFree(FreeState.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    supResult.setData(resultJson);
                    return supResult;
                }

            } else if (EMPTY.equals(code)) {
                supResult.setFree(FreeState.YES);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOGET);
                supResult.setData(resultObject.getJSONObject("msg"));
                return  supResult;
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  确信分查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        }  catch (Throwable e) {
            errMonitorMsg(log," 【北京银融致信科技供应商】 确信分 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
