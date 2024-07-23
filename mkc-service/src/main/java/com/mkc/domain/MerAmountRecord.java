package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 商户余额操作记录对象 t_mer_amount_record
 * 
 * @author atd
 * @date 2023-04-24
 */

@TableName(value = "t_mer_amount_record")
@Data
public class MerAmountRecord extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商户code */
    @Excel(name = "商户code")
    private String merCode;

    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merName;

    /** 变更类型 */
    @Excel(name = "变更类型")
    private String type;

    /** 本次操作金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /**商户操作之前的账户金额*/
    @Excel(name = "操作前金额")
    private BigDecimal beforeAmount;

    /**商户操作之后 的账户金额*/
    @Excel(name = "操作后金额")
    private BigDecimal afterAmount;

    /** 流水号 */
    @Excel(name = "流水号")
    private String orderNo;

    /** 订单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime orderTime;


    public MerAmountRecord(){

    }

    public MerAmountRecord(String merCode,BigDecimal amount){
        this.merCode=merCode;
        this.amount = amount;

    }
    public MerAmountRecord(String merCode,String merName,BigDecimal amount,String orderNo,LocalDateTime orderTime){
        this.merCode=merCode;
        this.merName=merName;
        this.amount = amount;
        this.orderNo = orderNo;
        this.orderTime = orderTime;

    }

}
