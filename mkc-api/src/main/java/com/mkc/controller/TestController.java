package com.mkc.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.ExcelTest2W;
import com.mkc.domain.ExcelTestDriving;
import com.mkc.domain.ExcelTestEdu;
import com.mkc.domain.ExcelTestEduZjhm;
import com.mkc.domain.MerInfo;
import com.mkc.service.IMerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tqlei
 * @date 2023/6/30 14:53
 */

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private IMerInfoService merchantService;

    private final static String KEY = "fsdgdfe647234bvdfgdfy54767";

    /**
     * 测试服务状态
     *
     * @return
     */
    @GetMapping("/state")
    public Result test(HttpServletRequest request, String key) {
        if (!KEY.equals(key)) {
            return Result.fail("无效的身份访问");
        }
        try {
            MerInfo merInfo = merchantService.selectMerInfoByCode(BaseController.TEST_MERCODE);
            return Result.ok("服务启动成功");

        } catch (Exception e) {
            return Result.fail("服务还未启动成功");
        }
    }

    /**
     * 社保经济能力评级
     */
    @PostMapping("/testEconomicRate")
    public void testEconomicRate(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelTest2W> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelTest2W.class)
                    .sheet(0)
                    .doReadSync();

            for (ExcelTest2W read : readList) {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
                com.alibaba.fastjson.JSONObject post = post2W("http://api.zjbhsk.com/bg/economicRate", jsonObject);
                read.setCode(post.getString("code"));
                try {
                    JSONObject data = post.getJSONObject("data");
                    if (Objects.nonNull(data)) {
                        read.setLevel(data.getString("level"));
                    }
                } catch (Exception e) {
                    read.setRange(post.getString("data"));
                    log.error(e.getMessage());
                }
            }
            log.info(readList.size() + "条样例测试完毕！");
            setExcelRespProp(response, DateUtils.dateTimeNow() + "社保经济能力评级测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelTest2W.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("社保经济能力评级测试结果")
                    .doWrite(readList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 社保经济能力2W
     */
    @PostMapping("/test2W")
    public void test2W(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelTest2W> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelTest2W.class)
                    .sheet(0)
                    .doReadSync();

            for (ExcelTest2W read : readList) {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
                com.alibaba.fastjson.JSONObject post = post2W("http://api.zjbhsk.com/bg/financeInfo", jsonObject);
                read.setCode(post.getString("code"));
                try {
                    JSONObject data = post.getJSONObject("data");
                    if (Objects.nonNull(data)) {
                        read.setRange(data.getString("range"));
                        read.setHistory(data.getString("history"));
                        read.setStability(data.getString("stability"));
                    }
                } catch (Exception e) {
                    read.setRange(post.getString("data"));
                    log.error(e.getMessage());
                }
            }
            log.info(readList.size() + "条样例测试完毕！");
            setExcelRespProp(response, DateUtils.dateTimeNow() + "社保经济能力2W测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelTest2W.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("社保经济能力2W测试结果")
                    .doWrite(readList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public JSONObject post2W(String url, JSONObject bodyObject) {
        String merCode = "BhCpTest";
        bodyObject.put("merCode", merCode);
        bodyObject.put("key", "80bb75f192ad62fc9dd59e6b39ce9ba5");
        bodyObject.put("merSeq", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        String plaintext = merCode + bodyObject.getString("idCard") + bodyObject.getString("name") + bodyObject.getString("mobile");
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String signText = plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        bodyObject.put("sign", signMd5);
        String bodyJson = bodyObject.toJSONString();
        HttpResponse execute = HttpRequest.post(url)
                .body(bodyJson)
                .execute();
        log.info("queryPost url = 【{}】 request = 【{}】, response = 【{}】", url, bodyJson, execute.body());
        return JSONObject.parseObject(execute.body());
    }


    /**
     * 行驶证核验
     */
    @PostMapping("/testDriving")
    public void testDriving(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelTestDriving> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelTestDriving.class)
                    .sheet(0)
                    .doReadSync();

            for (ExcelTestDriving read : readList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", read.getName());
                jsonObject.put("licensePlateNo", read.getLicensePlateNo());
                jsonObject.put("plateType", read.getPlateType());
                JSONObject post = postDriving("http://api.zjbhsk.com/bg/drivingLicense", jsonObject);
                read.setCode(post.getString("code"));
                try {
                    JSONObject data = post.getJSONObject("data");
                    if (Objects.nonNull(data)) {
                        JSONObject entity = data.getJSONObject("entity");
                        if (Objects.nonNull(entity)) {
                            read.setCrossWeight(entity.getString("crossWeight"));
                            read.setColor(entity.getString("color"));
                            read.setFuel(entity.getString("fuel"));
                            read.setWheelBase(entity.getString("wheelBase"));
                            read.setEngineType(entity.getString("engineType"));
                            read.setPlate(entity.getString("plate"));
                            read.setType(entity.getString("type"));
                            read.setRetirement(entity.getString("retirement"));
                            read.setResult(entity.getString("result"));
                            read.setEngine(entity.getString("engine"));
                            read.setVariety(entity.getString("variety"));
                            read.setRecord(entity.getString("record"));
                            read.setVin(entity.getString("vin"));
                            read.setMan(entity.getString("man"));
                            read.setState(entity.getString("state"));
                            read.setBrand(entity.getString("brand"));
                            read.setVehicleType(entity.getString("vehicleType"));
                            read.setCc(entity.getString("cc"));
                            read.setMaxJourney(entity.getString("maxJourney"));
                            read.setLoadWeight(entity.getString("loadWeight"));
                            read.setPassengers(entity.getString("passengers"));
                            read.setRearTread(entity.getString("rearTread"));
                            read.setJianCheTime(entity.getString("jianCheTime"));
                            read.setPps(entity.getString("pps"));
                            read.setFrontTread(entity.getString("frontTread"));
                            read.setCurbWeight(entity.getString("curbWeight"));
                            read.setValidity(entity.getString("validity"));
                            read.setProperties(entity.getString("properties"));
                            read.setShaft(entity.getString("shaft"));
                        }
                    }
                } catch (Exception e) {
                    read.setCrossWeight(post.getString("data"));
                    log.error(e.getMessage());
                }
            }
            log.info(readList.size() + "条样例测试完毕！");
            setExcelRespProp(response, DateUtils.dateTimeNow() + "行驶证核验测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelTestDriving.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("行驶证核验测试结果")
                    .doWrite(readList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public JSONObject postDriving(String url, JSONObject bodyObject) {
        String merCode = "BhCpTest";
        bodyObject.put("merCode", merCode);
        bodyObject.put("key", "80bb75f192ad62fc9dd59e6b39ce9ba5");
        bodyObject.put("merSeq", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        String plaintext = merCode + bodyObject.getString("name") + bodyObject.getString("licensePlateNo") + bodyObject.getString("plateType");
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String signText = plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        bodyObject.put("sign", signMd5);
        String bodyJson = bodyObject.toJSONString();
        HttpResponse execute = HttpRequest.post(url)
                .body(bodyJson)
                .execute();
        log.info("queryPost url = 【{}】 request = 【{}】, response = 【{}】", url, bodyJson, execute.body());
        return JSONObject.parseObject(execute.body());
    }


    /**
     * 高等学历-法信
     */
    @PostMapping("/testEdu")
    public void testEdu(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelTestEdu> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelTestEdu.class)
                    .sheet(0)
                    .doReadSync();

            for (ExcelTestEdu read : readList) {
                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("reqOrderNo", read.getReqOrderNo());
                jsonObject.put("xm", read.getXm());
                jsonObject.put("zsbh", read.getZsbh());
                JSONObject post = postEdu("http://api.zjbhsk.com/bg/highSchoolEduResultInfo", jsonObject);
                read.setCode(post.getString("code"));
                try {
                    JSONObject data = post.getJSONObject("data");
                    if (Objects.nonNull(data)) {
                        read.setResultCode(data.getString("resultCode"));
                        read.setReqOrderNo(data.getString("reqOrderNo"));
//                        read.setStatus(data.getString("status"));
//                        read.setIsExists(data.getBoolean("isExists"));
//                        read.setZjbh(data.getString("zjbh"));
//                        read.setYxmc(data.getString("yxmc"));
//                        read.setZymc(data.getString("zymc"));
//                        read.setCc(data.getString("cc"));
//                        read.setRxrq(data.getString("rxrq"));
//                        read.setByrq(data.getString("byrq"));
//                        read.setXxxs(data.getString("xxxs"));
                    }
                } catch (Exception e) {
                    read.setResultCode(post.getString("data"));
                    log.error(e.getMessage());
                }
                System.err.println(post);
            }
            setExcelRespProp(response, DateUtils.dateTimeNow() + "学历测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelTestEdu.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("学历测试结果")
                    .doWrite(readList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public JSONObject postEdu(String url, JSONObject bodyObject) {
        String merCode = "BhCpTest";
        bodyObject.put("merCode", merCode);
        bodyObject.put("key", "80bb75f192ad62fc9dd59e6b39ce9ba5");
        bodyObject.put("merSeq", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
//        String plaintext = merCode + bodyObject.getString("reqOrderNo");
        String plaintext = merCode + bodyObject.getString("xm") + bodyObject.getString("zsbh");
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String signText = plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        bodyObject.put("sign", signMd5);
        String bodyJson = bodyObject.toJSONString();
        HttpResponse execute = HttpRequest.post(url)
                .body(bodyJson)
                .execute();
        log.info("queryPost url = 【{}】 request = 【{}】, response = 【{}】", url, bodyJson, execute.body());
        return JSONObject.parseObject(execute.body());
    }

    /**
     * 高等学历-中电郑州
     */
    @PostMapping("/testEduZjhm")
    public void testEduZjhm(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelTestEduZjhm> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelTestEduZjhm.class)
                    .sheet(1)
                    .doReadSync();

            ArrayList<ExcelTestEduZjhm> resultList = new ArrayList<>();
            for (ExcelTestEduZjhm read : readList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("xm", read.getXm());
                jsonObject.put("zjhm", read.getZjhm());
                JSONObject post = postEduZjhm("http://api.zjbhsk.com/bg/educationInfo", jsonObject);
                try {
                    JSONArray dataList = post.getJSONArray("data");
                    for (int i = 0; i < dataList.size(); i++) {
                        JSONObject data = dataList.getJSONObject(i);
                        ExcelTestEduZjhm excelTestEduZjhm = new ExcelTestEduZjhm();
                        excelTestEduZjhm.setCode(post.getString("code"));
                        excelTestEduZjhm.setXm(read.getXm());
                        excelTestEduZjhm.setZjhm(read.getZjhm());
                        excelTestEduZjhm.setYxmc(data.getString("yxmc"));
                        excelTestEduZjhm.setZymc(data.getString("zymc"));
                        excelTestEduZjhm.setCc(data.getString("cc"));
                        excelTestEduZjhm.setRxrq(data.getString("rxrq"));
                        excelTestEduZjhm.setByrq(data.getString("byrq"));
                        excelTestEduZjhm.setXxxs(data.getString("xxxs"));
                        excelTestEduZjhm.setZsbh(data.getString("zhsb"));
                        resultList.add(excelTestEduZjhm);
                    }
                } catch (Exception e) {
                    ExcelTestEduZjhm excelTestEduZjhm = new ExcelTestEduZjhm();
                    excelTestEduZjhm.setXm(read.getXm());
                    excelTestEduZjhm.setZjhm(read.getZjhm());
                    excelTestEduZjhm.setCode(post.getString("code"));
                    excelTestEduZjhm.setYxmc(post.getString("msg"));
                    resultList.add(excelTestEduZjhm);
                    log.error(e.getMessage());
                }
                System.err.println(post);
            }
            setExcelRespProp(response, DateUtils.dateTimeNow() + "学历测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelTestEduZjhm.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("学历测试结果")
                    .doWrite(resultList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public JSONObject postEduZjhm(String url, JSONObject bodyObject) {
        String merCode = "BhCpTest";
        bodyObject.put("merCode", merCode);
        bodyObject.put("key", "80bb75f192ad62fc9dd59e6b39ce9ba5");
        bodyObject.put("merSeq", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
//        String plaintext = merCode + bodyObject.getString("reqOrderNo");
        String plaintext = merCode + bodyObject.getString("xm") + bodyObject.getString("zjhm");
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String signText = plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        bodyObject.put("sign", signMd5);
        String bodyJson = bodyObject.toJSONString();
        HttpResponse execute = HttpRequest.post(url)
                .body(bodyJson)
                .execute();
        log.info("queryPost url = 【{}】 request = 【{}】, response = 【{}】", url, bodyJson, execute.body());
        return JSONObject.parseObject(execute.body());
    }


    public static void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


}
