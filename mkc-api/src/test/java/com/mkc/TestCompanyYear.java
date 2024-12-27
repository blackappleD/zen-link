package com.mkc;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 企业年报统计
 *
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/23 15:25
 */
@SpringBootTest
public class TestCompanyYear {

	private static final String DOWNLOAD_FILEPATH = "C:/Users/achen/Downloads/";

	@Data
	public static class TestCell {
		@ExcelProperty("请求订单号")
		private String orderId;
	}

	@Test
	public void test() {

		String filepath = "C:\\Users\\achen\\Desktop\\年报20241223.xlsx";

		String resultFile = "C:\\Users\\achen\\Desktop\\测试样本结果.xlsx";

		List<Mapping> taxMaping = readExcel(filepath, 2, Mapping.class);
		Map<String, Mapping> mapingMap = taxMaping.stream().collect(Collectors.toMap(Mapping::getCode, Function.identity()));

		List<PeopleMap> peopleMapping = readExcel(filepath, 2, PeopleMap.class);
		Map<String, PeopleMap> peopleMap = peopleMapping.stream().filter(o -> Objects.nonNull(o.get单位人数编码())).collect(Collectors.toMap(PeopleMap::get单位人数编码, Function.identity()));

		List<Company> originalData = readExcel(filepath, Company.class);
		Map<String, Company> originalMap = originalData.stream().collect(Collectors.toMap(Company::getPid, Function.identity(), (a, b) -> a));

		List<Company> companies = readExcel(resultFile, Company.class);

		companies.forEach(company -> {

			Company originalCompany = originalMap.get(company.getPid());
			if (originalMap.containsKey(company.getPid())) {
				String 资产总额 = originalCompany.get资产总额();
				if (mapingMap.containsKey(资产总额)) {
					Mapping taxMap = mapingMap.get(资产总额);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set资产总额(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set资产总额(taxMap.getLeft());
					}
				}

				String 负债总额 = originalCompany.get负债总额();
				if (mapingMap.containsKey(负债总额)) {
					Mapping taxMap = mapingMap.get(负债总额);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set负债总额(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set负债总额(taxMap.getLeft());
					}
				}

				String 营业总收入 = originalCompany.get营业总收入();
				if (mapingMap.containsKey(营业总收入)) {
					Mapping taxMap = mapingMap.get(营业总收入);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set营业总收入(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set营业总收入(taxMap.getLeft());
					}
				}

				String 主营业务收入 = originalCompany.get主营业务收入();
				if (mapingMap.containsKey(主营业务收入)) {
					Mapping taxMap = mapingMap.get(主营业务收入);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set主营业务收入(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set主营业务收入(taxMap.getLeft());
					}
				}

				String 利润总额 = originalCompany.get利润总额();
				if (mapingMap.containsKey(利润总额)) {
					Mapping taxMap = mapingMap.get(利润总额);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set利润总额(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set利润总额(taxMap.getLeft());
					}
				}

				String 净利润 = originalCompany.get净利润();
				if (mapingMap.containsKey(净利润)) {
					Mapping taxMap = mapingMap.get(净利润);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set净利润(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set净利润(taxMap.getLeft());
					}
				}

				String 纳税总额 = originalCompany.get纳税总额();
				if (mapingMap.containsKey(纳税总额)) {
					Mapping taxMap = mapingMap.get(纳税总额);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set纳税总额(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set纳税总额(taxMap.getLeft());
					}
				}

				String 所有者权益 = originalCompany.get所有者权益();
				if (mapingMap.containsKey(所有者权益)) {
					Mapping taxMap = mapingMap.get(所有者权益);
					if (CharSequenceUtil.isNotBlank(taxMap.getRight())) {
						company.set所有者权益(CharSequenceUtil.format("{}-{}", taxMap.getLeft(), taxMap.getRight()));
					} else {
						company.set所有者权益(taxMap.getLeft());
					}
				}


				if (peopleMap.containsKey(originalCompany.get从业人数())) {
					PeopleMap peopMap = peopleMap.get(originalCompany.get从业人数());
					if (CharSequenceUtil.isBlank(peopMap.get单位人数右())) {
						company.set从业人数(peopMap.get单位人数左());
					} else {
						company.set从业人数(peopMap.get单位人数左() + "-" + peopMap.get单位人数右());
					}
				}
			}

		});

		writeExcel("C:\\Users\\achen\\Desktop\\Test.xlsx", Company.class, companies);

	}

	public static <T> List<T> readExcel(String filePath, Class<T> clazz) {
		return readExcel(filePath, 1, clazz);
	}

	public static <T> List<T> readExcel(String filePath, int sheetNum, Class<T> clazz) {
		return EasyExcel.read(new File(filePath))
				.headRowNumber(1)
				.head(clazz)
				.sheet(sheetNum - 1)
				.doReadSync();
	}

	public static void writeExcel(String filePath, Class<?> clazz, List<?> data) {
		EasyExcel.write(new File(filePath))
				.head(clazz)
				.excelType(ExcelTypeEnum.XLSX)
				.sheet("sheet")
				.doWrite(data);

	}

	@Data
	public static class Company {
		@ColumnWidth(10)
		@ExcelProperty("统一社会信用代码")
		private String pid;

		@ColumnWidth(10)
		@ExcelProperty("从业人数")
		private String 从业人数;

		@ColumnWidth(10)
		@ExcelProperty("资产总额")
		private String 资产总额;

		@ColumnWidth(10)
		@ExcelProperty("负债总额")
		private String 负债总额;
		@ColumnWidth(10)
		@ExcelProperty("营业总收入")
		private String 营业总收入;
		@ColumnWidth(10)
		@ExcelProperty("主营业务收入")
		private String 主营业务收入;
		@ColumnWidth(10)
		@ExcelProperty("利润总额")
		private String 利润总额;
		@ColumnWidth(10)
		@ExcelProperty("净利润")
		private String 净利润;
		@ColumnWidth(10)
		@ExcelProperty("纳税总额")
		private String 纳税总额;
		@ColumnWidth(10)
		@ExcelProperty("所有者权益")
		private String 所有者权益;

	}

	@Data
	public static class Mapping {
		@ExcelProperty("码值")
		private String code;

		@ExcelProperty("左闭区间")
		private String left;

		@ExcelProperty("右开区间")
		private String right;
	}

	@Data
	public static class PeopleMap {
		@ExcelProperty("单位人数编码")
		private String 单位人数编码;

		@ExcelProperty("单位人数左")
		private String 单位人数左;

		@ExcelProperty("单位人数右")
		private String 单位人数右;
	}

}
