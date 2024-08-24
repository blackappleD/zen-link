package com.mkc.api.supplier.ck;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.vo.ck.PopulationThreeReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/23 16:12
 */
@Service("CK_CDDATAGROUP")
@Slf4j
public class CdDataGroupCkSupImpl implements ICkSupService {


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


            JSONObject entries = new JSONObject();
            entries.put("name", name);
            entries.put("idcard", idcard);
            entries.put("photo", photo);
            entries.put("authorization", vo.getMerSeq());
            params.put("param", entries);
            String payload = params.toString();
            String response = reqData(url, appkey, appsecret, payload, timeOut);

        } catch (Throwable e) {
            errMonitorMsg(log," 【成都数据集团股份有限公司】 工作单位核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

        return null;
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
