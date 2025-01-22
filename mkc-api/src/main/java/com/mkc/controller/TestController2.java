package com.mkc.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.jayway.jsonpath.JsonPath;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.utils.Tuple2;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.dto.ExcelTestCar;
import com.mkc.dto.MerLogLine;
import com.mkc.dto.SupLogLine;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
				.sheet(0)
				.doReadSync();
	}


	/**
	 * 全国高等学历信息查询 /educationInfo 日志分析
	 */
	public void educationInfo() {

		Map<String, Statistic> map = new HashMap<>();

		List<SupLogLine> supLogLines = readExcel(DOWNLOAD_FILEPATH + "1734952290147调用供应商日志数据.xlsx", SupLogLine.class);
		for (SupLogLine supLogLine : supLogLines) {

			String resJson = supLogLine.getResJson();
			try {
				resJson = ZipStrUtils.gunzip(resJson);
			} catch (Exception ignored) {
			}
			String date = LocalDateTimeUtil.format(supLogLine.getReqTime(), "yyyy-MM-dd");
			if (!map.containsKey(date)) {
				map.put(date, new Statistic());
			}
			Statistic statistic = map.get(date);
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
		List<Statistic> collect = map.entrySet().stream().map(entry -> {
			Statistic value = entry.getValue();
			value.setDate(entry.getKey());
			return value;
		}).collect(Collectors.toList());
		EasyExcel.write(new File("C:\\Users\\achen\\Desktop\\1123-1223中电郑州学历接口统计.xlsx"))
				.head(Statistic.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("sheet")
				.doWrite(collect);

	}


	@Data
	private static class Statistic {

		@ColumnWidth(10)
		@ExcelProperty("日期")
		private String date;
		@ColumnWidth(10)
		@ExcelProperty("成功")
		private int success = 0;
		@ColumnWidth(10)
		@ExcelProperty("查得")
		private int findYes = 0;
		@ColumnWidth(10)
		@ExcelProperty("查无")
		private int findNo = 0;
		@ColumnWidth(10)
		@ExcelProperty("异常")
		private int exception = 0;
		@ColumnWidth(10)
		@ExcelProperty("总计")
		private int total = 0;

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

	public void test() {
		List<SupLogLine> readList = readExcel("C:/Users/achen/Downloads/1735895667106调用供应商日志数据.xlsx", SupLogLine.class);

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
				ExcelDTO.Result result = JSONUtil.toBean(gunzip, ExcelDTO.class).getResult();
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

		EasyExcel.write(new File("D:\\跑数\\车五项\\1.03\\1.03车五项跑数结果.xlsx"))
				.head(ExcelTestCar.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("车五项测试结果")
				.doWrite(uniqueItems);

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
		List<Test> list = new ArrayList<>();
		map.forEach((merCode, countMap) ->
				countMap.forEach((reqOrderNo, tuple) ->
						list.add(Test.builder().merCode(merCode)
								.reqOrderNo(reqOrderNo)
								.notFreeTimes(tuple.getV1())
								.freeTimes(tuple.getV2())
								.build())));

		EasyExcel.write(new File("D:\\跑数\\不动产\\11.19-11.29各商户不动产15日内免费调用统计.xlsx"))
				.head(Test.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("统计")
				.doWrite(list);


	}

	@Data
	@Builder
	public static class Test {
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
			ExcelDTO.Result result = JSONUtil.toBean(gunzip, ExcelDTO.class).getResult();
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
	public static class ExcelDTO {

		private String code;
		private String message;
		private String requestId;
		private String fee;
		private Result result;

		@Data
		public static class Result {

			private String vin;
			private String engine;
			private String recordDate;
			private String vehicleModel;
			private String carName;
		}

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
