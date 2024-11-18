package com.mkc.domain;

import com.mkc.common.annotation.Excel;
import lombok.Data;


/**
 * 法信账单详情
 *
 * @author linst
 * @date 2024-11-08
 */
@Data
public class FxReqRecordExcel{
    /** 请求订单号 */
    @Excel(name = "请求订单号")
    private String reqOrderNo;
    /** 商户code*/
    @Excel(name = "商户名称")
    private String merName;
    /** 商户code*/
    @Excel(name = "人员信息")
    private String persons;
    /**商户请求查询结果人员信息*/
    @Excel(name = "请求查询人员信息")
    private String merRequestData;
    /**供应商返回的结果*/
    @Excel(name = "请求查询结果")
    private String merResultData;
    /**用户查询结果标记，1=已核查*/
    @Excel(name = "是否核查")
    private Boolean userFlag;
    @Excel(name = "创建时间")
    private String createTimeStr;
    @Excel(name = "更新时间")
    private String updateTimeStr;
    @Excel(name = "商户未查询信息")
    private String unknownInfo;
    @Excel(name = "是否查得")
    private Boolean isGet;
    @Excel(name = "档次")
    private String level;
    @Excel(name = "计费笔数")
    private Integer feeCount = 0;
    @Excel(name = "备注")
    private String remark;
}
