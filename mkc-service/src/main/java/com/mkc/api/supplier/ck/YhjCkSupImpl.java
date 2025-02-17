package com.mkc.api.supplier.ck;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.constant.enums.ReqParamType;
import com.mkc.api.common.constant.enums.YysCode;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.ck.req.MobThreeReqDTO;
import com.mkc.api.dto.ck.res.MobTreeRespDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 云汇景 供应商 核验类 产品服务
 *
 * @author tqlei
 * @date 2023/10/07 16:17
 */
@Service("CK_YHJ")
@Slf4j
public class YhjCkSupImpl implements ICkSupService {

    private final static  String SUCCESS="200";
    private final static String NO="404";
    private final static String NOT="400";



    @Override
    public SupResult ckMobThree(MobThreeReqDTO vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            url = bean.getUrl() + "/yhj/ck/mobThMd5";
            String key = bean.getSignKey();
            String merCode = bean.getAcc();
            Integer timeOut = bean.getTimeOut();
            String passname = vo.getCertName();
            String mobile = vo.getMobile();
            String pid = vo.getCertNo();
            //判断当前入参类型是否是md5
            if (!ReqParamType.isMd5Type(vo.getParamType())) {
                passname= Md5Utils.md5(passname);
                pid= Md5Utils.md5(pid);
                mobile= Md5Utils.md5(mobile);
            }
            //判断当前入参类型是否是md5
            if (!ReqParamType.isMd5Type(vo.getParamType())) {
                passname= Md5Utils.md5(passname);
                pid= Md5Utils.md5(pid);
            }
            params.put("p1", passname);
            params.put("p2", pid);
            params.put("p3", mobile);
            params.put("merCode", merCode);
            params.put("key", key);

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
            JSONObject data = resultObject.getJSONObject("data");

            String isp = "CMCC";
            MobTreeRespDTO respVo = new MobTreeRespDTO();
            if (data != null) {
                isp = data.getString("isp");
            }

//                200：手机三要素一致（收费）
//                404：查无
//                400：不一致

            if (SUCCESS.equals(code)) {

                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("认证信息一致");
                supResult.setState(ReqState.SUCCESS);

            } else if (NOT.equals(code)) {

                supResult.setFree(FreeStatus.YES);
                supResult.setRemark("认证信息不一致");
                supResult.setState(ReqState.NOT);

            } else if (NO.equals(code)) {

                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查无");
                supResult.setState(ReqState.NOT_GET);
                return supResult;
            } else {
                supResult.setFree(FreeStatus.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.NOT_GET);
                return supResult;
            }
            //设置运营商信息，查无 直接移动
           //CMCC 移动， CUCC 联通,CTCC 电信
            YysCode yysCode = YysCode.CM;
            if("CTCC".equals(isp)){
                yysCode = YysCode.CT;
            }else if ("CUCC".equals(isp)){
                yysCode = YysCode.CU;
            }
            supResult.setYysCode(yysCode);

            respVo.setRetCode(MobTreeRespDTO.SUCCESS);
            respVo.setIsp(yysCode.getCode());
            supResult.setData(respVo);
            return supResult;

        } catch (Throwable e) {

            errMonitorMsg(log," 【云汇景供应商】 个人手机三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
