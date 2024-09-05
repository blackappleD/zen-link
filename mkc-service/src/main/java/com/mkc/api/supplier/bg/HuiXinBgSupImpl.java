package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.supplier.utils.AESUtil;
import com.mkc.api.vo.bg.DrivingLicenseReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/26 15:44
 */
@Service("BG_HUIXIN")
@Slf4j
public class HuiXinBgSupImpl implements IBgSupService {

    private final static  String SUCCESS="100";
    private final static String NO="101";

    @Override
    public SupResult queryDrivingLicenseInfo(DrivingLicenseReqVo vo, SuplierQueryBean bean) {
        String result = "null";
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/integration/verifyIdentityVehicleLicense";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();

            String licensePlateNo = vo.getLicensePlateNo();
            String plateType = vo.getPlateType();
            String name = vo.getName();
            params.put("licensePlateNo", licensePlateNo);
            params.put("plateType", plateType);
            params.put("name", name);
            // 生成签名, 不能传入accountId参数

            String sign = sign(url, appsecret, params);
            params.put("sign", sign);
            params.put("accountId", appkey);

            // 将业务字段的value进行AES加密(密钥为ACCOUNTKEY)
            for(String key : params.keySet()) {
                if ("accountId".equals(key) || "sign".equals(key)) continue;
                params.put(key, AESUtil.encode(appsecret, params.getString(key)));
            }
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = HttpUtil.post(url, params.to(Map.class), timeOut);
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
            if (SUCCESS.equals(code)) {
                supResult.setFree(FreeState.YES);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);

                JSONObject data = resultObject.getJSONObject("data");
                if (data != null) {
                    supResult.setData(data);
                    return supResult;
                }
            } else if (NO.equals(code)) {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOGET);
                return supResult;
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  行驶身份核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        }catch (Throwable e) {

            errMonitorMsg(log," 【慧信供应商】 行驶身份核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

    /**
     * 生成签名
     * @param url
     * @param accountKey
     * @param params
     * @return
     */
    public static String sign(String url, String accountKey, JSONObject params) {
        Map map = params.toJavaObject(Map.class);
        return Md5Utils.md5(url + accountKey + sortMap(params));
    }

    /**
     * 格式化参数，按key按字典序排列并以,隔开，生成形如key1=value1,key2=value2...的字符串
     *
     * @param params 简单参数键值对
     * @return key=value,key=value...格式的字符串
     */
    public static String sortMap(Map params) {
        // 参数map为空时返回空串
        if (null == params || params.size() == 0) {
            return "";
        }
        // 按key排序
        StringBuilder sb = new StringBuilder();
        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(params);
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {
            if (StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            sb.append(",").append(entry.getKey()).append("=")
                    .append(entry.getValue());
        }
        // 返回结果字符串
        if (sb.length() > 1) {
            return sb.substring(1);
        }
        return "";
    }
}
