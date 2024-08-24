package com.mkc.api.supplier.ck;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.vo.ck.PopulationThreeReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/23 16:12
 */
@Service("CK_CDDATAGROUP")
@Slf4j
public class CdDataGroupCkSupImpl implements ICkSupService {

    private final static String SUCCESS = "200";

    private final static String NO_FEE = "0";

    private final static String ERROR1 = "207";

    private final static String ERROR2 = "208";

    private final static String ERROR3 = "603";

    private final static String ERROR4 = "604";


    @Override
    public SupResult ckPopulationThree(PopulationThreeReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        try {
            url = bean.getUrl() + "/xs-api/xs-data-api/api/proxy/a18da9e85a052f9fe";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String name = vo.getName();
            String idcard = vo.getIdcard();
            String photo = vo.getPhoto();
            String authorization = vo.getAuthorization();

            JSONObject entries = new JSONObject();
            entries.put("name", name);
            entries.put("idcard", idcard);
            entries.put("photo", photo);
            entries.put("authorization", authorization);
            params.put("param", entries);
            String payload = params.toString();
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = reqData(url, appkey, appsecret, payload, timeOut);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            JSONObject resultObject = JSON.parseObject(result);
            String code = resultObject.getString("code");
            String status = resultObject.getString("status");
            if (NO_FEE.equals(status)) {
                supResult.setFree(FreeState.NO);
            } else {
                supResult.setFree(FreeState.YES);
            }
            if (SUCCESS.equals(code)) {
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);
                JSONObject data = resultObject.getJSONObject("data");
                if (data != null) {
                    supResult.setData(data);
                    return supResult;
                }
            } else if (ERROR1.equals(code)) {
                supResult.setRemark(resultObject.getString("msg"));
                supResult.setState(ReqState.ERROR);
                supResult.setDefinedFailMsg(true);
                errMonitorMsg(log," 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            } else if (ERROR2.equals(code)) {
                supResult.setRemark(resultObject.getString("msg"));
                supResult.setState(ReqState.ERROR);
                supResult.setDefinedFailMsg(true);
                errMonitorMsg(log," 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }  else if (ERROR3.equals(code)) {
                supResult.setRemark(resultObject.getString("msg"));
                supResult.setState(ReqState.ERROR);
                supResult.setDefinedFailMsg(true);
                errMonitorMsg(log," 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }  else if (ERROR4.equals(code)) {
                supResult.setRemark(resultObject.getString("msg"));
                supResult.setState(ReqState.ERROR);
                supResult.setDefinedFailMsg(true);
                errMonitorMsg(log," 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            } else {
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log," 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

    /**
     * 基于将appKey、timestamp、appSecret⽣成sign，将签名结果转换为⼗六进制
     *
     * @param timestamp 当前时间
     * @return ⼗六进制字符串
     */
    public static String createSign(String appKey, String timestamp, String appSecret) {
        return DigestUtils.md5Hex(appKey + timestamp + appSecret);
    }

    /**
     * 请求接⼝，获取数据
     *
     * @return
     */
    public static String reqData(String url, String appKey, String appSecret, String payload, Integer timeOut) {
        String timestamp = DateUtil.now(); // ⽣成当前时间

        return HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("appKey", appKey)
                .header("timestamp", timestamp)
                .header("sign", createSign(appKey, timestamp, appSecret))
                .body(payload)
                .timeout(timeOut).execute().body();
    }
}
