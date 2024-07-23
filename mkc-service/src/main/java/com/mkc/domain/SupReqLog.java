package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 调用供应商日志对象 t_sup_req_log
 * 
 * @author atd
 * @date 2023-04-24
 */

@TableName(value = "t_sup_req_log")
@Data
public class SupReqLog implements Serializable
{

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 流水号 */
    @Excel(name = "流水号")
    private String orderNo;

    /** 供应商code */
    private String supCode;

    /** 供应商code */
    @Excel(name = "产品分类")
    private String cgCode;

    /** 商户编码 */
    @Excel(name = "商户编码")
    private String merCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supName;

    /** 请求参数Json */
    @Excel(name = "请求参数Json")
    private String reqJson;

    /** 响应参数Json */
    @Excel(name = "响应参数Json")
    private String respJson;

    /** 供应产品 */
    @Excel(name = "供应产品")
    private String supProduct;
    /** 供应商订单号 */
    @Excel(name = "供应商订单号")
    private String supSeq;

    /** 产品 */
    @Excel(name = "产品")
    private String procductCode;

    /** 成本价 */
    private BigDecimal inPrice;

    /** 查询状态 */
    @Excel(name = "查询状态")
    private String status;

    /** 是否收费 0 否，1 是 */
    private String free;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    @Excel(name = "请求时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reqTime;

    /** 响应时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private LocalDateTime respTime;

    /** 总耗时(毫秒) */
    @Excel(name = "总耗时(毫秒)")
    private Long totalTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private LocalDateTime createTime;

    /** 备注 */
    private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("supCode", getSupCode())
            .append("merCode", getMerCode())
            .append("supName", getSupName())
            .append("reqjson", getReqJson())
            .append("respjson", getRespJson())
            .append("supProduct", getSupProduct())
            .append("procductCode", getProcductCode())
            .append("inPrice", getInPrice())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("reqtime", getReqTime())
            .append("resptime", getRespTime())
            .append("totaltime", getTotalTime())
            .append("remark", getRemark())
            .toString();
    }
}
