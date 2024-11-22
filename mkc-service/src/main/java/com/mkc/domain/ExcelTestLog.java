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
    private String ddh;
    @ColumnWidth(20)
    @ExcelProperty("产品名称")
    private String cpmc;
    @ColumnWidth(20)
    @ExcelProperty("商户名称")
    private String shmc;
    @ColumnWidth(20)
    @ExcelProperty("流水号")
    private String lsh;
    @ColumnWidth(30)
    @ExcelProperty("请求参数")
    private String qqcs;
    @ColumnWidth(30)
    @ExcelProperty("响应参数")
    private String xycs;
    @ColumnWidth(20)
    @ExcelProperty("查询状态")
    private String cxzt;
    @ColumnWidth(20)
    @ExcelProperty("请求时间")
    private String qqsj;
    @ColumnWidth(20)
    @ExcelProperty("响应时间")
    private String xxsj;


}
