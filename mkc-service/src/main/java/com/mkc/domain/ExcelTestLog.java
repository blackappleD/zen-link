package com.mkc.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author linst
 */
@Data
public class ExcelTestLog {
    @ColumnWidth(20)
    @ExcelProperty("订单号")
    private String xm;
    @ColumnWidth(20)
    @ExcelProperty("产品名称")
    private String zjhm;
    @ColumnWidth(20)
    @ExcelProperty("商户名称")
    private String code;
    @ColumnWidth(20)
    @ExcelProperty("流水号")
    private String yxmc;
    @ColumnWidth(30)
    @ExcelProperty("请求参数")
    private String zymc;
    @ColumnWidth(30)
    @ExcelProperty("响应参数")
    private String cc;
    @ColumnWidth(20)
    @ExcelProperty("查询状态")
    private String rxrq;
    @ColumnWidth(20)
    @ExcelProperty("请求时间")
    private String byrq;
    @ColumnWidth(20)
    @ExcelProperty("响应时间")
    private String xxxs;


}
