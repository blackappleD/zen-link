package com.mkc.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.utils.ApiUtils;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.*;
import com.mkc.service.IMerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
     * 高等学历-法信
     */
    @PostMapping("/testHouse")
    public void testHouse(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelTestHouse> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelTestHouse.class)
                    .sheet(0)
                    .doReadSync();
            ArrayList<ExcelTestHouse> writeList = new ArrayList<>();
            for (ExcelTestHouse read : readList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("reqOrderNo", read.getReqOrderNo());
                jsonObject.put("personCardNumList", Collections.singletonList(read.getPersonCardNum()));
                String plaintext = read.getReqOrderNo();
                JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", jsonObject, plaintext);
                try {
                    JSONObject data = post.getJSONObject("data");
                    if (Objects.nonNull(data)) {
                        JSONArray authResults = data.getJSONArray("authResults");
                        if (!CollectionUtils.isEmpty(authResults)) {
                            JSONObject authResult = authResults.getJSONObject(0);
                            JSONArray resultList = authResult.getJSONArray("resultList");
                            if (!CollectionUtils.isEmpty(resultList)) {
                                for (int i = 0; i < resultList.size(); i++) {
                                    JSONObject result = resultList.getJSONObject(i);
                                    ExcelTestHouse excelTestHouse = new ExcelTestHouse();
                                    excelTestHouse.setXm(read.getXm());
                                    excelTestHouse.setPersonCardNum(read.getPersonCardNum());
                                    excelTestHouse.setCode(post.getString("code"));
                                    excelTestHouse.setReqOrderNo(read.getReqOrderNo());
                                    excelTestHouse.setCertNo(result.getString("certNo"));
                                    excelTestHouse.setUnitNo(result.getString("unitNo"));
                                    excelTestHouse.setLocation(result.getString("location"));
                                    excelTestHouse.setOwnership(result.getString("ownership"));
                                    excelTestHouse.setHouseArea(result.getString("houseArea"));
                                    excelTestHouse.setRightsType(result.getString("rightsType"));
                                    excelTestHouse.setIsSealUp(result.getString("isSealUp"));
                                    excelTestHouse.setIsMortgaged(result.getString("isMortgaged"));
                                    excelTestHouse.setRightsStartTime(result.getString("rightsStartTime"));
                                    excelTestHouse.setRightsEndTime(result.getString("rightsEndTime"));
                                    excelTestHouse.setUseTo(result.getString("useTo"));
                                    writeList.add(excelTestHouse);
                                }
                            }else {
                                ExcelTestHouse excelTestHouse = new ExcelTestHouse();
                                excelTestHouse.setXm(read.getXm());
                                excelTestHouse.setPersonCardNum(read.getPersonCardNum());
                                excelTestHouse.setCode(post.getString("code"));
                                excelTestHouse.setReqOrderNo(read.getReqOrderNo());
                                writeList.add(excelTestHouse);
                            }
                        }
                    }
                } catch (Exception e) {
                    read.setCertNo(post.toJSONString());
                    log.error(e.getMessage());
                }
                System.err.println(post);
            }
            setExcelRespProp(response, DateUtils.dateTimeNow() + "不动产测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelTestHouse.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("不动产测试结果")
                    .doWrite(writeList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
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
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
                String plaintext = read.getIdCard() + read.getName() + read.getMobile();
                JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/economicRate", jsonObject, plaintext);
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
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("idCard", read.getIdCard());
                jsonObject.put("name", read.getName());
                jsonObject.put("mobile", read.getMobile());
                String plainText = read.getIdCard() + read.getName() + read.getMobile();
                JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfo", jsonObject, plainText);
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
                String plainText = read.getName() + read.getLicensePlateNo() + read.getPlateType();
                JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/drivingLicense", jsonObject, plainText);
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
                String plainText = read.getXm() + read.getZsbh();
                JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/highSchoolEduResultInfo", jsonObject, plainText);
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
                String plainText = read.getXm() + read.getZjhm();
                JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/educationInfo", jsonObject, plainText);
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



    public static void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


}
