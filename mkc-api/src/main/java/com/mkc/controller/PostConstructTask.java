package com.mkc.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.jayway.jsonpath.JsonPath;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.utils.Tuple2;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.domain.ExcelTestCar;
import com.mkc.dto.MerLogLine;
import com.mkc.dto.SupLogLine;
import com.mkc.dto.fx.HouseInfoDTO;
import com.mkc.dto.fx.statistic.FxHouseStatisticByPerson;
import com.mkc.tool.JsonUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/27 14:02
 */
@Slf4j
@Profile({"local"})
@Component
public class PostConstructTask {

	private static final String DOWNLOAD_FILEPATH = "C:/Users/achen/Downloads/";

	public static <T> List<T> readExcel(String filePath, Class<T> clazz) {

		return EasyExcel.read(new File(filePath))
				.headRowNumber(1)
				.head(clazz)
				.doReadAllSync();

	}

	public void houseStatistic() {
		List<FxReqDTO> dtoList = readExcel("D:\\work\\不动产\\t_fx_req_record.xlsx", FxReqDTO.class);

		List<FxReqDTO> filterList = dtoList.stream()
				.filter(dto -> {
					if (dto.getUpdateTime().isAfter(LocalDateTimeUtil.parse("2024-11-01 00:00:00", "yyyy-MM-dd HH:mm:ss"))
							&& dto.getUpdateTime().isBefore(LocalDateTimeUtil.parse("2024-11-30 23:59:59", "yyyy-MM-dd HH:mm:ss"))) {
						return true;
					}
					return dto.getCreateTime().isAfter(LocalDateTimeUtil.parse("2024-11-01 00:00:00", "yyyy-MM-dd HH:mm:ss"))
							&& dto.getCreateTime().isBefore(LocalDateTimeUtil.parse("2024-11-30 23:59:59", "yyyy-MM-dd HH:mm:ss"));
				})
				.collect(Collectors.toList());


		List<FxHouseStatisticByPerson> statistics = new ArrayList<>();

		filterList.forEach(dto -> {
			if (dto.getCreateTime().isAfter(LocalDateTimeUtil.parse("2024-11-01 00:00:00", "yyyy-MM-dd HH:mm:ss"))
					&& dto.getCreateTime().isBefore(LocalDateTimeUtil.parse("2024-11-30 23:59:59", "yyyy-MM-dd HH:mm:ss"))) {
				if (CharSequenceUtil.isNotBlank(dto.getMerResultData())) {
					HouseInfoDTO houseInfo = JsonUtil.fromJson(dto.getMerResultData(), HouseInfoDTO.class);
					for (HouseInfoDTO.AuthResult authResult : houseInfo.getAuthResults()) {
						FxHouseStatisticByPerson person = new FxHouseStatisticByPerson();
						person.setReqOrderNo(houseInfo.getReqOrderNo());

						person.setCreateTime(dto.getCreateTime());
						person.setUpdateTime(dto.getUpdateTime());
						person.setMerResultData(dto.getMerResultData());
						person.setMerRequestData(dto.getMerRequestData());
						person.setUserName(authResult.getCardNum());
						if (!authResult.getResultList().isEmpty()) {
							person.setPay("是");
							person.setGet("是");
						}
						statistics.add(person);
					}
				}
				// 如果不是11月创建的请求那么，调用半年前创建的请求也会计费
			} else if (dto.getUpdateTime().isAfter(dto.getCreateTime().plusMonths(6L))) {
				if (CharSequenceUtil.isNotBlank(dto.getMerResultData())) {
					HouseInfoDTO houseInfo = JsonUtil.fromJson(dto.getMerResultData(), HouseInfoDTO.class);
					for (HouseInfoDTO.AuthResult authResult : houseInfo.getAuthResults()) {
						FxHouseStatisticByPerson person = new FxHouseStatisticByPerson();
						person.setReqOrderNo(houseInfo.getReqOrderNo());
						person.setCreateTime(dto.getCreateTime());
						person.setUpdateTime(dto.getUpdateTime());
						person.setMerResultData(dto.getMerResultData());
						person.setMerRequestData(dto.getMerRequestData());
						person.setUserName(authResult.getCardNum());
						if (!authResult.getResultList().isEmpty()) {
							person.setPay("是");
							person.setGet("是");
						}
						statistics.add(person);
					}
				}
			}
		});

		EasyExcel.write(new File("D:\\work\\不动产\\法信11月不动产计费统计.xlsx"))
				.head(FxHouseStatisticByPerson.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("sheet")
				.doWrite(statistics);


	}

	@Data
	public static class FxReqDTO {

		@ColumnWidth(20)
		@ExcelProperty("req_order_no")
		private String reqOrderNo;

		@ColumnWidth(20)
		@ExcelProperty(value = "create_time", converter = SupLogLine.LocalDateTimeConverter.class)
		private LocalDateTime createTime;

		@ColumnWidth(20)
		@ExcelProperty(value = "update_time", converter = SupLogLine.LocalDateTimeConverter.class)
		private LocalDateTime updateTime;

		@ColumnWidth(20)
		@ExcelProperty("mer_request_data")
		private String merRequestData;

		@ColumnWidth(20)
		@ExcelProperty("mer_result_data")
		private String merResultData;

	}

	/**
	 * 全国高等学历信息查询 /educationInfo 日志分析
	 */
	public void educationInfo() {

		Map<String, Statistic> map = new HashMap<>();

		List<SupLogLine> supLogLines = readExcel(DOWNLOAD_FILEPATH + "1732936422003调用供应商日志数据.xlsx", SupLogLine.class);
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
		System.out.println("日期 总计  异常  成功  查得  查无");
		map.forEach((date, statistic) ->
				System.out.println(CharSequenceUtil.format("{}  {}  {}  {}  {} ",
						date, statistic.getTotal(), statistic.getException(), statistic.getFindYes(), statistic.findNo)));
	}

	@Data
	private static class EducationExport {
		@ExcelProperty("姓名")
		private String name;
		@ExcelProperty("身份证号")
		private String idNo;
		@ExcelProperty("手机号")
		private String mobile;
	}

	@Data
	private static class Statistic {
		private int success = 0;

		private int findYes = 0;

		private int findNo = 0;

		private int exception = 0;

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
		List<SupLogLine> readList = readExcel("C:/Users/achen/Downloads/1732852371939调用供应商日志数据.xlsx", SupLogLine.class);

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
				CarResDTO.Result result = JSONUtil.toBean(gunzip, CarResDTO.class).getResult();
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

		EasyExcel.write(new File("D:\\跑数\\车五项\\11.29\\11.27车五项跑数结果.xlsx"))
				.head(ExcelTestCar.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("车五项测试结果")
				.doWrite(uniqueItems);

	}

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
	public void dataRight() {
		String[] filePaths = {"D:\\跑数\\车五项\\11.09\\车五项跑数结果.xlsx", "D:\\跑数\\车五项\\11.21\\20241121180630车五项跑数结果.xlsx",
				"D:\\跑数\\车五项\\11.29\\11.27车五项跑数结果.xlsx", "D:\\跑数\\车五项\\12.06\\1206车五项跑数结果62w.xlsx"};
		for (String filepath : filePaths) {
			List<ExcelTestCar> excelTestCars = readExcel(filepath, ExcelTestCar.class);
			long aNull = excelTestCars.stream().filter(car -> {
				if ("200".equals(car.getCode())) {
					if (!CharSequenceUtil.isAllNotBlank(car.getModelNo(), car.getBrandName(), car.getVin(), car.getVin(), car.getInitialRegistrationDate())) {
						return true;
					} else {
						return isAnyEqual("null", car.getBrandName(), car.getVin(), car.getInitialRegistrationDate(), car.getEngineNo(), car.getModelNo());
					}
				}
				return false;
			}).count();
			System.err.println(filepath + "  缺项： " + aNull);
		}

	}

	private static boolean isAnyEqual(String target, String... args) {
		for (String arg : args) {
			if (!arg.equals(target)) {
				return false;
			}
		}
		return true;
	}


	public void testCarMerLogStatistic() {

		List<MerLogLine> readList = readExcel("C:\\Users\\achen\\Downloads\\111\\2024-12-10-15-19-33_EXPORT_XLSX_16777742_280_0.xlsx", MerLogLine.class);

		Map<String, ExcelTestCar> plateNoMap = readExcel("D:\\跑数\\车五项\\12.06\\1206车五项跑数结果.xlsx", ExcelTestCar.class)
				.stream()
				.distinct()
				.collect(Collectors.toMap(ExcelTestCar::getPlateNo, Function.identity()));

		List<MerResDTO> collect = readList.stream()
				.filter(line -> CharSequenceUtil.isNotBlank(line.getResJson()))
				.map(line -> {
					try {
						String gunzip = ZipStrUtils.gunzip(line.getResJson());
						MerResDTO bean = JSONUtil.toBean(gunzip, MerResDTO.class);
						bean.getData().setPlateNo(JsonPath.read(line.getReqJson(), "$.plateNo"));
						return bean;
					} catch (Exception e) {
						MerResDTO bean = JSONUtil.toBean(line.getResJson(), MerResDTO.class);
						if (Objects.isNull(bean.getData())) {
							bean.setData(new MerResDTO.Result());
						}
						bean.getData().setPlateNo(JsonPath.read(line.getReqJson(), "$.plateNo"));
						return bean;
					}
				}).collect(Collectors.toList());
		collect.forEach(
				res -> {
					MerResDTO.Result data = res.getData();
					if (plateNoMap.containsKey(data.getPlateNo())) {
						ExcelTestCar car = plateNoMap.get(data.getPlateNo());
						car.setCode(res.getCode());
						car.setMsg(res.getCode().equals("200") ? "成功" : res.getCode().equals("404") ? "查无" : "异常");
						car.setEngineNo(data.getEngineNo());
						car.setBrandName(data.getBrandName());
						car.setVin(data.getVin());
						car.setInitialRegistrationDate(data.getInitialRegistrationDate());
						car.setModelNo(data.getModelNo());
						car.setMissParam(CharSequenceUtil.isAllNotBlank(data.getVin(), data.getModelNo(), data.getBrandName(), data.getEngineNo(), data.getInitialRegistrationDate()) ? "否" : "是");
					}
				});

		List<ExcelTestCar> notQuery = plateNoMap.values().stream().filter(o -> CharSequenceUtil.isBlank(o.getCode()))
				.map(o -> {
					ExcelTestCar car = new ExcelTestCar();
					car.setPlateNo(o.getPlateNo());
					return car;
				}).collect(Collectors.toList());

		EasyExcel.write(new File("D:\\跑数\\车五项\\12.06\\1206车五项跑数结果.xlsx" + System.currentTimeMillis()))
				.head(ExcelTestCar.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("调用日志")
				.doWrite(plateNoMap.values());

		EasyExcel.write(new File("D:\\跑数\\车五项\\12.06\\1206车五项漏查.xlsx" + System.currentTimeMillis()))
				.head(ExcelTestCar.class)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("调用日志")
				.doWrite(notQuery);

	}

	@Data
	public static class MerResDTO {
		private String code;
		private Result data;
		private String free;
		private String msg;
		private String seqNo;

		@Data
		public static class Result {
			private String plateNo;
			private String engineNo;
			private String brandName;
			private String vin;
			private String initialRegistrationDate;
			private String modelNo;
		}
	}

	@Data
	public static class ExcelCell {

		@ExcelProperty("响应参数Json")
		private String json;

	}

	@Data
	public static class CarResDTO {

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
