package com.mkc.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.utils.ApiUtils;
import com.mkc.bean.MerReqLogBean;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.domain.*;
import com.mkc.mapper.FxReqRecordMapper;
import com.mkc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkc.mapper.MerReportMapper;
import com.mkc.mapper.MerReqLogMapper;
import org.springframework.util.CollectionUtils;

/**
 * 商户调用日志统计Service业务层处理
 *
 * @author mkc
 * @date 2023-06-16
 */
@Slf4j
@Service
@DS("business")
public class MerReportServiceImpl extends ServiceImpl<MerReportMapper, MerReport> implements IMerReportService {

    @Autowired
    private MerReqLogMapper merReqLogMapper;
    @Autowired
    private MerReportMapper merReportMapper;
    @Autowired
    private FxReqRecordMapper fxReqRecordMapper;

    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private IMerInfoService merInfoService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductSellService productSellService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean statMerReqLogReport(LocalDate date, String merCode) {
        LocalDate end = date.plusDays(1);

        List<MerReport> reports = selectMerReqLogReport(date, end, merCode);
        if (reports.isEmpty()) {
            return true;
        }

        reports.forEach(merReport -> {
            merReport.setReqDate(date);
            merReport.setSupName(getSupName(merReport.getSupCode()));
            merReport.setMerName(getMerName(merReport.getMerCode()));
            merReport.setProductName(getProcductName(merReport.getProductCode()));
        });

        LambdaQueryWrapper<MerReport> queryWrapper = new LambdaQueryWrapper<MerReport>();
        queryWrapper.ge(MerReport::getReqDate, date).lt(MerReport::getReqDate, end);
        queryWrapper.eq(StringUtils.isNotBlank(merCode), MerReport::getMerCode, merCode);
        remove(queryWrapper);

        return saveBatch(reports);
    }


    private List<MerReport> selectMerReqLogReport(LocalDate date, LocalDate end, String merCode) {
        List<MerReport> reports = merReqLogMapper.selectMerReqLogReport(date, end, merCode);
        return reports;
    }


    private String getSupName(String supCode) {
        Supplier supplier = supplierService.selectSupplierByCode(supCode);
        return supplier != null ? supplier.getName() : "缺省";
    }

    private String getMerName(String merCode) {
        MerInfo merInfo = merInfoService.selectMerInfoByCode(merCode);
        return merInfo != null ? merInfo.getMerName() : "缺省";
    }

    private String getProcductName(String procductCode) {
        Product product = productService.selectProductByCode(procductCode);
        return product != null ? product.getProductName() : "缺省";
    }

    @Override
    public List<MerReport> listMerReport(MerReport merReport) {
        return merReportMapper.selectMerReport(merReport);
    }


    @Override
    public List<FxReqRecord> listFxReport(MerReport merReport) {
        List<FxReqRecord> fxReqRecords = fxReqRecordMapper.listByRangeTime(merReport);
        for (FxReqRecord record : fxReqRecords) {
            record.setCreateTimeStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, record.getCreateTime()));
            record.setUpdateTimeStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, record.getUpdateTime()));
            String merResultData = record.getMerResultData();
            if (StringUtils.isNotBlank(merResultData)) {
                JSONObject jsonObject = JSON.parseObject(merResultData);
                if (!checkHouseSuccess(jsonObject, record, merReport)) {
                    //没核查成功的，且超过10天，去查结果
                    if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
//                        JSONObject request = new JSONObject();
//                        request.put("reqOrderNo", record.getReqOrderNo());
//                        request.put("personCardNumList", JSON.parseArray(record.getMerRequestData()));
//                        String plainText = record.getReqOrderNo();
//                        try {
//                            JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
//                            JSONObject data = post.getJSONObject("data");
//                            record.setUnknownInfo(data.toJSONString());
//                            checkHouseSuccess(data, record, merReport);
//                        } catch (Exception e) {
//                            log.info(request.toJSONString() + "【{}】", e.getMessage());
//                        }
                        record.setRemark("超过10天，查询结果");
                    } else {
                        record.setRemark("未超过10天");
                    }
                }
            }
            //未查询结果，去查结果
            else {
                if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
//                    JSONObject request = new JSONObject();
//                    request.put("reqOrderNo", record.getReqOrderNo());
//                    JSONArray jsonArray = JSON.parseArray(record.getPersons());
//                    ArrayList<String> strings = new ArrayList<>();
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        strings.add(jsonObject.getString("cardNum"));
//                    }
//                    request.put("personCardNumList", strings);
//                    System.err.println(request);
//                    String plainText = record.getReqOrderNo();
//                    try {
//                        JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
//                        JSONObject data = post.getJSONObject("data");
//                        record.setUnknownInfo(data.toJSONString());
//                        checkHouseSuccess(data, record, merReport);
//                    } catch (Exception e) {
//                        log.info(request.toJSONString() + "【{}】", e.getMessage());
//                    }
                    record.setRemark("超过10天，查询结果");
                } else {
                    record.setRemark("未超过10天");
                }
            }
        }
        return fxReqRecords;
    }

    @Override
    public List<FxReqRecord> listFxHouseReport(MerReport merReport) {
        MerReqLogBean merReqLogBean = new MerReqLogBean();
        merReqLogBean.setStartTime(merReport.getStartTime());
        merReqLogBean.setEndTime(merReport.getEndTime().atTime(23, 59, 59));
        merReqLogBean.setProductCode("BG_HOUSE_002");
        merReqLogBean.setMerCode(merReport.getMerCode());
        List<MerReqLog> merReqLogs = merReqLogMapper.selectListOrderByReqTime(merReqLogBean);
        ArrayList<FxReqRecord> fxReqRecords = getFxReqRecords(merReqLogs);
        for (FxReqRecord record : fxReqRecords) {
            String merResultData = record.getMerResultData();
            //已有查询结果
            if (StringUtils.isNotBlank(merResultData)) {
                JSONObject jsonObject = JSON.parseObject(merResultData);
                if (!checkHouseSuccess(jsonObject.getJSONObject("data"), record, merReport)) {
                    //没核查成功的，且超过10天，去查结果
                    if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
                        JSONObject request = JSON.parseObject(record.getMerRequestData());
                        String plainText = record.getReqOrderNo();
                        try {
                            JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
                            record.setUnknownInfo(post.toJSONString());
                            JSONObject data = post.getJSONObject("data");
                            checkHouseSuccess(data, record, merReport);
                        } catch (Exception e) {
                            log.info(request.toJSONString() + "【{}】", e.getMessage());
                        }
                        record.setRemark("超过10天，更新结果");
                    } else {
                        record.setRemark("未超过10天");
                    }
                }
            }
            //未查询结果，去查结果
            else {
                if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(record.getPersons());
                        JSONArray personCardNumList = new JSONArray();
                        JSONArray persons = jsonObject.getJSONArray("persons");
                        for (int i = 0; i < persons.size(); i++) {
                            personCardNumList.add(persons.getJSONObject(i).getString("cardNum"));
                        }
                        JSONObject request = new JSONObject();
                        request.put("personCardNumList", personCardNumList);
                        request.put("reqOrderNo", record.getReqOrderNo());
                        String plainText = record.getReqOrderNo();
                        JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
                        record.setUnknownInfo(post.toJSONString());
                        JSONObject data = post.getJSONObject("data");
                        checkHouseSuccess(data, record, merReport);
                    } catch (Exception e) {
                        log.info("listFxHouseReport 超过10天，查询结果 queryApi【{}】", e.getMessage());
                    }
                    record.setRemark("超过10天，查询结果");
                } else {
                    record.setRemark("未超过10天");
                }
            }
        }

        return fxReqRecords;
    }

    @Override
    public List<FxReqRecord> listFxEduReport(MerReport merReport) {
        MerReqLogBean merReqLogBean = new MerReqLogBean();
        merReqLogBean.setStartTime(merReport.getStartTime());
        merReqLogBean.setEndTime(merReport.getEndTime().atTime(23, 59, 59));
        merReqLogBean.setProductCode("BG_HIGH_SCHOOL_EDUCATION_001");
        merReqLogBean.setMerCode(merReport.getMerCode());
        List<MerReqLog> merReqLogs = merReqLogMapper.selectListOrderByReqTime(merReqLogBean);
        ArrayList<FxReqRecord> fxReqRecords = getFxReqRecords(merReqLogs);
        for (FxReqRecord record : fxReqRecords) {
            String merResultData = record.getMerResultData();
            //已有查询结果
            if (StringUtils.isNotBlank(merResultData)) {
                JSONObject jsonObject = JSON.parseObject(merResultData);
                if (!checkEduSuccess(jsonObject.getJSONObject("data"), record, merReport)) {
                    //没核查成功的，且超过10天，去查结果
                    if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
                        JSONObject request = new JSONObject();
                        request.put("reqOrderNo", record.getReqOrderNo());
                        String plainText = record.getReqOrderNo();
                        try {
                            JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/highSchoolEduResultInfo", request, plainText);
                            record.setUnknownInfo(post.toJSONString());
                            JSONObject data = post.getJSONObject("data");
                            checkEduSuccess(data, record, merReport);
                        } catch (Exception e) {
                            log.info(request.toJSONString() + "【{}】", e.getMessage());
                        }
                        record.setRemark("超过10天，查询结果");
                    } else {
                        record.setRemark("未超过10天");
                    }
                }
            }
            //未查询结果，去查结果
            else {
                if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
                    JSONObject request = new JSONObject();
                    request.put("reqOrderNo", record.getReqOrderNo());
                    System.err.println(request);
                    String plainText = record.getReqOrderNo();
                    try {
                        JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/highSchoolEduResultInfo", request, plainText);
                        record.setUnknownInfo(post.toJSONString());
                        JSONObject data = post.getJSONObject("data");
                        checkEduSuccess(data, record, merReport);
                    } catch (Exception e) {
                        log.info(request.toJSONString() + "【{}】", e.getMessage());
                    }
                    record.setRemark("超过10天，查询结果");
                } else {
                    record.setRemark("未超过10天");
                }
            }
        }

        return fxReqRecords;
    }

    @Override
    public List<MerReportExcel> listReport(List<FxReqRecord> fxReqRecords, String productName) {
        List<MerReportExcel> merReports = new ArrayList<>();
        Map<String, List<FxReqRecord>> collects = fxReqRecords.stream().collect(Collectors.groupingBy(FxReqRecord::getMerCode));
        for (Map.Entry<String, List<FxReqRecord>> collect : collects.entrySet()) {
            List<FxReqRecord> value = collect.getValue();
            MerReportExcel merReport = new MerReportExcel();
            merReport.setMerCode(collect.getKey());
            merReport.setMerName(value.get(0).getMerName());
            merReport.setProductName(productName);
            merReport.setInPrice(BigDecimal.valueOf(value.stream().mapToDouble(FxReqRecord::getInPrice).sum()));
            merReport.setSellPrice(productSellService.selectProductSellByMer(merReport.getMerCode(), ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode()).getSellPrice());
            merReport.setFeeTimes(value.stream().mapToInt(FxReqRecord::getBilledTimes).sum());
            merReport.setTotalPrice(merReport.getSellPrice().multiply(new BigDecimal(merReport.getFeeTimes().toString())));
            merReports.add(merReport);
        }
        return merReports;
    }

    @Override
    public List<MerReportExcel> listDateReport(List<FxReqRecord> fxReqRecords, String productName) {
        List<MerReportExcel> merReports = new ArrayList<>();
        Map<String, List<FxReqRecord>> collects = fxReqRecords.stream().collect(Collectors.groupingBy(FxReqRecord::getMerCode));
        for (Map.Entry<String, List<FxReqRecord>> collect : collects.entrySet()) {
            List<FxReqRecord> value = collect.getValue();
            Map<String, List<FxReqRecord>> dateValues = value.stream().collect(Collectors.groupingBy(p -> StringUtils.substring(p.getUpdateTimeStr(), 0, 10)));
            for (Map.Entry<String, List<FxReqRecord>> dateValue : dateValues.entrySet()) {
                MerReportExcel merReport = new MerReportExcel();
                merReport.setMerCode(collect.getKey());
                merReport.setMerName(dateValue.getValue().get(0).getMerName());
                merReport.setProductName(productName);
                merReport.setReqDate(dateValue.getKey());
                merReport.setInPrice(BigDecimal.valueOf(dateValue.getValue().stream().mapToDouble(FxReqRecord::getInPrice).sum()));
                merReport.setFeeTimes(dateValue.getValue().stream().mapToInt(FxReqRecord::getBilledTimes).sum());
                merReports.add(merReport);
            }
        }
        merReports.sort(Comparator.comparing(MerReportExcel::getReqDate));
        return merReports;
    }

    private ArrayList<FxReqRecord> getFxReqRecords(List<MerReqLog> merReqLogs) {
        ArrayList<FxReqRecord> fxReqRecords = new ArrayList<>();
        for (MerReqLog merReqLog : merReqLogs) {
            if (StringUtils.isNotBlank(merReqLog.getReqJson()) && StringUtils.isNotBlank(merReqLog.getRespJson())) {
                try {
                    //申请请求
                    if (!StringUtils.contains(merReqLog.getReqJson(), "reqOrderNo")) {
                        JSONObject jsonObject = JSON.parseObject(ZipStrUtils.gunzip(merReqLog.getRespJson()));
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (Objects.isNull(data) || StringUtils.isBlank(data.getString("reqOrderNo"))) {
                            continue;
                        }
                        String reqOrderNo = data.getString("reqOrderNo");
                        FxReqRecord record = new FxReqRecord();
                        record.setReqOrderNo(reqOrderNo);
                        Date date = Date.from(merReqLog.getReqTime().atZone(ZoneId.systemDefault()).toInstant());
                        String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
                        record.setCreateTime(date);
                        record.setUpdateTime(date);
                        record.setCreateTimeStr(dateStr);
                        record.setUpdateTimeStr(dateStr);
                        record.setMerCode(merReqLog.getMerCode());
                        record.setPersons(merReqLog.getReqJson());
//                        JSONObject reqJson = JSON.parseObject(merReqLog.getReqJson());
//                        JSONArray persons = reqJson.getJSONArray("persons");
                        record.setUserFlag("0");
                        fxReqRecords.add(record);
                    }
                    //查询结果请求
                    else {
                        JSONObject jsonObject = JSON.parseObject(merReqLog.getReqJson());
                        String reqOrderNo = jsonObject.getString("reqOrderNo");
                        FxReqRecord record = fxReqRecords.stream().filter(p -> Objects.equals(p.getReqOrderNo(), reqOrderNo) && Objects.equals(p.getMerCode(), merReqLog.getMerCode() )).findFirst().orElse(null);
                        if (Objects.nonNull(record)) {
                            Date date = Date.from(merReqLog.getReqTime().atZone(ZoneId.systemDefault()).toInstant());
                            record.setUpdateTime(date);
                            String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
                            record.setUpdateTimeStr(dateStr);
                            record.setMerRequestData(merReqLog.getReqJson());
                            record.setMerResultData(ZipStrUtils.gunzip(merReqLog.getRespJson()));
                            record.setUserFlag("1");
                        } else {
                            FxReqRecord recordTmp = new FxReqRecord();
                            recordTmp.setReqOrderNo(reqOrderNo);
                            Date date = Date.from(merReqLog.getReqTime().atZone(ZoneId.systemDefault()).toInstant());
                            recordTmp.setUpdateTime(date);
                            String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
                            recordTmp.setUpdateTimeStr(dateStr);
                            recordTmp.setMerCode(merReqLog.getMerCode());
                            recordTmp.setMerRequestData(merReqLog.getReqJson());
                            recordTmp.setMerResultData(ZipStrUtils.gunzip(merReqLog.getRespJson()));
                            recordTmp.setUserFlag("1");
                            fxReqRecords.add(recordTmp);
                        }
                    }
                } catch (Exception e) {
                    log.error("getFxReqRecords：{}", e.getMessage());
                }
            }
        }
        return fxReqRecords;
    }

    private boolean checkEduSuccess(JSONObject data, FxReqRecord record, MerReport merReport) {
        String status = data.getString("status");
        int billedTimes = 0;
        boolean isAllSuccess = true;
        List<MerReqLog> merReqLogs = null;
        //没有起始时间，找到起始时间
        if (Objects.isNull(record.getCreateTime())) {
            LocalDateTime localDateTime = merReqLogMapper.selectCreateTime("\"reqOrderNo\":\"" + record.getReqOrderNo() + "\"", merReport.getStartTime(), merReport.getEndTime(), "BG_HIGH_SCHOOL_EDUCATION_001");
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            record.setCreateTime(date);
            String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
            record.setCreateTimeStr(dateStr);
        }
        //超过15天，获本月取调用日志
        if (record.getUpdateTime().getTime() - record.getCreateTime().getTime() > 1296000000) {
            MerReqLogBean merReqLogBean = new MerReqLogBean();
            merReqLogBean.setStartTime(merReport.getStartTime());
            merReqLogBean.setEndTime(merReport.getEndTime().atTime(23, 59, 59));
            merReqLogBean.setProductCode("BG_HIGH_SCHOOL_EDUCATION_001");
            merReqLogBean.setReqJson("\"reqOrderNo\":\"" + record.getReqOrderNo() + "\"");
            merReqLogs = merReqLogMapper.selectListOrderByReqTime(merReqLogBean);
        }
        //核查成功
        if (Objects.equals(status, "2")) {
            //调用日志不为空（超过15天）
            if (!CollectionUtils.isEmpty(merReqLogs)) {
                int count = 0;
                for (MerReqLog log : merReqLogs) {
                    String logRes = ZipStrUtils.gunzip(log.getRespJson());
                    JSONObject resJson = JSON.parseObject(logRes);
                    JSONObject redData = resJson.getJSONObject("data");
                    if (Objects.equals(redData.getString("status"), "2") && Objects.equals(redData.getBoolean("isExists"), true)) {
                        ZonedDateTime zonedDateTime = log.getCreateTime().atZone(ZoneId.systemDefault());
                        if (zonedDateTime.toInstant().toEpochMilli() - record.getCreateTime().getTime() <= 1296000000) {
                            if (count == 0) {
                                count++;
                            }
                        } else {
                            count++;
                        }
                    }
                }
                billedTimes += count;
            } else {
                if (Objects.equals(data.getBoolean("isExists"), true)) {
                    billedTimes = 1;
                }
            }
        } else {
            isAllSuccess = false;
        }
        record.setBilledTimes(billedTimes);
        return isAllSuccess;
    }


    private boolean checkHouseSuccess(JSONObject data, FxReqRecord record, MerReport merReport) {
        JSONArray authResults = data.getJSONArray("authResults");
        int inPrice = 0;
        int billedTimes = 0;
        boolean isAllSuccess = true;
        List<MerReqLog> merReqLogs = null;
        //没有起始时间，找到起始时间
        if (Objects.isNull(record.getCreateTime())) {
            FxReqRecord reqRecordTmp = new FxReqRecord();
            reqRecordTmp.setReqOrderNo(record.getReqOrderNo());
            FxReqRecord reqRecord = fxReqRecordMapper.selectFxReqRecordByReqOrderNoAndUserFlag(reqRecordTmp);
            if (Objects.isNull(reqRecord)) {
                long time = java.sql.Date.valueOf(merReport.getStartTime()).getTime();
                Date date = new Date(time);
                record.setCreateTime(date);
                String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
                record.setCreateTimeStr(dateStr);
            }else {
                Date date = reqRecord.getCreateTime();
                record.setCreateTime(date);
                String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
                record.setCreateTimeStr(dateStr);
            }
        }
        //超过15天，获本月取调用日志
        if (record.getUpdateTime().getTime() - record.getCreateTime().getTime() > 1296000000) {
            System.err.println(record.getCreateTime());
            MerReqLogBean merReqLogBean = new MerReqLogBean();
            merReqLogBean.setStartTime(merReport.getStartTime());
            merReqLogBean.setEndTime(merReport.getEndTime().atTime(23, 59, 59));
            merReqLogBean.setMerCode(record.getMerCode());
            merReqLogBean.setProductCode("BG_HOUSE_002");
            merReqLogBean.setReqJson("\"reqOrderNo\":\"" + record.getReqOrderNo() + "\"");
            merReqLogs = merReqLogMapper.selectListOrderByReqTime(merReqLogBean);
        }
        if (Objects.nonNull(authResults)) {
            for (int i = 0; i < authResults.size(); i++) {
                JSONObject authResult = authResults.getJSONObject(i);
                if (Objects.equals(authResult.getString("authStateDesc"), "核查成功")) {
                    String cardNum = authResult.getString("cardNum");
                    JSONArray resultList = authResult.getJSONArray("resultList");
                    //根据最新结果计算进价
                    int resultInPrice = 0;
                    for (int j = 0; j < resultList.size(); j++) {
                        JSONObject result = resultList.getJSONObject(j);
                        String certNo = result.getString("certNo");
                        if (StringUtils.isBlank(certNo)) {
                            System.err.println(result.toJSONString());
                        } else {
                            int areaLevelPrice = getAreaLevelPrice(certNo);
                            resultInPrice = Math.max(areaLevelPrice, resultInPrice);
                        }
                    }
                    inPrice += resultInPrice;
                    //本月调用日志不为空（超过15天）。15天内的所有核查成功算一笔，15天外的每次算一笔。
                    if (!CollectionUtils.isEmpty(merReqLogs)) {
                        int count = 0;
                        for (MerReqLog merReqLog : merReqLogs) {
                            try {
                                String logRes = ZipStrUtils.gunzip(merReqLog.getRespJson());
                                JSONObject resJson = JSON.parseObject(logRes);
                                JSONObject redData = resJson.getJSONObject("data");
                                JSONArray resAuthResults = redData.getJSONArray("authResults");
                                for (int j = 0; j < resAuthResults.size(); j++) {
                                    JSONObject resAuthResult = resAuthResults.getJSONObject(j);
                                    if (Objects.equals(cardNum, resAuthResult.getString("cardNum"))) {
                                        if (Objects.equals(authResult.getString("authStateDesc"), "核查成功")) {
                                            JSONArray authResultList = authResult.getJSONArray("resultList");
                                            if (Objects.nonNull(authResultList) && authResultList.size() > 0) {
                                                ZonedDateTime zonedDateTime = merReqLog.getCreateTime().atZone(ZoneId.systemDefault());
                                                if (zonedDateTime.toInstant().toEpochMilli() - record.getCreateTime().getTime() <= 1296000000) {
                                                    if (count == 0) {
                                                        count++;
                                                    }
                                                } else {
                                                    count++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e) {
                                log.error("checkHouseSuccess：{}", e.getMessage());
                            }
                        }
                        billedTimes += count;
                    }
                    //没有调用日志，根据结果算笔数。
                    else {
                        billedTimes += (resultList.size() > 0 ? 1 : 0);
                    }
                } else {
                    isAllSuccess = false;
                }
            }
        }
        record.setInPrice(inPrice);
        record.setBilledTimes(billedTimes);
        return isAllSuccess;
    }


    private static final List<String> FIRST_CLASS_AREAS = Arrays.asList("京", "沪", "申", "广", "粤");
    private static final List<String> THIRD_CLASS_AREAS = Arrays.asList("陕", "秦", "甘", "陇", "蒙", "琼", "宁", "新", "川", "蜀", "吉", "贵", "黔", "赣");

    /**
     * 一类地区（北京、上海、广州、深圳）
     * 二类地区（除一三类以外的区域）
     * 三类地区（陕西、甘肃、内蒙、海南、宁夏、新疆、四川、吉林、陕西、贵州、江西）
     *
     * @param realEstateCertNo
     * @return
     */
    public static int getAreaLevelPrice(String realEstateCertNo) {
        if (FIRST_CLASS_AREAS.stream().anyMatch(realEstateCertNo::contains)) {
            return 10;
        } else if (THIRD_CLASS_AREAS.stream().anyMatch(realEstateCertNo::contains)) {
            return 5;
        } else {
            return 8;
        }
    }

}
