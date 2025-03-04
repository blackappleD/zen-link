package com.mkc.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.utils.Tuple2;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.common.utils.bean.BeanUtils;
import com.mkc.dto.ExcelTestCar;
import com.mkc.dto.ExcelTestHouse;
import com.mkc.dto.MerLogLine;
import com.mkc.dto.SupLogLine;
import com.mkc.util.JsonUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/27 14:02
 */
@Slf4j
@RestController
@Profile({"dev", "local", "test", "pre"})
@RequestMapping("/test2")
public class TestController2 {

	private static final String DOWNLOAD_FILEPATH = "C:/Users/achen/Downloads/";

	public static <T> List<T> readExcel(String filePath, Class<T> clazz) {
		return EasyExcel.read(new File(filePath))

				.headRowNumber(1)
				.head(clazz)
				.doReadAllSync();

	}

	public static <T> List<T> readExcelBatch(String dicPath, Class<T> clazz) {

		File folder = new File(dicPath);
		// 获取文件夹下的所有文件
		File[] files = folder.listFiles();
		List<T> list = new ArrayList<>();
		if (files == null) {
			return list;
		}
		for (File file : files) {
			List<T> objects = EasyExcel.read(file)
					.headRowNumber(1)
					.head(clazz)
					.doReadAllSync();
			System.out.println("读取文件：" + file.getName() + " 录入数据 " + objects.size() + "条");
			list.addAll(objects);
		}
		return list;

	}


	/**
	 * 全国高等学历信息查询 /educationInfo 日志分析
	 */
	@Test
	public void educationInfo() {

		// 月份 商户Code 统计数据
		Map<String, Map<String, Statistic>> map = new HashMap<>();

		List<SupLogLine> supLogLines = readExcelBatch("D:\\work\\学历日志分析", SupLogLine.class);

		for (SupLogLine supLogLine : supLogLines) {

			String merCode = supLogLine.getMerCode();
			LocalDateTime reqTime = supLogLine.getReqTime();
			String yearMonth = CharSequenceUtil.format("{}年{}月", reqTime.getYear(), reqTime.getMonth().getValue());

			if (!map.containsKey(yearMonth)) {
				map.put(yearMonth, new HashMap<>());
			}
			Map<String, Statistic> merStatisticMap = map.get(yearMonth);

			if (!merStatisticMap.containsKey(merCode)) {
				Statistic statistic = new Statistic();
				statistic.setMerCode(merCode);
				statistic.setProductName("全国高等学历信息查询");
				merStatisticMap.put(merCode, statistic);
			}
			Statistic statistic = merStatisticMap.get(merCode);

			String resJson = supLogLine.getResJson();
			try {
				resJson = ZipStrUtils.gunzip(resJson);
			} catch (Exception ignored) {
			}
			statistic.addTotal();
			switch (supLogLine.getStatus()) {
				case "1":
					statistic.addSuccess();
					if (resJson.contains("找不到匹配的学历信息")) {
						statistic.addFindNo();
					} else {
						statistic.addFindYes();
					}
					break;
				case "2":
					break;
				case "4":
					statistic.addException();
					break;
				default:
			}

		}

		try (ExcelWriter excelWriter = EasyExcel.write(new File("C:\\Users\\achen\\Desktop\\中电郑州学历接口各商户历史调用量统计.xlsx"))
				.excelType(ExcelTypeEnum.XLSX)
				.build()) {
			map.forEach((key, value) -> {
				WriteSheet writeSheet = EasyExcel.writerSheet(key)
						.head(Statistic.class)
						.build();
				excelWriter.write(value.values(), writeSheet);
			});

		}

	}


	@Data
	private static class Statistic {
		@ColumnWidth(10)
		@ExcelProperty("商户编码")
		private String merCode;

		@ColumnWidth(10)
		@ExcelProperty("产品名称")
		private String productName;

		@ColumnWidth(10)
		@ExcelProperty("总次数")
		private int total;

		@ColumnWidth(10)
		@ExcelProperty("成功")
		private int success = 0;

		@ColumnWidth(10)
		@ExcelProperty("查得有数据")
		private int findYes = 0;

		@ColumnWidth(10)
		@ExcelProperty("查得无数据")
		private int findNo = 0;

		@ColumnWidth(10)
		@ExcelProperty("异常")
		private int exception = 0;

		private void addSuccess() {
			success = success + 1;
		}

		private void addTotal() {
			total = total + 1;
		}

		private void addFindYes() {
			findYes = findYes + 1;
		}

		private void addFindNo() {
			findNo = findNo + 1;
		}

		private void addException() {
			exception = exception + 1;
		}
	}

	@Test
	public void financeIcsConvergence() {
		List<FinIcsBaseCell> baseList = readExcel("D:\\跑数\\ABEF分\\20250220\\samples_20241210_modify.xlsx", FinIcsBaseCell.class);
		Map<String, FinIcsResult> aMap = readExcel("D:\\跑数\\ABEF分\\20250220\\测试结果\\a.xlsx", FinIcsResult.class)
				.stream().collect(Collectors.toMap(FinIcsResult::getIdCard, Function.identity()));
		Map<String, FinIcsResult> bMap = readExcel("D:\\跑数\\ABEF分\\20250220\\测试结果\\b.xlsx", FinIcsResult.class)
				.stream().collect(Collectors.toMap(FinIcsResult::getIdCard, Function.identity()));
		Map<String, FinIcsResult> eMap = readExcel("D:\\跑数\\ABEF分\\20250220\\测试结果\\e.xlsx", FinIcsResult.class)
				.stream().collect(Collectors.toMap(FinIcsResult::getIdCard, Function.identity()));
		Map<String, FinIcsResult> fMap = readExcel("D:\\跑数\\ABEF分\\20250220\\测试结果\\f.xlsx", FinIcsResult.class)
				.stream().collect(Collectors.toMap(FinIcsResult::getIdCard, Function.identity()));

		baseList.forEach(cell -> {
			if (aMap.containsKey(cell.getIdCard())) {
				cell.setA(aMap.get(cell.getIdCard()).getScore());
			}
			if (bMap.containsKey(cell.getIdCard())) {
				cell.setB(bMap.get(cell.getIdCard()).getScore());
			}
			if (eMap.containsKey(cell.getIdCard())) {
				cell.setE(eMap.get(cell.getIdCard()).getScore());
			}
			if (fMap.containsKey(cell.getIdCard())) {
				cell.setF(fMap.get(cell.getIdCard()).getScore());
			}
		});

		EasyExcel.write(new File("D:\\跑数\\ABEF分\\20250220\\samples_20241210_modify.xlsx"))
				.head(FinIcsBaseCell.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("sheet1")
				.doWrite(baseList);


	}

	@Data
	public static class FinIcsResult {

		@ExcelProperty("身份证号")
		private String idCard;

		@ExcelProperty("分值")
		private String score;

	}

	@Data
	public static class FinIcsBaseCell {

		@ExcelProperty("身份证号")
		private String idCard;

		@ExcelProperty("手机号")
		private String mobile;

		@ExcelProperty("姓名")
		private String name;

		@ExcelProperty("A")
		private String a;

		@ExcelProperty("B")
		private String b;

		@ExcelProperty("E")
		private String e;

		@ExcelProperty("F")
		private String f;

	}

	@Test
	public void carInfoFromSupLog() {
		List<SupLogLine> readList = readExcel("D:/downloads/1741070778623调用供应商日志数据.xlsx", SupLogLine.class);

		List<ExcelTestCar> list = new ArrayList<>();
		for (SupLogLine data : readList) {

			String carNo = JsonPath.read(data.getReqJson(), "$.data.carNo");
			ExcelTestCar read = new ExcelTestCar();
			list.add(read);
			read.setPlateNo(carNo);
			read.setCode(data.getStatus());
			if (CharSequenceUtil.isBlank(data.getResJson())) {
				read.setMsg("异常");
			} else if ("1".equals(data.getStatus())) {
				String gunzip = ZipStrUtils.gunzip(data.getResJson());

				ExcelDTO<CarResult> response = JsonUtil.fromJson(gunzip, new TypeReference<ExcelDTO<CarResult>>() {
				});
				CarResult result = response.getResult();
				read.setMsg("成功");
				String engineNo = result.getEngine();
				String vin = result.getVin();
				String recordDate = result.getRecordDate();
				String carName = result.getCarName();
				String vehicleModel = result.getVehicleModel();
				read.setEngineNo(engineNo);
				read.setBrandName(carName);
				read.setVin(vin);
				read.setInitialRegistrationDate(recordDate);
				read.setModelNo(vehicleModel);
				if (CharSequenceUtil.isAllNotBlank(engineNo, vin, recordDate, carName, vehicleModel)) {
					read.setMissParam("否");
				} else {
					read.setMissParam("是");
				}
			} else if ("2".equals(data.getStatus())) {
				read.setMsg("查无");
			} else if ("4".equals(data.getStatus())) {
				if (data.getResJson().contains("车牌号有误")) {
					read.setMsg("车牌号有误");
				} else {
					read.setMsg("异常");
				}
			}
		}

		List<ExcelTestCar> uniqueItems = list.stream()
				.distinct()
				.collect(Collectors.toList());

		EasyExcel.write(new File("D:\\跑数\\车五项\\2.18\\金润\\jr车五项样本(0218)跑数结果-完整.xlsx"))
				.head(ExcelTestCar.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("车五项测试结果")
				.doWrite(uniqueItems);

	}

	@Test
	public void testHouseJson() {

		String json = FileUtil.readString("D:\\跑数\\不动产\\2.11\\response.json", StandardCharsets.UTF_8);
		HouseJson houseJson = JsonUtil.fromJson(json, Response.class).getData();
		List<ExcelTestHouse> excelTestHouses = new ArrayList<>();

		Map<String, String> idNameMap = readExcel("D:\\跑数\\不动产\\2.11\\银融-房产测试.xlsx", TestController.IdCardNameCell.class)
				.stream().collect(Collectors.toMap(TestController.IdCardNameCell::getCardNum, TestController.IdCardNameCell::getName));

		houseJson.getAuthResults().forEach(authResult -> {
			String cardNum = authResult.getCardNum();
			String name = idNameMap.get(cardNum);
			String reqOrderNo = houseJson.getReqOrderNo();

			if (authResult.getResultList().isEmpty()) {
				ExcelTestHouse excelTestHouse = new ExcelTestHouse();
				excelTestHouse.setXm(name);
				excelTestHouse.setPersonCardNum(cardNum);
				excelTestHouse.setCode("200");
				excelTestHouse.setReqOrderNo(reqOrderNo);
				excelTestHouses.add(excelTestHouse);
			} else {
				authResult.getResultList().forEach(resultItem -> {
					ExcelTestHouse excelTestHouse = new ExcelTestHouse();
					excelTestHouse.setXm(name);
					excelTestHouse.setPersonCardNum(cardNum);
					excelTestHouse.setCode("200");
					excelTestHouse.setReqOrderNo(reqOrderNo);
					BeanUtils.copyProperties(resultItem, excelTestHouse);
					excelTestHouses.add(excelTestHouse);
				});
			}

		});

		EasyExcel.write(new File("D:\\跑数\\不动产\\2.11\\不动产跑数结果.xlsx"))
				.head(ExcelTestHouse.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("不动产跑数结果")
				.doWrite(excelTestHouses);


	}

	@Data
	public static class Response {
		public String code;
		public String msg;
		public String seqNo;
		public String free;
		public HouseJson data;
	}

	@Data
	public static class HouseJson {

		public String reqOrderNo;
		public String approvalStatus;
		public List<AuthResult> authResults;

		@Data
		public static class AuthResult {
			public String cardNum;
			public String authState;
			public String authStateDesc;
			public Integer isReAuth;
			public List<ResultItem> resultList;
		}

		@Data
		public static class ResultItem {
			public String certNo;
			public String unitNo;
			public String location;
			public String ownership;
			public String houseArea;
			public String rightsType;
			public String isSealUp;
			public String isMortgaged;
			public String rightsStartTime;
			public String rightsEndTime;
			public String useTo;
		}

	}

	/**
	 * 不动产商户调用统计
	 */
	public void test2() {
		List<MerLogLine> readList = EasyExcel.read(new File("C:\\Users\\achen\\Downloads\\1732873569158商户调用日志数据.xlsx"))
				.headRowNumber(1)
				.head(MerLogLine.class)
				.sheet(0)
				.doReadSync();

		// 计费次数，不计费次数
		Map<String, Map<String, Tuple2<Integer, Integer>>> map = new HashMap<>();


		for (MerLogLine data : readList) {
			if ("1".equals(data.getStatus())) {
				String gunzip = ZipStrUtils.gunzip(data.getResJson());
				String merCode = JsonPath.read(data.getReqJson(), "$.merCode");

				String free = JsonPath.read(gunzip, "$.free");
				if (!map.containsKey(merCode)) {
					Map<String, Tuple2<Integer, Integer>> countMap = new HashMap<>();
					map.put(merCode, countMap);
				}
				Map<String, Tuple2<Integer, Integer>> countMap = map.get(merCode);
				if (!data.getReqJson().contains("reqOrderNo")) {
					String reqOrderNo = JsonPath.read(gunzip, "$.data.reqOrderNo");
					if (!countMap.containsKey(reqOrderNo)) {
						countMap.put(reqOrderNo, new Tuple2<>(0, 0));
					}
					Tuple2<Integer, Integer> tuple = countMap.get(reqOrderNo);
					tuple.setV1(tuple.getV1() + 1);
				} else {
					String reqOrderNo = JsonPath.read(data.getReqJson(), "$.reqOrderNo");

					if (!countMap.containsKey(reqOrderNo)) {
						countMap.put(reqOrderNo, new Tuple2<>(0, 0));
					}
					Tuple2<Integer, Integer> tuple = countMap.get(reqOrderNo);
					if (free.equals(FreeStatus.NO.getCode())) {
						tuple.setV2(tuple.getV2() + 1);
					} else {
						tuple.setV1(tuple.getV1() + 1);
					}
				}
			}
		}
		List<HouseTestStatistic> list = new ArrayList<>();
		map.forEach((merCode, countMap) ->
				countMap.forEach((reqOrderNo, tuple) ->
						list.add(HouseTestStatistic.builder().merCode(merCode)
								.reqOrderNo(reqOrderNo)
								.notFreeTimes(tuple.getV1())
								.freeTimes(tuple.getV2())
								.build())));

		EasyExcel.write(new File("D:\\跑数\\不动产\\11.19-11.29各商户不动产15日内免费调用统计.xlsx"))
				.head(HouseTestStatistic.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("统计")
				.doWrite(list);


	}

	@Data
	@Builder
	public static class HouseTestStatistic {
		@ExcelProperty("商户编码")
		private String merCode;

		@ExcelProperty("请求编码")
		private String reqOrderNo;

		@ExcelProperty("计费次数")
		private Integer notFreeTimes;

		@ExcelProperty("15日免费次数")
		private Integer freeTimes;
	}

	// 车五项，查得数据中缺项数据统计
	@PostMapping("/cwx/data_right")
	public void dataRight(@RequestParam("file") MultipartFile file) {

		List<ExcelCell> dataList = new ArrayList<>();
		int yes = 0;
		int no = 0;
		try (ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build()) {

			ReadSheet readSheet1 =
					EasyExcel.readSheet(0).head(ExcelCell.class).registerReadListener(new DemoDataListener(dataList)).build();
			ReadSheet readSheet2 =
					EasyExcel.readSheet(1).head(ExcelCell.class).registerReadListener(new DemoDataListener(dataList)).build();
			excelReader.read(readSheet1, readSheet2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (ExcelCell data : dataList) {
			String gunzip = ZipStrUtils.gunzip(data.getJson());
			ExcelDTO<CarResult> response = JsonUtil.fromJson(gunzip, new TypeReference<ExcelDTO<CarResult>>() {
			});
			CarResult result = response.getResult();
			if (CharSequenceUtil.isAllNotBlank(result.getVin(), result.getEngine(), result.getCarName(), result.getRecordDate(), result.getVehicleModel())) {
				yes++;
			} else {
				no++;
			}

		}
		System.out.println(CharSequenceUtil.format("yes = {}, no = {}", yes, no));

	}

	/**
	 * 车五项异常请求中查询的请求数统计
	 *
	 * @param file
	 */
	@PostMapping("/cwx/exception")
	public void exception(@RequestParam("file") MultipartFile file) {

		List<ExcelCell> dataList = new ArrayList<>();
		int none = 0;
		try (ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build()) {

			ReadSheet readSheet1 =
					EasyExcel.readSheet(0).head(ExcelCell.class).registerReadListener(new DemoDataListener(dataList)).build();
			ReadSheet readSheet2 =
					EasyExcel.readSheet(1).head(ExcelCell.class).registerReadListener(new DemoDataListener(dataList)).build();
			excelReader.read(readSheet1, readSheet2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (ExcelCell data : dataList) {
			ExcelDTO response = JSONUtil.toBean(data.getJson(), ExcelDTO.class);
			if ("1910020001".equals(response.getCode())) {
				none++;
			}

		}

		System.out.println(CharSequenceUtil.format("not found = {}", none));
	}


	@Data
	public static class ExcelCell {

		@ExcelProperty("响应参数Json")
		private String json;

	}

	@Data
	public static class ExcelDTO<T> {

		private String code;
		private String message;
		private String requestId;
		private String fee;
		private T result;


	}

	@Data
	public static class CarResult {

		private String vin;
		private String engine;
		private String recordDate;
		private String vehicleModel;
		private String carName;
	}

	public static class DemoDataListener implements ReadListener<ExcelCell> {

		private final List<ExcelCell> dataList;

		public DemoDataListener(List<ExcelCell> dataList) {
			this.dataList = dataList;
		}

		@Override
		public void invoke(ExcelCell data, AnalysisContext context) {
			dataList.add(data);
		}

		@Override
		public void doAfterAllAnalysed(AnalysisContext context) {
			log.info("所有数据解析完成！");
		}

	}

}
