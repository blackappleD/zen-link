package com.mkc.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.mkc.common.utils.ZipStrUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
