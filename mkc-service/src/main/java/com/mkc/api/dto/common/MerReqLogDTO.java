package com.mkc.api.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.api.common.constant.enums.YysProductCode;
import com.mkc.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户调用日志对象 t_mer_req_log
 *
 * @author atd
 * @date 2023-04-24
 */

@Data
public class MerReqLogDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 流水号 */
    @Excel(name = "流水号")
    private String orderNo;

    /** 产品分类code */
    private String cgCode;
    /**
     * 请求的产品code 运营商相关产品需要
     */
    private String reqProductCode;

    private String productCode;

    private YysProductCode yysProductCode;

    /** 产品名称 */
    private String productName;

    /** 商户code */
    private String merCode;

    /** 商户名 */
    @Excel(name = "商户名")
    private String merName;

    /** 供应商code */
    //@Excel(name = "供应商code")
    private String supCode;

    @Excel(name = "供应商名称")
    private String supName;

    /** 售价(元) */
    @Excel(name = "售价(元)")
    private BigDecimal sellPrice;

    /** 进价(元) */
    @Excel(name = "进价(元)")
    private BigDecimal inPrice;

    /** 商户请求参数 */
    @Excel(name = "商户请求参数")
    private String reqJson;

    /** 响应商户参数 */
    @Excel(name = "响应商户参数")
    private String respJson;

    /** 查询状态 */
    @Excel(name = "查询状态")
    private String status;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    @Excel(name = "请求时间")
    private LocalDateTime reqTime;

    /** 响应时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    @Excel(name = "响应时间")
    private LocalDateTime respTime;

    /** 响应时间 - 请求时间 =总耗时(毫秒) */
    private Long totalTime;

    /**
     * string	是	商户流水号;
     */
    private String merSeq;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private LocalDateTime createTime;

    /** 备注 */
    private String remark;

    /** ip地址 */
    private String ipaddr;

    /** 是否收费 0 否，1 是 */
    private String free;

    /** 档次 */
    private String level;

    /** 实际收费 金额 (元)*/
    private BigDecimal actualPrice=BigDecimal.ZERO;


    /** 切换路由供应商条件 */
    private String routeCon="";


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("merCode", getMerCode())
            .append("merSeq", getMerSeq())
            .append("merName", getMerName())
            .append("supCode", getSupCode())
            .append("sellPrice", getSellPrice())
            .append("inPrice", getInPrice())
           // .append("reqJson", getReqJson())
           // .append("respJson", getRespJson())
            .append("status", getStatus())
            .append("reqTime", getReqTime())
            .append("respTime", getRespTime())
            .append("totalTime", getTotalTime())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
