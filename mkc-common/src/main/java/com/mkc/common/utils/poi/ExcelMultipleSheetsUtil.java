package com.mkc.common.utils.poi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.exception.UtilException;
import com.mkc.common.utils.DictUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mkc.common.core.text.CharsetKit.UTF_8;

@Slf4j
public class ExcelMultipleSheetsUtil {

    /**
     * 导出excel：可多个sheet页
     *
     * @param data          数据：Map 集合【key == 每一个sheet页的名称，value == sheet页数据】
     * @param excelFileName excel文件名
     * @param suffixName    后缀名
     * @param response      响应
     * @throws IOException 异常
     */
    public static void excelMultipleSheets(Map<String, Object> data, String excelFileName, String suffixName, HttpServletResponse response) throws IOException {
        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String sheetName = entry.getKey();
                Object sheetData = entry.getValue();
                Sheet sheet = workbook.createSheet(sheetName);
                if (ObjectUtil.isNotEmpty(sheetData)) {
                    createSheetWithData(sheet, sheetData);
                }
            }

            setResponseHeader(response, excelFileName, suffixName);
            // 写出文件
            workbook.write(response.getOutputStream());
        }
    }


    public static AjaxResult excelMultipleSheets(HashMap<String, Object> data, String fileName) {

        OutputStream out = null;
        Workbook workbook = new XSSFWorkbook();
        // 创建工作簿
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String sheetName = entry.getKey();
                Object sheetData = entry.getValue();
                Sheet sheet = workbook.createSheet(sheetName);
                if (ObjectUtil.isNotEmpty(sheetData)) {
                    createSheetWithData(sheet, sheetData);
                }
            }
//            setResponseHeader(response, excelFileName, suffixName);
            // 写出文件

            String filename = ExcelUtil.encodingFilename(fileName);
            out = new FileOutputStream(ExcelUtil.getAbsoluteFile(filename));
            workbook.write(out);
            return AjaxResult.success(filename);
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new UtilException("导出Excel失败，请联系网站管理员！");
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }

    }


    /**
     * 创建表单并填充数据
     *
     * @param sheet 表单
     * @param data  数据
     */
    private static void createSheetWithData(Sheet sheet, Object data) {
        if (data instanceof List) {
            createSheetWithListData(sheet, (List<?>) data);
        } else {
            createSheetWithObjectData(sheet, data);
        }
    }

    /**
     * 创建列表类型数据对应的Excel表单
     *
     * @param sheet    表单
     * @param dataList 数据列表
     */
    private static void createSheetWithListData(Sheet sheet, List<?> dataList) {
        if (CollUtil.isNotEmpty(dataList)) {
            Object firstItem = dataList.get(0);
            createHeaderRow(sheet, firstItem.getClass());
            int rowIndex = 1;
            for (Object item : dataList) {
                createDataRow(sheet, item, rowIndex++);
            }
        }
    }

    /**
     * 创建对象类型数据对应的Excel表单
     *
     * @param sheet 表单
     * @param data  数据
     */
    private static void createSheetWithObjectData(Sheet sheet, Object data) {
        createHeaderRow(sheet, data.getClass());
        createDataRow(sheet, data, 1);
    }

    /**
     * 创建表头行
     *
     * @param sheet 表单
     * @param clazz 数据类
     */
    private static void createHeaderRow(Sheet sheet, Class<?> clazz) {
        // 创建单元格样式
        CellStyle headerCellStyle = createHeadCellStyle(sheet.getWorkbook());

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            createHeaderCell(sheet, headerCellStyle, fields, headerRow, i);
        }
    }

    /**
     * 创建数据行
     *
     * @param sheet    表单
     * @param data     数据
     * @param rowIndex 行号
     */
    private static void createDataRow(Sheet sheet, Object data, int rowIndex) {
        // 创建单元格样式
        CellStyle dataCellStyle = createCellStyle(sheet.getWorkbook());

        // 创建数据行
        Row dataRow = sheet.createRow(rowIndex);
        Field[] fields = data.getClass().getDeclaredFields();


        for (int i = 0; i < fields.length; i++) {
            createDataCell(dataCellStyle, fields, dataRow, i, data);
        }
    }


    /**
     * 创建单元格样式
     *
     * @param workbook 工作簿
     * @return 单元格样式
     */
    private static CellStyle createHeadCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // 设置 水平和垂直 居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置 上 下 左 右 边框及颜色
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        // 设置字体
        Font dataFont = workbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        dataFont.setColor(IndexedColors.WHITE.getIndex());
        dataFont.setBold(true);
        cellStyle.setFont(dataFont);

        return cellStyle;
    }
    /**
     * 创建单元格样式
     *
     * @param workbook 工作簿
     * @return 单元格样式
     */
    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // 设置 水平和垂直 居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置 上 下 左 右 边框及颜色
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());


        // 设置字体
        Font dataFont = workbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(dataFont);

        return cellStyle;
    }

    /**
     * 创建Excel表头单元格
     *
     * @param sheet           表单
     * @param headerCellStyle 单元格样式
     * @param fields          字段
     * @param headerRow       标题行
     * @param i               序号
     */
    private static void createHeaderCell(Sheet sheet, CellStyle headerCellStyle, Field[] fields, Row headerRow, int i) {
        // 默认宽度
        double width = 16;
        Excel excelAnnotation = fields[i].getAnnotation(Excel.class);
        if (excelAnnotation != null && !ObjectUtil.isEmpty(excelAnnotation.width())) {
            width = excelAnnotation.width();
        }

        // 设置宽度
        sheet.setColumnWidth(i, (int) ((width + 0.72) * 256));

        if (excelAnnotation != null) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(excelAnnotation.name());
            cell.setCellStyle(headerCellStyle);
        }
    }

    /**
     * 创建Excel数据单元格
     *
     * @param dataCellStyle 单元格样式
     * @param fields        字段
     * @param dataRow       数据行
     * @param i             序号
     * @param data          数据
     */
    private static void createDataCell(CellStyle dataCellStyle, Field[] fields, Row dataRow, int i, Object data) {
        Cell cell = dataRow.createCell(i);
        cell.setCellStyle(dataCellStyle);

        try {
            fields[i].setAccessible(true);
            Object value = fields[i].get(data);
            handleAnnotationAndSetValue(cell, fields[i], value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理注解并设置单元格值
     *
     * @param cell  单元格
     * @param field 字段
     * @param value 值
     */
    private static void handleAnnotationAndSetValue(Cell cell, Field field, Object value) {
        if (field.isAnnotationPresent(Excel.class) && field.getAnnotation(Excel.class).dictType().length() > 0) {
            value = DictUtils.getDictLabel(field.getAnnotation(Excel.class).dictType(), String.valueOf(value));
        }
        if (field.isAnnotationPresent(Excel.class) && StrUtil.isNotEmpty(field.getAnnotation(Excel.class).dateFormat())) {
            value = DateUtil.format(Convert.convert(Date.class, value), field.getAnnotation(Excel.class).dateFormat());
        }
        cell.setCellValue(ObjectUtil.isEmpty(value) ? null : value.toString());
    }

    /**
     * 设置响应头
     *
     * @param response      响应
     * @param excelFileName 文件名
     * @param suffixName    后缀名
     * @throws UnsupportedEncodingException 编码异常
     */
    private static void setResponseHeader(HttpServletResponse response, String excelFileName, String suffixName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelFileName + suffixName, UTF_8));
    }
}

