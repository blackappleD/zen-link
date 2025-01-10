package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.req.CarInfoReqVo;
import com.mkc.api.dto.bg.req.PersonCarDetailReqVo;
import com.mkc.api.dto.bg.req.VehicleLicenseReqVo;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 温州大数据 供应商 报告类 产品服务
 *
 * @author tqlei
 * @date 2023/10/07 16:17
 */
@Service("BG_WZBIGDATA")
@Slf4j
public class WzBigDataBgSupImpl implements IBgSupService {

    private final static  String SUCCESS = "0";
    private final static String NO="100";


    @Override
    public SupResult queryVehicleLicenseInfo(VehicleLicenseReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        try {
            url = bean.getUrl() + "/api/car/runCard";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String plateNo = vo.getPlateNo();
            String plateType = vo.getPlateType();
            String name = vo.getName();

            params.put("appkey", appkey);
            params.put("plateNo", plateNo);
            params.put("plateType", plateType);
            params.put("name", name);
            String timestamp=System.currentTimeMillis()+"";
            params.put("timestamp", timestamp);
            String sign=Md5Utils.md5(appkey+appsecret+timestamp);
            params.put("sign", sign);

            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            String token = "Bearer 58266869751cd646933f418ea5e3811e";
            HttpRequest post = HttpUtil.createPost(url);
            post.header("Authorization", token);
            post.body(params.toJSONString());
            post.timeout(timeOut);
            result = post.execute().body();

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

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    JSONObject data = resultJson.getJSONObject("data");
                    if (data != null) {
                        supResult.setData(data);
                        return supResult;
                    }
                }

            } else if (NO.equals(code)) {

                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOT_GET);
                return supResult;
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  行驶证信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }

            return supResult;
        } catch (Throwable e) {

            errMonitorMsg(log," 【温州大数据供应商】 行驶证信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
    public SupResult queryPersonCarInfo(PersonCarDetailReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/api/car/fit";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String plateNo = vo.getPlateNo();
            String name = vo.getName();
            String cid = vo.getCid();

            params.put("appkey", appkey);
            params.put("plateNo", plateNo);
            params.put("name", name);
            params.put("cid", cid);
            String timestamp=System.currentTimeMillis()+"";
            params.put("timestamp", timestamp);

            String sign = Md5Utils.md5(appkey+appsecret+timestamp);
            params.put("sign", sign);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            String token = "Bearer 58266869751cd646933f418ea5e3811e";
            HttpRequest post = HttpUtil.createPost(url);
            post.header("Authorization", token);
            post.body(params.toJSONString());
            post.timeout(timeOut);
            result = post.execute().body();
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

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    JSONObject data = resultJson.getJSONObject("data");
                    if (data != null) {
                        supResult.setData(data);
                        return supResult;
                    }
                }

            } else if (NO.equals(code)) {

                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOT_GET);
                return supResult;
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  人车核验详版查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }

            return supResult;


        }  catch (Throwable e) {

            errMonitorMsg(log," 【温州大数据供应商】 人车核验详版 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
    public SupResult queryCarInfo(CarInfoReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {

            url = bean.getUrl() + "/api/car/info5";
            String appsecret = bean.getSignKey();
            String appkey = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String plateNo = vo.getPlateNo();
            String plateType = vo.getPlateType();

            params.put("appkey", appkey);
            params.put("plateNo", plateNo);
            params.put("plateType", plateType);
            String timestamp=System.currentTimeMillis()+"";
            params.put("timestamp", timestamp);
            String sign=Md5Utils.md5(appkey+appsecret+timestamp);
            params.put("sign", sign);


            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            String token = "Bearer 58266869751cd646933f418ea5e3811e";
            HttpRequest post = HttpUtil.createPost(url);
            post.header("Authorization", token);
            post.body(params.toJSONString());
            post.timeout(timeOut);
            result = post.execute().body();
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

                JSONObject resultJson = resultObject.getJSONObject("result");
                if (resultJson != null) {
                    JSONObject data = resultJson.getJSONObject("data");
                    if (data != null) {
                        supResult.setData(data);
                        return supResult;
                    }
                }

            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  车五项信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }

            return supResult;

        } catch (Throwable e) {

            errMonitorMsg(log," 【温州大数据供应商】 车五项 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
