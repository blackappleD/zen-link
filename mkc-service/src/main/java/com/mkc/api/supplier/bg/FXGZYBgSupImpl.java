package com.mkc.api.supplier.bg;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.vo.bg.HouseInfoReqVo;
import com.mkc.api.vo.bg.HouseResultInfoReqVo;
import com.mkc.api.vo.bg.MaterialReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("BG_FXGZY")
@Slf4j
public class FXGZYBgSupImpl implements IBgSupService {

    private final static  String SUCCESS="E00000";
    private final static String NO="405";

    @Override
    public SupResult queryHouseResultInfo(HouseResultInfoReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url=null;
        try {
            String[] baseUrlArr = null;
            if (StringUtils.isNotBlank(bean.getUrl())) {
                baseUrlArr = bean.getUrl().split(",");
            }
            url = baseUrlArr[0] + "/open/verification/house/getCheckResult";
            List<String> personCardNumList = vo.getPersonCardNumList();

            JSONObject data = new JSONObject();
            data.put("reqOrderNo", vo.getReqOrderNo());
            data.put("personCardNumList", personCardNumList);
            params.put("data", data);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = FxSdkTool.houseCheckResult(params, baseUrlArr[0], bean.getAcc(), bean.getSignPwd(), bean.getSignKey());
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

                JSONObject resultJson = resultObject;
                if (resultJson != null) {
                    supResult.setData(resultJson.getJSONObject("data"));
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
                errMonitorMsg(log,"  不动产信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {

            errMonitorMsg(log," 【法信公证云供应商】 不动产信息 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
    public SupResult queryHouseInfo(HouseInfoReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        List<MaterialReqVo> materialReqVoList = new ArrayList<>();
        String tempDir = System.getProperty("user.dir");
        try {
            String[] baseUrlArr = null;
            if (StringUtils.isNotBlank(bean.getUrl())) {
                baseUrlArr = bean.getUrl().split(",");
            }
            //执行上传文件操作
            List<MultipartFile> files = vo.getFiles();
            List<String> types = vo.getTypes();
            int i = 0;
            for (MultipartFile file : files) {
                MaterialReqVo reqVo = new MaterialReqVo();
                String originalFileName = file.getOriginalFilename();
                int dotIndex = originalFileName.lastIndexOf('.');
                String fileExtension = "";
                if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
                    fileExtension = originalFileName.substring(dotIndex + 1);
                }
                File tempFile = new File(tempDir + File.separator + UUID.randomUUID()+"."+fileExtension);
                file.transferTo(tempFile);
                JSONObject reqStrJsonObject = new JSONObject();
                reqStrJsonObject.put("originalFilename",file.getOriginalFilename());
                supResult = new SupResult(JSONUtil.toJsonStr(reqStrJsonObject), LocalDateTime.now());
                result = FxSdkTool.uploadFile(tempFile, bean.getSignPwd(), bean.getSignKey(), baseUrlArr[1], bean.getAcc());
                if (StringUtils.isBlank(result)) {
                    supResult.setRemark("供应商没有响应结果");
                    supResult.setState(ReqState.ERROR);
                    return supResult;
                }
                JSONObject resultObject = JSON.parseObject(result);
                String code = resultObject.getString("code");
                boolean flag = tempFile.delete();
                if (!flag) {
                    supResult.setRemark("文件清理失败");
                    supResult.setState(ReqState.ERROR);
                    return supResult;
                }
                supResult.setRespTime(LocalDateTime.now());
                supResult.setReqJson(JSONUtil.toJsonStr(reqStrJsonObject));
                if (StringUtils.isBlank(result)) {
                    supResult.setRemark("供应商没有响应结果");
                    supResult.setState(ReqState.ERROR);
                    return supResult;
                }
                if (SUCCESS.equals(code)) {
                    reqVo.setFileId(resultObject.getJSONObject("data").getString("fileId"));
                    reqVo.setType(types.get(i++));
                    materialReqVoList.add(reqVo);
                } else {
                    supResult.setFree(FreeState.NO);
                    supResult.setRemark("上传文件失败");
                    supResult.setState(ReqState.ERROR);
                    supResult.setData(resultObject);
                    errMonitorMsg(log,"  不动产信息文件上传 接口 发生异常 orderNo {} URL {} , 报文: {} "
                            , bean.getOrderNo(),url, result);
                }

            }
            url = baseUrlArr[0] + "/open/verification/house/houseCheck";

            JSONObject reqData = new JSONObject();
            reqData.put("persons", vo.getPersons());
            reqData.put("materials", materialReqVoList);
            params.put("data", reqData);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            result = FxSdkTool.houseCheck(params, baseUrlArr[0], bean.getAcc(), bean.getSignPwd(), bean.getSignKey());
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }

            JSONObject resultObject = JSON.parseObject(result);
            String code = resultObject.getString("code");
            //返回结果是成功
            if (SUCCESS.equals(code)) {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);
                JSONObject data = resultObject.getJSONObject("data");
                if (data != null) {
                    supResult.setData(data);
                    return supResult;
                }
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log,"  不动产信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(),url, result);
                return supResult;
            }

            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log," 【法信公证云】  接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(),url, result, e);
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常:"+e.getMessage());
            return supResult;
        }
    }


}
