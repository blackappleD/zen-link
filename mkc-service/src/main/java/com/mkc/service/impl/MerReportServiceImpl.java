package com.mkc.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkc.api.common.utils.ApiUtils;
import com.mkc.bean.MerReqLogBean;
import com.mkc.common.enums.HouseLevelEnum;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.domain.*;
import com.mkc.mapper.FxReqRecordMapper;
import com.mkc.mapper.MerReportMapper;
import com.mkc.mapper.MerReqLogMapper;
import com.mkc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
//                    if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
					JSONObject request = JSON.parseObject(record.getMerRequestData());
					String plainText = record.getReqOrderNo();
					try {
						JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
						record.setUnknownInfo(post.toJSONString());
						JSONObject data = post.getJSONObject("data");
						checkHouseSuccess(data, record, merReport);
					} catch (Exception e) {
						log.info("listFxHouseReport 更新结果 queryApi【{}】", e.getMessage());
					}
					record.setRemark("更新结果");
//                    } else {
//                        record.setRemark("未超过10天");
//                    }
				}
			}
			//未查询结果，去查结果
			else {
//                if (DateUtils.getNowDate().getTime() - record.getCreateTime().getTime() >= 864000000) {
				try {

					JSONObject request = new JSONObject();
					request.put("personCardNumList", getCardsFromRecord(record));
					request.put("reqOrderNo", record.getReqOrderNo());
					String plainText = record.getReqOrderNo();
					JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
					record.setUnknownInfo(post.toJSONString());
					JSONObject data = post.getJSONObject("data");
					checkHouseSuccess(data, record, merReport);
				} catch (Exception e) {
					log.info("listFxHouseReport 查询结果 queryApi【{}】", e.getMessage());
				}
				record.setRemark("查询结果");
//                } else {
//                    record.setRemark("未超过10天");
//                }
			}
		}
		fxReqRecords.sort(Comparator.comparing(FxReqRecord::getCreateTime));
		return fxReqRecords;
	}

	@Override
	public List<FxReqRecord> listFxHouseReportV2(MerReport merReport) {
		List<FxReqRecord> fxReqRecords = fxReqRecordMapper.listByRangeTime(merReport);
		for (FxReqRecord record : fxReqRecords) {
			//未核查成功,去查询结果
			if (!Objects.equals(record.getUserFlag(), "1")) {
				try {
					JSONObject request = new JSONObject();
					if (StringUtils.isNotBlank(record.getMerRequestData())) {
						request.put("personCardNumList", record.getMerRequestData());
					} else {
						request.put("personCardNumList", getCardsFromRecord(record));
					}
					request.put("reqOrderNo", record.getReqOrderNo());
					String plainText = record.getReqOrderNo();
					JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", request, plainText);
					record.setUnknownInfo(post.toJSONString());
					JSONObject data = post.getJSONObject("data");
					//处理结果，计算计费次数和档次等
					checkHouseData(data, record);
				} catch (Exception e) {
					log.info("listFxHouseReport 查询结果 queryApi【{}】", e.getMessage());
				}
				record.setRemark("查询结果");
			}
		}
		return fxReqRecords;
	}

	private JSONArray getCardsFromRecord(FxReqRecord record) {
		JSONObject jsonObject = JSON.parseObject(record.getPersons());
		JSONArray personCardNumList = new JSONArray();
		JSONArray persons = jsonObject.getJSONArray("persons");
		for (int i = 0; i < persons.size(); i++) {
			personCardNumList.add(persons.getJSONObject(i).getString("cardNum"));
		}
		return personCardNumList;
	}

	public void checkHouseData(JSONObject returnData, FxReqRecord fxReqRecord) {
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
						JSONArray resultList = jsonObject.getJSONArray("resultList");
						if (!CollectionUtils.isEmpty(resultList)) {
							//15天后计费每次
							if (fxReqRecord.getUpdateTime().getTime() - fxReqRecord.getCreateTime().getTime() > 1296000000) {
								count++;
							}
							//15天内计费仅计费本月发起申请的首次查询结果
							else if (Objects.equals(fxReqRecord.getUserFlag(), "0")
									&& Objects.equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM, fxReqRecord.getCreateTime()), DateUtils.getDateMonth())) {
								count++;
							}
							for (int j = 0; j < resultList.size(); j++) {
								JSONObject perResult = resultList.getJSONObject(j);
								maxLevel = Math.min(HouseLevelEnum.getAreaLevel(perResult.getString("certNo")).getlevel(), maxLevel);
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
					} else {
						level.append(maxLevel);
					}
				}
				fxReqRecord.setFeeCount(fxReqRecord.getFeeCount() + count);
				fxReqRecord.setLevel(String.valueOf(level));
				fxReqRecord.setUserFlag(resultUserFlag);
			}
		}
	}

	@Override
	public List<FxReqRecord> listFxEduReport(MerReport merReport) {
		//查询商户调用日志
		MerReqLogBean merReqLogBean = new MerReqLogBean();
		merReqLogBean.setStartTime(merReport.getStartTime());
		merReqLogBean.setEndTime(merReport.getEndTime().atTime(23, 59, 59));
		merReqLogBean.setProductCode("BG_HIGH_SCHOOL_EDUCATION_001");
		merReqLogBean.setMerCode(merReport.getMerCode());
		List<MerReqLog> merReqLogs = merReqLogMapper.selectListOrderByReqTime(merReqLogBean);
		//将商户调用日志记录转化为不动产订单列表
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
			//未有查询结果，去查结果
			else {
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

		return fxReqRecords;
	}

	/**
	 * 总账
	 *
	 * @param fxReqRecords
	 * @param productName
	 * @return
	 */
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
//            merReport.setInPrice(BigDecimal.valueOf(value.stream().mapToDouble(FxReqRecord::getInPrice).sum()));
//            merReport.setSellPrice(productSellService.selectProductSellByMer(merReport.getMerCode(), ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode()).getSellPrice());
			merReport.setFeeTimes(value.stream().mapToInt(FxReqRecord::getFeeCount).sum());
//            merReport.setTotalPrice(merReport.getSellPrice().multiply(new BigDecimal(merReport.getFeeTimes().toString())));
			setLevel(value, merReport);
			merReports.add(merReport);
		}
		return merReports;
	}

	/**
	 * 明细，根据调用日期分类
	 *
	 * @param fxReqRecords
	 * @param productName
	 * @return
	 */
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
//                merReport.setInPrice(BigDecimal.valueOf(dateValue.getValue().stream().mapToDouble(FxReqRecord::getInPrice).sum()));
				merReport.setFeeTimes(dateValue.getValue().stream().mapToInt(FxReqRecord::getFeeCount).sum());
				setLevel(dateValue.getValue(), merReport);
				merReports.add(merReport);
			}
		}
		merReports.sort(Comparator.comparing(MerReportExcel::getReqDate));
		return merReports;
	}

	/**
	 * 计算分类不动产档次
	 *
	 * @param value
	 * @param merReport
	 */
	private void setLevel(List<FxReqRecord> value, MerReportExcel merReport) {
		int level1 = 0;
		int level2 = 0;
		int level3 = 0;
		for (FxReqRecord record : value) {
			if (StringUtils.isNotBlank(record.getLevel())) {
				String[] levels = record.getLevel().split(",");
				for (String level : levels) {
					if (Objects.equals(level, HouseLevelEnum.A.getlevel().toString())) {
						level1++;
					} else if (Objects.equals(level, HouseLevelEnum.B.getlevel().toString())) {
						level2++;
					} else if (Objects.equals(level, HouseLevelEnum.C.getlevel().toString())) {
						level3++;
					}
				}
			}
		}

		merReport.setLevel1(level1);
		merReport.setLevel2(level2);
		merReport.setLevel3(level3);
	}

	/**
	 * 将商户调用日志转化为不动产订单列表
	 *
	 * @param merReqLogs
	 * @return
	 */
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
						FxReqRecord record = fxReqRecords.stream().filter(p -> Objects.equals(p.getReqOrderNo(), reqOrderNo) && Objects.equals(p.getMerCode(), merReqLog.getMerCode())).findFirst().orElse(null);
						if (Objects.isNull(record)) {
							FxReqRecord recordTmp = new FxReqRecord();
							recordTmp.setReqOrderNo(reqOrderNo);
							Date date = Date.from(merReqLog.getReqTime().atZone(ZoneId.systemDefault()).toInstant());
							String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
							recordTmp.setCreateTime(date);
							recordTmp.setUpdateTime(date);
							recordTmp.setCreateTimeStr(dateStr);
							recordTmp.setUpdateTimeStr(dateStr);
							recordTmp.setMerCode(merReqLog.getMerCode());
							recordTmp.setPersons(merReqLog.getReqJson());
							recordTmp.setMerName(merReqLog.getMerName());
//                        JSONObject reqJson = JSON.parseObject(merReqLog.getReqJson());
//                        JSONArray persons = reqJson.getJSONArray("persons");
							recordTmp.setUserFlag("0");
							fxReqRecords.add(recordTmp);
						} else {
							Date date = Date.from(merReqLog.getReqTime().atZone(ZoneId.systemDefault()).toInstant());
							String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
							record.setCreateTime(date);
							record.setCreateTimeStr(dateStr);
							record.setPersons(merReqLog.getReqJson());
						}
					}
					//查询结果请求
					else {
						JSONObject jsonObject = JSON.parseObject(merReqLog.getReqJson());
						String reqOrderNo = jsonObject.getString("reqOrderNo");
						FxReqRecord record = fxReqRecords.stream().filter(p -> Objects.equals(p.getReqOrderNo(), reqOrderNo) && Objects.equals(p.getMerCode(), merReqLog.getMerCode())).findFirst().orElse(null);
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
							recordTmp.setMerName(merReqLog.getMerName());
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

	/**
	 * 分析该条学历订单
	 *
	 * @param data
	 * @param record
	 * @param merReport
	 * @return
	 */
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
		record.setFeeCount(billedTimes);
		return isAllSuccess;
	}

	/**
	 * 分析该条不动产订单
	 *
	 * @param data
	 * @param record
	 * @param merReport
	 * @return
	 */
	private boolean checkHouseSuccess(JSONObject data, FxReqRecord record, MerReport merReport) {
		JSONArray authResults = data.getJSONArray("authResults");
		StringBuilder level = new StringBuilder(StringUtils.EMPTY);
		int inPrice = 0;
		int billedTimes = 0;
		boolean isAllSuccess = true;
		List<MerReqLog> merReqLogs = null;
		//没有起始时间，找到起始时间
		if (Objects.isNull(record.getCreateTime())) {
			FxReqRecord reqRecordTmp = new FxReqRecord();
			reqRecordTmp.setReqOrderNo(record.getReqOrderNo());
			reqRecordTmp.setMerCode(record.getMerCode());
			FxReqRecord reqRecord = fxReqRecordMapper.selectFxReqRecordByReqOrderNoAndUserFlag(reqRecordTmp);
			if (Objects.isNull(reqRecord)) {
				long time = java.sql.Date.valueOf(merReport.getStartTime()).getTime();
				Date date = new Date(time);
				record.setCreateTime(date);
				String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
				record.setCreateTimeStr(dateStr);
			} else {
				Date date = reqRecord.getCreateTime();
				record.setCreateTime(date);
				String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
				record.setCreateTimeStr(dateStr);
			}
		}
		//超过15天，获本月取调用日志
		if (record.getUpdateTime().getTime() - record.getCreateTime().getTime() > 1296000000) {
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
				StringBuilder levelTmp = new StringBuilder(StringUtils.EMPTY);
				JSONObject authResult = authResults.getJSONObject(i);
				if (Objects.equals(authResult.getString("authStateDesc"), "核查成功")) {
					String cardNum = authResult.getString("cardNum");
					JSONArray resultList = authResult.getJSONArray("resultList");
					record.setIsGet(Objects.nonNull(record.getIsGet()) && record.getIsGet() || resultList.size() > 0);
					//根据最新结果计算进价
					int resultInPrice = 0;
					for (int j = 0; j < resultList.size(); j++) {
						JSONObject result = resultList.getJSONObject(j);
						String certNo = result.getString("certNo");
						int areaLevelPrice = HouseLevelEnum.getAreaLevel(certNo).getprice();
						resultInPrice = Math.max(areaLevelPrice, resultInPrice);

					}
					inPrice += resultInPrice;
					if (resultInPrice > 0) {
						levelTmp.append(HouseLevelEnum.getLevelByPrice(resultInPrice)).append(",");
					}
					//本月调用日志不为空（超过15天）。15天内的所有核查成功算一笔，15天外的每次算一笔。
					if (!CollectionUtils.isEmpty(merReqLogs)) {
						int count = 0;
						for (MerReqLog merReqLog : merReqLogs) {
							try {
								JSONObject logJson = JSON.parseObject(ZipStrUtils.gunzip(merReqLog.getRespJson()));
								JSONArray logAuthResults = logJson.getJSONObject("data").getJSONArray("authResults");
								for (int j = 0; j < logAuthResults.size(); j++) {
									JSONObject logAuthResult = logAuthResults.getJSONObject(j);
									if (Objects.equals(cardNum, logAuthResult.getString("cardNum"))) {
										if (Objects.equals(logAuthResult.getString("authStateDesc"), "核查成功")) {
											JSONArray logResultList = logAuthResult.getJSONArray("resultList");
											if (Objects.nonNull(logResultList) && logResultList.size() > 0) {
												//调用时间
												long createTimeStamp = merReqLog.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
												//15天内（调用时间-提交时间）
												if (createTimeStamp - record.getCreateTime().getTime() <= 1296000000) {
													//只算本月提交申请的且第一次查询（提交时间>本月第一天）
													if (count == 0 && record.getCreateTime().getTime() > java.sql.Date.valueOf(merReport.getStartTime()).getTime()) {
														count++;
														level.append(levelTmp);
													}
												}
												//15天外
												else {
													count++;
													level.append(levelTmp);
												}
											}
										}
									}
								}
							} catch (Exception e) {
								log.error("checkHouseSuccess：{}", e.getMessage());
							}
						}
						billedTimes += count;
					}
					//没有调用日志，根据结果算笔数。
					else {
						billedTimes += (resultList.size() > 0 ? 1 : 0);
						level.append(levelTmp);
					}
				} else {
					isAllSuccess = false;
				}
			}
		}
		record.setInPrice(inPrice);
		record.setLevel(StringUtils.isNotBlank(level) ? level.substring(0, level.length() - 1) : null);
		record.setFeeCount(billedTimes);
		return isAllSuccess;
	}

}
