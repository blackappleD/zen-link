package com.mkc.domain;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.BaseEntity;

import lombok.Data;

/**
 * 供应商产品信息管理对象 t_supplier_product
 * 
 * @author atd
 * @date 2023-04-24
 */
@TableName(value = "t_supplier_product")
@Data
public class SupplierProduct extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 名称 */
    @Excel(name = "供应商产品名称")
    private String supProductName;

    /** 供应商产品编码 */
    //@Excel(name = "供应商产品编码")
    private String supProductCode;


    /** 供应商code */
    //@Excel(name = "供应商code")
    private String supCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 产品 */
   // @Excel(name = "关联产品")
    private String productCode;


    /** 产品 */
    @TableField(exist = false)
    @Excel(name = "关联产品名称")
    private String productName;

    /** 成本价 */
    @Excel(name = "成本价")
    @NotNull(message = "成本价不能为空")
    private BigDecimal inPrice;

    /** 产品调用超时时间 毫秒*/
    @Excel(name = "超时时间 毫秒")
    private Integer timeOut;
    
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("supProductCode", getSupProductCode())
            .append("supProductName", getSupProductName())
            .append("supCode", getSupCode())
            .append("supName", getSupName())
            .append("status", getStatus())
            .append("productCode", getProductCode())
            .append("inPrice", getInPrice())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
