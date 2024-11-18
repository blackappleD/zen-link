package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.vo.bg.*;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.FxReqRecord;
import com.mkc.mapper.FxReqRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("BG_FXGZY")
@Slf4j
public class FXGZYBgSupImpl implements IBgSupService {

    public static String DEFAULT_CHARSET = "UTF-8";

    static {
        //清除安全设置
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final static String SUCCESS = "E00000";
    private final static String SUCCESS2 = "E00000";
    private final static String NO = "405";

    @Autowired
    private FxReqRecordMapper fxReqRecordMapper;

    @Override
    public SupResult queryHouseResultInfo(HouseResultInfoReqVo vo, SuplierQueryBean bean) {

        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
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
//            result = "{\"code\":\"E00000\",\"message\":\"请求成功\",\"requestId\":\"049ecf7d61b745e6a0bf413530aba6b7\",\"sign\":\"GeMHFQ65UbLMkMNf3kow/5EwnTEaYA20vclvttWFHSgaq2g9f9zRIaY2jqttBlk8H0x0LD4qXPvmAM70lUYFD3ucrHmN2OEix4xDFSg4tyh09TkpyzYu5AvJzBkHGu1/3AldqDa6mYwfeQGq4eGHxjvJ3fP7dl4Rqp+gXOxw94eHrDipIiV4sW/W77mtdHNQVoUzUR7PBgPDf9LdxHGL/7ZUwpOiQwVeIivMKBTSwOzEB6I8djVmh4i8HpfycMoKm9HdKwUqFQjsy/2fpHb0OtfRsZF00Nsubi7+ZY4ha+xhaqppWk0Un9Mb5/kbbYuoFrC9x29qABXd2Mj+ZjEgJw==\",\"data\":{\"reqOrderNo\":\"ed6ca24402a9418d900212993418b4c8\",\"approvalStatus\":\"APPROVED\",\"authResults\":[{\"cardNum\":\"432801196806012027\",\"authState\":\"30\",\"authStateDesc\":\"核查成功\",\"isReAuth\":0,\"resultList\":[{\"certNo\":\"湘（2022）苏仙不动产权第0062645号\",\"unitNo\":\"431003002021GB00196F00010008\",\"location\":\"郴州市肖家洞路10号苏仙住宅小区12栋104\",\"ownership\":\"单独所有\",\"houseArea\":\"13.01\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"\",\"rightsEndTime\":\"\"},{\"certNo\":\"湘（2022）苏仙不动产权第0062709号\",\"unitNo\":\"431003002021GB00196F00010022\",\"location\":\"郴州市肖家洞路10号苏仙住宅小区12栋601\",\"ownership\":\"单独所有\",\"houseArea\":\"127.17\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"\",\"rightsEndTime\":\"\",\"useTo\":\"住宅\"},{\"certNo\":\"湘（2019）苏仙不动产权第0092013号\",\"unitNo\":\"431003009002GB00002F00020018\",\"location\":\"郴州市苏仙区白鹿洞镇上白水村四组雅苑小区3栋306\",\"ownership\":\"单独所有\",\"houseArea\":\"131.47\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"\",\"rightsEndTime\":\"2079-06-02\",\"useTo\":\"成套住宅\"}]},{\"cardNum\":\"432801196806012027\",\"authState\":\"30\",\"authStateDesc\":\"核查成功\",\"isReAuth\":0,\"resultList\":[{\"certNo\":\"湘（2022）苏仙不动产权第0062645号\",\"unitNo\":\"431003002021GB00196F00010008\",\"location\":\"郴州市肖家洞路10号苏仙住宅小区12栋104\",\"ownership\":\"单独所有\",\"houseArea\":\"13.01\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"\",\"rightsEndTime\":\"\"},{\"certNo\":\"沪（2022）苏仙不动产权第0062709号\",\"unitNo\":\"431003002021GB00196F00010022\",\"location\":\"郴州市肖家洞路10号苏仙住宅小区12栋601\",\"ownership\":\"单独所有\",\"houseArea\":\"127.17\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"\",\"rightsEndTime\":\"\",\"useTo\":\"住宅\"}]}]}}";
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
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询成功");
                supResult.setState(ReqState.SUCCESS);
                JSONObject resultJson = resultObject;
                if (resultJson != null) {
                    //查询该商户该流水号的记录
                    boolean isExist = false;
                    FxReqRecord fxReqRecord = new FxReqRecord();
                    fxReqRecord.setReqOrderNo(vo.getReqOrderNo());
                    fxReqRecord.setMerCode(vo.getMerCode());
                    List<FxReqRecord> list = fxReqRecordMapper.list(fxReqRecord);
                    //已存在
                    if (!CollectionUtils.isEmpty(list)) {
                        fxReqRecord = list.get(0);
                        isExist = true;
                    }
                    //不存在
                    else {
                        fxReqRecord.setCreateTime(new Date());
                    }

                    fxReqRecord.setUpdateTime(new Date());
                    fxReqRecord.setMerResultData(JSONUtil.toJsonStr(resultJson.getJSONObject("data")));
                    fxReqRecord.setMerRequestData(JSONUtil.toJsonStr(personCardNumList));


                    JSONObject returnData = resultJson.getJSONObject("data");
                    supResult.setData(returnData);

                    if (returnData != null && "APPROVED".equals(returnData.getString("approvalStatus"))) {
                        JSONArray authResults = returnData.getJSONArray("authResults");
                        if (Objects.nonNull(authResults) && authResults.size() > 0) {
                            //初始化是否查得最终结果
                            String resultUserFlag = "1";
                            //初始化计费次数
                            int count = 0;
                            //初始化档次
                            StringBuilder level = new StringBuilder(StringUtils.EMPTY);

                            for (int i = 0; i < authResults.size(); i++) {
                                int maxLevel = 4;
                                JSONObject jsonObject = authResults.getJSONObject(i);
                                if (Objects.equals(jsonObject.getString("authStateDesc"), "核查成功")) {
                                    fxReqRecord.setUserFlag("1");
                                    JSONArray resultList = jsonObject.getJSONArray("resultList");
                                    if (!CollectionUtils.isEmpty(resultList)) {
                                        //15天后计费每次
                                        if (fxReqRecord.getUpdateTime().getTime() - fxReqRecord.getCreateTime().getTime() > 1296000000) {
                                            count++;
                                            supResult.setFree(FreeState.YES);
                                        }
                                        //15天内计费仅计费本月发起申请的首次查询结果
                                        else if (Objects.equals(fxReqRecord.getUserFlag(), "0")
                                                && Objects.equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM, fxReqRecord.getCreateTime()), DateUtils.getDateMonth())) {
                                            count++;
                                            supResult.setFree(FreeState.YES);
                                        }
                                        for (int j = 0; j < resultList.size(); j++) {
                                            JSONObject perResult = resultList.getJSONObject(j);
                                            maxLevel = Math.min(getAreaLevelPrice(perResult.getString("certNo")), maxLevel);
                                        }
                                    }
                                } else {
                                    resultUserFlag = "0";
                                }
                                if (StringUtils.isNotBlank(level)) {
                                    level.append(",");
                                }
                                if (maxLevel == 4) {
                                    level.append(0);
                                }else {
                                    level.append(maxLevel);
                                }
                            }
                            supResult.setBilledTimes(count);
                            fxReqRecord.setFeeCount(count);
                            supResult.setLevel(String.valueOf(level));
                            fxReqRecord.setLevel(String.valueOf(level));
                            fxReqRecord.setUserFlag(resultUserFlag);
                        }

                    }
                    if (isExist) {
                        fxReqRecordMapper.updateById(fxReqRecord);
                    }
                    //测试账号查询结果不新增
                    else if (!Objects.equals(vo.getMerCode(), "BhCpTest")){
                        fxReqRecordMapper.insertFxReqRecord(fxReqRecord);
                    }
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
                errMonitorMsg(log, "  不动产信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(), url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {

            errMonitorMsg(log, " 【法信公证云供应商】 不动产信息 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
                File tempFile = new File(tempDir + File.separator + UUID.randomUUID() + "." + fileExtension);
                file.transferTo(tempFile);
                JSONObject reqStrJsonObject = new JSONObject();
                reqStrJsonObject.put("originalFilename", file.getOriginalFilename());
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
                    errMonitorMsg(log, "  不动产信息文件上传 接口 发生异常 orderNo {} URL {} , 报文: {} "
                            , bean.getOrderNo(), url, result);
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
                    FxReqRecord fxReqRecord = new FxReqRecord();
                    fxReqRecord.setMerCode(vo.getMerCode());
                    fxReqRecord.setReqOrderNo(data.getString("reqOrderNo"));
                    fxReqRecord.setPersons(vo.getPersonsStr());
                    fxReqRecord.setCreateTime(new Date());
                    fxReqRecord.setUpdateTime(new Date());
                    fxReqRecordMapper.insertFxReqRecord(fxReqRecord);
                    return supResult;
                }
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log, "  不动产信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(), url, result);
                return supResult;
            }

            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log, " 【法信公证云】  接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
                    , bean.getOrderNo(), url, result, e);
            supResult.setState(ReqState.ERROR);
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            supResult.setRemark("异常:" + e.getMessage());
            return supResult;
        }
    }


    @Override
    public SupResult queryHighSchoolEducationResultInfo(HighSchoolEducationResultInfoReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        try {
            String[] baseUrlArr = null;
            if (StringUtils.isNotBlank(bean.getUrl())) {
                baseUrlArr = bean.getUrl().split(",");
            }
            url = baseUrlArr[0] + "/open/verification/xl/xmjzsbhcxjg";
            JSONObject jsonData = new JSONObject();
            jsonData.put("reqOrderNo", vo.getReqOrderNo());
            params.put("data", jsonData);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            String data = doEncrypt(jsonData.toJSONString(), bean.getSignKey());
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("appId", bean.getAcc());
            map.put("requestId", UUID.randomUUID().toString());
            map.put("securityType", "RSA2");
            map.put("requestTime", LocalDateTime.now().format(FORMATTER));
            map.put("version", "3.0");
            map.put("data", data);

            // 签名
            String sign = doSign(getPlainTextSign(map), bean.getSignPwd());
            map.put("sign", sign);

            String jsonStrParam = JSONUtil.toJsonStr(map);
            log.info("请求参数：{}", jsonStrParam);
            result = HttpUtil.post(url, jsonStrParam);
            log.info("响应参数：{}", result);
            System.out.println("result:" + result);
            JSONObject resultJson = JSON.parseObject(result);
            String returnData = resultJson.getString("data");
            // 校验返回的签名
            boolean isValid = doVerifyReturnSign(resultJson, bean.getSignKey());
            if (!isValid) {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("签名校验失败");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            String decryptResult = null;
            if (StringUtils.isNotBlank(returnData)) {
                decryptResult = doDecrypt(returnData, bean.getSignPwd());
                log.info("解密后data参数:{}", decryptResult);
            }
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            if (SUCCESS.equals(resultJson.get("code"))) {
                supResult.setData(JSONObject.parseObject(decryptResult));
                supResult.setFree(FreeState.NO);
                supResult.setState(ReqState.SUCCESS);
                supResult.setRemark("查询成功");
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log, " 高校学历核查结果接口查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(), url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {
            errMonitorMsg(log, " 【法信公证云供应商】 高校学历核查结果接口查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

    @Override
    public SupResult queryHighSchoolEducationInfo(HighSchoolEducationInfoReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        try {
            String[] baseUrlArr = null;
            if (StringUtils.isNotBlank(bean.getUrl())) {
                baseUrlArr = bean.getUrl().split(",");
            }
            url = baseUrlArr[0] + "/open/verification/xl/xmjzsbhcx";
            JSONObject jsonData = new JSONObject();
            jsonData.put("xm", vo.getXm());
            jsonData.put("zsbh", vo.getZsbh());
            params.put("data", jsonData);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            String data = doEncrypt(jsonData.toJSONString(), bean.getSignKey());
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("appId", bean.getAcc());
            map.put("requestId", UUID.randomUUID().toString());
            map.put("securityType", "RSA2");
            map.put("requestTime", LocalDateTime.now().format(FORMATTER));
            map.put("version", "3.0");
            map.put("data", data);

            // 签名
            String sign = doSign(getPlainTextSign(map), bean.getSignPwd());
            map.put("sign", sign);

            String jsonStrParam = JSONUtil.toJsonStr(map);
            log.info("请求参数：{}", jsonStrParam);
            result = HttpUtil.post(url, jsonStrParam);
            log.info("响应参数：{}", result);

            JSONObject resultJson = JSON.parseObject(result);
            String returnData = (String) resultJson.get("data");
            // 校验返回的签名
            boolean isValid = doVerifyReturnSign(resultJson, bean.getSignKey());
            if (!isValid) {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("签名校验失败");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            log.info("签名校验通过");
            String decryptResult = null;
            // 返回的data数据解密
            if (null != returnData) {
                decryptResult = doDecrypt(returnData, bean.getSignPwd());
                log.info("解密后data参数:{}", decryptResult);
            }
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            if (SUCCESS.equals(resultJson.get("code"))) {
                supResult.setData(JSONObject.parseObject(decryptResult));
                supResult.setFree(FreeState.NO);
                supResult.setState(ReqState.SUCCESS);
                supResult.setRemark("查询成功");
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log, " 高校学历核查接口查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(), url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {

            errMonitorMsg(log, " 【法信公证云供应商】 高校学历核查接口查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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


    @Override
    public SupResult queryHighSchoolEducation(HighSchoolEducationInfoReqVo vo, SuplierQueryBean bean) {
        String result = null;
        SupResult supResult = null;
        JSONObject params = new JSONObject();
        String url = null;
        try {
            String[] baseUrlArr = null;
            if (StringUtils.isNotBlank(bean.getUrl())) {
                baseUrlArr = bean.getUrl().split(",");
            }
            url = baseUrlArr[0] + "/open/verification/xl/check";
            JSONObject jsonData = new JSONObject();
            jsonData.put("xm", vo.getXm());
            jsonData.put("zsbh", vo.getZsbh());
            params.put("data", jsonData);
            supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
            String data = doEncrypt(jsonData.toJSONString(), bean.getSignKey());
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("appId", bean.getAcc());
            map.put("requestId", UUID.randomUUID().toString());
            map.put("securityType", "RSA2");
            map.put("requestTime", LocalDateTime.now().format(FORMATTER));
            map.put("version", "3.0");
            map.put("data", data);

            // 签名
            String sign = doSign(getPlainTextSign(map), bean.getSignPwd());
            map.put("sign", sign);

            String jsonStrParam = JSONUtil.toJsonStr(map);
            log.info("请求参数：{}", jsonStrParam);
            result = HttpUtil.post(url, jsonStrParam);
            log.info("响应参数：{}", result);

            JSONObject resultJson = JSON.parseObject(result);
            String returnData = (String) resultJson.get("data");
            // 校验返回的签名
            boolean isValid = doVerifyReturnSign(resultJson, bean.getSignKey());
            if (!isValid) {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("签名校验失败");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            log.info("签名校验通过");
            String decryptResult = null;
            // 返回的data数据解密
            if (null != returnData) {
                decryptResult = doDecrypt(returnData, bean.getSignPwd());
                log.info("解密后data参数:{}", decryptResult);
            }
            supResult.setRespTime(LocalDateTime.now());
            supResult.setRespJson(result);
            if (StringUtils.isBlank(result)) {
                supResult.setRemark("供应商没有响应结果");
                supResult.setState(ReqState.ERROR);
                return supResult;
            }
            if (SUCCESS.equals(resultJson.get("code"))) {
                supResult.setData(JSONObject.parseObject(decryptResult));
                supResult.setFree(FreeState.NO);
                supResult.setState(ReqState.SUCCESS);
                supResult.setRemark("查询成功");
            } else {
                supResult.setFree(FreeState.NO);
                supResult.setRemark("查询失败");
                supResult.setState(ReqState.ERROR);
                errMonitorMsg(log, " 高校学历核查实时接口查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
                        , bean.getOrderNo(), url, result);
                return supResult;
            }
            return supResult;
        } catch (Throwable e) {

            errMonitorMsg(log, " 【法信公证云供应商】 高校学历核查实时接口查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

    /**
     * 解密方法
     *
     * @param cipherTextBase64 已加密内容（经过Base64编码）
     * @param privateKey       私钥（经过Base64编码）
     * @return 解密后明文
     * @throws Exception 解密异常
     */
    public static String doDecrypt(String cipherTextBase64, String privateKey) throws Exception {
        byte[] encryptedData = Base64.decodeBase64(cipherTextBase64.getBytes(DEFAULT_CHARSET));
        PrivateKey priKey = getPrivateKeyFromPKCS8(privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        int inputLen = encryptedData.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int maxDecrypt = 256;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxDecrypt) {
                    cache = cipher.doFinal(encryptedData, offSet, maxDecrypt);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxDecrypt;
            }
            return out.toString(DEFAULT_CHARSET);
        }
    }

//    public static void main(String[] args) {
//        try {
//            System.err.println(doDecrypt("PadSZDsfMWdt3RQpNGTypwVmJyKum+T9Z9xkD5DGay/3CDFjZ79hq+6GlMsMJYFQL9C7CjfBYQYEaIqPwWc63fT2mdw1ZrZVw5/xLirY8qHBUOxVlN/vKiRvUn5O/tFGhvz1G5/5w5qm4zkmMc4bDVj0t/w1yo7r2Tn6joMpRtFepcfCGMy6UX47e6Y2l/VDt+nwfr2/T7VXH5hyp+u3l+MeHPjqenk7C+ioEe22vyYp/Eza6xXrQxiEV6wOzfsWMZVyE4tINNqvxy57BE/IAtvL2TtZ3ZC3IHdRizNOgKeQSAANM37n3pWDFe5UsY/8IzNozYHlpaf2doi5cOg5yw==","MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDuvLaCR3ctbgW2h7QJTqzGRM6bgIrRCPtQR8cUXwvMVTat37PDEKIPdzqigaq9fPgAsDyWIl3sggmlsmaGJuGAXZp75tV/hOG2yAhXeaqBYkB6Nq2WIbAVirQ2gFdspz9ECvy/BMH4AAfYGmus8INMd80JSU/5HfVZ4sDGASdnX/aLnFpOmtLFO6LURFuWZOXGbQvKNmmQp3br94jGa/AM4AmVv0CJ4wZvlNv75pQUUWu/rj5hFPHPPIz0RZkqVaZ7v7Ve3JqO9d53MA6FI//3eFesFgt/n0F2dIAsD7LE1JB1BqJenNRFbhCw5M2vUxJ7fhhckkt5d5paZ30o6IENAgMBAAECggEAMGMZTrBzbEbHa6Z3jAdhJ1Jc2dGk0RdT8IKLzAozlveSfI1BnwCpCB9vR+6mD3/buDAAJ6HRjZURpX7SHtCUlfLi08gGb7EcGnLSlgA32b9zOBIK/snXKZaSThWXIpOHVCAx9/lb4wsGi/tW5Zdd1UrWyNIOHny+9LfrwHYNaXr6e1i589ffCxzN/PcJfj6MXtxLo4Q853n2dLiGDCcihebNeLUUh7io8qRkTl83Ms/1oertmBfWrYrTjqhrW5Ydw2MpmCJ4cb/RyMcyWnPscd/vSpnQ9YapA9CFVUZOQfbLpF9AiX+jKiBlvyRVisfrEQcjQg4aX0UX/y5uvCPe4QKBgQD9pPZukzKD6s2RAb+i93vmZiH0aBaFR+YOFJ6J0ABpxX17GtEjmvbDklPpdUhBx4fjqzLKzaVnKNUyuCh1eQJnBaekWTAbzISXGUxFHyrd62LEr7mZ+0h7ZppLnA9YSzJzEyUoSPNnFPJwSSVasnJteGB4Y0XuE6LnatovP9+36QKBgQDw9E75xVsIAcTBq+BG9sErD0IBFp3F9FL6W0oQZDGxd+hSUfIUISTfJCbQg5Ui/BvChHI1v56q+3uz3PIGpgWwBhUKgJMtylvHhOECR2unV2oGMCAqrte8jSJcE1oneWaliodvs49lQOloGWIeCAIf0bc5JmO8BRKZEQBOdUothQKBgAluie9gr98x6wT6YKrpswTFRXv2Jug73TUm1SJgxrMoMdtTCvXc/FIXbeFfiBlSFx6DLjdWYFEutobVTEPaBRnZnvtAicZu23tW6HwlhY2NBDY2JpuwOWjFTvWCzxtEaJLY4mfWFp79q2o7fhUaT6D8efHgFyicOFGMqqJZKLOJAoGBAOsAe94i24vNncZ+CXlsxC1o7ij9k9czWZKPYobmEYlStyVCguWbBckUCz8mdlfupmxqnLFLOEbui1F61hoh5j4tUGwzxlfMmwLW2FVOrvJA0UlDurumVwawZ6iBv1Xsk240067f46UPUBWVXBqKGPhLo3V7uZ3xHJho+nGK91O5AoGAfzg/xKpm0hvFqHbfuuvy1wvFjgsKrg7hDMj9y7eQMs2vY2mF4Mn24bfzxrgv9iCe8Ib1lzQvNsaHCKk4ljtKy3MaXTYNx414sUZ9fHnB+qiKEFNxoyVN5rlacY5XRjTeCacXwbqFvTKwaJsyG5iEtz0buovMlBEcxgxlB6ubM8c="));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 返回结果验签
     *
     * @param resultJson 返回的参数
     * @return boolean
     */
    public static boolean doVerifyReturnSign(JSONObject resultJson, String PLATFORM_PUBLIC_KEY) throws Exception {
        String sign = (String) resultJson.get("sign");
        resultJson.remove("sign");

        Map<String, Object> dataMap = JSON.parseObject(resultJson.toJSONString(), Map.class);
        TreeMap<String, Object> dataTreeMap = new TreeMap<>(dataMap);

        String plainTextSign = getPlainTextSign(dataTreeMap);

        System.out.println(plainTextSign);
        return doVerifySign(plainTextSign, PLATFORM_PUBLIC_KEY, sign);
    }

    /**
     * sign明文参数拼接
     *
     * @param map 参数
     * @return 待签名字符串
     */
    public static String getPlainTextSign(TreeMap<String, Object> map) {
        StringBuilder plainTextSign = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            plainTextSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return plainTextSign.substring(0, plainTextSign.length() - 1);
    }

    /**
     * 加密方法
     *
     * @param plainText 待加密明文
     * @param publicKey 公钥（经过Base64编码）
     * @return 加密后字符串（经过Base64编码）
     * @throws Exception 加密异常
     */
    public static String doEncrypt(String plainText, String publicKey) throws Exception {
        byte[] data = plainText.getBytes(DEFAULT_CHARSET);
        PublicKey pubKey = getPublicKeyFromX509(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            int maxEncrypt = 244;
            int inputLen = data.length;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxEncrypt) {
                    cache = cipher.doFinal(data, offSet, maxEncrypt);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxEncrypt;
            }
            return new String(Base64.encodeBase64(out.toByteArray()), DEFAULT_CHARSET);
        }
    }

    /**
     * 签名方法
     *
     * @param content    待加密字符串
     * @param privateKey 私钥（经过Base64编码）
     * @return 签名结果
     * @throws Exception 签名异常
     */
    public static String doSign(String content, String privateKey) throws Exception {
        PrivateKey priKey = getPrivateKeyFromPKCS8(privateKey);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(DEFAULT_CHARSET));
        byte[] signed = signature.sign();
        return Base64.encodeBase64String(signed);
    }

    /**
     * 验证签名方法
     *
     * @param content   待验证字符串
     * @param publicKey 公钥
     * @param sign      外部计算的签名字符串（经过Base64编码）
     * @return 签名验证通过返回true，否则返回false
     * @throws Exception 验证签名异常
     */
    public static boolean doVerifySign(String content, String publicKey, String sign) throws Exception {
        PublicKey pubKey = getPublicKeyFromX509(publicKey);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(pubKey);
        signature.update(content.getBytes(DEFAULT_CHARSET));
        return signature.verify(Base64.decodeBase64(sign));
    }

    private static PrivateKey getPrivateKeyFromPKCS8(String encodedKeyStr) throws Exception {
        if (null == encodedKeyStr || encodedKeyStr.isEmpty()) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(encodedKeyStr);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    private static PublicKey getPublicKeyFromX509(String encodedKeyStr) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(encodedKeyStr);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }


    private static final List<String> FIRST_CLASS_AREAS = Arrays.asList("京", "沪", "申", "广", "粤");
    private static final List<String> THIRD_CLASS_AREAS = Arrays.asList("陕", "秦", "甘", "陇", "蒙", "琼", "宁", "新", "川", "蜀", "吉", "贵", "黔", "赣");

    private static int getAreaLevelPrice(String realEstateCertNo) {
        if (StringUtils.isBlank(realEstateCertNo)) {
            return 2;
        }
        if (FIRST_CLASS_AREAS.stream().anyMatch(realEstateCertNo::contains)) {
            return 1;
        } else if (THIRD_CLASS_AREAS.stream().anyMatch(realEstateCertNo::contains)) {
            return 3;
        } else {
            return 2;
        }
    }

    public static void main(String[] args) {
        String certNo = "京(2021)海不动产权第0020831号";
        System.out.println(getAreaLevelPrice(certNo));
    }

}
