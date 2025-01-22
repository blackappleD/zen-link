package com.mkc.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.utils.ApiUtils;
import com.mkc.api.dto.bg.res.CreditA107ResDTO;
import com.mkc.api.dto.bg.res.CreditA108ResDTO;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.StringUtils;
import com.mkc.common.utils.Tuple2;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.dto.*;
import com.mkc.dto.bdc.BdcRequest;
import com.mkc.dto.bdc.BdcResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author tqlei
 * @date 2023/6/30 14:53
 */

@Slf4j
@RestController
@RequestMapping("/test")
@Profile({"dev", "local", "test", "pre"})
public class TestController {

	private final ThreadPoolExecutor carThreadPoolExecutor = new ThreadPoolExecutor(10, 10,
			1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new Sleep10sResubmitHandler());
	private final ThreadPoolExecutor towWThreadPoolExecutor = new ThreadPoolExecutor(10, 10,
			1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new Sleep10sResubmitHandler());
	private final ThreadPoolExecutor houseThreadPoolExecutor = new ThreadPoolExecutor(10, 10,
			1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new Sleep10sResubmitHandler());

	public static class Sleep10sResubmitHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				Thread.sleep(5000);
				executor.submit(r);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 车五项
	 */
	@PostMapping("/testCar")
	public void testCar(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestCar> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestCar.class)
					.sheet(sheetNo - 1)
					.doReadSync();
			List<ExcelTestCar> list = readList.stream()
					.filter(cell -> {
						if (cell.getPlateNo().contains("粤")) {
							return !CharSequenceUtil.containsAny(cell.getPlateNo(), "港", "澳", "领", "学", "外", "警", ".");
						}
						return true;
					})
					.collect(Collectors.toList());

			CountDownLatch latch = new CountDownLatch(list.size());
			for (ExcelTestCar read : list) {
				carThreadPoolExecutor.submit(() -> {
					JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
					String plaintext = read.getPlateNo();
					JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/carInfo", jsonObject, plaintext);
					read.setCode(post.getString("code"));
					read.setMsg(post.getString("msg"));
					try {
						JSONObject data = post.getJSONObject("data");
						if (Objects.nonNull(data)) {
							String engineNo = data.getString("engineNo");
							String brandName = data.getString("brandName");
							String vin = data.getString("vin");
							String initialRegistrationDate = data.getString("initialRegistrationDate");
							String modelNo = data.getString("modelNo");
							read.setEngineNo(engineNo);
							read.setBrandName(brandName);
							read.setVin(vin);
							read.setInitialRegistrationDate(initialRegistrationDate);
							read.setModelNo(modelNo);
							if (CharSequenceUtil.isAllNotBlank(engineNo, brandName, vin, initialRegistrationDate, modelNo)) {
								read.setMissParam("否");
							} else {
								read.setMissParam("是");
							}
						}
					} catch (Exception e) {
						read.setEngineNo(post.getString("data"));
						log.error(e.getMessage());
					}
					latch.countDown();
				});
			}
			latch.await();
			log.info(list.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "车五项测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestCar.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("车五项测试结果" + sheetNo)
					.doWrite(list);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 通过供应商调用日志，获取不动产核查结果
	 *
	 * @param file
	 * @param sheetNo
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/test_hose_with_export_log")
	public void testHouseWithExportLog(@RequestParam MultipartFile file,
	                                   Integer sheetNo,
	                                   HttpServletResponse response) throws IOException {
		List<SupLogLine> readList = EasyExcel.read(file.getInputStream())
				.headRowNumber(1)
				.head(SupLogLine.class)
				.sheet(sheetNo - 1)
				.doReadSync();
		List<ExcelTestHouse> writeList = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(readList.size());
		readList.forEach(line -> houseThreadPoolExecutor.submit(() -> {
			try {
				String responseJson = ZipStrUtils.gunzip(line.getResJson());
				BdcResponse res = JSONUtil.toBean(responseJson, BdcResponse.class);

				BdcRequest req = JSONUtil.toBean(line.getReqJson(), BdcRequest.class);

				String reqOrderNo = res.getData().getReqOrderNo();
				req.getData().getPersons().forEach(person -> {
					String name = person.getName();
					String cardNum = person.getCardNum();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("reqOrderNo", reqOrderNo);
					jsonObject.put("personCardNumList", ListUtil.of(cardNum));
					JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", jsonObject, reqOrderNo);
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
									excelTestHouse.setXm(name);
									excelTestHouse.setPersonCardNum(cardNum);
									excelTestHouse.setCode(post.getString("code"));
									excelTestHouse.setReqOrderNo(reqOrderNo);
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
							} else {
								ExcelTestHouse excelTestHouse = new ExcelTestHouse();
								excelTestHouse.setXm(name);
								excelTestHouse.setPersonCardNum(cardNum);
								excelTestHouse.setCode(post.getString("code"));
								excelTestHouse.setReqOrderNo(reqOrderNo);
								writeList.add(excelTestHouse);
							}
						}
					}
					System.err.println(post);
				});
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			latch.countDown();
		}));
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		setExcelRespProp(response, DateUtils.dateTimeNow() + "不动产测试结果");
		EasyExcel.write(response.getOutputStream())
				.head(ExcelTestHouse.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("不动产测试结果")
				.doWrite(writeList);

	}


	/**
	 * 社保经济能力评级V3
	 */
	@PostMapping("/testV3")
	public void testEconomicRate(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestV> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestV.class)
					.sheet(sheetNo - 1)
					.doReadSync();

			for (ExcelTestV read : readList) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
				String plaintext = read.getIdCard() + read.getName() + read.getMobile();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfoV3", jsonObject, plaintext);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setLevel(data.getString("level"));
					}
				} catch (Exception e) {
					read.setLevel(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "经济能力评级V3测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestV.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("经济能力评级V3测试结果" + sheetNo)
					.doWrite(readList);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 社保经济能力评级V7
	 */
	@PostMapping("/testV7")
	public void testV7(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestV> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestV.class)
					.sheet(sheetNo - 1)
					.doReadSync();

			for (ExcelTestV read : readList) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
				String plaintext = read.getIdCard() + read.getName() + read.getMobile();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfoV7", jsonObject, plaintext);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setLevel(data.getString("level"));
					}
				} catch (Exception e) {
					read.setLevel(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "经济能力评级V7测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestV.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("经济能力评级V7测试结果" + sheetNo)
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

			AtomicInteger success = new AtomicInteger(0);
			AtomicInteger fail = new AtomicInteger(0);
			CountDownLatch countDownLatch = new CountDownLatch(readList.size());
			long start = System.currentTimeMillis();
			for (ExcelTest2W read : readList) {
				towWThreadPoolExecutor.submit(() -> {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("idCard", read.getIdCard());
					jsonObject.put("name", read.getName());
					jsonObject.put("mobile", read.getMobile());
					String plainText = read.getIdCard() + read.getName() + read.getMobile();
					Tuple2<Integer, JSONObject> result = ApiUtils.queryApiWithStatus("http://api.zjbhsk.com/bg/financeInfo", jsonObject, plainText);
					Integer status = result.getV1();
					JSONObject post = result.getV2();
					if (status == 200) {
						success.incrementAndGet();
					} else {
						fail.incrementAndGet();
					}
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
					countDownLatch.countDown();
				});
			}
			countDownLatch.await();
			log.info(CharSequenceUtil.format("{}条样例测试完毕！总耗时{}ms, {}条成功, {}条失败"),
					readList.size(), System.currentTimeMillis() - start, success.get(), fail.get());
			setExcelRespProp(response, DateUtils.dateTimeNow() + "社保经济能力2W测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTest2W.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("社保经济能力2W测试结果")
					.doWrite(readList);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 企业四要素
	 */
	@PostMapping("/testEnterpriseFour")
	public void testEnterpriseFour(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelEnterpriseFour> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelEnterpriseFour.class)
					.sheet(0)
					.doReadSync();

			for (ExcelEnterpriseFour read : readList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("companyName", read.getCompanyName());
				jsonObject.put("creditCode", StringUtils.isNotBlank(read.getCreditCode()) ? read.getCreditCode() : read.getRegistrationNumber());
				jsonObject.put("legalPerson", read.getLegalPerson());
				jsonObject.put("certNo", read.getCertNo());
				String plainText = jsonObject.getString("companyName") + jsonObject.getString("creditCode") + jsonObject.getString("legalPerson") + jsonObject.getString("certNo");
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/enterpriseFourElementInfo", jsonObject, plainText);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setReturnCode(data.getString("returnCode"));
						read.setCreditCodeMatch(data.getString("creditCodeMatch"));
						read.setCompanyNameMatch(data.getString("companyNameMatch"));
						read.setLegalPersonMatch(data.getString("legalPersonMatch"));
						read.setCertNoMatch(data.getString("certNoMatch"));
					}
				} catch (Exception e) {
					read.setReturnCode(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "企业四要素测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelEnterpriseFour.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("企业四要素测试结果")
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

	@PostMapping("/testMaritalStatus")
	public void testMaritalStatus(@RequestBody MultipartFile excel, HttpServletResponse response) throws IOException {
		List<MaritalStatusCell> readList = EasyExcel.read(excel.getInputStream())
				.headRowNumber(1)
				.head(MaritalStatusCell.class)
				.sheet(0)
				.doReadSync();
		for (MaritalStatusCell read : readList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("xm", read.getName());
			jsonObject.put("sfzh", read.getIdCard());
			String plainText = read.getName() + read.getIdCard();
			JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/maritalStatus", jsonObject, plainText);
			read.setCode(post.getString("code"));
			JSONObject data = post.getObject("data", JSONObject.class);
			if (Objects.nonNull(data)) {
				read.setResult(data.getString("result"));
			} else {
				read.setResult("查无");
			}
			System.err.println(post);
		}
		setExcelRespProp(response, DateUtils.dateTimeNow() + "婚姻状况测试结果");
		EasyExcel.write(response.getOutputStream())
				.head(MaritalStatusCell.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("婚姻状况测试结果")
				.doWrite(readList);

	}

	@Data
	public static class MaritalStatusCell {

		@ExcelProperty("姓名")
		private String name;

		@ExcelProperty("身份证号")
		private String idCard;

		@ExcelProperty("返回码")
		private String code;

		@ExcelProperty("婚姻状况")
		private String result;


	}

	@PostMapping("/testMaritalRelationship")
	public void testMaritalRelationship(@RequestBody MultipartFile excel, HttpServletResponse response) throws IOException {
		List<MaritalRelationshipCell> readList = EasyExcel.read(excel.getInputStream())
				.headRowNumber(1)
				.head(MaritalRelationshipCell.class)
				.sheet(0)
				.doReadSync();
		for (MaritalRelationshipCell read : readList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("manIdcard", read.getManIdcard());
			jsonObject.put("manName", read.getManName());
			jsonObject.put("womanIdcard", read.getWomanIdcard());
			jsonObject.put("womanName", read.getWomanName());
			String plainText = read.getManIdcard() + read.getManName() + read.getWomanIdcard() + read.getWomanName();
			JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/maritalRelationship", jsonObject, plainText);
			read.setCode(post.getString("code"));
			JSONObject data = post.getObject("data", JSONObject.class);
			if (Objects.nonNull(data)) {
				read.setResult(data.getString("result"));
			} else {
				read.setResult("查无");
			}
			System.err.println(post);
		}
		setExcelRespProp(response, DateUtils.dateTimeNow() + "婚姻关系测试结果");
		EasyExcel.write(response.getOutputStream())
				.head(MaritalStatusCell.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("婚姻关系测试结果")
				.doWrite(readList);

	}

	@PostMapping("/testCreditA107")
	public void testCreditA107(@RequestBody MultipartFile excel, HttpServletResponse response) throws IOException {
		List<CreditA107Cell> readList = EasyExcel.read(excel.getInputStream())
				.headRowNumber(1)
				.head(CreditA107Cell.class)
				.sheet(0)
				.doReadSync();
		for (CreditA107Cell read : readList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cid", read.getCid());
			jsonObject.put("name", read.getName());
			jsonObject.put("mobile", read.getMobile());
			String plainText = read.getCid() + read.getName() + read.getMobile();
			JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/credit_a107", jsonObject, plainText);
			read.setCode(post.getString("code"));
			CreditA107ResDTO data = post.getObject("data", CreditA107ResDTO.class);
			if (Objects.nonNull(data)) {
				read.setResult("成功");
				BeanUtils.copyProperties(data, read);
			} else {
				read.setResult("查无");
			}

			System.err.println(post);
		}
		setExcelRespProp(response, DateUtils.dateTimeNow() + "网贷（申请多头）测试结果");
		EasyExcel.write(response.getOutputStream())
				.head(CreditA107Cell.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("网贷（申请多头）测试结果")
				.doWrite(readList);

	}

	@PostMapping("/testCreditA108")
	public void testCreditA108(@RequestBody MultipartFile excel, HttpServletResponse response) throws IOException {
		List<CreditA108Cell> readList = EasyExcel.read(excel.getInputStream())
				.headRowNumber(1)
				.head(CreditA108Cell.class)
				.sheet(0)
				.doReadSync();
		for (CreditA108Cell read : readList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cid", read.getCid());
			jsonObject.put("name", read.getName());
			jsonObject.put("mobile", read.getMobile());
			String plainText = read.getCid() + read.getName() + read.getMobile();
			JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/credit_a108", jsonObject, plainText);
			read.setCode(post.getString("code"));
			CreditA108ResDTO data = post.getObject("data", CreditA108ResDTO.class);
			if (Objects.nonNull(data)) {
				read.setResult("成功");
				BeanUtils.copyProperties(data, read);
			} else {
				read.setResult("查无");
			}

			System.err.println(post);
		}
		setExcelRespProp(response, DateUtils.dateTimeNow() + "网贷（授信多头）测试结果");
		EasyExcel.write(response.getOutputStream())
				.head(CreditA108Cell.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("网贷（授信多头）测试结果")
				.doWrite(readList);

	}

	@Data
	public static class CreditA107Cell {

		@ExcelProperty("身份证号")
		private String cid;

		@ExcelProperty("姓名")
		private String name;

		@ExcelProperty("手机号")
		private String mobile;

		@ExcelProperty("响应码")
		private String code;

		@ExcelProperty("请求结果")
		private String result;

		@ExcelProperty("score")
		private Long score;

		@ExcelProperty("D156")
		private String D156;

		@ExcelProperty("D157")
		private String D157;

		@ExcelProperty("D834")
		private String D834;

		@ExcelProperty("D857")
		private String D857;

		@ExcelProperty("D859")
		private String D859;

		@ExcelProperty("D863")
		private String D863;

		@ExcelProperty("D868")
		private String D868;

		@ExcelProperty("D869")
		private String D869;

		@ExcelProperty("D875")
		private String D875;
	}

	@Data
	public static class CreditA108Cell {

		@ExcelProperty("身份证号")
		private String cid;

		@ExcelProperty("姓名")
		private String name;

		@ExcelProperty("手机号")
		private String mobile;

		@ExcelProperty("响应码")
		private String code;

		@ExcelProperty("请求结果")
		private String result;

		@ExcelProperty("score")
		private Long score;

		@ExcelProperty("D901")
		private String D901;

		@ExcelProperty("D903")
		private String D903;

		@ExcelProperty("D153")
		private String D153;

		@ExcelProperty("D151")
		private String D151;

		@ExcelProperty("D150")
		private String D150;

		@ExcelProperty("D381")
		private String D381;

		@ExcelProperty("D917")
		private String D917;

		@ExcelProperty("D106")
		private String D106;

		@ExcelProperty("D108")
		private String D108;

		@ExcelProperty("D921")
		private String D921;

		@ExcelProperty("D935")
		private String D935;

		@ExcelProperty("D949")
		private String D949;

		@ExcelProperty("D950")
		private String D950;

		@ExcelProperty("D951")
		private String D951;

		@ExcelProperty("D952")
		private String D952;

		@ExcelProperty("D957")
		private String D957;

		@ExcelProperty("D958")
		private String D958;

		@ExcelProperty("D959")
		private String D959;
	}

	@Data
	public static class MaritalRelationshipCell {

		@ExcelProperty("男方身份证号")
		private String manIdcard;

		@ExcelProperty("男方姓名")
		private String manName;

		@ExcelProperty("女方身份证号")
		private String womanIdcard;

		@ExcelProperty("女方姓名")
		private String womanName;

		@ExcelProperty("请求状态码")
		private String code;

		@ExcelProperty("结果")
		private String result;

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


	public void testEduZjhm() {

		String filePath = "C:\\Users\\achen\\Desktop\\TT-1223.xlsx";


		List<ExcelTestEduZjhm> readList = EasyExcel.read(new File(filePath))
				.headRowNumber(1)
				.head(ExcelTestEduZjhm.class)
				.sheet(0)
				.doReadSync();

		ArrayList<ExcelTestEduZjhm> resultList = new ArrayList<>();
		for (ExcelTestEduZjhm read : readList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("xm", read.getXm());
			jsonObject.put("zjhm", read.getZjhm());
			String plainText = read.getXm() + read.getZjhm();
			JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/educationInfo", jsonObject, plainText);
			if (post.getString("msg").contains("超限")) {
				log.info("接口仍然超限！");
				return;
			}
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
		EasyExcel.write(new File("C:\\Users\\achen\\Desktop\\学历跑数结果.xlsx"))
				.head(ExcelTestEduZjhm.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("sheet")
				.doWrite(resultList);

	}

	/**
	 * 高等学历跑数-中电郑州
	 *
	 * @param excel
	 * @param response
	 */
	@PostMapping("/testEduZjhm")
	public void testEduZjhm(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTestEduZjhm> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestEduZjhm.class)
					.sheet(0)
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
